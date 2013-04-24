package it.skb.carsharing.ui;

import it.skb.carsharing.R;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ValidatorFields {
	
	private final MainActivity mainActivity;

	public ValidatorFields(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public void execute() {
		String message = null;
		
		final TextView fromReservation = (TextView)mainActivity.findViewById(R.id.textView3);
		final TextView toReservation = (TextView)mainActivity.findViewById(R.id.TextView03);
		final TextView kilometers = (TextView)mainActivity.findViewById(R.id.TextView04);
		
		final String fromReservationAsString = String.valueOf(fromReservation.getText());
		final String toReservationAsString = String.valueOf(toReservation.getText());
		final String kilometersAsString = String.valueOf(kilometers.getText());
		
		if (StringUtils.isBlank(fromReservationAsString)) {
			message = "The Reservation from field is mandatory";
		} else if (StringUtils.isBlank(toReservationAsString)) {
			message = "The Reservation to field is mandatory";
		} else if(!isAfterNow(fromReservationAsString)) {
			message = "Incorretct Reservation from field";
		} else if(!isAfterNow(toReservationAsString)) {
			message = "Incorretct Reservation to field";
		} else if (!toDate(fromReservationAsString).before(toDate(toReservationAsString))) {
			message = "The Reservation from must be before of Reservation to";
		} else if (StringUtils.isBlank(kilometersAsString)) {
			message = "Kilometer field is mandatory";
		}
		
		if (message != null) {
			Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show();
		} else {
			final BigDecimal effort = mainActivity.calculateEffort();
			
			new AlertDialog.Builder(mainActivity)
				.setTitle("Cost")
				.setMessage(effort + " € (without VAT)")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})
				.create()
				.show();
		}
	}

	@SuppressLint("SimpleDateFormat")
	private boolean isAfterNow(final String s) {
		final Date d = toDate(s);
		
		return d.after(new Date());
	}
	
	private Date toDate(final String s){
		try {
			return new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ITALY).parse(s);
		} catch (ParseException e) {
			Log.e("", e.getMessage());
		}
		
		return null;
	}

}
