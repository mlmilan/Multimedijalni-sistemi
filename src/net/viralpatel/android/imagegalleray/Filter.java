package net.viralpatel.android.imagegalleray;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Filter {
	
	private static double[][] gaussianBlur;
	private static int gaussianBlurRange;
	
	private static int[][] sharpen;
	private static int sharpenRange = 1;
	
	private static double[][] blur;
	private static int blurRange = 1;
	
	private static int[][] edge;
	private static int edgeRange = 1;
	
	private static int[][] emboss;
	private static int embossRange = 1;
	
	private static int[][] engraving;
	private static int engravingRange = 1;         // PROVERITI DA LI TREBA DA SE DODAJE NEKE FIKSNA VREDNOST (OFFSET) !!!
	
	private static int[][] smooth;
	private static int smoothRange = 1;
	
	static {
		sharpen = new int[3][3];
		sharpen[0][0] = 0;
		sharpen[0][1] = -1;
		sharpen[0][2] = 0;
		sharpen[1][0] = -1;
		sharpen[1][1] = 5;
		sharpen[1][2] = -1;
		sharpen[2][0] = 0;
		sharpen[2][1] = -1;
		sharpen[2][2] = 0;
		
		blur = new double[3][3];
		blur[0][0] = 1.0/9.0f;
		blur[0][1] = 1.0/9.0f;
		blur[0][2] = 1.0/9.0f;
		blur[1][0] = 1.0/9.0f;
		blur[1][1] = 1.0/9.0f;
		blur[1][2] = 1.0/9.0f;
		blur[2][0] = 1.0/9.0f;
		blur[2][1] = 1.0/9.0f;
		blur[2][2] = 1.0/9.0f;
		
		edge = new int[3][3];
		edge[0][0] = -1;
		edge[0][1] = -1;
		edge[0][2] = -1;
		edge[1][0] = -1;
		edge[1][1] = 8;
		edge[1][2] = -1;
		edge[2][0] = -1;
		edge[2][1] = -1;
		edge[2][2] = -1;
		
		emboss = new int[3][3];
		emboss[0][0] = -2;
		emboss[0][1] = -1;
		emboss[0][2] = 0;
		emboss[1][0] = -1;
		emboss[1][1] = 1;
		emboss[1][2] = 1;
		emboss[2][0] = 0;
		emboss[2][1] = 1;
		emboss[2][2] = 2;
		
		engraving = new int[3][3];
		engraving[0][0] = -2;
		engraving[0][1] = 0;
		engraving[0][2] = 0;
		engraving[1][0] = 0;
		engraving[1][1] = 2;
		engraving[1][2] = 0;
		engraving[2][0] = 0;
		engraving[2][1] = 0;
		engraving[2][2] = 0;
		
		smooth = new int[3][3];
		smooth[0][0] = 1;
		smooth[0][1] = 1;
		smooth[0][2] = 1;
		smooth[1][0] = 1;
		smooth[1][1] = 5;
		smooth[1][2] = 1;
		smooth[2][0] = 1;
		smooth[2][1] = 1;
		smooth[2][2] = 1;
	}
	
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
		int r, g, b, a;
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
		
		if (scale >=- 100 && scale <= 100) {
			scale = (scale+100)/100;
			scale = scale*scale;
			for(int i = 0; i < width; i++){
				for(int j = 0; j < height; j++){
					r = Color.red(colorArray[j * width + i]);
					g = Color.green(colorArray[j * width + i]);
					b = Color.blue(colorArray[j * width + i]);
					a = Color.alpha(colorArray[j * width + i]);
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
					
					colorArray[j * width + i] = Color.argb(a, r, g, b);
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
	
	
	private static void initBlur(double sigma, int ksize) {
		if (ksize == 1 || ksize % 2 == 0) {
			return;
		}

		gaussianBlurRange = ksize / 2;
		gaussianBlur = new double[ksize][ksize];
		double scale = -0.5 / (sigma * sigma);
		double cons = -scale / Math.PI;
		double sum = 0;

		for (int i = 0; i < ksize; i++) {
			for (int j = 0; j < ksize; j++) {
				int x = i - (ksize - 1) / 2;
				int y = j - (ksize - 1) / 2;
				gaussianBlur[i ][ j] = cons * Math.exp(scale * (x * x + y * y));

				sum += gaussianBlur[i ][ j];
			}
		}

		for (int i = 0; i < ksize; i++) {
			for (int j = 0; j < ksize; j++) {
				gaussianBlur[i ][ j] /= sum;
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

		int bitmapX;
		int bitmapY;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				//Èuvaju podatke o trenutnom rezultujuæem pixel-u
				int red = 0;
				int green = 0;
				int blue = 0;
				
				//Iteracija kroz matricu filtera i taèke u slici koje odgovaraju njenim poljima
				for (int deltaX=-gaussianBlurRange; deltaX <= gaussianBlurRange; deltaX++)
					for (int deltaY=-gaussianBlurRange; deltaY <= gaussianBlurRange; deltaY++) {
						int filtX = deltaX + gaussianBlurRange;
						int filtY = deltaY + gaussianBlurRange;

						bitmapX = x + deltaX;
						bitmapX = bitmapX < 0 ? 0 : bitmapX >= width? width-1 : bitmapX;
						bitmapY = y + deltaY;
						bitmapY = bitmapY<0 ? 0 : bitmapY>=height?height-1:bitmapY;

						red   += gaussianBlur[filtY][filtX] * Color.red(bitmap.getPixel(bitmapX, bitmapY));
						green += gaussianBlur[filtY][filtX] * Color.green(bitmap.getPixel(bitmapX, bitmapY));
						blue  += gaussianBlur[filtY][filtX] * Color.blue(bitmap.getPixel(bitmapX, bitmapY));
					}

				colorArray[y * width + x] = Color.rgb((red>255 ? 255 : red<0?0:red) + 1, (green>255 ? 255 : green<0?0:green) + 1, (blue>255 ? 255 : blue<0?0:blue) + 1);
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		return returnBitmap;
	}
	
	public static Bitmap sharpen(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

		int bitmapX;
		int bitmapY;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				//Èuvaju podatke o trenutnom rezultujuæem pixel-u
				int red = 0;
				int green = 0;
				int blue = 0;
				
				//Iteracija kroz matricu filtera i taèke u slici koje odgovaraju njenim poljima
				for (int deltaX=-sharpenRange; deltaX <= sharpenRange; deltaX++)
					for (int deltaY=-sharpenRange; deltaY <= sharpenRange; deltaY++) {
						int filtX = deltaX + sharpenRange;
						int filtY = deltaY + sharpenRange;

						bitmapX = x + deltaX;
						bitmapX = bitmapX < 0 ? 0 : bitmapX >= width? width-1 : bitmapX;
						bitmapY = y + deltaY;
						bitmapY = bitmapY<0 ? 0 : bitmapY>=height?height-1:bitmapY;

						red   += sharpen[filtY][filtX] * Color.red(bitmap.getPixel(bitmapX, bitmapY));
						green += sharpen[filtY][filtX] * Color.green(bitmap.getPixel(bitmapX, bitmapY));
						blue  += sharpen[filtY][filtX] * Color.blue(bitmap.getPixel(bitmapX, bitmapY));
					}

				colorArray[y * width + x] = Color.rgb((red>255 ? 255 : red<0?0:red) + 1, (green>255 ? 255 : green<0?0:green) + 1, (blue>255 ? 255 : blue<0?0:blue) + 1);
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		return returnBitmap;
	}
	
	public static Bitmap blur(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

		int bitmapX;
		int bitmapY;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				//Èuvaju podatke o trenutnom rezultujuæem pixel-u
				int red = 0;
				int green = 0;
				int blue = 0;
				
				//Iteracija kroz matricu filtera i taèke u slici koje odgovaraju njenim poljima
				for (int deltaX=-blurRange; deltaX <= blurRange; deltaX++)
					for (int deltaY=-blurRange; deltaY <= blurRange; deltaY++) {
						int filtX = deltaX + blurRange;
						int filtY = deltaY + blurRange;

						bitmapX = x + deltaX;
						bitmapX = bitmapX < 0 ? 0 : bitmapX >= width? width-1 : bitmapX;
						bitmapY = y + deltaY;
						bitmapY = bitmapY<0 ? 0 : bitmapY>=height?height-1:bitmapY;

						red   += blur[filtY][filtX] * Color.red(bitmap.getPixel(bitmapX, bitmapY));
						green += blur[filtY][filtX] * Color.green(bitmap.getPixel(bitmapX, bitmapY));
						blue  += blur[filtY][filtX] * Color.blue(bitmap.getPixel(bitmapX, bitmapY));
					}

				colorArray[y * width + x] = Color.rgb((red>255 ? 255 : red<0?0:red) + 1, (green>255 ? 255 : green<0?0:green) + 1, (blue>255 ? 255 : blue<0?0:blue) + 1);
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		return returnBitmap;
	}
	
	public static Bitmap edge(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

		int bitmapX;
		int bitmapY;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				//Èuvaju podatke o trenutnom rezultujuæem pixel-u
				int red = 0;
				int green = 0;
				int blue = 0;
				
				//Iteracija kroz matricu filtera i taèke u slici koje odgovaraju njenim poljima
				for (int deltaX=-edgeRange; deltaX <= edgeRange; deltaX++)
					for (int deltaY=-edgeRange; deltaY <= edgeRange; deltaY++) {
						int filtX = deltaX + edgeRange;
						int filtY = deltaY + edgeRange;

						bitmapX = x + deltaX;
						bitmapX = bitmapX < 0 ? 0 : bitmapX >= width? width-1 : bitmapX;
						bitmapY = y + deltaY;
						bitmapY = bitmapY<0 ? 0 : bitmapY>=height?height-1:bitmapY;

						red   += edge[filtY][filtX] * Color.red(bitmap.getPixel(bitmapX, bitmapY));
						green += edge[filtY][filtX] * Color.green(bitmap.getPixel(bitmapX, bitmapY));
						blue  += edge[filtY][filtX] * Color.blue(bitmap.getPixel(bitmapX, bitmapY));
					}

				colorArray[y * width + x] = Color.rgb((red>255 ? 255 : red<0?0:red) + 1, (green>255 ? 255 : green<0?0:green) + 1, (blue>255 ? 255 : blue<0?0:blue) + 1);
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		return returnBitmap;
	}
	
	public static Bitmap emboss(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

		int bitmapX;
		int bitmapY;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				//Èuvaju podatke o trenutnom rezultujuæem pixel-u
				int red = 0;
				int green = 0;
				int blue = 0;
				
				//Iteracija kroz matricu filtera i taèke u slici koje odgovaraju njenim poljima
				for (int deltaX=-embossRange; deltaX <= embossRange; deltaX++)
					for (int deltaY=-embossRange; deltaY <= embossRange; deltaY++) {
						int filtX = deltaX + embossRange;
						int filtY = deltaY + embossRange;

						bitmapX = x + deltaX;
						bitmapX = bitmapX < 0 ? 0 : bitmapX >= width? width-1 : bitmapX;
						bitmapY = y + deltaY;
						bitmapY = bitmapY<0 ? 0 : bitmapY>=height?height-1:bitmapY;

						red   += emboss[filtY][filtX] * Color.red(bitmap.getPixel(bitmapX, bitmapY));
						green += emboss[filtY][filtX] * Color.green(bitmap.getPixel(bitmapX, bitmapY));
						blue  += emboss[filtY][filtX] * Color.blue(bitmap.getPixel(bitmapX, bitmapY));
					}

				colorArray[y * width + x] = Color.rgb((red>255 ? 255 : red<0?0:red) + 1, (green>255 ? 255 : green<0?0:green) + 1, (blue>255 ? 255 : blue<0?0:blue) + 1);
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		return returnBitmap;
	}
	
	public static Bitmap engraving(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

		int bitmapX;
		int bitmapY;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				//Èuvaju podatke o trenutnom rezultujuæem pixel-u
				int red = 0;
				int green = 0;
				int blue = 0;
				
				//Iteracija kroz matricu filtera i taèke u slici koje odgovaraju njenim poljima
				for (int deltaX=-engravingRange; deltaX <= engravingRange; deltaX++)
					for (int deltaY=-engravingRange; deltaY <= engravingRange; deltaY++) {
						int filtX = deltaX + engravingRange;
						int filtY = deltaY + engravingRange;

						bitmapX = x + deltaX;
						bitmapX = bitmapX < 0 ? 0 : bitmapX >= width? width-1 : bitmapX;
						bitmapY = y + deltaY;
						bitmapY = bitmapY<0 ? 0 : bitmapY>=height?height-1:bitmapY;

						red   += engraving[filtY][filtX] * Color.red(bitmap.getPixel(bitmapX, bitmapY));
						green += engraving[filtY][filtX] * Color.green(bitmap.getPixel(bitmapX, bitmapY));
						blue  += engraving[filtY][filtX] * Color.blue(bitmap.getPixel(bitmapX, bitmapY));
					}

				colorArray[y * width + x] = Color.rgb(((red+95)>255 ? 255 : (red+95)<0?0:(red+95)) + 1, ((green+95)>255 ? 255 : (green+95)<0?0:(green+95)) + 1, ((blue+95)>255 ? 255 : (blue+95)<0?0:(blue+95)) + 1);
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		return returnBitmap;
	}
	
	public static Bitmap smooth(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int colorArray[] = new int[width * height];
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);

		int bitmapX;
		int bitmapY;
		for (int x = smoothRange; x < width - smoothRange; x++)
			for (int y = smoothRange; y < height - smoothRange; y++) {
				//Èuvaju podatke o trenutnom rezultujuæem pixel-u
				int red = 0;
				int green = 0;
				int blue = 0;
				
				//Iteracija kroz matricu filtera i taèke u slici koje odgovaraju njenim poljima
				for (int deltaX=-smoothRange; deltaX <= smoothRange; deltaX++)
					for (int deltaY=-smoothRange; deltaY <= smoothRange; deltaY++) {
						int filtX = deltaX + smoothRange;
						int filtY = deltaY + smoothRange;

						bitmapX = x + deltaX;
						bitmapX = bitmapX < 0 ? 0 : bitmapX >= width? width-1 : bitmapX;
						bitmapY = y + deltaY;
						bitmapY = bitmapY<0 ? 0 : bitmapY>=height?height-1:bitmapY;

						red   += smooth[filtY][filtX] * Color.red(bitmap.getPixel(bitmapX, bitmapY));
						green += smooth[filtY][filtX] * Color.green(bitmap.getPixel(bitmapX, bitmapY));
						blue  += smooth[filtY][filtX] * Color.blue(bitmap.getPixel(bitmapX, bitmapY));
					}

				colorArray[y * width + x] = Color.rgb(((red/13)>255 ? 255 : (red/13)<0?0:(red/13)), ((green/13)>255 ? 255 : (green/13)<0?0:(green/13)), ((blue/13)>255 ? 255 : (blue/13)<0?0:(blue/13)));
				returnBitmap.setPixel(x, y, colorArray[y * width + x]);
			}
		return returnBitmap;
	}
	

	
	public static Bitmap gammaCorection(Bitmap src, double red, double green, double blue) {
	    // create output image
	    Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
	    // get image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // color information
	    int A, R, G, B;
	    int pixel;
	    // constant value curve
	    final int    MAX_SIZE = 256;
	    final double MAX_VALUE_DBL = 255.0;
	    final int    MAX_VALUE_INT = 255;
	    final double REVERSE = 1.0;
	 
	    // gamma arrays
	    int[] gammaR = new int[MAX_SIZE];
	    int[] gammaG = new int[MAX_SIZE];
	    int[] gammaB = new int[MAX_SIZE];
	 
	    if (red > 0 && green > 0 && blue > 0) {
	    // setting values for every gamma channels
	    for(int i = 0; i < MAX_SIZE; ++i) {
	        gammaR[i] = (int)Math.min(MAX_VALUE_INT,
	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
	        gammaG[i] = (int)Math.min(MAX_VALUE_INT,
	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
	        gammaB[i] = (int)Math.min(MAX_VALUE_INT,
	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
	    }
	 
	    // apply gamma table
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            // look up gamma
	            R = gammaR[Color.red(pixel)];
	            G = gammaG[Color.green(pixel)];
	            B = gammaB[Color.blue(pixel)];
	            // set new color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    }
	    // return final image
	    return bmOut;
	}
	
	public static Bitmap colorFilter(Bitmap src, double red, double green, double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
 
        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int)(Color.red(pixel) * red);
                G = (int)(Color.green(pixel) * green);
                B = (int)(Color.blue(pixel) * blue);
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
 
        // return final image
        return bmOut;
    }
	
	public static Bitmap saturationFilter(Bitmap source, int level) {
	    // get image size
	    int width = source.getWidth();
	    int height = source.getHeight();
	    int[] pixels = new int[width * height];
	    float[] HSV = new float[3];
	    // get pixel array from source
	    source.getPixels(pixels, 0, width, 0, 0, width, height);
	 
	    int index = 0;
	    // iteration through pixels
	    for(int y = 0; y < height; ++y) {
	        for(int x = 0; x < width; ++x) {
	            // get current index in 2D-matrix
	            index = y * width + x;
	            // convert to HSV
	            Color.colorToHSV(pixels[index], HSV);
	            // increase Saturation level
	            HSV[1] *= level;
	            HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));
	            // take color back
	            pixels[index] |= Color.HSVToColor(HSV);
	        }
	    }
	    // output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	    return bmOut;
	}
	
	public static Bitmap hueFilter(Bitmap source, int level) {
	    // get image size
	    int width = source.getWidth();
	    int height = source.getHeight();
	    int[] pixels = new int[width * height];
	    float[] HSV = new float[3];
	    // get pixel array from source
	    source.getPixels(pixels, 0, width, 0, 0, width, height);
	     
	    int index = 0;
	    // iteration through pixels
	    for(int y = 0; y < height; ++y) {
	        for(int x = 0; x < width; ++x) {
	            // get current index in 2D-matrix
	            index = y * width + x;              
	            // convert to HSV
	            Color.colorToHSV(pixels[index], HSV);
	            // increase Saturation level
	            HSV[0] *= level;
	            HSV[0] = (float) Math.max(0.0, Math.min(HSV[0], 360.0));
	            // take color back
	            pixels[index] |= Color.HSVToColor(HSV);
	        }
	    }
	    // output bitmap                
	    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	    return bmOut;       
	}

	public static Bitmap shadingFilter(Bitmap source, int shadingColor) {
	    // get image size
	    int width = source.getWidth();
	    int height = source.getHeight();
	    int[] pixels = new int[width * height];
	    // get pixel array from source
	    source.getPixels(pixels, 0, width, 0, 0, width, height);
	 
	    int index = 0;
	    // iteration through pixels
	    for(int y = 0; y < height; ++y) {
	        for(int x = 0; x < width; ++x) {
	            // get current index in 2D-matrix
	            index = y * width + x;
	            // AND
	            pixels[index] &= shadingColor;
	        }
	    }
	    // output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	    return bmOut;
	}
}

