package com.example.imagegallery.dialogs;

import net.viralpatel.android.imagegalleray.R;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BlackWhiteDialog {
	public interface OnBlackWhiteListener {
		void onCancel(BlackWhiteDialog dialog);
		void onOk(BlackWhiteDialog dialog, int color);
	}

	final AlertDialog dialog;
	final OnBlackWhiteListener listener;
	final SeekBar sb;
	final TextView tv;
	private int progres = 0;

	public BlackWhiteDialog(final Context context, OnBlackWhiteListener listener) {
		this.listener = listener;

		final View view = LayoutInflater.from(context).inflate(R.layout.black_white, null);
		
		sb = (SeekBar) view.findViewById(R.id.seekBarBlackWhite);
		tv = (TextView) view.findViewById(R.id.textViewBlackWhite);
		
		tv.setText(sb.getProgress() + "/" + sb.getMax());
		sb.setOnSeekBarChangeListener(
		                new OnSeekBarChangeListener() {
							
		                	
		                	
							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								tv.setText(progres + "/" + sb.getMax());
							}
							
							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								// TODO Auto-generated method stub
								progres = progress;
							}
						});


		

		dialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (BlackWhiteDialog.this.listener != null) {
						BlackWhiteDialog.this.listener.onOk(BlackWhiteDialog.this, getProgres());
					}
				}

			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (BlackWhiteDialog.this.listener != null) {
						BlackWhiteDialog.this.listener.onCancel(BlackWhiteDialog.this);
					}
				}
			})
			.setOnCancelListener(new OnCancelListener() {
				// if back button is used, call back our listener.
				@Override public void onCancel(DialogInterface paramDialogInterface) {
					if (BlackWhiteDialog.this.listener != null) {
						BlackWhiteDialog.this.listener.onCancel(BlackWhiteDialog.this);
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