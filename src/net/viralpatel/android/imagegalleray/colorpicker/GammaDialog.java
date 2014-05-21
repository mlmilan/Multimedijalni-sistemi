package net.viralpatel.android.imagegalleray.colorpicker;

import net.viralpatel.android.imagegalleray.R;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class GammaDialog {
	public interface OnGammaListener {
		void onCancel(GammaDialog dialog);
		void onOk(GammaDialog dialog, double color);
	}

	final AlertDialog dialog;
	final OnGammaListener listener;
	final SeekBar sb;
	final TextView tv;
	private double progres = 0;

	public GammaDialog(final Context context, OnGammaListener listener) {
		this.listener = listener;

		final View view = LayoutInflater.from(context).inflate(R.layout.gamma, null);
		
		sb = (SeekBar) view.findViewById(R.id.seekBarGamma);
		tv = (TextView) view.findViewById(R.id.tvGamma);
		
		tv.setText(sb.getProgress()/10 + "/5");
		sb.setOnSeekBarChangeListener(
		                new OnSeekBarChangeListener() {
							
		                	
		                	
							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								tv.setText(progres + "/5");
							}
							
							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								// TODO Auto-generated method stub
								progres = (double)progress/10.0;
							}
						});


		

		dialog = new AlertDialog.Builder(context)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (GammaDialog.this.listener != null) {
						GammaDialog.this.listener.onOk(GammaDialog.this, getProgres());
						Log.d("progres:", Double.toString(getProgres()));
					}
				}

			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override public void onClick(DialogInterface dialog, int which) {
					if (GammaDialog.this.listener != null) {
						GammaDialog.this.listener.onCancel(GammaDialog.this);
					}
				}
			})
			.setOnCancelListener(new OnCancelListener() {
				// if back button is used, call back our listener.
				@Override public void onCancel(DialogInterface paramDialogInterface) {
					if (GammaDialog.this.listener != null) {
						GammaDialog.this.listener.onCancel(GammaDialog.this);
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