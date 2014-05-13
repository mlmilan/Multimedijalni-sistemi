package net.viralpatel.android.imagegalleray;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.widget.Button;
import android.widget.ImageView;

public class ImageGalleryDemoActivity extends Activity {
    
	
	private static int RESULT_LOAD_IMAGE = 1;
	private String picturePath;
    private Bitmap changeBitmap = null;
    private Bitmap originalBitmap = null;
    private ImageView imageView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        Button filter = (Button) findViewById(R.id.buttonFilter);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
        filter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//new Jpeg(picturePath);
				//File outFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.jpg");
				changeBitmap = Filter.gausianBlur(originalBitmap, 10, 5);
				originalBitmap.recycle();
				originalBitmap = null;
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = changeBitmap;
				
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
			cursor.close();
			
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
			//originalBitmap = BitmapFactory.decodeFile(picturePath);
			//int nh = (int) ( originalBitmap.getHeight() * (512.0 / originalBitmap.getWidth()) );
			//Bitmap scaled = Bitmap.createScaledBitmap(originalBitmap, 512, nh, true);
			//originalBitmap = scaled;
			//imageView.setImageBitmap(scaled);
			//imageView.setImageBitmap(originalBitmap);
		
		}
    
    
    }
}