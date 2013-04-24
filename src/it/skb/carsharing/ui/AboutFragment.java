package it.skb.carsharing.ui;

import it.skb.carsharing.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class AboutFragment extends DialogFragment {
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Activity activity = getActivity();
		final LayoutInflater inflater = activity.getLayoutInflater();
		
		return new AlertDialog.Builder(activity)
			.setView(inflater.inflate(R.layout.about, null))
			.setCancelable(false)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface d,int paramInt) {						
					d.cancel();
				}
			})
			.create();	
	}
}
