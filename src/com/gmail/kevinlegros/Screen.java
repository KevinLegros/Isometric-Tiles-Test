package com.gmail.kevinlegros;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.*;

public class Screen {
	private BufferedImage image;
	public int[] pixels;
	
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private static final int MAP_WIDTH = 64;						// 2^n
	private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	
	public int[] tiles = new int[MAP_WIDTH * MAP_WIDTH * 2];		// xt, yt
	public int[] colors = new int[MAP_WIDTH * MAP_WIDTH * 4];		// c0 c1 c2 c3
	public int[] bits = new int[MAP_WIDTH * MAP_WIDTH * 2];			// bits
	
	public List<Tile> tileList = new ArrayList<Tile>();
	
	public int xScroll, yScroll;
	
	public int w, h;
	private int zoom = 3;
	private int oldzoom = zoom;
	private int newW, newH;
	
	public int xd = 0, yd = 0;
	
	public SpriteSheet sheet;
	
	public Screen(int w, int h, SpriteSheet sheet){
		this.w = w * zoom;
		this.h = h * zoom;
		newW = this.w;
		newH = this.h;
		this.sheet = sheet;
		image = new BufferedImage(w * zoom, h * zoom, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		xScroll = 32;
		yScroll = 32;
		
		colors[0] = 0xffffff;
		/*for(int i = 0; i < MAP_WIDTH * MAP_WIDTH; i++){
			colors[i * 4 + 0] = 0xffbf00;
			colors[i * 4 + 1] = 0x005810;
			colors[i * 4 + 2] = 0x267f00;
			colors[i * 4 + 3] = 0x007f46;
		}*/
		
		/*for(int i = 0; i < bits.length; i++){
			bits[i] = 2;
		}*/
		
		//createLevel();
		
	}
	
	public void update(){
		if(newW != w) w = newW;
		if(newH != h) h = newH;

		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		if(xd < 0 && xScroll > 32) xScroll += xd*5;
		if(xd > 0 && xScroll < (64*32)-32-w) xScroll += xd*5;
		if(yd < 0 && yScroll > 32) yScroll += yd*5;
		if(yd > 0 && yScroll < (64*16)-16-h) yScroll += yd*5;

		xd = yd = 0;
	}

	/*public void render(){
		int offs = 0;
		int tw = 32;
		int th = 16;
		
		if(newW != w) w = newW;
		if(newH != h) h = newH;

		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		//System.out.println("zoom level: "+zoom+" "+image.getWidth()+","+image.getHeight());
		
		
		for(int yt = (yScroll - th) >> 3; yt <= (yScroll + h + th) >> 3; yt++){
			//System.out.println(yt);
			int yoff = 0;
			if(yt % 2 == 1) yoff = th/2;
			int y0 = yt * th/2 - yoff - yScroll;
			int y1 = y0 + th;
			if(y0 < 0) y0 = 0;
			if(y1 > h) y1 = h;
			for(int xt = (xScroll - tw) >> 5; xt <= (xScroll + w + tw) >> 5; xt++){
				int xoff = 0;
				if(yt % 2 == 1) xoff -= tw/2;
				int x0 = xt * tw + xoff - xScroll;
				//System.out.println(yt % 2);
				int x1 = x0 + tw;
				if(x0 < 0) x0 = 0;
				if(x1 > w) x1 = w;
				
				int tileIndex = (xt & (MAP_WIDTH_MASK)) + (yt & (MAP_WIDTH_MASK)) * MAP_WIDTH;
				
				
				for(int y = y0; y < y1; y++){
					int sp = ((y - yoff + yScroll) & (th-1)) * sheet.width + ((x0 - xoff + xScroll) & (tw-1));		// source pointer
					int tp = offs + x0 + y * w;											// target pointer
					//System.out.println("line:"+(yt % 2)+" sp="+sp+" tp="+tp);
					for(int x = x0; x < x1; x++){
						//int col = tileIndex * 4 + sheet.pixels[sp++];
						//pixels[tp++] = colors[col];
						int fromCol = sheet.pixels[sp++];
						int toCol = tp++;
						if(fromCol != 0) {
							pixels[toCol] = fromCol;
						}
					}
				}
			}
		}
	}*/
	
	public void render(int xt, int yt, int tile, boolean selected){
		
		int tw = 32;
		int th = 16;
		
		int offs = 0;
		int yoff = 0;
		if(yt % 2 == 1) yoff = th/2;
		int y0 = yt * th/2 - yScroll;
		int y1 = y0 + th;
		if(y0 < 0) y0 = 0;
		if(y1 > h) y1 = h;

		int xoff = 0;
		if(yt % 2 == 1) xoff -= tw/2;
		int x0 = xt * tw + xoff - xScroll;
		//System.out.println(yt % 2);
		int x1 = x0 + tw;
		if(x0 < 0) x0 = 0;
		if(x1 > w) x1 = w;
		
		//if(yt == 1) System.out.println("tile at "+x0+","+y0+" for "+xt+","+yt+" offsets "+xoff+","+yoff);
		
		for(int y = y0; y < y1; y++){
			int sp = ((y - yoff + yScroll) & (th-1)) * sheet.width + ((x0 - xoff + xScroll) & (tw-1));		// source pointer
			int tp = offs + x0 + y * w;																		// target pointer
			for(int x = x0; x < x1; x++){
				int fromCol = sheet.pixels[sp++];
				int toCol = tp++;
				if(fromCol != 0) {
					pixels[toCol] = fromCol;
					if(selected) pixels[toCol] = colors[0];
				}
			}
		}
		
		
		/*xp -= xScroll;
		yp -= yScroll;

		int xTile = tile % 32;
		int yTile = tile / 32;
		int toffs = xTile * 32 + yTile * 16 * sheet.width;

		for (int y = 0; y < 16; y++) {
			int ys = y;
			if (y + yp < 0 || y + yp >= h) continue;
			for (int x = 0; x < 32; x++) {
				if (x + xp < 0 || x + xp >= w) continue;

				int xs = x;
				int col = (sheet.pixels[xs + ys * sheet.width + toffs] * 32);
				if (col < 255) {
					pixels[(x + xp) + (y + yp) * w] = col;
				}
			}
		}*/
		
		/*int x0 = x;
		int x1 = x + 32;
		int y0 = y;
		int y1 = y + 16;
		int w = x1 - x0;
		
		for(int yy = y0; yy < y1; yy++){
			int tp = yy * 32 + x0;
			int sp = (yy - y) * 32 + (x0 - x);
			tp -= sp;
			for(int xx = sp; xx < sp + w; sp++){
				int col = sPixels[xx];
				pixels[tp] = col;
			}
		}*/
	}
	
	public BufferedImage getImg(){
		return image;
	}
	
	public void zoomIn(){
		if(zoom == 5) return;
		oldzoom = zoom;
		zoom++;
		newW = w/oldzoom * zoom;
		newH = h/oldzoom * zoom;
	}
	
	public void zoomOut(){
		if(zoom == 1) return;
		oldzoom = zoom;
		zoom--;
		newW = w/oldzoom * zoom;
		newH = h/oldzoom * zoom;
		/*w = w/oldzoom * zoom;
		h = h/oldzoom * zoom;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		System.out.println("zoom level: "+zoom+" "+image.getWidth()+","+image.getHeight());*/
	}
}
