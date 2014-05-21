package net.viralpatel.android.imagegalleray.colorpicker;

import net.viralpatel.android.imagegalleray.R;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BrightnessDialog {
	public interface OnBrightnessListener {
		void onCancel(BrightnessDialog dialog);
		void onOk(BrightnessDialog dialog, int color);
	}

	final AlertDialog dialog;
	final OnBrightnessListener listener;
	final SeekBar sb;
	final TextView tv;
	private int progres = -255;

	public BrightnessDialog(final Context context, OnBrightnessListener listener) {
		this.listener = listener;

		final View view = LayoutInflater.from(context).inflate(R.layout.brightness, null);
		
		sb = (SeekBar) view.findViewById(R.id.seekBarBrightness);
		tv = (TextView) view.findViewById(R.id.tvBrightness);
		
		tv.setText(sb.getProgress()-255 + "/(-255..255)");
		sb.setOnSeekBarChangeListener(
		                new OnSeekBarChangeListener() {
							
		                	
		                	
							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								tv.setText(progres + "/(-255..255)");
							}
							
							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								// TODO Auto-generated method stub
								progres = progress - 255;
							}
						});


		

		dialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (BrightnessDialog.this.listener != null) {
						BrightnessDialog.this.listener.onOk(BrightnessDialog.this, getProgres());
					}
				}

			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (BrightnessDialog.this.listener != null) {
						BrightnessDialog.this.listener.onCancel(BrightnessDialog.this);
					}
				}
			})
			.setOnCancelListener(new OnCancelListener() {
				// if back button is used, call back our listener.
				@Override public void onCancel(DialogInterface paramDialogInterface) {
					if (BrightnessDialog.this.listener != null) {
						BrightnessDialog.this.listener.onCancel(BrightnessDialog.this);
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