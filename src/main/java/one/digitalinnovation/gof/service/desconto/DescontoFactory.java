package one.digitalinnovation.gof.service.desconto;

import one.digitalinnovation.gof.model.NivelFidelidade;

/**
 * Padrão <b>Factory Method</b> (estático): escolhe qual {@link DescontoStrategy}
 * usar a partir do nível do cliente, escondendo as classes concretas de quem
 * chama.
 */
public final class DescontoFactory {

	private DescontoFactory() {
	}

	public static DescontoStrategy para(NivelFidelidade nivel) {
		switch (nivel) {
		case OURO:
			return new DescontoOuro();
		case PRATA:
			return new DescontoPrata();
		default:
			return new DescontoBronze();
		}
	}
}
