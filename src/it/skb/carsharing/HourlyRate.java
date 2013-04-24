package it.skb.carsharing;

import java.math.BigDecimal;

public class HourlyRate {
	
	private final BigDecimal mornigRental;
	private final BigDecimal afternoonRental;
	private final BigDecimal fuelEffort;
	
	public static HourlyRate b(BigDecimal nightRental, BigDecimal dayRental, BigDecimal fuelEffort) {
		return new HourlyRate(nightRental, dayRental, fuelEffort);
	}

	private HourlyRate(BigDecimal nightRental, BigDecimal dayRental, BigDecimal fuelEffort) {
		this.mornigRental = nightRental;
		this.afternoonRental = dayRental;
		this.fuelEffort = fuelEffort;
	}

	public BigDecimal getMornigRental() {
		return mornigRental;
	}

	public BigDecimal getAfternoonRental() {
		return afternoonRental;
	}

	public BigDecimal getFuelEffort() {
		return fuelEffort;
	} 
	
}
