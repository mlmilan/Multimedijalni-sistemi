package net.viralpatel.android.imagegalleray.colorpicker;

import net.viralpatel.android.imagegalleray.R;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class HueDialog {
	public interface OnHueListener {
		void onCancel(HueDialog dialog);
		void onOk(HueDialog dialog, int hue);
	}

	final AlertDialog dialog;
	final OnHueListener listener;
	final SeekBar sb;
	final TextView tv;
	private int progres = 0;

	public HueDialog(final Context context, OnHueListener listener) {
		this.listener = listener;

		final View view = LayoutInflater.from(context).inflate(R.layout.hue, null);
		
		sb = (SeekBar) view.findViewById(R.id.seekBarHue);
		tv = (TextView) view.findViewById(R.id.tvHue);
		
		tv.setText(sb.getProgress()+ "/360");
		sb.setOnSeekBarChangeListener(
		                new OnSeekBarChangeListener() {
							
		                	
		                	
							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								tv.setText(progres + "/360");
							}
							
							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								// TODO Auto-generated method stub
								progres =  progress;
							}
						});


		

		dialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (HueDialog.this.listener != null) {
						HueDialog.this.listener.onOk(HueDialog.this, getProgres());
					}
				}

			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (HueDialog.this.listener != null) {
						HueDialog.this.listener.onCancel(HueDialog.this);
					}
				}
			})
			.setOnCancelListener(new OnCancelListener() {
				// if back button is used, call back our listener.
				@Override public void onCancel(DialogInterface paramDialogInterface) {
					if (HueDialog.this.listener != null) {
						HueDialog.this.listener.onCancel(HueDialog.this);
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