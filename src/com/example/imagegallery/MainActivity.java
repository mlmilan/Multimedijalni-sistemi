package com.example.imagegallery;


import net.viralpatel.android.imagegalleray.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    
	
	private static int RESULT_LOAD_IMAGE = 1;
	private String picturePath;
	public static final String PREFS_NAME = "picturePath";
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Thread timer = new Thread() {
			
			public void run() {
				try {
					sleep(5000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				finally {
					Intent openStartingPoint = new Intent(MainActivity.this, ImageGalleryDemoActivity.class);  
					startActivity(openStartingPoint);  
				}
			}
		};
		
		timer.start();
        
        /*
        
        Bitmap buttonBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.load_image);
        
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        
        buttonLoadImage.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(this.getResources(), buttonBitmap), null, null);

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		
		*/
    }
    
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			Log.d("PUTANJA", picturePath);
			cursor.close();

			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putString("putanja", picturePath);

		      // Commit the edits!
		    editor.commit();
		    
		    Intent i = new Intent(this, ImageGalleryDemoActivity.class);
		    startActivity(i);
		
		}
    }
    
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					MainActivity.this);

			// set title
			alertDialogBuilder.setTitle("Zelite da napustite aplikaciju?");

			// set dialog message
			alertDialogBuilder
					.setMessage(
							"Da li zelite da zavrsite rad sa aplikacijom?")
					.setCancelable(false)
					.setPositiveButton("DA",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
								}
							}).setNegativeButton("NE",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
	    */
    
    @Override
	protected void onPause() {  
		// TODO Auto-generated method stub
		super.onPause();
		finish(); 
	}
    
}