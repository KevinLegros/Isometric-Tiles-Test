package com.gmail.kevinlegros;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	public int width, height;
	public int[] pixels;
	public boolean[] mask;
	
	public SpriteSheet(BufferedImage image){
		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		mask = new boolean[pixels.length];
		
		for(int i = 0; i < pixels.length; i++){
			int alpha = (pixels[i]) & 0xff;
			if(alpha == 0) mask[i] = false; else mask[i] = true;
		}
		
		/*
		for(int i = 0; i < pixels.length; i++){
			int alpha = (pixels[i]) & 0xff;
			int red = (pixels[i] >> 24) & 0xff;
			int green = (pixels[i] >> 16) & 0xff;
			int blue = (pixels[i] >> 8) & 0xff;
			if(alpha == 0) pixels[i] = 0;
			//pixels[i] = alpha + red + green + blue;
			if(pixels[i] != 0) System.out.println("pixel:"+pixels[i]+" alpha:"+alpha+",red:"+red+",green:"+green+",blue:"+blue);
			
			//pixels[i] = -1 << 24;
			
		}*/
	}
	
	public int[] getPixels(int x, int y){
		int[] spritePixels = new int[32*16];
		for(int yy = y; yy <= y+16; y++){
			for(int xx = x; xx <= x+32; x++){
				spritePixels[xx*yy] = pixels[xx*yy];
			}	
		}
		return spritePixels;
	}
	
	/*
	 * int alpha = (rgb >>> 24) & 0xff; == int alpha = rgb >>> 24
	 * int red = (rgb >>> 16) & 0xff;
	 * int green = (rgb >>> 8) & 0xff;
	 * int blue = rgb & 0xff;
	 * 
	 * &0xff keep last part, get one color
	 */
	
	/*
	 * arith
	 * 
	 * 00010111 << 1 = 00101110 (left add 0)
	 * 
	 * 10010111 >> 1 = 11001011 (right add most significant bit
	 * 
	 * 
	 * logic
	 * 
	 * 00010111 <<< 1 = 00010111 << 1
	 * 
	 * 00010111 >>> 1 = 00001011 (right add 0)
	 */
}
