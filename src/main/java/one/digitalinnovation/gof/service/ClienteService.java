package one.digitalinnovation.gof.service;

import java.math.BigDecimal;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.CompraResultado;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio de cliente. Com
 * isso, se necessário, podemos ter multiplas implementações dessa mesma
 * interface.
 *
 * @author falvojr
 */
public interface ClienteService {

	Iterable<Cliente> buscarTodos();

	Cliente buscarPorId(Long id);

	void inserir(Cliente cliente);

	void atualizar(Long id, Cliente cliente);

	void deletar(Long id);

	/**
	 * Registra uma compra: aplica o desconto do nível atual do cliente e
	 * acumula 1 ponto a cada R$ 10,00 pagos.
	 */
	CompraResultado registrarCompra(Long id, BigDecimal valor);

}
