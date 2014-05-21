package net.viralpatel.android.imagegalleray;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
        
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);

        
        /*
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            color = extras.getInt("color");
            //Toast.makeText(getBaseContext(), "Boja: " + color, Toast.LENGTH_LONG).show();
            
            imageView = (ImageView) findViewById(R.id.imgView);
			File imageF = new File(putanja);
			try {
				BitmapScaler scaler = new BitmapScaler(imageF, 512);
				originalBitmap = scaler.getScaled();
				imageView.setImageBitmap(scaler.getScaled());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			changeBitmap = Filter.shadingFilter(originalBitmap, color);
			originalBitmap.recycle();
			originalBitmap = null;
			imageView.setImageBitmap(changeBitmap);
			originalBitmap = changeBitmap;
        }
        */
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
    }
    
    
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
			/*
			imageView = (ImageView) findViewById(R.id.imgView);
			File imageF = new File(picturePath);
			try {
				BitmapScaler scaler = new BitmapScaler(imageF, 512);
				originalBitmap = scaler.getScaled();
				imageView.setImageBitmap(scaler.getScaled());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
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
	    
}