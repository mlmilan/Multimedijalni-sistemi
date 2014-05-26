package com.example.imagegallery.dialogs;

import net.viralpatel.android.imagegalleray.R;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ContrastDialog {
	public interface OnContrastListener {
		void onCancel(ContrastDialog dialog);
		void onOk(ContrastDialog dialog, int color);
	}

	final AlertDialog dialog;
	final OnContrastListener listener;
	final SeekBar sb;
	final TextView tv;
	private int progres = -100;

	public ContrastDialog(final Context context, OnContrastListener listener) {
		this.listener = listener;

		final View view = LayoutInflater.from(context).inflate(R.layout.contrast, null);
		
		sb = (SeekBar) view.findViewById(R.id.seekBarContrast);
		tv = (TextView) view.findViewById(R.id.tvContrast);
		
		tv.setText(sb.getProgress()-100 + "/(-100..100)");
		sb.setOnSeekBarChangeListener(
		                new OnSeekBarChangeListener() {
							
		                	
		                	
							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								tv.setText(progres + "/(-100..100)");
							}
							
							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								// TODO Auto-generated method stub
								progres = progress - 100;
							}
						});


		

		dialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (ContrastDialog.this.listener != null) {
						ContrastDialog.this.listener.onOk(ContrastDialog.this, getProgres());
						Log.d("progres:", Integer.toString(getProgres()));
					}
				}

			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (ContrastDialog.this.listener != null) {
						ContrastDialog.this.listener.onCancel(ContrastDialog.this);
					}
				}
			})
			.setOnCancelListener(new OnCancelListener() {
				// if back button is used, call back our listener.
				@Override public void onCancel(DialogInterface paramDialogInterface) {
					if (ContrastDialog.this.listener != null) {
						ContrastDialog.this.listener.onCancel(ContrastDialog.this);
					}

				}
			})
			.create();
		// kill all padding from the dialog window
		dialog.setView(view, 0, 0, 0, 0);

	}
	
	private int getProgres() {
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