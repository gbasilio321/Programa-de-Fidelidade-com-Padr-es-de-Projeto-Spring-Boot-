package one.digitalinnovation.gof.service.desconto;

import java.math.BigDecimal;

public class DescontoPrata implements DescontoStrategy {

	@Override
	public BigDecimal percentual() {
		return new BigDecimal("0.05");
	}
}
