package one.digitalinnovation.gof.model;

/**
 * Níveis do programa de fidelidade. O nível é calculado a partir dos pontos
 * acumulados pelo cliente.
 */
public enum NivelFidelidade {

	BRONZE(0), PRATA(100), OURO(300);

	private final int pontosMinimos;

	NivelFidelidade(int pontosMinimos) {
		this.pontosMinimos = pontosMinimos;
	}

	public int getPontosMinimos() {
		return pontosMinimos;
	}

	public static NivelFidelidade porPontos(int pontos) {
		NivelFidelidade resultado = BRONZE;
		for (NivelFidelidade nivel : values()) {
			if (pontos >= nivel.pontosMinimos) {
				resultado = nivel;
			}
		}
		return resultado;
	}
}
