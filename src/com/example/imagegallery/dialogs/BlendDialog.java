package com.example.imagegallery.dialogs;

import net.viralpatel.android.imagegalleray.R;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BlendDialog {
	public interface OnBlendListener {
		void onCancel(BlendDialog dialog);
		void onOk(BlendDialog dialog, float blend);
	}

	final AlertDialog dialog;
	final OnBlendListener listener;
	final SeekBar sb;
	final TextView tv;
	private float progres = 0;

	public BlendDialog(final Context context, OnBlendListener listener) {
		this.listener = listener;

		final View view = LayoutInflater.from(context).inflate(R.layout.blend, null);
		
		sb = (SeekBar) view.findViewById(R.id.seekBarBlend);
		tv = (TextView) view.findViewById(R.id.tvBlend);
		
		tv.setText(sb.getProgress()/100 + "/1");
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
								progres = (float) (progress/100.0);
							}
						});


		

		dialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (BlendDialog.this.listener != null) {
						BlendDialog.this.listener.onOk(BlendDialog.this, getProgres());
					}
				}

			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (BlendDialog.this.listener != null) {
						BlendDialog.this.listener.onCancel(BlendDialog.this);
					}
				}
			})
			.setOnCancelListener(new OnCancelListener() {
				// if back button is used, call back our listener.
				@Override public void onCancel(DialogInterface paramDialogInterface) {
					if (BlendDialog.this.listener != null) {
						BlendDialog.this.listener.onCancel(BlendDialog.this);
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