package net.viralpatel.android.imagegalleray;

import java.io.File;
import java.io.IOException;

import net.viralpatel.android.imagegalleray.colorpicker.AmbilWarnaDialog;
import net.viralpatel.android.imagegalleray.colorpicker.AmbilWarnaDialog.OnAmbilWarnaListener;
import net.viralpatel.android.imagegalleray.colorpicker.BlackWhiteDialog;
import net.viralpatel.android.imagegalleray.colorpicker.BlackWhiteDialog.OnBlackWhiteListener;
import net.viralpatel.android.imagegalleray.colorpicker.BrightnessDialog;
import net.viralpatel.android.imagegalleray.colorpicker.BrightnessDialog.OnBrightnessListener;
import net.viralpatel.android.imagegalleray.colorpicker.ColorDialog;
import net.viralpatel.android.imagegalleray.colorpicker.ColorDialog.OnColorListener;
import net.viralpatel.android.imagegalleray.colorpicker.ContrastDialog;
import net.viralpatel.android.imagegalleray.colorpicker.ContrastDialog.OnContrastListener;
import net.viralpatel.android.imagegalleray.colorpicker.GammaDialog;
import net.viralpatel.android.imagegalleray.colorpicker.GammaDialog.OnGammaListener;
import net.viralpatel.android.imagegalleray.colorpicker.HueDialog;
import net.viralpatel.android.imagegalleray.colorpicker.HueDialog.OnHueListener;
import net.viralpatel.android.imagegalleray.colorpicker.SaturationDialog;
import net.viralpatel.android.imagegalleray.colorpicker.SaturationDialog.OnSaturationListener;
import net.viralpatel.android.imagegalleray.colorpicker.WrappingSlidingDrawer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SlidingDrawer;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ImageGalleryDemoActivity extends Activity {
    
	private String picturePath;
    private Bitmap changeBitmap = null;
    private Bitmap originalBitmap = null, first_original = null;
    private ImageView imageView, imageViewResizedInvert, imageViewResizedBlackWhite, imageViewResizedBrightness, 
    imageViewResizedContrast,imageViewResizedFlipVertical, imageViewResizedFlipHorizontal, 
    imageViewResizedGrayscale, imageViewResizedGamma, imageViewResizedColorRGB, imageViewResizedSaturation, 
    imageViewResizedHue, imageViewResizedShading, imageViewResizedBlur, imageViewResizedGausianBlur, 
    imageViewResizedSharpen, imageViewResizedEdge, imageViewResizedEmboss, imageViewResizedEngraving, 
    imageViewResizedSmooth, imageViewResizedOriginal;
    private Button slideButton;
    private WrappingSlidingDrawer slidingDrawer;
    private DrawOnTop mDrawOnTop;
	private long tStart, tEnd, tElapsed1, tElapsed2, bmpSize1, bmpSize2;
	private boolean first = true;
	private int ccolor, sscale, bright, contrast, hue;
	private double gamma, rred, ggreen, bblue;
	private float saturation;
	public static final String PREFS_NAME = "picturePath";
	private OnAmbilWarnaListener listenerColor;
	private OnBlackWhiteListener listenerBlackWhite;
	private OnBrightnessListener listenerBrightness;
	private OnContrastListener listenerContrast;
	private OnGammaListener listenerGamma;
	private OnColorListener listenerRGB;
	private OnSaturationListener listenerSaturation;
	private OnHueListener listenerHue;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters);
        TabHost th = (TabHost) findViewById(R.id.tabhost);
        th.setup();
        
        TabSpec spec = th.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Filters");
        th.addTab(spec);
        
        spec = th.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Operations");
        th.addTab(spec);
        
       // Button filter = (Button) findViewById(R.id.buttonFilter);
       // Button histogram = (Button) findViewById(R.id.buttonHistogram);
       // Button grafik = (Button) findViewById(R.id.buttonGrafik);
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
        
        Bitmap filterBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.filter);
        
        slideButton = (Button) findViewById(R.id.handle);
        slidingDrawer = (WrappingSlidingDrawer) findViewById(R.id.drawer);
        
        slideButton.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(this.getResources(), filterBitmap), null, null);
        listenerColor = new OnAmbilWarnaListener() {
				
				@Override
				public void onOk(AmbilWarnaDialog dialog, int color) {
					// TODO Auto-generated method stub
					//Toast.makeText(this, "Boja: " + color, Toast.LENGTH_LONG).show();
					ccolor = color;
					changeBitmap = Filter.shadingFilter(originalBitmap, ccolor);
	 				originalBitmap.recycle();
	 				originalBitmap = null;
	 				imageView.setImageBitmap(changeBitmap);
	 				originalBitmap = Bitmap.createBitmap(changeBitmap);
	 				createSmallerImage();
				}
				
				@Override
				public void onCancel(AmbilWarnaDialog dialog) {
					// TODO Auto-generated method stub
					
				}
			};
			
			listenerBlackWhite = new OnBlackWhiteListener() {
				
				@Override
				public void onOk(BlackWhiteDialog dialog, int scale) {
					// TODO Auto-generated method stub
					//Toast.makeText(this, "Boja: " + color, Toast.LENGTH_LONG).show();
					sscale = scale;
					changeBitmap = Filter.blackWhite(originalBitmap, sscale);
	 				originalBitmap.recycle();
	 				originalBitmap = null;
	 				imageView.setImageBitmap(changeBitmap);
	 				originalBitmap = Bitmap.createBitmap(changeBitmap);
	 				createSmallerImage();
				}
				
				@Override
				public void onCancel(BlackWhiteDialog dialog) {
					// TODO Auto-generated method stub
					
				}
			};
			
			listenerBrightness = new OnBrightnessListener() {
				
				@Override
				public void onOk(BrightnessDialog dialog, int scale) {
					// TODO Auto-generated method stub
					//Toast.makeText(this, "Boja: " + color, Toast.LENGTH_LONG).show();
					bright = scale;
					changeBitmap = Filter.brightness(originalBitmap, bright);
	 				originalBitmap.recycle();
	 				originalBitmap = null;
	 				imageView.setImageBitmap(changeBitmap);
	 				originalBitmap = Bitmap.createBitmap(changeBitmap);
	 				createSmallerImage();
				}
				
				@Override
				public void onCancel(BrightnessDialog dialog) {
					// TODO Auto-generated method stub
					
				}
			};
			
			listenerContrast = new OnContrastListener() {
				
				@Override
				public void onOk(ContrastDialog dialog, int scale) {
					// TODO Auto-generated method stub
					//Toast.makeText(this, "Boja: " + color, Toast.LENGTH_LONG).show();
					contrast = scale;
					changeBitmap = Filter.contrast(originalBitmap, contrast);
	 				originalBitmap.recycle();
	 				originalBitmap = null;
	 				imageView.setImageBitmap(changeBitmap);
	 				originalBitmap = Bitmap.createBitmap(changeBitmap);
	 				createSmallerImage();
				}
				
				@Override
				public void onCancel(ContrastDialog dialog) {
					// TODO Auto-generated method stub
					
				}
			};
			
			listenerGamma = new OnGammaListener() {
				
				@Override
				public void onOk(GammaDialog dialog, double scale) {
					// TODO Auto-generated method stub
					//Toast.makeText(this, "Boja: " + color, Toast.LENGTH_LONG).show();
					gamma = scale;
					changeBitmap = Filter.gammaCorection(originalBitmap, gamma, gamma, gamma);
	 				originalBitmap.recycle();
	 				originalBitmap = null;
	 				imageView.setImageBitmap(changeBitmap);
	 				originalBitmap = Bitmap.createBitmap(changeBitmap);
	 				createSmallerImage();
				}
				
				@Override
				public void onCancel(GammaDialog dialog) {
					// TODO Auto-generated method stub
					
				}
			};
			
			listenerRGB = new OnColorListener() {
				
				@Override
				public void onOk(ColorDialog dialog, double red, double green, double blue) {
					// TODO Auto-generated method stub
					//Toast.makeText(this, "Boja: " + color, Toast.LENGTH_LONG).show();
					rred = red; ggreen = green; bblue = blue;
					changeBitmap = Filter.colorFilter(originalBitmap, rred, ggreen, bblue);
	 				originalBitmap.recycle();
	 				originalBitmap = null;
	 				imageView.setImageBitmap(changeBitmap);
	 				originalBitmap = Bitmap.createBitmap(changeBitmap);
	 				createSmallerImage();
				}
				
				@Override
				public void onCancel(ColorDialog dialog) {
					// TODO Auto-generated method stub
					
				}
			};
			
			listenerSaturation = new OnSaturationListener() {
				
				@Override
				public void onOk(SaturationDialog dialog, float scale) {
					// TODO Auto-generated method stub
					//Toast.makeText(this, "Boja: " + color, Toast.LENGTH_LONG).show();
					saturation = scale;
					changeBitmap = Filter.saturationFilter(originalBitmap, saturation);
	 				originalBitmap.recycle();
	 				originalBitmap = null;
	 				imageView.setImageBitmap(changeBitmap);
	 				originalBitmap = Bitmap.createBitmap(changeBitmap);
	 				createSmallerImage();
				}
				
				@Override
				public void onCancel(SaturationDialog dialog) {
					// TODO Auto-generated method stub
					
				}
			};
			
			listenerHue = new OnHueListener() {
				
				@Override
				public void onOk(HueDialog dialog, int scale) {
					// TODO Auto-generated method stub
					//Toast.makeText(this, "Boja: " + color, Toast.LENGTH_LONG).show();
					hue = scale;
					changeBitmap = Filter.hueFilter(originalBitmap, hue);
	 				originalBitmap.recycle();
	 				originalBitmap = null;
	 				imageView.setImageBitmap(changeBitmap);
	 				originalBitmap = Bitmap.createBitmap(changeBitmap);
	 				createSmallerImage();
				}
				
				@Override
				public void onCancel(HueDialog dialog) {
					// TODO Auto-generated method stub
					
				}
			};
			
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
                first_original = Bitmap.createBitmap(originalBitmap);
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
		
        imageViewResizedOriginal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeBitmap = Bitmap.createBitmap(first_original);
				originalBitmap.recycle();
				originalBitmap = null;
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
        
        imageViewResizedInvert.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeBitmap = Filter.invert(originalBitmap);
				originalBitmap.recycle();
				originalBitmap = null;
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
        imageViewResizedBlackWhite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BlackWhiteDialog dialog = new BlackWhiteDialog(ImageGalleryDemoActivity.this,  listenerBlackWhite);
 				dialog.show();
			}
		});
        imageViewResizedBrightness.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BrightnessDialog dialog = new BrightnessDialog(ImageGalleryDemoActivity.this,  listenerBrightness);
 				dialog.show();
			}
		});
        imageViewResizedContrast.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ContrastDialog dialog = new ContrastDialog(ImageGalleryDemoActivity.this,  listenerContrast);
 				dialog.show();
			}
		});
        imageViewResizedFlipVertical.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeBitmap = Filter.flipVertical(originalBitmap);
				originalBitmap.recycle();
				originalBitmap = null;
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
        imageViewResizedFlipHorizontal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeBitmap = Filter.flipHorizontal(originalBitmap);
				originalBitmap.recycle();
				originalBitmap = null;
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
        imageViewResizedGrayscale.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeBitmap = Filter.grayscale(originalBitmap);
				originalBitmap.recycle();
				originalBitmap = null;
				imageView.setImageBitmap(changeBitmap);
				originalBitmap = Bitmap.createBitmap(changeBitmap);
				createSmallerImage();
			}
		});
        imageViewResizedGamma.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GammaDialog dialog = new GammaDialog(ImageGalleryDemoActivity.this,  listenerGamma);
 				dialog.show();
			}
		});
        imageViewResizedColorRGB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ColorDialog dialog = new ColorDialog(ImageGalleryDemoActivity.this,  listenerRGB);
 				dialog.show();
			}
		});
        imageViewResizedSaturation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SaturationDialog dialog = new SaturationDialog(ImageGalleryDemoActivity.this,  listenerSaturation);
 				dialog.show();
			}
		});
        imageViewResizedHue.setOnClickListener(new View.OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				HueDialog dialog = new HueDialog(ImageGalleryDemoActivity.this,  listenerHue);
 				dialog.show();
 			}
 		});
        imageViewResizedShading.setOnClickListener(new View.OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				
 				
 				AmbilWarnaDialog dialog = new AmbilWarnaDialog(ImageGalleryDemoActivity.this, Color.parseColor("magenta"), listenerColor);
 				
 				dialog.show();
 				
 			}
 		});
        imageViewResizedBlur.setOnClickListener(new View.OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				changeBitmap = Filter.blur(originalBitmap);
 				originalBitmap.recycle();
 				originalBitmap = null;
 				imageView.setImageBitmap(changeBitmap);
 				originalBitmap = Bitmap.createBitmap(changeBitmap);
 				createSmallerImage();
 			}
 		});
        imageViewResizedGausianBlur.setOnClickListener(new View.OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				changeBitmap = Filter.gausianBlur(originalBitmap, 10, 5);
 				originalBitmap.recycle();
 				originalBitmap = null;
 				imageView.setImageBitmap(changeBitmap);
 				originalBitmap = Bitmap.createBitmap(changeBitmap);
 				createSmallerImage();
 			}
 		});
        imageViewResizedSharpen.setOnClickListener(new View.OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				changeBitmap = Filter.sharpen(originalBitmap);
 				originalBitmap.recycle();
 				originalBitmap = null;
 				imageView.setImageBitmap(changeBitmap);
 				originalBitmap = Bitmap.createBitmap(changeBitmap);
 				createSmallerImage();
 			}
 		});
        imageViewResizedEdge.setOnClickListener(new View.OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				changeBitmap = Filter.edge(originalBitmap);
 				originalBitmap.recycle();
 				originalBitmap = null;
 				imageView.setImageBitmap(changeBitmap);
 				originalBitmap = Bitmap.createBitmap(changeBitmap);
 				createSmallerImage();
 			}
 		});
        imageViewResizedEmboss.setOnClickListener(new View.OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				changeBitmap = Filter.emboss(originalBitmap);
 				originalBitmap.recycle();
 				originalBitmap = null;
 				imageView.setImageBitmap(changeBitmap);
 				originalBitmap = Bitmap.createBitmap(changeBitmap);
 				createSmallerImage();
 			}
 		});
        imageViewResizedEngraving.setOnClickListener(new View.OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				changeBitmap = Filter.engraving(originalBitmap);
 				originalBitmap.recycle();
 				originalBitmap = null;
 				imageView.setImageBitmap(changeBitmap);
 				originalBitmap = Bitmap.createBitmap(changeBitmap);
 				createSmallerImage();
 			}
 		});
        imageViewResizedSmooth.setOnClickListener(new View.OnClickListener() {
			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				changeBitmap = Filter.smooth(originalBitmap);
 				originalBitmap.recycle();
 				originalBitmap = null;
 				imageView.setImageBitmap(changeBitmap);
 				originalBitmap = Bitmap.createBitmap(changeBitmap);
 				createSmallerImage();
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

		*/
    }

	private void createSmallerImage() {
		// TODO Auto-generated method stub

		Bitmap resized = Bitmap.createScaledBitmap(originalBitmap ,(int)(originalBitmap.getWidth()*0.4), (int)(originalBitmap.getHeight()*0.4), true);
		Bitmap resizedOriginal = Bitmap.createScaledBitmap(first_original ,(int)(first_original.getWidth()*0.4), (int)(first_original.getHeight()*0.4), true);
		Bitmap resizedFilterInvert = Filter.invert(resized);
		Bitmap resizedFilterBlackWhite = Filter.blackWhite(resized, 50);
		Bitmap resizedFilterBrightness = Filter.brightness(resized, -100);
		Bitmap resizedFilterContrast = Filter.contrast(resized, 100);
		Bitmap resizedFilterFlipVertical = Filter.flipVertical(resized);
		Bitmap resizedFilterFlipHorizontal = Filter.flipHorizontal(resized);
		Bitmap resizedFilterGrayscale = Filter.grayscale(resized);
		Bitmap resizedFilterGamma = Filter.gammaCorection(resized, 1.8, 1.8, 1.8);
		Bitmap resizedFilterColorRGB = Filter.colorFilter(resized, 1, 0, 0);
		Bitmap resizedFilterSaturation = Filter.saturationFilter(resized, (float)0.5);
		Bitmap resizedFilterHue = Filter.hueFilter(resized, 180);
		Bitmap resizedFilterShading = Filter.shadingFilter(resized, Color.parseColor("magenta"));
		Bitmap resizedFilterBlur = Filter.blur(resized);
		Bitmap resizedFilterGausianBlur = Filter.gausianBlur(resized, 10, 5);
		Bitmap resizedFilterSharpen = Filter.sharpen(resized);
		Bitmap resizedFilterEdge = Filter.edge(resized);
		Bitmap resizedFilterEmboss = Filter.emboss(resized);
		Bitmap resizedFilterEngraving = Filter.engraving(resized);
		Bitmap resizedFilterSmooth = Filter.smooth(resized);
		
		imageViewResizedOriginal.setImageBitmap(resizedOriginal);
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
    
	    
}