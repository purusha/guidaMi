package it.skb.carsharing.ui;

import it.skb.carsharing.CarType;
import it.skb.carsharing.EffortCalculator;
import it.skb.carsharing.R;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	final public int ABOUT = 0;

	private Map<String, Pair<Integer, Integer>> times = new HashMap<String, Pair<Integer, Integer>>();
	private Map<String, Calendar> dates = new HashMap<String, Calendar>();
	private Integer kilometers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final MainActivity ma = this;
		
		final LinearLayout from = (LinearLayout)findViewById(R.id.textView3).getParent();		
		from.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				new DatePickerFragment(ma).show(getFragmentManager(), String.valueOf(R.id.textView3));
			}
		});
		
		final LinearLayout to = (LinearLayout)findViewById(R.id.TextView03).getParent();	
		to.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				new DatePickerFragment(ma).show(getFragmentManager(), String.valueOf(R.id.TextView03));
			}
		});

		final LinearLayout kilometers = (LinearLayout)findViewById(R.id.TextView04).getParent();	
		kilometers.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				new ListPickerFragment(ma).show(getFragmentManager(), String.valueOf(R.id.TextView04));
			}
		});
		
		final Button calculate = (Button)findViewById(R.id.button1);
		calculate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View paramView) {
				new ValidatorFields(ma).execute();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ABOUT, 0, "About");
		
		return true;	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case ABOUT: {
				new AboutFragment().show(getFragmentManager(), "");
			} break;
		}
		
		return true;
	}

	public void setTime(String tag, int hourOfDay, int minute) {
		final Pair<Integer, Integer> pair = Pair.create(hourOfDay, minute);
		times.put(tag, pair);
		
		for (String key : times.keySet()) {
			final Calendar c = buildCalendarFor(key);
			final TextView tw = (TextView)findViewById(Integer.valueOf(key));
			
			tw.setText(
				String.format(
					"%02d-%02d-%s %02d:%02d", 
					c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR), 
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)
				) 
			);
		}
	}
	
	public void setDate(String tag, int year, int monthOfYear, int dayOfMonth) {
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, monthOfYear);
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		
		dates.put(tag, c);   
	}
	
	public void setKilometers(String tag, String k) {
		this.kilometers = Integer.parseInt(k);
		
		final TextView tw = (TextView)findViewById(Integer.valueOf(tag));
		tw.setText(k);		
	}
	
	private Calendar buildCalendarFor(String tag) {
		final Calendar c = dates.get(tag);
		final Pair<Integer, Integer> pair = times.get(tag);
		
		c.set(Calendar.HOUR_OF_DAY, pair.first);
		c.set(Calendar.MINUTE, pair.second);
        
        return c;
	}

	public BigDecimal calculateEffort() {
		final EffortCalculator ec = new EffortCalculator();
		
		ec.setFrom(buildCalendarFor(String.valueOf(R.id.textView3)));
		ec.setTo(buildCalendarFor(String.valueOf(R.id.TextView03)));
		ec.setKilometers(kilometers);
		
		final String ct = String.valueOf(((Spinner)findViewById(R.id.spinner1)).getSelectedItem());
		ec.setCarType(CarType.secureValueOf(ct));
		
		return ec.execute();
	}
}
