package net.viralpatel.android.imagegalleray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import junit.framework.Test;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImageGalleryDemoActivity extends Activity {
    
	
	private static int RESULT_LOAD_IMAGE = 1;
	private String picturePath;
    private Bitmap changeBitmap = null;
    private Bitmap originalBitmap = null;
    private ImageView imageView;
    private DrawOnTop mDrawOnTop;
	private long tStart, tEnd, tElapsed1, tElapsed2, bmpSize1, bmpSize2;
	private boolean first = true;
	private int color = 0;
	public static final String PREFS_NAME = "picturePath";
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        Button filter = (Button) findViewById(R.id.buttonFilter);
        Button histogram = (Button) findViewById(R.id.buttonHistogram);
        Button grafik = (Button) findViewById(R.id.buttonGrafik);
        Button colorPicker = (Button) findViewById(R.id.buttonColorPicker);
        
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String putanja = settings.getString("putanja", "");
        
        
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
				
				
				tStart = System.currentTimeMillis();
				//changeBitmap = Filter.gausianBlur(originalBitmap, 10, 5);
				//changeBitmap = Filter.blackWhite(originalBitmap, 10);
				//changeBitmap = Filter.brightness(originalBitmap, -100);
				//changeBitmap = Filter.contrast(originalBitmap, 50);
				//changeBitmap = Filter.flipVertical(originalBitmap);
				//changeBitmap = Filter.flipHorizontal(originalBitmap);
				//changeBitmap = Filter.grayscale(originalBitmap);
				//changeBitmap = Filter.invert(originalBitmap);
				//changeBitmap = Filter.gammaCorection(originalBitmap, 1.8, 1.8, 1.8);
				//changeBitmap = Filter.colorFilter(originalBitmap, 1, 0, 0);
				//changeBitmap = Filter.saturationFilter(originalBitmap, 2);
				//changeBitmap = Filter.hueFilter(originalBitmap, 10);
				//changeBitmap = Filter.shadingFilter(originalBitmap, color);
				//changeBitmap = Filter.sharpen(originalBitmap);
				//changeBitmap = Filter.blur(originalBitmap);
				//changeBitmap = Filter.edge(originalBitmap);
				//changeBitmap = Filter.emboss(originalBitmap);
				//changeBitmap = Filter.engraving(originalBitmap);
				changeBitmap = Filter.smooth(originalBitmap);
				tEnd = System.currentTimeMillis();
				if (first) {
					tElapsed1 = (tEnd - tStart);
					//bmpSize1 = originalBitmap.getByteCount();
					File f1 = new File(picturePath);
					bmpSize1 = f1.length();
					
					first = false;
					Log.d("prva tacka", "" + tElapsed1 + " time" + f1.length() + " size");
				}
				else {
					tElapsed2 = (tEnd - tStart);
					//bmpSize2 = originalBitmap.getByteCount();
					File f2 = new File(picturePath);
					bmpSize2 = f2.length();
					Log.d("druga tacka", "" + tElapsed2 + " time" + f2.length() + " size");
				}
				originalBitmap.recycle();
				originalBitmap = null;
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = changeBitmap;
				
			}
		});
        histogram.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDrawOnTop = new DrawOnTop(ImageGalleryDemoActivity.this, originalBitmap);
				ImageGalleryDemoActivity.this.addContentView(mDrawOnTop, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
		});
        

        
        
				
        grafik.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// graph with dynamically genereated horizontal and vertical labels
				GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
		                new GraphViewData(tElapsed1, bmpSize1)
		                , new GraphViewData(tElapsed2, bmpSize2)
		        });
		        
		        GraphView graphView;
		        graphView = new LineGraphView(
		                    ImageGalleryDemoActivity.this // context
		                    , "GraphViewDemo" // heading
		            );
		         //   ((LineGraphView) graphView).setDrawDataPoints(true);
		         //   ((LineGraphView) graphView).setDataPointsRadius(15f);
		        

		        graphView.addSeries(exampleSeries); // data
				RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl);
				layout.addView(graphView);
			}
		});
        
        colorPicker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ImageGalleryDemoActivity.this, net.viralpatel.android.imagegalleray.colorpicker.Test.class);
				startActivity(i);
				
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
			
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putString("putanja", picturePath);

		      // Commit the edits!
		    editor.commit();
		
		}
    }
	    
}