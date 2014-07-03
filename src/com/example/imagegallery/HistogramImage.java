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
	int[] mRGBData;
	int mImageWidth, mImageHeight;
	int[] mRedHistogram;
	int[] mGreenHistogram;
	int[] mBlueHistogram;
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

		mRGBData = new int[mImageWidth * mImageHeight];

		ByteBuffer buffer = ByteBuffer.allocate(mBitmap.getByteCount()); // Create
																			// a
																			// new
																			// buffer
		mBitmap.copyPixelsToBuffer(buffer); // Move the byte data to the buffer

		mRedHistogram = new int[256];
		mGreenHistogram = new int[256];
		mBlueHistogram = new int[256];

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

			mBitmap.getPixels(mRGBData, 0, mBitmap.getWidth(), 0, 0,
					mBitmap.getWidth(), mBitmap.getHeight());

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

			// Draw mean
			String imageMeanStr = "Srednje vrednosti (R,G,B): "
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

			// Draw red intensity histogram
			barMaxHeight = 12000;
			barWidth = ((float) newImageWidth) / 256;
			barMarginHeight = 2;

			barRectRed.bottom = canvasHeight - 600;
			barRectRed.left = marginWidth;
			barRectRed.right = barRectRed.left + barWidth;
			float prob;
			for (int bin = 0; bin < 256; bin++) {

				prob = (float) mRedHistogram[bin] / (float) redHistogramSum;
				barRectRed.top = barRectRed.bottom
						- Math.min(200, prob * barMaxHeight) - barMarginHeight;

				canvas.drawRect(barRectRed, mPaintBlack);
				barRectRed.top += barMarginHeight;
				canvas.drawRect(barRectRed, mPaintRed);
				RectF tempRed = new RectF();
				tempRed.bottom = barRectRed.bottom;
				tempRed.top = barRectRed.top;
				tempRed.left = barRectRed.left;
				tempRed.right = barRectRed.right;
				arrayRed.add(tempRed);
				barRectRed.left += barWidth;
				barRectRed.right += barWidth;
			} // bin

			// Draw green intensity histogram

			barRectGreen.bottom = canvasHeight - 300;
			barRectGreen.left = marginWidth;
			barRectGreen.right = barRectGreen.left + barWidth;
			for (int bin = 0; bin < 256; bin++) {

				barRectGreen.top = barRectGreen.bottom
						- Math.min(200, ((float) mGreenHistogram[bin])
								/ ((float) greenHistogramSum) * barMaxHeight)
						- barMarginHeight;
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

	static public void calculateIntensityHistogram(int[] rgb, int[] histogram,
			int width, int height, int component) {
		for (int bin = 0; bin < 256; bin++) {
			histogram[bin] = 0;
		} // bin
		if (component == 0) // red
		{
			for (int pix = 0; pix < width * height; pix++) {
				int pixVal = (rgb[pix] >> 16) & 0xff;
				histogram[pixVal]++;
			} // pix
		} else if (component == 1) // green
		{
			for (int pix = 0; pix < width * height; pix++) {
				int pixVal = (rgb[pix] >> 8) & 0xff;
				histogram[pixVal]++;
			} // pix
		} else // blue
		{
			for (int pix = 0; pix < width * height; pix++) {
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
			if (red.contains(x, y)) {
				Log.d("Tacka je u crvenom delu! x i y imaju vrednosti: ",
						Float.toString(x) + " " + Float.toString(y));

				float temp = red.bottom - red.top - barMarginHeight;
				if (temp != 200) {
					float bright = (float) (x / (float) newImageWidth * 256.0);
					int numOfPxls = mRedHistogram[(int) (bright)]; // x -
																	// osvetljenje,
																	// mRedHistogram[x]
																	// - broj
																	// piksela
					Toast.makeText(
							ccontext,
							"Broj piksela koji imaju osvetljenje crvene boje koje ste odabrali ("
									+ Integer.toString((int) (bright))
									+ ") je " + Integer.toString(numOfPxls)
									+ ".", Toast.LENGTH_SHORT).show();
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
																		// -
																		// broj
																		// piksela
					Toast.makeText(
							ccontext,
							"Broj piksela koji imaju osvetljenje zelene boje koje ste odabrali ("
									+ Integer.toString((int) (bright))
									+ ") je " + Integer.toString(numOfPxls)
									+ ".", Toast.LENGTH_SHORT).show();
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
									+ Integer.toString((int) (bright))
									+ ") je " + Integer.toString(numOfPxls)
									+ ".", Toast.LENGTH_SHORT).show();
				}
			}
		}

		return true;
	}
}