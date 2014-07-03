package com.example.imagegallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

// ----------------------------------------------------------------------

public class HistogramRealTime extends Activity {    
    private Preview mPreview;
    private DrawOnTop mDrawOnTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Create our Preview view and set it as the content of our activity.
        // Create our DrawOnTop view.
        mDrawOnTop = new DrawOnTop(this);
        mPreview = new Preview(this, mDrawOnTop);
        setContentView(mPreview);
        addContentView(mDrawOnTop, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
}

//----------------------------------------------------------------------

class DrawOnTop extends View {
	Bitmap mBitmap;
	Paint mPaintBlack;
	Paint mPaintYellow;
	Paint mPaintRed;
	Paint mPaintGreen;
	Paint mPaintBlue;
	byte[] mYUVData;
	int[] mRGBData;
	int mImageWidth, mImageHeight;
	int[] mRedHistogram;
	int[] mGreenHistogram;
	int[] mBlueHistogram;

    public DrawOnTop(Context context) {
        super(context);
        
        mPaintBlack = new Paint();
        mPaintBlack.setStyle(Paint.Style.FILL);
        mPaintBlack.setColor(Color.BLACK);
        mPaintBlack.setTextSize(25);
        
        mPaintYellow = new Paint();
        mPaintYellow.setStyle(Paint.Style.FILL);
        mPaintYellow.setColor(Color.YELLOW);
        mPaintYellow.setTextSize(25);
        
        mPaintRed = new Paint();
        mPaintRed.setStyle(Paint.Style.FILL);
        mPaintRed.setColor(Color.RED);
        mPaintRed.setTextSize(25);
        
        mPaintGreen = new Paint();
        mPaintGreen.setStyle(Paint.Style.FILL);
        mPaintGreen.setColor(Color.GREEN);
        mPaintGreen.setTextSize(25);
        
        mPaintBlue = new Paint();
        mPaintBlue.setStyle(Paint.Style.FILL);
        mPaintBlue.setColor(Color.BLUE);
        mPaintBlue.setTextSize(25);
        
        mBitmap = null;
        mYUVData = null;
        mRGBData = null;
        mRedHistogram = new int[256];
        mGreenHistogram = new int[256];
        mBlueHistogram = new int[256];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null)
        {
        	int canvasWidth = canvas.getWidth();
        	int canvasHeight = canvas.getHeight();
        	int newImageWidth = canvasWidth;
        	int newImageHeight = canvasHeight;
        	int marginWidth = (canvasWidth - newImageWidth)/2;
        
        	        	
        	// Convert from YUV to RGB
        	//decodeYUV420SP(mRGBData, mYUVData, mImageWidth, mImageHeight);
        	convertYUV420_NV21toRGB8888(mRGBData, mYUVData, mImageWidth, mImageHeight);
        	
        	
        	// Draw bitmap
        	mBitmap.setPixels(mRGBData, 0, mImageWidth, 0, 0, 
        			mImageWidth, mImageHeight);
        	Rect src = new Rect(0, 0, mImageWidth, mImageHeight);
        	Rect dst = new Rect(marginWidth, 0, 
        			canvasWidth-marginWidth, canvasHeight);
        	canvas.drawBitmap(mBitmap, src, dst, mPaintBlack);
        	
            canvas.save();
        	canvas.translate(canvas.getHeight()/(float)1.3-2, canvas.getHeight()-2); //canvas.getWidth()/2
        	canvas.rotate(-90);
        	
        	// Draw black borders        	        	
        	canvas.drawRect(0, 0, marginWidth, canvasHeight, mPaintBlack);
        	canvas.drawRect(canvasWidth - marginWidth, 0, 
        			canvasWidth, canvasHeight, mPaintBlack);
        	
        	// Calculate histogram
        	calculateIntensityHistogram(mRGBData, mRedHistogram, 
        			mImageWidth, mImageHeight, 0);
        	calculateIntensityHistogram(mRGBData, mGreenHistogram, 
        			mImageWidth, mImageHeight, 1);
        	calculateIntensityHistogram(mRGBData, mBlueHistogram, 
        			mImageWidth, mImageHeight, 2);
        	
        	// Calculate mean
        	double imageRedMean = 0, imageGreenMean = 0, imageBlueMean = 0;
        	double redHistogramSum = 0, greenHistogramSum = 0, blueHistogramSum = 0;
        	for (int bin = 0; bin < 256; bin++)
        	{
        		imageRedMean += mRedHistogram[bin] * bin;
        		redHistogramSum += mRedHistogram[bin];
        		imageGreenMean += mGreenHistogram[bin] * bin;
        		greenHistogramSum += mGreenHistogram[bin];
        		imageBlueMean += mBlueHistogram[bin] * bin;
        		blueHistogramSum += mBlueHistogram[bin];
        	} // bin
        	imageRedMean /= redHistogramSum;
        	imageGreenMean /= greenHistogramSum;
        	imageBlueMean /= blueHistogramSum;
        	

        	// Draw mean
        	String imageMeanStr = "Srednje vrednosti (R,G,B): " + String.format("%.4g", imageRedMean) + ", " + String.format("%.4g", imageGreenMean) + ", " + String.format("%.4g", imageBlueMean);
        	canvas.drawText(imageMeanStr, marginWidth+10-1, -canvasWidth/(float)2.5-1, mPaintBlack);  
        	canvas.drawText(imageMeanStr, marginWidth+10+1, -canvasWidth/(float)2.5-1, mPaintBlack);  
        	canvas.drawText(imageMeanStr, marginWidth+10+1, -canvasWidth/(float)2.5+1, mPaintBlack);  
        	canvas.drawText(imageMeanStr, marginWidth+10-1, -canvasWidth/(float)2.5+1, mPaintBlack);  
        	canvas.drawText(imageMeanStr, marginWidth+10, -canvasWidth/(float)2.5, mPaintYellow);     
        	
        	
        	// Draw red intensity histogram
        	float barMaxHeight = 12000;  
        	float barWidth = ((float)newImageWidth) / 256;
        	float barMarginHeight = 2;
        	RectF barRect = new RectF();
        	barRect.bottom = canvasHeight - 600;  
        	barRect.left = marginWidth;
        	barRect.right = barRect.left + barWidth;
        	for (int bin = 0; bin < 256; bin++)
        	{
        		float prob = (float)mRedHistogram[bin] / (float)redHistogramSum;
        		barRect.top = barRect.bottom - 
        			Math.min(200,prob*barMaxHeight) - barMarginHeight;  
        		canvas.drawRect(barRect, mPaintBlack);
        		barRect.top += barMarginHeight;
        		canvas.drawRect(barRect, mPaintRed);
        		barRect.left += barWidth;
        		barRect.right += barWidth;
        	} // bin
        	
        	// Draw green intensity histogram
        	barRect.bottom = canvasHeight - 300;  
        	barRect.left = marginWidth;
        	barRect.right = barRect.left + barWidth;
        	for (int bin = 0; bin < 256; bin++)
        	{
        		barRect.top = barRect.bottom - Math.min(200, ((float)mGreenHistogram[bin])/((float)greenHistogramSum) * barMaxHeight) - barMarginHeight; // bilo min 80
        		canvas.drawRect(barRect, mPaintBlack);
        		barRect.top += barMarginHeight;
        		canvas.drawRect(barRect, mPaintGreen);
        		barRect.left += barWidth;
        		barRect.right += barWidth;
        	} // bin
        	
        	// Draw blue intensity histogram
        	barRect.bottom = canvasHeight;
        	barRect.left = marginWidth;
        	barRect.right = barRect.left + barWidth;
        	for (int bin = 0; bin < 256; bin++)
        	{
        		barRect.top = barRect.bottom - Math.min(200, ((float)mBlueHistogram[bin])/((float)blueHistogramSum) * barMaxHeight) - barMarginHeight;  // bilo min 80
        		canvas.drawRect(barRect, mPaintBlack);
        		barRect.top += barMarginHeight;
        		canvas.drawRect(barRect, mPaintBlue);
        		barRect.left += barWidth;
        		barRect.right += barWidth;
        	} // bin
        } // end if statement
        
        super.onDraw(canvas);
        
        canvas.restore();
        
    } // end onDraw method

    static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
    	final int frameSize = width * height;
    	
    	for (int j = 0, yp = 0; j < height; j++) {
    		int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
    		for (int i = 0; i < width; i++, yp++) {
    			int y = (0xff & ((int) yuv420sp[yp])) - 16;
    			if (y < 0) y = 0;
    			if ((i & 1) == 0) {
    				v = (0xff & yuv420sp[uvp++]) - 128;
    				u = (0xff & yuv420sp[uvp++]) - 128;
    			}
    			
    			int y1192 = 1192 * y;
    			int r = (y1192 + 1634 * v);
    			int g = (y1192 - 833 * v - 400 * u);
    			int b = (y1192 + 2066 * u);
    			
    			if (r < 0) r = 0; else if (r > 262143) r = 262143;
    			if (g < 0) g = 0; else if (g > 262143) g = 262143;
    			if (b < 0) b = 0; else if (b > 262143) b = 262143;
    			
    			rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
    		}
    	}
    }
    
    /**
     * Converts YUV420 NV21 to RGB8888
     * 
     * @param data byte array on YUV420 NV21 format.
     * @param width pixels width
     * @param height pixels height
     * @return a RGB8888 pixels int array. Where each int is a pixels ARGB. 
     */
    public static void convertYUV420_NV21toRGB8888(int [] rgb, byte [] data, int width, int height) {
        int size = width*height;
        int offset = size;
        int u, v, y1, y2, y3, y4;

        // i percorre os Y and the final pixels
        // k percorre os pixles U e V
        for(int i=0, k=0; i < size; i+=2, k+=2) {
            y1 = data[i  ]&0xff;
            y2 = data[i+1]&0xff;
            y3 = data[width+i  ]&0xff;
            y4 = data[width+i+1]&0xff;

            u = data[offset+k  ]&0xff;
            v = data[offset+k+1]&0xff;
            u = u-128;
            v = v-128;

            rgb[i  ] = convertYUVtoRGB(y1, u, v);
            rgb[i+1] = convertYUVtoRGB(y2, u, v);
            rgb[width+i  ] = convertYUVtoRGB(y3, u, v);
            rgb[width+i+1] = convertYUVtoRGB(y4, u, v);

            if (i!=0 && (i+2)%width==0)
                i+=width;
        }
    }

    private static int convertYUVtoRGB(int y, int u, int v) {
        int r,g,b;

        r = y + (int)1.402f*v;
        g = y - (int)(0.344f*u +0.714f*v);
        b = y + (int)1.772f*u;
        r = r>255? 255 : r<0 ? 0 : r;
        g = g>255? 255 : g<0 ? 0 : g;
        b = b>255? 255 : b<0 ? 0 : b;
        return 0xff000000 | (b<<16) | (g<<8) | r;
    }
    
    
    static public void calculateIntensityHistogram(int[] rgb, int[] histogram, int width, int height, int component)
    {
    	for (int bin = 0; bin < 256; bin++)
    	{
    		histogram[bin] = 0;
    	} // bin
    	if (component == 0) // red
    	{
    		for (int pix = 0; pix < width*height; pix ++)
    		{
	    		int pixVal = (rgb[pix] >> 16) & 0xff;
	    		histogram[ pixVal ]++;
    		} // pix
    	}
    	else if (component == 1) // green
    	{
    		for (int pix = 0; pix < width*height; pix ++)
    		{
	    		int pixVal = (rgb[pix] >> 8) & 0xff;
	    		histogram[ pixVal ]++;
    		} // pix
    	}
    	else // blue
    	{
    		for (int pix = 0; pix < width*height; pix ++)
    		{
	    		int pixVal = rgb[pix] & 0xff;
	    		histogram[ pixVal ]++;
    		} // pix
    	}
    }
} 

// ----------------------------------------------------------------------

class Preview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    Camera mCamera;
    DrawOnTop mDrawOnTop;
    boolean mFinished;

    Preview(Context context, DrawOnTop drawOnTop) {
        super(context);
        
        mDrawOnTop = drawOnTop;
        mFinished = false;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open(); 
        try {
           mCamera.setPreviewDisplay(holder);
           // Preview callback used whenever new viewfinder frame is available
           
           mCamera.setPreviewCallback(new PreviewCallback() {
        	  public void onPreviewFrame(byte[] data, Camera camera)
        	  {
        		  if ( (mDrawOnTop == null) || mFinished )
        			  return;
        		  
        		  if (mDrawOnTop.mBitmap == null)
        		  {
        			  // Initialize the draw-on-top companion
        			  Camera.Parameters params = camera.getParameters();
        			  mDrawOnTop.mImageWidth = params.getPreviewSize().width;
        			  mDrawOnTop.mImageHeight = params.getPreviewSize().height;
        			  mDrawOnTop.mBitmap = Bitmap.createBitmap(mDrawOnTop.mImageWidth, 
        					  mDrawOnTop.mImageHeight, Bitmap.Config.ARGB_8888);  // bilo rgb565
        			  mDrawOnTop.mRGBData = new int[mDrawOnTop.mImageWidth * mDrawOnTop.mImageHeight]; 
        			  
        			// pWidth and pHeight define the size of the preview
        			  mDrawOnTop.mYUVData = new byte[data.length];        			  
        		  }
        		  
        		  // Pass YUV data to draw-on-top companion
        		 System.arraycopy(data, 0, mDrawOnTop.mYUVData, 0, data.length);
    			 mDrawOnTop.invalidate();
        	  }
           });
        } 
        catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        } 
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    	mFinished = true;
    	mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(320, 240);
        parameters.setPreviewFrameRate(15);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_NIGHT);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

}