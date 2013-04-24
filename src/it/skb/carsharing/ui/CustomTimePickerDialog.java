package it.skb.carsharing.ui;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

public class CustomTimePickerDialog extends TimePickerDialog {

	private static final int TIME_PICKER_INTERVAL = 15;
	private boolean ignoreChangeEvent = false;

	public CustomTimePickerDialog(Context context, OnTimeSetListener callBack) {
		super(context, callBack, Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1, 0, true);
	}

	@Override
	public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
		super.onTimeChanged(timePicker, hourOfDay, minute);

		if (!ignoreChangeEvent) {
			ignoreChangeEvent = true;
			timePicker.setCurrentMinute(getRoundedMinute(minute));
			ignoreChangeEvent = false;
		}
	}

	private int getRoundedMinute(int minute) {
		int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
		int m = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);

		if (m == 60) {
			m = 0;
		}

		return m;
	}
}
