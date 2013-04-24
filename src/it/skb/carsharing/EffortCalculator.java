package it.skb.carsharing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.time.DateUtils;

public class EffortCalculator {
	
	private static final double DAY_HOUR = 2.20;
	private static final double NIGHT_HOUR = 1.0;
	private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	
	private Range<Double> night;
	private Range<Double> day;
	
	private Integer kilometers;
	private Calendar from;
	private Calendar to;
	private CarType carType;
	
	public EffortCalculator() {
		night = Range.between(0d, 7d);
		day = Range.between(7d, 24d);
	}

	public BigDecimal execute() {
		BigDecimal result = BigDecimal.ZERO;
		
		if(DateUtils.isSameDay(from, to)) {
			int i = from.get(Calendar.HOUR_OF_DAY);
			int j = to.get(Calendar.HOUR_OF_DAY);
			int d = from.get(Calendar.MINUTE);
			int t = to.get(Calendar.MINUTE);

			result = calculateIntraDayRangeEffort(i, j, d, t);
			result = result.add(fuel(carType.getHr().getFuelEffort().doubleValue()));
		} else {
			final List<Date> dates = dailySplit(from.getTime(), to.getTime());
			int startHourDiffEndHour = startHourDiffEndHour(dates);
			
			if (startHourDiffEndHour == 0) {
				result = sameHour(dates);
			} else {
				result = sameHour(dates.subList(0, dates.size()-1));
				
				if (startHourDiffEndHour > 0) {
					{
						final Calendar ff = toCalendar(dates.get(dates.size()-2));
						
						int i = ff.get(Calendar.HOUR_OF_DAY);
						int j = 23;
						int d = ff.get(Calendar.MINUTE);
						int t = 59;
						
						result = result.add(calculateIntraDayRangeEffort(i, j, d, t));
					}
					{
						final Calendar tt = toCalendar(dates.get(dates.size()-1));
						
						int i = 0;
						int j = tt.get(Calendar.HOUR_OF_DAY);
						int d = 0;
						int t = tt.get(Calendar.MINUTE);
						
						result = result.add(calculateIntraDayRangeEffort(i, j, d, t));
					}
				} else {
					final Calendar ff = toCalendar(dates.get(dates.size()-2));
					final Calendar tt = toCalendar(dates.get(dates.size()-1));
					
					int i = ff.get(Calendar.HOUR_OF_DAY);
					int j = tt.get(Calendar.HOUR_OF_DAY);
					int d = ff.get(Calendar.MINUTE);
					int t = tt.get(Calendar.MINUTE);
					
					result = result.add(calculateIntraDayRangeEffort(i, j, d, t));
				}
			}
			
			result = result.add(fuel(carType.getDr().getDailyFuelEffort().doubleValue()));
		}
		
		return result.setScale(2, RoundingMode.HALF_DOWN);
	}

	private BigDecimal calculateIntraDayRangeEffort(int i, int j, int d, int t) {
		BigDecimal result = BigDecimal.ZERO;
		
		for (Range<Double> r : splitIntoRanges(i, j, d, t)) {
			result = result.add(
				hours(r.getMinimum(), r.getMaximum(), nightOrDay(r))
			);
		}
		
		return result;
	}
	
	private BigDecimal sameHour(List<Date> x) {
		BigDecimal result = BigDecimal.ZERO;
		
		/*
		 * TODO this logic must be placed into DailyRate.class !!?
		 */
		
		if (x.size() == 2) {
			result = new BigDecimal(carType.getDr().getTo24h());
		} else if (x.size() == 3) {
			result = new BigDecimal(carType.getDr().getTo48h());
		} else if (x.size() == 4) {
			result = new BigDecimal(carType.getDr().getTo72h());
		} else {
			result = new BigDecimal(carType.getDr().getTo72h());
			
			for (int i = 4; i < x.size(); i++) {
				result = result.add(
					new BigDecimal(carType.getDr().getAfter72h())
				);
			}
		}
		
		return result;
	}

	private int startHourDiffEndHour(List<Date> x) {
		final Calendar first = toCalendar(x.get(0));
		final Calendar last = toCalendar(x.get(x.size()-1));

		return first.get(Calendar.HOUR_OF_DAY) - last.get(Calendar.HOUR_OF_DAY);
	}

	private List<Date> dailySplit(Date from, Date to) {
		final List<Date> dateList = new LinkedList<Date>();
		
		for (long t = from.getTime(); t < to.getTime() ; t += DAY_IN_MILLIS) {
			dateList.add(new Date(t));
		}		
		
		dateList.add(to);
		
		return dateList;
	}	

	private double nightOrDay(Range<Double> r) {
		if (night.containsRange(r)) {
			return NIGHT_HOUR;
		} else if (day.containsRange(r)) {
			return DAY_HOUR;
		}		
		
		throw new RuntimeException("Can't found range types: " + r);
	}

	private BigDecimal hours(double i, double j, double m) {
		return new BigDecimal((j-i)*m);
	}
	
	private BigDecimal fuel(double d) {
		return new BigDecimal(d * kilometers);
	}

	private List<Range<Double>> splitIntoRanges(double i, double j, double d, double t) {
		Range<Double> iR = null;
		Range<Double> jR = null;
		
		if (night.contains(i)) {
			iR = Range.between(toDouble(i,d), night.getMaximum());
			
			if (night.contains(j)) {
				iR = Range.between(toDouble(i,d), toDouble(j,t));				
			}
		} else if (day.contains(i)) {
			iR = Range.between(toDouble(i,d), day.getMaximum());
			
			if (day.contains(j)) {
				iR = Range.between(toDouble(i,d), toDouble(j,t));
			}			
		}
		
		if (!iR.contains(j)) {
			if (night.contains(j)) {
				jR = Range.between(night.getMinimum(), toDouble(j,t));
			} else if (day.contains(j)) {
				jR = Range.between(day.getMinimum(), toDouble(j,t));
			}
		}
		
		final List<Range<Double>> r = new ArrayList<Range<Double>>();
		
		if (iR != null) {
			if (iR.getMinimum() != iR.getMaximum()) {
				r.add(iR);
			}
		}
		
		if (jR != null) {
			r.add(jR);
		}
		
		return r;
	}

	private Double toDouble(double i, double d) {
		return Double.valueOf(i + (d / 100));
	}
	
	private Calendar toCalendar(Date d) {
		final Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		return c;
	}

	public void setKilometers(Integer kilometers) {
		this.kilometers = kilometers;
	}

	public void setFrom(Calendar from) {
		this.from = from;
	}

	public void setTo(Calendar to) {
		this.to = to;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

}
