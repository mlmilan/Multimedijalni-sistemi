package com.example.imagegallery.dialogs;

import net.viralpatel.android.imagegalleray.R;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SaturationDialog {
	public interface OnSaturationListener {
		void onCancel(SaturationDialog dialog);
		void onOk(SaturationDialog dialog, float saturation);
	}

	final AlertDialog dialog;
	final OnSaturationListener listener;
	final SeekBar sb;
	final TextView tv;
	private float progres = 0;

	public SaturationDialog(final Context context, OnSaturationListener listener) {
		this.listener = listener;

		final View view = LayoutInflater.from(context).inflate(R.layout.saturation, null);
		
		sb = (SeekBar) view.findViewById(R.id.seekBarSaturation);
		tv = (TextView) view.findViewById(R.id.tvSaturation);
		
		tv.setText(sb.getProgress()/1000 + "/1");
		sb.setOnSeekBarChangeListener(
		                new OnSeekBarChangeListener() {
							
		                	
		                	
							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								tv.setText(progres + "/1");
							}
							
							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								// TODO Auto-generated method stub
								progres = (float) (progress/1000.0);
							}
						});


		

		dialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (SaturationDialog.this.listener != null) {
						SaturationDialog.this.listener.onOk(SaturationDialog.this, getProgres());
					}
				}

			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (SaturationDialog.this.listener != null) {
						SaturationDialog.this.listener.onCancel(SaturationDialog.this);
					}
				}
			})
			.setOnCancelListener(new OnCancelListener() {
				// if back button is used, call back our listener.
				@Override public void onCancel(DialogInterface paramDialogInterface) {
					if (SaturationDialog.this.listener != null) {
						SaturationDialog.this.listener.onCancel(SaturationDialog.this);
					}

				}
			})
			.create();
		// kill all padding from the dialog window
		dialog.setView(view, 0, 0, 0, 0);

	}
	
	private float getProgres() {
		// TODO Auto-generated method stub
		return progres;
	}

	public void show() {
		dialog.show();
	}

	public AlertDialog getDialog() {
		return dialog;
	}
}