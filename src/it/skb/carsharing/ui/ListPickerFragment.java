package it.skb.carsharing.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

@SuppressLint("ValidFragment")
public class ListPickerFragment extends DialogFragment {
	
	private final NumberPicker numberPicker;
	private final MainActivity ma;
	private EditText et;

	public ListPickerFragment(MainActivity ma) {
		this.ma = ma;
		
		numberPicker = new NumberPicker(ma);
		numberPicker.setMinValue(1);
		numberPicker.setMaxValue(999);
		numberPicker.setValue(10);
		
		//the number child must not be modifiable
		int childCount = numberPicker.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childAt = numberPicker.getChildAt(i);
			if (childAt instanceof EditText) {
				et = (EditText)childAt;
				et.setFocusable(false);
				break;
			}
		}
		
		if (et == null) {
			throw new RuntimeException("ERROR: can't instantiate NumberPicker");
		}
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
			.setTitle("Kilometers")
			.setView(numberPicker)
			.setPositiveButton("Set", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,int paramInt) {
					ma.setKilometers(getTag(), et.getText().toString());
				}
			})
			.setNegativeButton("Cancel", null)
			.create();
    }
}
