package one.digitalinnovation.gof.service.desconto;

import java.math.BigDecimal;

public class DescontoBronze implements DescontoStrategy {

	@Override
	public BigDecimal percentual() {
		return BigDecimal.ZERO;
	}
}
