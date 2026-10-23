package one.digitalinnovation.gof.service.desconto;

import java.math.BigDecimal;

/**
 * Padrão <b>Strategy</b>: cada nível de fidelidade tem sua própria regra de
 * desconto, todas atrás da mesma interface.
 */
public interface DescontoStrategy {

	/**
	 * @return percentual de desconto (ex.: 0.10 = 10%).
	 */
	BigDecimal percentual();

	default BigDecimal aplicar(BigDecimal valor) {
		return valor.subtract(valor.multiply(percentual()));
	}
}
