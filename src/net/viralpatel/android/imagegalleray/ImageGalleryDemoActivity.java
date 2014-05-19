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
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImageGalleryDemoActivity extends Activity {
    
	private String picturePath;
    private Bitmap changeBitmap = null;
    private Bitmap originalBitmap = null;
    private ImageView imageView, imageViewResized, imageViewResized1, imageViewResized2, imageViewResized3,
    imageViewResized4, imageViewResized5, imageViewResized6;
    private DrawOnTop mDrawOnTop;
	private long tStart, tEnd, tElapsed1, tElapsed2, bmpSize1, bmpSize2;
	private boolean first = true;
	private int color = 0;
	public static final String PREFS_NAME = "picturePath";
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters);
        
       // Button filter = (Button) findViewById(R.id.buttonFilter);
       // Button histogram = (Button) findViewById(R.id.buttonHistogram);
       // Button grafik = (Button) findViewById(R.id.buttonGrafik);
       // Button colorPicker = (Button) findViewById(R.id.buttonColorPicker);
        imageView = (ImageView) findViewById(R.id.imgView);
        imageViewResized = (ImageView) findViewById(R.id.imgViewProba);
        imageViewResized1 = (ImageView) findViewById(R.id.imgViewProba1);
        imageViewResized2 = (ImageView) findViewById(R.id.imgViewProba2);
        imageViewResized3 = (ImageView) findViewById(R.id.imgViewProba3);
        imageViewResized4 = (ImageView) findViewById(R.id.imgViewProba4);
        imageViewResized5 = (ImageView) findViewById(R.id.imgViewProba5);
        imageViewResized6 = (ImageView) findViewById(R.id.imgViewProba6);
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        picturePath = settings.getString("putanja", "");
        
        
		File imageF = new File(picturePath);
		try {
			BitmapScaler scaler = new BitmapScaler(imageF, 512);
			originalBitmap = scaler.getScaled();

                ExifInterface exif = new ExifInterface(picturePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                }
                else if (orientation == 3) {
                    matrix.postRotate(180);
                }
                else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true); // rotating bitmap
           
            if (originalBitmap.getWidth() <= originalBitmap.getHeight())
            	imageView.setScaleType(ScaleType.CENTER_CROP);
            else 
            	imageView.setScaleType(ScaleType.FIT_CENTER);
			imageView.setImageBitmap(originalBitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		createSmallerImage();
		
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            color = extras.getInt("color");
            //Toast.makeText(getBaseContext(), "Boja: " + color, Toast.LENGTH_LONG).show();
            
            imageView = (ImageView) findViewById(R.id.imgView);
			File imageFile = new File(picturePath);
			try {
				BitmapScaler scaler = new BitmapScaler(imageFile, 512);
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
        
        imageViewResized.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeBitmap = Filter.invert(originalBitmap);
				originalBitmap.recycle();
				originalBitmap = null;
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = changeBitmap;
			}
		});
        /*
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
				FrameLayout layout = (FrameLayout) findViewById(R.id.frame);
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
		*/
    }

	private void createSmallerImage() {
		// TODO Auto-generated method stub

		Bitmap resized = Bitmap.createScaledBitmap(originalBitmap ,(int)(originalBitmap.getWidth()*0.4), (int)(originalBitmap.getHeight()*0.4), true);
		Bitmap resizedFilter = Filter.invert(resized);
		imageViewResized.setImageBitmap(resizedFilter);
		imageViewResized1.setImageBitmap(resizedFilter);
		imageViewResized2.setImageBitmap(resizedFilter);
		imageViewResized3.setImageBitmap(resizedFilter);
		imageViewResized4.setImageBitmap(resizedFilter);
		imageViewResized5.setImageBitmap(resizedFilter);
		imageViewResized6.setImageBitmap(resizedFilter);
	}
    
	    
}