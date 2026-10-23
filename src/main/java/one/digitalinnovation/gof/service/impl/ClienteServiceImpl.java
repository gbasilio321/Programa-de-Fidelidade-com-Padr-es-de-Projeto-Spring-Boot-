package one.digitalinnovation.gof.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.CompraResultado;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import one.digitalinnovation.gof.service.desconto.DescontoFactory;
import one.digitalinnovation.gof.service.desconto.DescontoStrategy;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 *
 * @author falvojr
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	// Regra de negócio: 1 ponto a cada R$ 10,00 pagos.
	private static final BigDecimal VALOR_POR_PONTO = new BigDecimal("10");

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;

	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		// Buscar todos os Clientes.
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		// Buscar Cliente por ID.
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		// Buscar Cliente por ID, caso exista:
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isPresent()) {
			// Preservar os pontos: o cliente não pode alterá-los via PUT.
			cliente.setId(id);
			cliente.setPontos(clienteBd.get().getPontos());
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {
		// Deletar Cliente por ID.
		clienteRepository.deleteById(id);
	}

	@Override
	public CompraResultado registrarCompra(Long id, BigDecimal valor) {
		if (valor == null || valor.signum() <= 0) {
			throw new IllegalArgumentException("O valor da compra deve ser maior que zero.");
		}
		Cliente cliente = buscarPorId(id);

		// Strategy + Factory: o desconto vem do nível atual do cliente.
		DescontoStrategy desconto = DescontoFactory.para(cliente.getNivel());
		BigDecimal valorFinal = desconto.aplicar(valor).setScale(2, RoundingMode.HALF_UP);

		// Pontos são calculados sobre o valor efetivamente pago.
		int pontosGanhos = valorFinal.divideToIntegralValue(VALOR_POR_PONTO).intValue();
		cliente.setPontos(cliente.getPontos() + pontosGanhos);
		clienteRepository.save(cliente);

		return new CompraResultado(valor, valorFinal, pontosGanhos, cliente.getPontos(), cliente.getNivel());
	}

	private void salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		clienteRepository.save(cliente);
	}

}
