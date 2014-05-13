package net.viralpatel.android.imagegalleray;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Filter {
	
	public static Bitmap invert(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		int r, g, b;
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				r = 255 - Color.red(colorArray[y * width + x]);
				g = 255 - Color.green(colorArray[y * width + x]);
				b = 255 - Color.blue(colorArray[y * width + x]);

				colorArray[y * width + x] = Color.rgb(r, g, b);
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		}
		return returnBitmap;
	}
	
	public static Bitmap blackWhite(Bitmap bitmap, double scale) {
		double newValue;
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();		
		

		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		int r, g, b;
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
		
		
		if (scale >=0 && scale <= 100) {						
			scale = (scale/100)*255;									/*odredjivanje praga*/
			
			for(int i = 0; i < width; i++) {									
				for(int j = 0; j < height; j++) {									
					r = Color.red(colorArray[j * width + i]);
					g = Color.green(colorArray[j * width + i]);
					b = Color.blue(colorArray[j * width + i]);
					newValue=0.2126*r+0.7152*g+0.0722*b;				/*odredjivanje osvetljenosti pixela*/
					if (newValue>scale) newValue=255;					/*odredjivanje nove vrednosti pixela*/					
					else if (newValue<scale) newValue=0;				/*odredjivanje nove vrednosti pixela*/	
					else if (scale>127.5) newValue=255;					/*odredjivanje nove vrednosti pixela*/	
					else if (scale<127.5) newValue=0;					/*odredjivanje nove vrednosti pixela*/	
					else newValue=255;									/*odredjivanje nove vrednosti pixela*/	
	
		
					colorArray[j * width + i] = Color.rgb((int)newValue, (int)newValue, (int)newValue);
					returnBitmap.setPixel(i, j, colorArray[j * width + i]);
				}
			}			
		}
		return returnBitmap;
	}
	
	public static Bitmap brightness(Bitmap bitmap, int scale) {
		int newValue;
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();		
		

		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		int r, g, b;
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
		
		if (scale <= 255 && scale >= -255) {
			for(int i=0; i < width; i++) {
				for(int j=0; j < height; j++) {
					r = Color.red(colorArray[j * width + i]);
					g = Color.green(colorArray[j * width + i]);
					b = Color.blue(colorArray[j * width + i]);
					
					newValue = r + scale;
					if (newValue > 255) r = 255;
					else if (newValue < 0) r = 0;
					else r = newValue;
				
					newValue = g + scale;
					if (newValue > 255) g = 255;
					else if (newValue < 0) g = 0;
					else g = newValue;
				
					newValue = b + scale;
					if (newValue > 255) b = 255;
					else if (newValue < 0) b = 0;
					else b = newValue;	
					
					colorArray[j * width + i] = Color.rgb(r, g, b);
					returnBitmap.setPixel(i, j, colorArray[j * width + i]);
				}
			}			
		}
		return returnBitmap;
	}
	
	public static Bitmap contrast(Bitmap bitmap, double scale) {
		int newValue;
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();		
		

		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		int r, g, b;
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
		
		if (scale >- 100 && scale <= 100) {
			scale = (scale+100)/100;
			scale = scale*scale;
			for(int i = 0; i < width; i++){
				for(int j = 0; j < height; j++){
					r = Color.red(colorArray[j * width + i]);
					g = Color.green(colorArray[j * width + i]);
					b = Color.blue(colorArray[j * width + i]);
		
					newValue = r;
					newValue -= 126;
					newValue *= scale;
					newValue += 126;
					if (newValue <= 0) r = 1;
					else if (newValue >= 255) r = 254;
					else r = newValue;
					
					newValue = g;
					newValue -= 126;
					newValue *= scale;
					newValue += 126;
					if (newValue <= 0) g = 1;
					else if (newValue >= 255) g = 254;
					else g = newValue;
					
					newValue = b;
					newValue -= 126;
					newValue *= scale;
					newValue += 126;
					if (newValue <= 0) b = 1;
					else if (newValue >= 255) b = 254;
					else b = newValue;
					
					colorArray[j * width + i] = Color.rgb(r, g, b);
					returnBitmap.setPixel(i, j, colorArray[j * width + i]);
				}
			}
		}
		return returnBitmap;
	}
	
	public static Bitmap flipVertical(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();		
		

		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		int r, g, b;
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
		
		for (int i = 0; i < width; i++) {
			for(int j=0; j < height/2; j++) {
				int rtemp = Color.red(colorArray[j * width + i]);
				int gtemp = Color.green(colorArray[j * width + i]);
				int btemp = Color.blue(colorArray[j * width + i]);
				
				r = Color.red(colorArray[(height-j-1) * width + i]);
				g = Color.green(colorArray[(height-j-1) * width + i]);
				b = Color.blue(colorArray[(height-j-1) * width + i]);
				
				colorArray[j * width + i] = Color.rgb(r, g, b);
				returnBitmap.setPixel(i, j, colorArray[j * width + i]);
				
				colorArray[(height-j-1) * width + i] = Color.rgb(rtemp, gtemp, btemp);
				returnBitmap.setPixel(i, (height-j-1), colorArray[(height-j-1) * width + i]);
			}
		}
		return returnBitmap;
	}
	
	public static Bitmap flipHorizontal(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();		
		

		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		int r, g, b;
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
		
		
		for (int i=0; i < width/2; i++) {
			for (int j = 0; j < height; j++) {
				int rtemp = Color.red(colorArray[j * width + i]);
				int gtemp = Color.green(colorArray[j * width + i]);
				int btemp = Color.blue(colorArray[j * width + i]);
				
				r = Color.red(colorArray[j * width + (width-i-1)]);
				g = Color.green(colorArray[j * width + (width-i-1)]);
				b = Color.blue(colorArray[j * width + (width-i-1)]);
				
				colorArray[j * width + i] = Color.rgb(r, g, b);
				returnBitmap.setPixel(i, j, colorArray[j * width + i]);
				
				colorArray[j * width + (width-i-1)] = Color.rgb(rtemp, gtemp, btemp);
				returnBitmap.setPixel((width-i-1), j, colorArray[j * width + (width-i-1)]);
			}
		}		
		return returnBitmap;
	}
	
	public static Bitmap grayscale(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();	
		double newValue;
		

		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		int r, g, b;
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				r = Color.red(colorArray[y * width + x]);
				g = Color.green(colorArray[y * width + x]);
				b = Color.blue(colorArray[y * width + x]);
				
				newValue = 0.2126*r+0.7152*g+0.0722*b;

				colorArray[y * width + x] = Color.rgb((int)newValue, (int)newValue, (int)newValue);
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		}
		return returnBitmap;
	}
	
	private static double[][] blur;
	private static int blurOpseg;

	
	private static void initBlur(double sigma, int ksize) {
		if (ksize == 1 || ksize % 2 == 0) {
			return;
		}

		blurOpseg = ksize / 2;
		blur = new double[ksize][ksize];
		double scale = -0.5 / (sigma * sigma);
		double cons = -scale / Math.PI;
		double sum = 0;

		for (int i = 0; i < ksize; i++) {
			for (int j = 0; j < ksize; j++) {
				int x = i - (ksize - 1) / 2;
				int y = j - (ksize - 1) / 2;
				blur[i ][ j] = cons * Math.exp(scale * (x * x + y * y));

				sum += blur[i ][ j];
			}
		}

		for (int i = 0; i < ksize; i++) {
			for (int j = 0; j < ksize; j++) {
				blur[i ][ j] /= sum;
			}
		}
	}
	
	
	public static Bitmap gausianBlur(Bitmap bitmap, double sigma, int ksize) {
		if (bitmap == null) {
			return null;
		}
		initBlur(sigma, ksize);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

		int slikaX;
		int slikaY;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				//Èuvaju podatke o trenutnom rezultujuæem pixel-u
				int red = 0;
				int green = 0;
				int blue = 0;
				
				//Iteracija kroz matricu filtera i taèke u slici koje odgovaraju njenim poljima
				for (int deltaX=-blurOpseg; deltaX <= blurOpseg; deltaX++)
					for (int deltaY=-blurOpseg; deltaY <= blurOpseg; deltaY++) {
						int filtX = deltaX + blurOpseg;
						int filtY = deltaY + blurOpseg;

						slikaX = x + deltaX;
						slikaX = slikaX < 0 ? 0 : slikaX >= width? width-1 : slikaX;
						slikaY = y + deltaY;
						slikaY = slikaY<0 ? 0 : slikaY>=height?height-1:slikaY;

						red   += blur[filtY][filtX] * Color.red(bitmap.getPixel(slikaX, slikaY));
						green += blur[filtY][filtX] * Color.green(bitmap.getPixel(slikaX, slikaY));
						blue  += blur[filtY][filtX] * Color.blue(bitmap.getPixel(slikaX, slikaY));
					}

				colorArray[y * width + x] = Color.rgb((red>255 ? 255 : red<0?0:red) + 1, (green>255 ? 255 : green<0?0:green) + 1, (blue>255 ? 255 : blue<0?0:blue) + 1);
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		return returnBitmap;
	}
	

	
	
	
	

}
