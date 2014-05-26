package com.example.imagegallery.dialogs;

import net.viralpatel.android.imagegalleray.R;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ColorDialog {
	public interface OnColorListener {
		void onCancel(ColorDialog dialog);
		void onOk(ColorDialog dialog, double red, double green, double blue);
	}

	final AlertDialog dialog;
	final OnColorListener listener;
	final SeekBar sbRed, sbGreen, sbBlue;
	final TextView tvRed, tvGreen, tvBlue;
	private double progresRed = 0, progresGreen = 0, progresBlue = 0;

	public ColorDialog(final Context context, OnColorListener listener) {
		this.listener = listener;

		final View view = LayoutInflater.from(context).inflate(R.layout.color, null);
		
		sbRed = (SeekBar) view.findViewById(R.id.seekBarColorRed);
		sbGreen = (SeekBar) view.findViewById(R.id.seekBarColorGreen);
		sbBlue = (SeekBar) view.findViewById(R.id.seekBarColorBlue);
		tvRed = (TextView) view.findViewById(R.id.tvColorRed);
		tvGreen = (TextView) view.findViewById(R.id.tvColorGreen);
		tvBlue = (TextView) view.findViewById(R.id.tvColorBlue);
		
		tvRed.setText(sbRed.getProgress()/10 + "/1");
		tvGreen.setText(sbGreen.getProgress()/10 + "/1");
		tvBlue.setText(sbBlue.getProgress()/10 + "/1");
		
		sbRed.setOnSeekBarChangeListener(
		                new OnSeekBarChangeListener() {
							
		                	
		                	
							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								tvRed.setText(progresRed + "/1");
							}
							
							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								// TODO Auto-generated method stub
								progresRed = (double)progress/10.0;
							}
						});

		sbGreen.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
					
                	
                	
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						tvGreen.setText(progresGreen + "/1");
					}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						// TODO Auto-generated method stub
						progresGreen = (double)progress/10.0;
					}
				});
		
		sbBlue.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
					
                	
                	
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						tvBlue.setText(progresBlue + "/1");
					}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						// TODO Auto-generated method stub
						progresBlue = (double)progress/10.0;
					}
				});

		

		dialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (ColorDialog.this.listener != null) {
						ColorDialog.this.listener.onOk(ColorDialog.this, getProgresRed(), getProgresGreen(), getProgresBlue());
					}
				}

			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (ColorDialog.this.listener != null) {
						ColorDialog.this.listener.onCancel(ColorDialog.this);
					}
				}
			})
			.setOnCancelListener(new OnCancelListener() {
				// if back button is used, call back our listener.
				@Override public void onCancel(DialogInterface paramDialogInterface) {
					if (ColorDialog.this.listener != null) {
						ColorDialog.this.listener.onCancel(ColorDialog.this);
					}

				}
			})
			.create();
		// kill all padding from the dialog window
		dialog.setView(view, 0, 0, 0, 0);

	}
	
	private double getProgresRed() {
		// TODO Auto-generated method stub
		return progresRed;
	}
	
	private double getProgresGreen() {
		// TODO Auto-generated method stub
		return progresGreen;
	}
	
	private double getProgresBlue() {
		// TODO Auto-generated method stub
		return progresBlue;
	}

	public void show() {
		dialog.show();
	}

	public AlertDialog getDialog() {
		return dialog;
	}
}