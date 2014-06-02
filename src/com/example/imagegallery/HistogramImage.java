package com.example.imagegallery;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import net.viralpatel.android.imagegalleray.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.View.OnTouchListener;

public class HistogramImage extends RelativeLayout implements OnTouchListener {
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
	double[] mBinSquared;
	Context ccontext;
	ImageView imageClose, imageMaximize, imageRestore;
	RelativeLayout layout;
	int maximize = 0;
	RectF barRectRed, barRectGreen, barRectBlue;
	float barWidth, barMarginHeight, barMaxHeight;
	double redHistogramSum = 0, greenHistogramSum = 0, blueHistogramSum = 0;
	int maxRed, maxGreen, maxBlue, newImageWidth;
	ArrayList<RectF> arrayRed = new ArrayList<RectF>();
	ArrayList<RectF> arrayGreen = new ArrayList<RectF>();
	ArrayList<RectF> arrayBlue = new ArrayList<RectF>();

	public HistogramImage(Context context, Bitmap b) {
		super(context);
		ccontext = context;

		mBitmap = b;
		mImageWidth = mBitmap.getWidth();
		mImageHeight = mBitmap.getHeight();

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

		// mBitmap = null;,
		mRGBData = new int[mImageWidth * mImageHeight];
		mYUVData = new byte[mBitmap.getByteCount()];

		ByteBuffer buffer = ByteBuffer.allocate(mBitmap.getByteCount()); // Create
																			// a
																			// new
																			// buffer
		mBitmap.copyPixelsToBuffer(buffer); // Move the byte data to the buffer

		mYUVData = buffer.array();

		mRedHistogram = new int[256];
		mGreenHistogram = new int[256];
		mBlueHistogram = new int[256];
		mBinSquared = new double[256];
		for (int bin = 0; bin < 256; bin++) {
			mBinSquared[bin] = ((double) bin) * bin;
		} // bin

		barRectRed = new RectF();
		barRectGreen = new RectF();
		barRectBlue = new RectF();

		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		imageClose = new ImageView(ccontext);
		imageClose.setBackgroundResource(R.drawable.close);
		imageClose.setId(1);
		imageClose.setLayoutParams(imageParams);
		this.addView(imageClose);

		this.setOnTouchListener(this);
		setWillNotDraw(false);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mBitmap != null) {
			int canvasWidth = canvas.getWidth();
			int canvasHeight = canvas.getHeight();
			newImageWidth = canvasWidth;
			int marginWidth = (canvasWidth - newImageWidth) / 2;

			Log.d("Sirina canvasa i margine je: ",
					Integer.toString(newImageWidth) + " "
							+ Integer.toString(marginWidth));

			// Convert from YUV to RGB
			// decodeYUV420SP(mRGBData, mYUVData, mImageWidth, mImageHeight);

			mBitmap.getPixels(mRGBData, 0, mBitmap.getWidth(), 0, 0,
					mBitmap.getWidth(), mBitmap.getHeight());

			// Draw bitmap
			// mBitmap.setPixels(mRGBData, 0, mImageWidth, 0, 0,
			// mImageWidth, mImageHeight);
			// Rect src = new Rect(0, 0, mImageWidth, mImageHeight);
			// Rect dst = new Rect(marginWidth, 0,
			// canvasWidth-marginWidth, canvasHeight);
			// canvas.drawBitmap(mBitmap, src, dst, mPaintBlack);

			// Draw black borders
			canvas.drawRect(0, 0, marginWidth, canvasHeight, mPaintBlack);
			canvas.drawRect(canvasWidth - marginWidth, 0, canvasWidth,
					canvasHeight, mPaintBlack);

			// Calculate histogram
			calculateIntensityHistogram(mRGBData, mRedHistogram, mImageWidth,
					mImageHeight, 0);
			calculateIntensityHistogram(mRGBData, mGreenHistogram, mImageWidth,
					mImageHeight, 1);
			calculateIntensityHistogram(mRGBData, mBlueHistogram, mImageWidth,
					mImageHeight, 2);

			//maxRed = findMaxValueHistogram(mRedHistogram);
			//maxGreen = findMaxValueHistogram(mGreenHistogram);
			//maxBlue = findMaxValueHistogram(mBlueHistogram);

			// Calculate mean
			double imageRedMean = 0, imageGreenMean = 0, imageBlueMean = 0;

			for (int bin = 0; bin < 256; bin++) {
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

			// Calculate second moment
			double imageRed2ndMoment = 0, imageGreen2ndMoment = 0, imageBlue2ndMoment = 0;
			for (int bin = 0; bin < 256; bin++) {
				imageRed2ndMoment += mRedHistogram[bin] * mBinSquared[bin];
				imageGreen2ndMoment += mGreenHistogram[bin] * mBinSquared[bin];
				imageBlue2ndMoment += mBlueHistogram[bin] * mBinSquared[bin];
			} // bin
			imageRed2ndMoment /= redHistogramSum;
			imageGreen2ndMoment /= greenHistogramSum;
			imageBlue2ndMoment /= blueHistogramSum;
			double imageRedStdDev = Math.sqrt(imageRed2ndMoment - imageRedMean
					* imageRedMean);
			double imageGreenStdDev = Math.sqrt(imageGreen2ndMoment
					- imageGreenMean * imageGreenMean);
			double imageBlueStdDev = Math.sqrt(imageBlue2ndMoment
					- imageBlueMean * imageBlueMean);

			// Draw mean
			String imageMeanStr = "Mean (R,G,B): "
					+ String.format("%.4g", imageRedMean) + ", "
					+ String.format("%.4g", imageGreenMean) + ", "
					+ String.format("%.4g", imageBlueMean);
			canvas.drawText(imageMeanStr, marginWidth + 10 - 1, 30 - 1,
					mPaintBlack);
			canvas.drawText(imageMeanStr, marginWidth + 10 + 1, 30 - 1,
					mPaintBlack);
			canvas.drawText(imageMeanStr, marginWidth + 10 + 1, 30 + 1,
					mPaintBlack);
			canvas.drawText(imageMeanStr, marginWidth + 10 - 1, 30 + 1,
					mPaintBlack);
			canvas.drawText(imageMeanStr, marginWidth + 10, 30, mPaintYellow);

			// Draw standard deviation
			String imageStdDevStr = "Std Dev (R,G,B): "
					+ String.format("%.4g", imageRedStdDev) + ", "
					+ String.format("%.4g", imageGreenStdDev) + ", "
					+ String.format("%.4g", imageBlueStdDev);
			canvas.drawText(imageStdDevStr, marginWidth + 10 - 1, 60 - 1,
					mPaintBlack);
			canvas.drawText(imageStdDevStr, marginWidth + 10 + 1, 60 - 1,
					mPaintBlack);
			canvas.drawText(imageStdDevStr, marginWidth + 10 + 1, 60 + 1,
					mPaintBlack);
			canvas.drawText(imageStdDevStr, marginWidth + 10 - 1, 60 + 1,
					mPaintBlack);
			canvas.drawText(imageStdDevStr, marginWidth + 10, 60, mPaintYellow);

			// Draw red intensity histogram
			barMaxHeight = 12000;  // bilo 3000
			barWidth = ((float) newImageWidth) / 256;
			barMarginHeight = 2;

			barRectRed.bottom = canvasHeight - 600; // bilo 200
			barRectRed.left = marginWidth;
			barRectRed.right = barRectRed.left + barWidth;
			float prob;
			for (int bin = 0; bin < 256; bin++) {

				prob = (float) mRedHistogram[bin] / (float) redHistogramSum;
				barRectRed.top = barRectRed.bottom
						- Math.min(200, prob * barMaxHeight) - barMarginHeight; // bilo
																				// 80
				canvas.drawRect(barRectRed, mPaintBlack);
				barRectRed.top += barMarginHeight;
				canvas.drawRect(barRectRed, mPaintRed);
				RectF tempRed = new RectF();
				tempRed.bottom = barRectRed.bottom;
				tempRed.top = barRectRed.top;
				tempRed.left = barRectRed.left;
				tempRed.right = barRectRed.right;
				arrayRed.add(tempRed);
				// Log.d("Crveni deo (top, bottom, left, right",
				// Float.toString(barRectRed.top) + " " +
				// Float.toString(barRectRed.bottom) + " " +
				// Float.toString(barRectRed.left) + " " +
				// Float.toString(barRectRed.right));
				barRectRed.left += barWidth;
				barRectRed.right += barWidth;
			} // bin

			// Draw green intensity histogram

			barRectGreen.bottom = canvasHeight - 300; // bilo 100
			barRectGreen.left = marginWidth;
			barRectGreen.right = barRectGreen.left + barWidth;
			for (int bin = 0; bin < 256; bin++) {

				barRectGreen.top = barRectGreen.bottom
							- Math.min(200, ((float) mGreenHistogram[bin])
									/ ((float) greenHistogramSum)
									* barMaxHeight) - barMarginHeight;
				canvas.drawRect(barRectGreen, mPaintBlack);
				barRectGreen.top += barMarginHeight;
				canvas.drawRect(barRectGreen, mPaintGreen);
				RectF tempGreen = new RectF();
				tempGreen.bottom = barRectGreen.bottom;
				tempGreen.top = barRectGreen.top;
				tempGreen.left = barRectGreen.left;
				tempGreen.right = barRectGreen.right;
				arrayGreen.add(tempGreen);
				barRectGreen.left += barWidth;
				barRectGreen.right += barWidth;
			} // bin

			// Draw blue intensity histogram

			barRectBlue.bottom = canvasHeight;
			barRectBlue.left = marginWidth;
			barRectBlue.right = barRectBlue.left + barWidth;
			for (int bin = 0; bin < 256; bin++) {

				barRectBlue.top = barRectBlue.bottom
							- Math.min(200, ((float) mBlueHistogram[bin])
									/ ((float) blueHistogramSum) * barMaxHeight)
							- barMarginHeight;
				canvas.drawRect(barRectBlue, mPaintBlack);
				barRectBlue.top += barMarginHeight;
				canvas.drawRect(barRectBlue, mPaintBlue);
				RectF tempBlue = new RectF();
				tempBlue.bottom = barRectBlue.bottom;
				tempBlue.top = barRectBlue.top;
				tempBlue.left = barRectBlue.left;
				tempBlue.right = barRectBlue.right;
				arrayBlue.add(tempBlue);
				barRectBlue.left += barWidth;
				barRectBlue.right += barWidth;
			} // bin
		} // end if statement

		super.onDraw(canvas);

	} // end onDraw method
	/*
	private int findMaxValueHistogram(int[] mHistogram) {
		// TODO Auto-generated method stub
		int max = mHistogram[0];
		for (int bin = 1; bin < 256; bin++) {
			int histogram = mHistogram[bin];
			if (histogram > max)
				max = histogram;

		} // bin
		return max * 14;
	}
	*/
	static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width,
			int height) {
		final int frameSize = width * height;

		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;

				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}
	}

	static public void decodeYUV420SPGrayscale(int[] rgb, byte[] yuv420sp,
			int width, int height) {
		final int frameSize = width * height;

		for (int pix = 0; pix < frameSize; pix++) {
			int pixVal = (0xff & ((int) yuv420sp[pix])) - 16;
			if (pixVal < 0)
				pixVal = 0;
			if (pixVal > 255)
				pixVal = 255;
			rgb[pix] = 0xff000000 | (pixVal << 16) | (pixVal << 8) | pixVal;
		} // pix
	}

	static public void calculateIntensityHistogram(int[] rgb, int[] histogram,
			int width, int height, int component) {
		for (int bin = 0; bin < 256; bin++) {
			histogram[bin] = 0;
		} // bin
		if (component == 0) // red
		{
			for (int pix = 0; pix < width * height; pix += 3) {
				int pixVal = (rgb[pix] >> 16) & 0xff;
				histogram[pixVal]++;
			} // pix
		} else if (component == 1) // green
		{
			for (int pix = 0; pix < width * height; pix += 3) {
				int pixVal = (rgb[pix] >> 8) & 0xff;
				histogram[pixVal]++;
			} // pix
		} else // blue
		{
			for (int pix = 0; pix < width * height; pix += 3) {
				int pixVal = rgb[pix] & 0xff;
				histogram[pixVal]++;
			} // pix
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();

		for (RectF red : arrayRed) {
			// Log.d("Crveni deo (top, bottom, left, right",
			// Float.toString(red.top) + " " + Float.toString(red.bottom) + " "
			// + Float.toString(red.left) + " " + Float.toString(red.right));
			if (red.contains(x, y)) {
				Log.d("Tacka je u crvenom delu! x i y imaju vrednosti: ",
						Float.toString(x) + " " + Float.toString(y));
				
				  float temp = red.bottom - red.top - barMarginHeight; 
				  if (temp != 200) {
					  /*
					  float prob = temp/barMaxHeight; int sumPixels =
							  mImageWidth * mImageHeight; 
					  double numOfPixels; 
					  
					  numOfPixels = prob * redHistogramSum;
				  Log.d("Piksela od ukupno piksela: ",
				  Double.toString(numOfPixels) + "/" +
				  Integer.toString(sumPixels));
				 
				for (int j = 0; j < 256; j++) {
					Log.d("Vrednosti histograma niz[j], j : ",
							Integer.toString(mRedHistogram[j]) + " "
									+ Integer.toString(j));
					// sum += mRedHistogram[j];
					 if (mRedHistogram[j] == (int)numOfPixels ||
					 (mRedHistogram[j]-1) == (int)numOfPixels ||
					 (mRedHistogram[j]+1) == (int)numOfPixels)
					 Log.d("Svetlina ovog broja piksela jednaka je i iznosi: ",
					 Integer.toString(j));
				}
				
				 */
				float bright = (float) (x / (float) newImageWidth * 256.0);
				int numOfPxls = mRedHistogram[(int) (bright)]; // x -
																	// osvetljenje,
																	// mRedHistogram[x]
																	// - broj
																	// piksela
				Toast.makeText(
						ccontext,
						"Broj piksela koji imaju osvetljenje crvene boje koje ste odabrali ("
								+ Integer.toString((int) (bright)) + ") je "
								+ Integer.toString(numOfPxls) + ".",
						Toast.LENGTH_SHORT).show();
			}
		}
		}	
		for (RectF green : arrayGreen) {
			if (green.contains(x, y)) {
				Log.d("Tacka je u zelenom delu! x i y imaju vrednosti: ",
						Float.toString(x) + " " + Float.toString(y));
				
				  float temp = green.bottom - green.top - barMarginHeight; 
				  if (temp != 200) {
				
					  float bright = (float) (x / (float) newImageWidth * 256.0);
					  int numOfPxls = mGreenHistogram[(int) (bright)]; // x -
																	// osvetljenje,
																	// mRedHistogram[x]
																	// - broj
																	// piksela
				Toast.makeText(
						ccontext,
						"Broj piksela koji imaju osvetljenje zelene boje koje ste odabrali ("
								+ Integer.toString((int) (bright)) + ") je "
								+ Integer.toString(numOfPxls) + ".",
						Toast.LENGTH_SHORT).show();
			}
		}
		}
		for (RectF blue : arrayBlue) {
			if (blue.contains(x, y)) {
				Log.d("Tacka je u plavom delu! x i y imaju vrednosti: ",
						Float.toString(x) + " " + Float.toString(y));
				
				  float temp = blue.bottom - blue.top - barMarginHeight; 
				  if (temp != 200) {
				
					  float bright = (float) (x / (float) newImageWidth * 256.0);
					  int numOfPxls = mBlueHistogram[(int) (bright)]; // x -
																	// osvetljenje,
																	// mRedHistogram[x]
																	// - broj
																	// piksela
				Toast.makeText(
						ccontext,
						"Broj piksela koji imaju osvetljenje plave boje koje ste odabrali ("
								+ Integer.toString((int) (bright)) + ") je "
								+ Integer.toString(numOfPxls) + ".",
						Toast.LENGTH_SHORT).show(); 
			}
		}
		}

		return true;
	}
}