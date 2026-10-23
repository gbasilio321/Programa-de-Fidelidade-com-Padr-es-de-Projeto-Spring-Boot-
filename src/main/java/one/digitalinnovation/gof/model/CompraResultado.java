package one.digitalinnovation.gof.model;

import java.math.BigDecimal;

/**
 * Resposta de uma compra: mostra o desconto aplicado e a situação do cliente
 * no programa de fidelidade.
 */
public class CompraResultado {

	private final BigDecimal valorOriginal;
	private final BigDecimal valorFinal;
	private final int pontosGanhos;
	private final int pontosTotais;
	private final NivelFidelidade nivel;

	public CompraResultado(BigDecimal valorOriginal, BigDecimal valorFinal, int pontosGanhos, int pontosTotais,
			NivelFidelidade nivel) {
		this.valorOriginal = valorOriginal;
		this.valorFinal = valorFinal;
		this.pontosGanhos = pontosGanhos;
		this.pontosTotais = pontosTotais;
		this.nivel = nivel;
	}

	public BigDecimal getValorOriginal() {
		return valorOriginal;
	}

	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public int getPontosGanhos() {
		return pontosGanhos;
	}

	public int getPontosTotais() {
		return pontosTotais;
	}

	public NivelFidelidade getNivel() {
		return nivel;
	}
}
