package com.example.imagegallery;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.imagegallery.dialogs.BlendDialog;
import com.example.imagegallery.dialogs.BlendDialog.OnBlendListener;
import com.example.imagegallery.dialogs.ColorPickerDialog;
import com.example.imagegallery.dialogs.BlackWhiteDialog;
import com.example.imagegallery.dialogs.BrightnessDialog;
import com.example.imagegallery.dialogs.ColorDialog;
import com.example.imagegallery.dialogs.ContrastDialog;
import com.example.imagegallery.dialogs.GammaDialog;
import com.example.imagegallery.dialogs.GausianBlurDialog;
import com.example.imagegallery.dialogs.HueDialog;
import com.example.imagegallery.dialogs.SaturationDialog;
import com.example.imagegallery.dialogs.ColorPickerDialog.OnColorPickerListener;
import com.example.imagegallery.dialogs.BlackWhiteDialog.OnBlackWhiteListener;
import com.example.imagegallery.dialogs.BrightnessDialog.OnBrightnessListener;
import com.example.imagegallery.dialogs.ColorDialog.OnColorListener;
import com.example.imagegallery.dialogs.ContrastDialog.OnContrastListener;
import com.example.imagegallery.dialogs.GammaDialog.OnGammaListener;
import com.example.imagegallery.dialogs.GausianBlurDialog.OnGausianBlurListener;
import com.example.imagegallery.dialogs.HueDialog.OnHueListener;
import com.example.imagegallery.dialogs.SaturationDialog.OnSaturationListener;
import com.example.imagegallery.utils.CsvFile;
import com.example.imagegallery.utils.Save;
import com.example.imagegallery.utils.WrappingSlidingDrawer;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.GraphViewSeries.*;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.GraphView.GraphViewData;

import net.viralpatel.android.imagegalleray.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class ImageGalleryDemoActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
	private static int RESULT_LOAD_OPERATIONS_IMAGE = 2;
	private String picturePath;
	private String pictureOperationsPath;
	public static final String PREFS_NAME = "picturePath";
	private Bitmap changeBitmap = null, changeBitmapOperations = null;
	private Bitmap originalBitmap = null, first_original = null,
			originalOperationsBitmap = null, first_original_operations = null;
	private ImageView imageView, imageViewResizedInvert,
			imageViewResizedBlackWhite, imageViewResizedBrightness,
			imageViewResizedContrast, imageViewResizedFlipVertical,
			imageViewResizedFlipHorizontal, imageViewResizedGrayscale,
			imageViewResizedGamma, imageViewResizedColorRGB,
			imageViewResizedSaturation, imageViewResizedHue,
			imageViewResizedShading, imageViewResizedBlur,
			imageViewResizedGausianBlur, imageViewResizedSharpen,
			imageViewResizedEdge, imageViewResizedEmboss,
			imageViewResizedEngraving, imageViewResizedSmooth,
			imageViewResizedOriginal, imageViewOperations, imageViewResizedOriginalOperations,
			imageViewResizedBlend, imageViewResizedMultiply, imageViewResizedDifference,
			imageViewResizedLighter, imageViewResizedDarker;
	private Button slideButton;
	private Button slideButtonOperations;
	private WrappingSlidingDrawer slidingDrawer;
	private WrappingSlidingDrawer slidingDrawerOperations;
	private HistogramImage mDrawOnTop;
	private long tStart, tEnd, tElapsed1, tElapsed2, bmpSize1, bmpSize2;
	private boolean first = true;
	private int ccolor, sscale, bright, contrast, hue;
	private double gamma, rred, ggreen, bblue, ssigma;
	private float saturation, alfa;
	private OnColorPickerListener listenerColor;
	private OnBlackWhiteListener listenerBlackWhite;
	private OnBrightnessListener listenerBrightness;
	private OnContrastListener listenerContrast;
	private OnGammaListener listenerGamma;
	private OnColorListener listenerRGB;
	private OnSaturationListener listenerSaturation;
	private OnHueListener listenerHue;
	private OnGausianBlurListener listenerGausianBlur;
	private OnBlendListener listenerBlend;
	public PopupWindow popup;
	private int width, height;
	private boolean click = true;
	private ImageView close;
	private TabHost th;
	private CsvFile csv;
	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();
		}
		
		
		setContentView(R.layout.filters);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;

		popup = new PopupWindow(this);
		
		csv = new CsvFile();
		
		slideButton = (Button) findViewById(R.id.handle);
		slideButtonOperations = (Button) findViewById(R.id.handleOperations);
		slidingDrawer = (WrappingSlidingDrawer) findViewById(R.id.drawer);
		slidingDrawerOperations = (WrappingSlidingDrawer) findViewById(R.id.drawerOperations);

		slideButton.setVisibility(View.GONE);
		slidingDrawer.setVisibility(View.GONE);	
		slideButtonOperations.setVisibility(View.GONE);
		slidingDrawerOperations.setVisibility(View.GONE);

		th = (TabHost) findViewById(R.id.tabhost);
		th.setup();

		TabSpec spec = th.newTabSpec("tag1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Filters");
		th.addTab(spec);

		spec = th.newTabSpec("tag2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Operations");
		th.addTab(spec);
		
		spec = th.newTabSpec("tag3");
		spec.setContent(R.id.tab3);
		spec.setIndicator("Graphics");
		th.addTab(spec);
		
		// Button filter = (Button) findViewById(R.id.buttonFilter);
		// Button histogram = (Button) findViewById(R.id.buttonHistogram);
		Button grafik = (Button) findViewById(R.id.buttonGrafik);
		// Button colorPicker = (Button) findViewById(R.id.buttonColorPicker);
		imageView = (ImageView) findViewById(R.id.imgView);
		imageViewResizedOriginal = (ImageView) findViewById(R.id.imgViewOriginal);
		imageViewResizedInvert = (ImageView) findViewById(R.id.imgViewInvert);		
		imageViewResizedBlackWhite = (ImageView) findViewById(R.id.imgViewBlackWhite);
		imageViewResizedBrightness = (ImageView) findViewById(R.id.imgViewBrightness);
		imageViewResizedContrast = (ImageView) findViewById(R.id.imgViewContrast);
		imageViewResizedFlipVertical = (ImageView) findViewById(R.id.imgViewFlipVertical);
		imageViewResizedFlipHorizontal = (ImageView) findViewById(R.id.imgViewFlipHorizontal);
		imageViewResizedGrayscale = (ImageView) findViewById(R.id.imgViewGrayscale);
		imageViewResizedGamma = (ImageView) findViewById(R.id.imgViewGammaCorection);
		imageViewResizedColorRGB = (ImageView) findViewById(R.id.imgViewColorRGB);
		imageViewResizedSaturation = (ImageView) findViewById(R.id.imgViewSaturation);
		imageViewResizedHue = (ImageView) findViewById(R.id.imgViewHue);
		imageViewResizedShading = (ImageView) findViewById(R.id.imgViewShading);
		imageViewResizedBlur = (ImageView) findViewById(R.id.imgViewBlur);
		imageViewResizedGausianBlur = (ImageView) findViewById(R.id.imgViewGausianBlur);
		imageViewResizedSharpen = (ImageView) findViewById(R.id.imgViewSharpen);
		imageViewResizedEdge = (ImageView) findViewById(R.id.imgViewEdge);
		imageViewResizedEmboss = (ImageView) findViewById(R.id.imgViewEmboss);
		imageViewResizedEngraving = (ImageView) findViewById(R.id.imgViewEngraving);
		imageViewResizedSmooth = (ImageView) findViewById(R.id.imgViewSmooth);

		imageViewOperations = (ImageView) findViewById(R.id.imgViewOperations);
		imageViewResizedOriginalOperations = (ImageView) findViewById(R.id.imgViewOriginalOperations);
		imageViewResizedBlend = (ImageView) findViewById(R.id.imgViewBlend);
		imageViewResizedMultiply = (ImageView) findViewById(R.id.imgViewMultiply);
		imageViewResizedDifference = (ImageView) findViewById(R.id.imgViewDifference);
		imageViewResizedLighter = (ImageView) findViewById(R.id.imgViewLighter);
		imageViewResizedDarker = (ImageView) findViewById(R.id.imgViewDarker);
		
		 grafik.setOnClickListener(new View.OnClickListener() {
		  
		  @Override public void onClick(View arg0) { // graph with dynamically genereated horizontal and vertical labels 
		 
			  
		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
					   	  new GraphViewData(0, 2.0d)
					    , new GraphViewData(2, 3.0d)
					    , new GraphViewData(4, 10.0d)
					    , new GraphViewData(6, 5.0d)
					   	, new GraphViewData(8, 0.0d)
					});
		
		  GraphView graphView = new BarGraphView(
		  ImageGalleryDemoActivity.this, "GraphViewDemo");
		  /*
		  graphView.getGraphViewStyle().setNumHorizontalLabels(4);
		  CustomLabelFormatter clf = new CustomLabelFormatter() {
			
			@Override
			public String formatLabel(double arg0, boolean arg1) {
				// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						return "" + (int) arg0;
			}
		};
		 //  ((LineGraphView) graphView).setDrawDataPoints(true); 
		  graphView.setBackgroundColor(Color.BLACK);
		  
		  String[] labels = new String[4];
		  int min = 0, max = 4;
			
		  
		  for (int i=min; i<max; i++)
			  labels[i] = clf.formatLabel(min + ((max-min)*i/4), true);
		 
		//  graphView.setCustomLabelFormatter(clf);
		 * 
		 */
		  graphView.setBackgroundColor(Color.BLACK);
		  graphView.setHorizontalLabels(new String[] {"inv", "gs", "bw", "gb", "", ""});
		  
		  graphView.addSeries(exampleSeries); // data 
		  FrameLayout layout =
		  (FrameLayout) findViewById(R.id.tab3);
		  layout.addView(graphView); }
		  });
		 
	}

	private void createSmallerImageOperations() {
		Bitmap resized = Bitmap.createScaledBitmap(originalBitmap,
				(int) (originalBitmap.getWidth() * 0.4),
				(int) (originalBitmap.getHeight() * 0.4), true);
		Bitmap resizedOperation = Bitmap.createScaledBitmap(originalOperationsBitmap,
				(int) (originalOperationsBitmap.getWidth() * 0.4),
				(int) (originalOperationsBitmap.getHeight() * 0.4), true);
		//Bitmap resizedOriginalOperation = Bitmap.createScaledBitmap(first_original_operations,
		//		(int) (first_original_operations.getWidth() * 0.4),
		//		(int) (first_original_operations.getHeight() * 0.4), true);
		Bitmap resizedOperationBlend = Operation.blend(resized, resizedOperation, 0.5);
		Bitmap resizedOperationMultiply = Operation.multiply(resized, resizedOperation);
		Bitmap resizedOperationDifference = Operation.difference(resized, resizedOperation);
		Bitmap resizedOperationLighter = Operation.lighter(resized, resizedOperation);
		Bitmap resizedOperationDarker = Operation.darker(resized, resizedOperation);
		//imageViewResizedOriginalOperations.setImageBitmap(resizedOriginalOperation);
		imageViewResizedBlend.setImageBitmap(resizedOperationBlend);
		imageViewResizedMultiply.setImageBitmap(resizedOperationMultiply);
		imageViewResizedDifference.setImageBitmap(resizedOperationDifference);
		imageViewResizedLighter.setImageBitmap(resizedOperationLighter);
		imageViewResizedDarker.setImageBitmap(resizedOperationDarker);
	}
	
	private void createSmallerImage() {
		

		Bitmap resized = Bitmap.createScaledBitmap(originalBitmap,
				(int) (originalBitmap.getWidth() * 0.4),
				(int) (originalBitmap.getHeight() * 0.4), true);
		//Bitmap resizedOriginal = Bitmap.createScaledBitmap(first_original,
		//		(int) (first_original.getWidth() * 0.4),
		//		(int) (first_original.getHeight() * 0.4), true);
		
		Bitmap resizedFilterInvert = Filter.invert(resized);
		Bitmap resizedFilterBlackWhite = Filter.blackWhite(resized, 50);
		Bitmap resizedFilterBrightness = Filter.brightness(resized, -100);
		Bitmap resizedFilterContrast = Filter.contrast(resized, 100);
		Bitmap resizedFilterFlipVertical = Filter.flipVertical(resized);
		Bitmap resizedFilterFlipHorizontal = Filter.flipHorizontal(resized);
		Bitmap resizedFilterGrayscale = Filter.grayscale(resized);
		Bitmap resizedFilterGamma = Filter.gammaCorection(resized, 1.8, 1.8,
				1.8);
		Bitmap resizedFilterColorRGB = Filter.colorFilter(resized, 1, 0, 0);
		Bitmap resizedFilterSaturation = Filter.saturationFilter(resized,
				(float) 0.5);
		Bitmap resizedFilterHue = Filter.hueFilter(resized, 180);
		Bitmap resizedFilterShading = Filter.shadingFilter(resized,
				Color.parseColor("magenta"));
		Bitmap resizedFilterBlur = Filter.blur(resized);
		Bitmap resizedFilterGausianBlur = Filter.gausianBlur(resized, 1);
		Bitmap resizedFilterSharpen = Filter.sharpen(resized);
		Bitmap resizedFilterEdge = Filter.edge(resized);
		Bitmap resizedFilterEmboss = Filter.emboss(resized);
		Bitmap resizedFilterEngraving = Filter.engraving(resized);
		Bitmap resizedFilterSmooth = Filter.smooth(resized);

		//imageViewResizedOriginal.setImageBitmap(resizedOriginal);
		imageViewResizedInvert.setImageBitmap(resizedFilterInvert);
		imageViewResizedBlackWhite.setImageBitmap(resizedFilterBlackWhite);
		imageViewResizedBrightness.setImageBitmap(resizedFilterBrightness);
		imageViewResizedContrast.setImageBitmap(resizedFilterContrast);
		imageViewResizedFlipVertical.setImageBitmap(resizedFilterFlipVertical);
		imageViewResizedFlipHorizontal.setImageBitmap(resizedFilterFlipHorizontal);
		imageViewResizedGrayscale.setImageBitmap(resizedFilterGrayscale);
		imageViewResizedGamma.setImageBitmap(resizedFilterGamma);
		imageViewResizedColorRGB.setImageBitmap(resizedFilterColorRGB);
		imageViewResizedSaturation.setImageBitmap(resizedFilterSaturation);
		imageViewResizedHue.setImageBitmap(resizedFilterHue);
		imageViewResizedShading.setImageBitmap(resizedFilterShading);
		imageViewResizedBlur.setImageBitmap(resizedFilterBlur);
		imageViewResizedGausianBlur.setImageBitmap(resizedFilterGausianBlur);
		imageViewResizedSharpen.setImageBitmap(resizedFilterSharpen);
		imageViewResizedEdge.setImageBitmap(resizedFilterEdge);
		imageViewResizedEmboss.setImageBitmap(resizedFilterEmboss);
		imageViewResizedEngraving.setImageBitmap(resizedFilterEngraving);
		imageViewResizedSmooth.setImageBitmap(resizedFilterSmooth);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		// Toast.makeText(this, "Kliknuto!", Toast.LENGTH_LONG).show();

		switch (item.getItemId()) {
		case R.id.loadimage:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);
			return true;
		case R.id.loadOperationsImage:
			if (originalBitmap == null) {
				Toast.makeText(this, "Ucitajte prvo glavnu sliku!", Toast.LENGTH_LONG).show();
				return false;
			}
			Intent j = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(j, RESULT_LOAD_OPERATIONS_IMAGE);		
			return true;
		case R.id.saveimage:
			int tab_index_save = th.getCurrentTab(); 
			Save saveFile = new Save();
			if (originalBitmap != null && tab_index_save == 0)
				saveFile.saveImage(ImageGalleryDemoActivity.this,
						originalBitmap);
			if (originalOperationsBitmap != null && tab_index_save == 1)
				saveFile.saveImage(ImageGalleryDemoActivity.this,
						originalOperationsBitmap);
			return true;

		case R.id.histogramimage:
			
			int tab_index = th.getCurrentTab();   // 0 ako su unarne operacije, 1 ako su binarne
			if ((tab_index == 0 && originalBitmap != null) || (tab_index == 1 && originalOperationsBitmap != null)) {
				if (tab_index == 0)
					mDrawOnTop = new HistogramImage(ImageGalleryDemoActivity.this,
						originalBitmap);
				else 
					mDrawOnTop = new HistogramImage(ImageGalleryDemoActivity.this,
							originalOperationsBitmap);
				popup.setContentView(mDrawOnTop);

				close = (ImageView) mDrawOnTop.findViewById(1);
				
				close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						popup.dismiss();
					}
				});
				
				if (click) {
					popup.showAtLocation(mDrawOnTop, Gravity.CENTER, 10, 10);
					int w = (int) (width * 0.85);
					int h = (int) (height * 0.85);
					popup.update(0, 0, w, h);
					click = false;
				} else {
					popup.dismiss();
					click = true;
				}
			}
			return true;
			
		case R.id.histogramcamera:
			Intent hist = new Intent(ImageGalleryDemoActivity.this, HistogramRealTime.class);
			startActivity(hist);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
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
			
			if (originalBitmap != null && !originalBitmap.isRecycled()) {
			    originalBitmap.recycle();
			    originalBitmap = null; 
			    System.gc();
			}
			
			if (first_original != null && !first_original.isRecycled()) {
			    first_original.recycle();
			    first_original = null; 
			    System.gc();
			}

			loadImages();

		}
		
		if (requestCode == RESULT_LOAD_OPERATIONS_IMAGE && resultCode == RESULT_OK
				&& null != data) {
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
			
			if (originalOperationsBitmap != null && !originalOperationsBitmap.isRecycled()) {
			    originalOperationsBitmap.recycle();
			    originalOperationsBitmap = null; 
			    System.gc();
			}
			
			if (first_original_operations != null && !first_original_operations.isRecycled()) {
			    first_original_operations.recycle();
			    first_original_operations = null; 
			    System.gc();
			}

			loadOperationsImages();

		}
	}
	
	private void loadOperationsImages() {
		
		listenerBlend = new OnBlendListener() {

			@Override
			public void onOk(BlendDialog dialog, float scale) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				alfa = scale;
				changeBitmapOperations = Operation.blend(originalBitmap, originalOperationsBitmap, alfa);
				if (originalOperationsBitmap != null && !originalOperationsBitmap.isRecycled()) {
				    originalOperationsBitmap.recycle();
				    originalOperationsBitmap = null; 
				    System.gc();
				}
				
				if (changeBitmapOperations.getWidth() <= changeBitmapOperations.getHeight())
					imageViewOperations.setScaleType(ScaleType.CENTER_CROP);
				else
					imageViewOperations.setScaleType(ScaleType.FIT_CENTER);
				
				imageViewOperations.setImageBitmap(changeBitmapOperations);
				originalOperationsBitmap = Bitmap.createBitmap(changeBitmapOperations);
				createSmallerImageOperations();
			}

			@Override
			public void onCancel(BlendDialog dialog) {
				

			}
		};
		
		
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		pictureOperationsPath = settings.getString("putanja", "");

		final File imageF = new File(picturePath);
		try {
			BitmapScaler scaler = new BitmapScaler(imageF, 512);
			originalOperationsBitmap = scaler.getScaled();

			ExifInterface exif = new ExifInterface(picturePath);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 1);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
			} else if (orientation == 3) {
				matrix.postRotate(180);
			} else if (orientation == 8) {
				matrix.postRotate(270);
			}
			
			originalOperationsBitmap = Bitmap.createBitmap(originalOperationsBitmap, 0, 0,
					originalOperationsBitmap.getWidth(), originalOperationsBitmap.getHeight(),
					matrix, true); // rotating bitmap
			first_original_operations = Bitmap.createBitmap(originalOperationsBitmap);
			Bitmap resizedOriginalOperation = Bitmap.createScaledBitmap(first_original_operations,
							(int) (first_original_operations.getWidth() * 0.4),
							(int) (first_original_operations.getHeight() * 0.4), true);
			imageViewResizedOriginalOperations.setImageBitmap(resizedOriginalOperation);
			if (originalOperationsBitmap.getWidth() <= originalOperationsBitmap.getHeight())
				imageViewOperations.setScaleType(ScaleType.CENTER_CROP);
			else
				imageViewOperations.setScaleType(ScaleType.FIT_CENTER);
			imageViewOperations.setImageBitmap(originalOperationsBitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createSmallerImageOperations();
		
		Bitmap filterBitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.filter);

		slideButtonOperations.setCompoundDrawablesWithIntrinsicBounds(null,
				new BitmapDrawable(this.getResources(), filterBitmap), null,
				null);
		slideButtonOperations.setVisibility(View.VISIBLE);
		slidingDrawerOperations.setVisibility(View.VISIBLE);
		
		
		imageViewResizedOriginalOperations.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*
				changeBitmapOperations = Bitmap.createBitmap(first_original_operations);
				if (originalOperationsBitmap != null && !originalOperationsBitmap.isRecycled()) {
				    originalOperationsBitmap.recycle();
				    originalOperationsBitmap = null; 
				    System.gc();
				}
				imageViewOperations.setImageBitmap(changeBitmapOperations);
				originalOperationsBitmap = Bitmap.createBitmap(changeBitmapOperations);
				*/
				try {
					BitmapScaler scaler = new BitmapScaler(imageF, 512);
					originalOperationsBitmap = scaler.getScaled();

					ExifInterface exif = new ExifInterface(picturePath);
					int orientation = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION, 1);
					Log.d("EXIF", "Exif: " + orientation);
					Matrix matrix = new Matrix();
					if (orientation == 6) {
						matrix.postRotate(90);
					} else if (orientation == 3) {
						matrix.postRotate(180);
					} else if (orientation == 8) {
						matrix.postRotate(270);
					}
					
					originalOperationsBitmap = Bitmap.createBitmap(originalOperationsBitmap, 0, 0,
							originalOperationsBitmap.getWidth(), originalOperationsBitmap.getHeight(),
							matrix, true); // rotating bitmap
					if (originalOperationsBitmap.getWidth() <= originalOperationsBitmap.getHeight())
						imageViewOperations.setScaleType(ScaleType.CENTER_CROP);
					else
						imageViewOperations.setScaleType(ScaleType.FIT_CENTER);
					imageViewOperations.setImageBitmap(originalOperationsBitmap);
					createSmallerImageOperations();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		imageViewResizedBlend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				BlendDialog dialog = new BlendDialog(
						ImageGalleryDemoActivity.this, listenerBlend);
				dialog.show();
			}
		});

		
		imageViewResizedMultiply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				changeBitmapOperations = Operation.multiply(originalBitmap, originalOperationsBitmap);
				if (originalOperationsBitmap != null && !originalOperationsBitmap.isRecycled()) {
				    originalOperationsBitmap.recycle();
				    originalOperationsBitmap = null; 
				    System.gc();
				}
				
				if (changeBitmapOperations.getWidth() <= changeBitmapOperations.getHeight())
					imageViewOperations.setScaleType(ScaleType.CENTER_CROP);
				else
					imageViewOperations.setScaleType(ScaleType.FIT_CENTER);
				
				imageViewOperations.setImageBitmap(changeBitmapOperations);
				originalOperationsBitmap = Bitmap.createBitmap(changeBitmapOperations);
				createSmallerImageOperations();
			}
		});
		
		imageViewResizedDifference.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				changeBitmapOperations = Operation.difference(originalBitmap, originalOperationsBitmap);
				if (originalOperationsBitmap != null && !originalOperationsBitmap.isRecycled()) {
				    originalOperationsBitmap.recycle();
				    originalOperationsBitmap = null; 
				    System.gc();
				}
				if (changeBitmapOperations.getWidth() <= changeBitmapOperations.getHeight())
					imageViewOperations.setScaleType(ScaleType.CENTER_CROP);
				else
					imageViewOperations.setScaleType(ScaleType.FIT_CENTER);
				
				imageViewOperations.setImageBitmap(changeBitmapOperations);
				originalOperationsBitmap = Bitmap.createBitmap(changeBitmapOperations);
				createSmallerImageOperations();
			}
		});
		
		imageViewResizedLighter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				changeBitmapOperations = Operation.lighter(originalBitmap, originalOperationsBitmap);
				if (originalOperationsBitmap != null && !originalOperationsBitmap.isRecycled()) {
				    originalOperationsBitmap.recycle();
				    originalOperationsBitmap = null; 
				    System.gc();
				}
				
				if (changeBitmapOperations.getWidth() <= changeBitmapOperations.getHeight())
					imageViewOperations.setScaleType(ScaleType.CENTER_CROP);
				else
					imageViewOperations.setScaleType(ScaleType.FIT_CENTER);
				
				imageViewOperations.setImageBitmap(changeBitmapOperations);
				originalOperationsBitmap = Bitmap.createBitmap(changeBitmapOperations);
				createSmallerImageOperations();
			}
		});
		
		imageViewResizedDarker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				changeBitmapOperations = Operation.darker(originalBitmap, originalOperationsBitmap);
				if (originalOperationsBitmap != null && !originalOperationsBitmap.isRecycled()) {
				    originalOperationsBitmap.recycle();
				    originalOperationsBitmap = null; 
				    System.gc();
				}
				
				if (changeBitmapOperations.getWidth() <= changeBitmapOperations.getHeight())
					imageViewOperations.setScaleType(ScaleType.CENTER_CROP);
				else
					imageViewOperations.setScaleType(ScaleType.FIT_CENTER);
				
				imageViewOperations.setImageBitmap(changeBitmapOperations);
				originalOperationsBitmap = Bitmap.createBitmap(changeBitmapOperations);
				createSmallerImageOperations();
			}
		});

	}

	private void loadImages() {
		
		listenerColor = new OnColorPickerListener() {

			@Override
			public void onOk(ColorPickerDialog dialog, int color) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				ccolor = color;
				
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.shadingFilter(originalBitmap, ccolor);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("ColorFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}

			@Override
			public void onCancel(ColorPickerDialog dialog) {
				

			}
		};

		listenerBlackWhite = new OnBlackWhiteListener() {

			@Override
			public void onOk(BlackWhiteDialog dialog, int scale) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				sscale = scale;
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.blackWhite(originalBitmap, sscale);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("BlackWhiteFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}

			@Override
			public void onCancel(BlackWhiteDialog dialog) {
				

			}
		};

		listenerBrightness = new OnBrightnessListener() {

			@Override
			public void onOk(BrightnessDialog dialog, int scale) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				bright = scale;
				long start = System.currentTimeMillis();
				changeBitmap = Filter.brightness(originalBitmap, bright);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("BrightnessFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}

			@Override
			public void onCancel(BrightnessDialog dialog) {
				

			}
		};

		listenerContrast = new OnContrastListener() {

			@Override
			public void onOk(ContrastDialog dialog, int scale) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				contrast = scale;
				long start = System.currentTimeMillis();
				changeBitmap = Filter.contrast(originalBitmap, contrast);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("ContrastFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}

			@Override
			public void onCancel(ContrastDialog dialog) {
				

			}
		};

		listenerGamma = new OnGammaListener() {

			@Override
			public void onOk(GammaDialog dialog, double scale) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				gamma = scale;
				long start = System.currentTimeMillis();
				changeBitmap = Filter.gammaCorection(originalBitmap, gamma,
						gamma, gamma);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("GammaCorectionFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}

			@Override
			public void onCancel(GammaDialog dialog) {
				

			}
		};

		listenerRGB = new OnColorListener() {

			@Override
			public void onOk(ColorDialog dialog, double red, double green,
					double blue) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				rred = red;
				ggreen = green;
				bblue = blue;
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.colorFilter(originalBitmap, rred, ggreen,
						bblue);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("RGBFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}

			@Override
			public void onCancel(ColorDialog dialog) {
				

			}
		};

		listenerSaturation = new OnSaturationListener() {

			@Override
			public void onOk(SaturationDialog dialog, float scale) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				saturation = scale;
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.saturationFilter(originalBitmap,
						saturation);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("SaturationFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}

			@Override
			public void onCancel(SaturationDialog dialog) {
				

			}
		};

		listenerHue = new OnHueListener() {

			@Override
			public void onOk(HueDialog dialog, int scale) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				hue = scale;
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.hueFilter(originalBitmap, hue);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("HueFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}

			@Override
			public void onCancel(HueDialog dialog) {
				

			}
		};

		listenerGausianBlur = new OnGausianBlurListener() {

			@Override
			public void onOk(GausianBlurDialog dialog, double scale) {
				
				// Toast.makeText(this, "Boja: " + color,
				// Toast.LENGTH_LONG).show();
				ssigma = scale;
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.gausianBlur(originalBitmap, ssigma);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("GausianBlurFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}

			@Override
			public void onCancel(GausianBlurDialog dialog) {
				

			}
		};

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		picturePath = settings.getString("putanja", "");
		
		final File imageF = new File(picturePath);
		try {
			
			BitmapScaler scaler = new BitmapScaler(imageF, 512);
			originalBitmap = scaler.getScaled();
			Matrix matrix = new Matrix();
			ExifInterface exif = new ExifInterface(picturePath);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, 1);
			Log.d("EXIF", "Exif: " + orientation);
			if (orientation == 6) {
				matrix.postRotate(90);
			} else if (orientation == 3) {
				matrix.postRotate(180);
			} else if (orientation == 8) {
				matrix.postRotate(270);
			}
			originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
					originalBitmap.getWidth(), originalBitmap.getHeight(),
					matrix, true); // rotating bitmap
			first_original = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth(), originalBitmap.getHeight(), true);
			Bitmap resizedOriginal = Bitmap.createScaledBitmap(first_original,
								(int) (first_original.getWidth() * 0.4),
								(int) (first_original.getHeight() * 0.4), true);
			imageViewResizedOriginal.setImageBitmap(resizedOriginal);
			if (originalBitmap.getWidth() <= originalBitmap.getHeight())
				imageView.setScaleType(ScaleType.CENTER_CROP);
			else
				imageView.setScaleType(ScaleType.FIT_CENTER);
			imageView.setImageBitmap(originalBitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createSmallerImage();

		Bitmap filterBitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.filter);

		slideButton.setCompoundDrawablesWithIntrinsicBounds(null,
				new BitmapDrawable(this.getResources(), filterBitmap), null,
				null);
		slideButton.setVisibility(View.VISIBLE);
		slidingDrawer.setVisibility(View.VISIBLE);
		imageViewResizedOriginal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				/*	changeBitmap = Bitmap.createBitmap(first_original);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				*/
				BitmapScaler bscaler;
				Matrix mmatrix = new Matrix();
				try {
					bscaler = new BitmapScaler(imageF, 512);
					originalBitmap = bscaler.getScaled();

					ExifInterface exif = new ExifInterface(picturePath);
					int orientation = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION, 1);
					Log.d("EXIF", "Exif: " + orientation);
					if (orientation == 6) {
						mmatrix.postRotate(90);
					} else if (orientation == 3) {
						mmatrix.postRotate(180);
					} else if (orientation == 8) {
						mmatrix.postRotate(270);
					}
						
						originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
								originalBitmap.getWidth(), originalBitmap.getHeight(),
								mmatrix, true); // rotating bitmap
						
						imageView.setImageBitmap(originalBitmap);
						createSmallerImage();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		});

		imageViewResizedInvert.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.invert(originalBitmap);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("InvertFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
		imageViewResizedBlackWhite
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						BlackWhiteDialog dialog = new BlackWhiteDialog(
								ImageGalleryDemoActivity.this,
								listenerBlackWhite);
						dialog.show();
					}
				});
		imageViewResizedBrightness
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						BrightnessDialog dialog = new BrightnessDialog(
								ImageGalleryDemoActivity.this,
								listenerBrightness);
						dialog.show();
					}
				});
		imageViewResizedContrast.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				ContrastDialog dialog = new ContrastDialog(
						ImageGalleryDemoActivity.this, listenerContrast);
				dialog.show();
			}
		});
		imageViewResizedFlipVertical
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						long start = System.currentTimeMillis();
						changeBitmap = Filter.flipVertical(originalBitmap);
						long end = System.currentTimeMillis();
						long elapsed = end - start;
						csv.writeAll("FlipVerticalFilter", elapsed);
						
						if (originalBitmap != null && !originalBitmap.isRecycled()) {
						    originalBitmap.recycle();
						    originalBitmap = null; 
						    System.gc();
						}
						imageView.setImageBitmap(changeBitmap);
						originalBitmap = Bitmap.createBitmap(changeBitmap);
						createSmallerImage();
					}
				});
		imageViewResizedFlipHorizontal
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						long start = System.currentTimeMillis();
						changeBitmap = Filter.flipHorizontal(originalBitmap);
						long end = System.currentTimeMillis();
						long elapsed = end - start;
						csv.writeAll("FlipHorizontalFilter", elapsed);
						
						if (originalBitmap != null && !originalBitmap.isRecycled()) {
						    originalBitmap.recycle();
						    originalBitmap = null; 
						    System.gc();
						}
						imageView.setImageBitmap(changeBitmap);
						originalBitmap = Bitmap.createBitmap(changeBitmap);
						createSmallerImage();
					}
				});
		imageViewResizedGrayscale
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						long start = System.currentTimeMillis();
						changeBitmap = Filter.grayscale(originalBitmap);
						long end = System.currentTimeMillis();
						long elapsed = end - start;
						csv.writeAll("GrayscaleFilter", elapsed);
						
						if (originalBitmap != null && !originalBitmap.isRecycled()) {
						    originalBitmap.recycle();
						    originalBitmap = null; 
						    System.gc();
						}
						imageView.setImageBitmap(changeBitmap);
						originalBitmap = Bitmap.createBitmap(changeBitmap);
						createSmallerImage();
					}
				});
		imageViewResizedGamma.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				GammaDialog dialog = new GammaDialog(
						ImageGalleryDemoActivity.this, listenerGamma);
				dialog.show();
			}
		});
		imageViewResizedColorRGB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				ColorDialog dialog = new ColorDialog(
						ImageGalleryDemoActivity.this, listenerRGB);
				dialog.show();
			}
		});
		imageViewResizedSaturation
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						SaturationDialog dialog = new SaturationDialog(
								ImageGalleryDemoActivity.this,
								listenerSaturation);
						dialog.show();
					}
				});
		imageViewResizedHue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				HueDialog dialog = new HueDialog(ImageGalleryDemoActivity.this,
						listenerHue);
				dialog.show();
			}
		});
		imageViewResizedShading.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				

				ColorPickerDialog dialog = new ColorPickerDialog(
						ImageGalleryDemoActivity.this, Color
								.parseColor("magenta"), listenerColor);

				dialog.show();

			}
		});
		imageViewResizedBlur.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.blur(originalBitmap);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("BlurFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
		imageViewResizedGausianBlur
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						GausianBlurDialog dialog = new GausianBlurDialog(
								ImageGalleryDemoActivity.this,
								listenerGausianBlur);
						dialog.show();
					}
				});
		imageViewResizedSharpen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.sharpen(originalBitmap);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("SharpenFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
		imageViewResizedEdge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.edge(originalBitmap);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("EdgeFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
		imageViewResizedEmboss.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.emboss(originalBitmap);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("EmbossFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
		imageViewResizedEngraving
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						long start = System.currentTimeMillis();
						changeBitmap = Filter.engraving(originalBitmap);
						long end = System.currentTimeMillis();
						long elapsed = end - start;
						csv.writeAll("EngravingFilter", elapsed);
						
						if (originalBitmap != null && !originalBitmap.isRecycled()) {
						    originalBitmap.recycle();
						    originalBitmap = null; 
						    System.gc();
						}
						imageView.setImageBitmap(changeBitmap);
						originalBitmap = Bitmap.createBitmap(changeBitmap);
						createSmallerImage();
					}
				});
		imageViewResizedSmooth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				long start = System.currentTimeMillis();
				changeBitmap = Filter.smooth(originalBitmap);
				long end = System.currentTimeMillis();
				long elapsed = end - start;
				csv.writeAll("SmoothFilter", elapsed);
				
				if (originalBitmap != null && !originalBitmap.isRecycled()) {
				    originalBitmap.recycle();
				    originalBitmap = null; 
				    System.gc();
				}
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});

	}

	@Override
	public void onBackPressed() {
		

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ImageGalleryDemoActivity.this);

		// set title
		alertDialogBuilder.setTitle("Zelite da napustite aplikaciju?");

		// set dialog message
		alertDialogBuilder
				.setMessage("Da li zelite da zavrsite rad sa aplikacijom?")
				.setCancelable(false)
				.setPositiveButton("DA", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(getApplicationContext(),
								ImageGalleryDemoActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("EXIT", true);
						startActivity(intent);
					}
				})
				.setNegativeButton("NE", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

}