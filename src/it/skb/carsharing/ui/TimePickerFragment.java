package it.skb.carsharing.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.widget.TimePicker;

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {
	
	private MainActivity ma;
	
	public TimePickerFragment(MainActivity ma) {
		this.ma = ma;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		ma.setTime(getTag(), hourOfDay, minute);		
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new CustomTimePickerDialog(getActivity(), this);
    }
}
