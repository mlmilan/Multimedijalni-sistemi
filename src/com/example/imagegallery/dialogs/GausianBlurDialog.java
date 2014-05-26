package com.example.imagegallery.dialogs;

import net.viralpatel.android.imagegalleray.R;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class GausianBlurDialog {
	public interface OnGausianBlurListener {
		void onCancel(GausianBlurDialog dialog);
		void onOk(GausianBlurDialog dialog, double sigma);
	}

	final AlertDialog dialog;
	final OnGausianBlurListener listener;
	final SeekBar sb;
	final TextView tv;
	private double progres = 0;

	public GausianBlurDialog(final Context context, OnGausianBlurListener listener) {
		this.listener = listener;

		final View view = LayoutInflater.from(context).inflate(R.layout.gausian_blur, null);
		
		sb = (SeekBar) view.findViewById(R.id.seekBarGausianBlur);
		tv = (TextView) view.findViewById(R.id.tvGausianBlur);
		
		tv.setText(sb.getProgress()+ "/2");
		sb.setOnSeekBarChangeListener(
		                new OnSeekBarChangeListener() {
							
		                	
		                	
							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								tv.setText(progres + "/2");
							}
							
							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								// TODO Auto-generated method stub
								progres =  (double)progress/100.0;
							}
						});


		

		dialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (GausianBlurDialog.this.listener != null) {
						GausianBlurDialog.this.listener.onOk(GausianBlurDialog.this, getProgres());
					}
				}

			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (GausianBlurDialog.this.listener != null) {
						GausianBlurDialog.this.listener.onCancel(GausianBlurDialog.this);
					}
				}
			})
			.setOnCancelListener(new OnCancelListener() {
				// if back button is used, call back our listener.
				@Override public void onCancel(DialogInterface paramDialogInterface) {
					if (GausianBlurDialog.this.listener != null) {
						GausianBlurDialog.this.listener.onCancel(GausianBlurDialog.this);
					}

				}
			})
			.create();
		// kill all padding from the dialog window
		dialog.setView(view, 0, 0, 0, 0);

	}
	
	private double getProgres() {
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