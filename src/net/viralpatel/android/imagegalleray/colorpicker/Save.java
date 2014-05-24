package net.viralpatel.android.imagegalleray.colorpicker;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.viralpatel.android.imagegalleray.ImageGalleryDemoActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Save {
	
	private Context ccontext;
	private String nameOfFolder = "/filters";
	private String nameOfFile = "filterImage";

	public void saveImage(Context context,
			Bitmap originalBitmap) {
		// TODO Auto-generated method stub
		ccontext = context;
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + nameOfFolder;
		String currentDateAndTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		File dir = new File(file_path);
		if (!dir.exists())
			dir.mkdirs();
		
		File file = new File(dir, nameOfFile + currentDateAndTime + ".jpg");
		
		try {
			FileOutputStream fOut = new FileOutputStream(file);
			originalBitmap.compress(CompressFormat.JPEG, 85, fOut);
			fOut.flush();
			fOut.close();
			makeSureTheFileWasCreated(file);
			Toast.makeText(ccontext, "Slika je uspesno sacuvana u galeriji telefona.", Toast.LENGTH_LONG).show();
			
		}
		catch (Exception e) {
			Toast.makeText(ccontext, "Doslo je do greske! Slika nije sacuvana u galeriji telefona.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
	}

	private void makeSureTheFileWasCreated(File file) {
		// TODO Auto-generated method stub
		MediaScannerConnection.scanFile(ccontext, new String[] {file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
			
			@Override
			public void onScanCompleted(String path, Uri uri) {
				// TODO Auto-generated method stub
				Log.d("Path: ", path);
				Log.d("Uri: ", uri.toString());
			}
		});
	}

}
