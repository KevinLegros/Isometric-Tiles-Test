package com.gmail.kevinlegros;

public class Tile {
	public int x, y;
	public int w, h;
	private Level level;
	public boolean selected = false;
	
	public Tile(Level level, int x, int y){
		this.level = level;
		this.x = x;
		this.y = y;
		
		this.w = 32;
		this.h = 16;
	}
	
	public void render(Screen screen){
		screen.render(x, y, 0, selected);
	}
}
