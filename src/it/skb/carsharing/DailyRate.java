package it.skb.carsharing;

import java.math.BigDecimal;

public class DailyRate {
	
	private final int to24h; 
	private final int to48h;
	private final int to72h;
	private final int after72h;
	private final BigDecimal dailyFuelEffort;
	
	public static DailyRate b(int to24h, int to48h, int to72h, int after72h, BigDecimal dailyFuelEffort) {
		return new DailyRate(to24h, to48h, to72h, after72h, dailyFuelEffort);
	}
	
	private DailyRate(int to24h, int to48h, int to72h, int after72h, BigDecimal dailyFuelEffort) {
		this.to24h = to24h;
		this.to48h = to48h;
		this.to72h = to72h;
		this.after72h = after72h;
		this.dailyFuelEffort = dailyFuelEffort;
	}

	public int getTo24h() {
		return to24h;
	}

	public int getTo48h() {
		return to48h;
	}

	public int getTo72h() {
		return to72h;
	}

	public int getAfter72h() {
		return after72h;
	}

	public BigDecimal getDailyFuelEffort() {
		return dailyFuelEffort;
	}

}
