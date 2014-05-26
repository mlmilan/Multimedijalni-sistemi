package com.example.imagegallery;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Operation {
	
	public static Bitmap blend(Bitmap bitmap1, Bitmap bitmap2, double alpha) {
		if (bitmap1 == null) {
			return null;
		}
		if (bitmap2 == null) {
			return bitmap1;
		}
		int width = bitmap1.getWidth();
		int height = bitmap1.getHeight();
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		if(alpha > 0 || alpha < 1) {
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++) {
					int red, green, blue;
					if (bitmap2.getHeight() > y && bitmap2.getWidth() > x) {
				    	blue = (int)(Color.blue(bitmap1.getPixel(x, y))*(1-alpha) + Color.blue(bitmap2.getPixel(x, y))*alpha);
				    	green = (int)(Color.green(bitmap1.getPixel(x, y))*(1-alpha) + Color.green(bitmap2.getPixel(x, y))*alpha);
				    	red = (int)(Color.red(bitmap1.getPixel(x, y))*(1-alpha) + Color.red(bitmap2.getPixel(x, y))*alpha);
					} else {
						blue = (int)(Color.blue(bitmap1.getPixel(x, y)));
					    green = (int)(Color.green(bitmap1.getPixel(x, y)));
					    red = (int)(Color.red(bitmap1.getPixel(x, y)));
					}
					    
				    returnBitmap.setPixel(x, y, Color.rgb(red, green, blue));
				}
		} else {
			return bitmap1;
		}
		return returnBitmap;
	}
	public static Bitmap multiply(Bitmap bitmap1, Bitmap bitmap2) {
		if (bitmap1 == null) {
			return null;
		}
		if (bitmap2 == null) {
			return bitmap1;
		}
		int width = bitmap1.getWidth();
		int height = bitmap1.getHeight();
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				int red, green, blue;
				if (bitmap2.getHeight() > y && bitmap2.getWidth() > x) {
			    	blue = Color.blue(bitmap1.getPixel(x, y)) & Color.blue(bitmap2.getPixel(x, y));			    		
			    	green = Color.green(bitmap1.getPixel(x, y)) & Color.green(bitmap2.getPixel(x, y));
			    	red = Color.red(bitmap1.getPixel(x, y)) & Color.red(bitmap2.getPixel(x, y));
				} else {
					blue = (int)(Color.blue(bitmap1.getPixel(x, y)));
				    green = (int)(Color.green(bitmap1.getPixel(x, y)));
				    red = (int)(Color.red(bitmap1.getPixel(x, y)));
				}
				    
			    returnBitmap.setPixel(x, y, Color.rgb(red, green, blue));
			}
		return returnBitmap;
	}
	
	public static Bitmap difference(Bitmap bitmap1, Bitmap bitmap2) {
		if (bitmap1 == null) {
			return null;
		}
		if (bitmap2 == null) {
			return bitmap1;
		}
		int width = bitmap1.getWidth();
		int height = bitmap1.getHeight();
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				int red, green, blue;
				if (bitmap2.getHeight() > y && bitmap2.getWidth() > x) {
			    	blue = (int)Math.abs((Color.blue(bitmap1.getPixel(x, y)) - Color.blue(bitmap2.getPixel(x, y))));
			    	green = (int)Math.abs((Color.green(bitmap1.getPixel(x, y)) - Color.green(bitmap2.getPixel(x, y))));
			    	red = (int)Math.abs((Color.red(bitmap1.getPixel(x, y)) - Color.red(bitmap2.getPixel(x, y))));
				} else {
					blue = (int)(Color.blue(bitmap1.getPixel(x, y)));
				    green = (int)(Color.green(bitmap1.getPixel(x, y)));
				    red = (int)(Color.red(bitmap1.getPixel(x, y)));
				}
				    
			    returnBitmap.setPixel(x, y, Color.rgb(red, green, blue));
			}
		return returnBitmap;
	}
	private static double luminance(int pixel) {
		return (0.2126 * Color.red(pixel)) + (0.7152 * Color.green(pixel)) + (0.0722 * Color.blue(pixel));
	}

	public static Bitmap lighter (Bitmap bitmap1, Bitmap bitmap2) {
		if (bitmap1 == null) {
			return null;
		}
		if (bitmap2 == null) {
			return bitmap1;
		}
		int width = bitmap1.getWidth();
		int height = bitmap1.getHeight();
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				int red, green, blue;
				if (bitmap2.getHeight() > y && bitmap2.getWidth() > x) {
					if (luminance(bitmap1.getPixel(x, y)) >= luminance(bitmap2.getPixel(x, y))) {
						blue = Color.blue(bitmap1.getPixel(x, y));
						green = Color.green(bitmap1.getPixel(x, y));
						red = Color.red(bitmap1.getPixel(x, y));
					} else {
						blue = Color.blue(bitmap2.getPixel(x, y));
						green = Color.green(bitmap2.getPixel(x, y));
						red = Color.red(bitmap2.getPixel(x, y));
					}
				} else {
					blue = Color.blue(bitmap1.getPixel(x, y));
				    green = Color.green(bitmap1.getPixel(x, y));
				    red = Color.red(bitmap1.getPixel(x, y));
				}
				    
			    returnBitmap.setPixel(x, y, Color.rgb(red, green, blue));
			}
		return returnBitmap;
	}
	
	public static Bitmap darker (Bitmap bitmap1, Bitmap bitmap2) {
		if (bitmap1 == null) {
			return null;
		}
		if (bitmap2 == null) {
			return bitmap1;
		}
		int width = bitmap1.getWidth();
		int height = bitmap1.getHeight();
		Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				int red, green, blue;
				if (bitmap2.getHeight() > y && bitmap2.getWidth() > x) {
					if (luminance(bitmap1.getPixel(x, y)) <= luminance(bitmap2.getPixel(x, y))) {
						blue = Color.blue(bitmap1.getPixel(x, y));
						green = Color.green(bitmap1.getPixel(x, y));
						red = Color.red(bitmap1.getPixel(x, y));
					} else {
						blue = Color.blue(bitmap2.getPixel(x, y));
						green = Color.green(bitmap2.getPixel(x, y));
						red = Color.red(bitmap2.getPixel(x, y));
					}
				} else {
					blue = Color.blue(bitmap1.getPixel(x, y));
				    green = Color.green(bitmap1.getPixel(x, y));
				    red = Color.red(bitmap1.getPixel(x, y));
				}
				    
			    returnBitmap.setPixel(x, y, Color.rgb(red, green, blue));
			}
		return returnBitmap;
	}



}
