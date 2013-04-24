package it.skb.carsharing;

import java.math.BigDecimal;


public enum CarType {
	ECONOMY(b("1.0"), b("2.20"), b("0.45"), 45, 75, 105, 30, b("0.20")), 
	CITY(b("1.0"), b("2.40"), b("0.55"), 60, 90, 120, 35, b("0.20")), 
	FLEXY(b("1.0"), b("2.60"), b("0.65"), 70, 120, 160, 40, b("0.25")), 
	PREMIUM(b("1.5"), b("2.80"), b("0.75"), 80, 130, 170, 45, b("0.25")), 
	CARGO(b("1.5"), b("3.00"), b("0.80"), 90, 150, 190, 50, b("0.30"));
	
	private HourlyRate hr; 
	private DailyRate dr;	
	
	private CarType (
		BigDecimal nightRental, BigDecimal dayRental, BigDecimal rentalFuelEffort,
		int to24h, int to48h, int to72h, int after72h, BigDecimal dailyFuelEffort
	) {
		hr = HourlyRate.b(nightRental, dayRental, rentalFuelEffort);
		dr = DailyRate.b(to24h, to48h, to72h, after72h, dailyFuelEffort);
	}

	public static CarType secureValueOf(String ct) {
		for (CarType c : values()) {
			if (ct.startsWith(c.name())) {
				return c;
			}
		}
		
		throw new RuntimeException("can't find car type: [" + ct + "]");
	}

	private static BigDecimal b(String s) {
		return new BigDecimal(s);
	}

	public HourlyRate getHr() {
		return hr;
	}

	public DailyRate getDr() {
		return dr;
	}
}
