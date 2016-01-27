package com.gmail.kevinlegros;

public class Level {
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	
	private Tile[] tiles = new Tile[WIDTH * HEIGHT];
	private int selectedIndex = -1;
	
	public Level(){
		for(int y = 0; y < HEIGHT; y++){
			for(int x = 0; x < WIDTH; x++){
				tiles[x + y * WIDTH] = new Tile(this, x, y);
				//System.out.println("new tile at "+x+","+y);
			}
		}
		tiles[1].selected = true;
	}
	
	public void render(Screen screen){
		for(int y = 0; y < HEIGHT; y++){
			for(int x = 0; x < WIDTH; x++){
				tiles[x + y * WIDTH].render(screen);
				//System.out.println("render tile "+tiles[x + y * WIDTH].x+","+tiles[x + y * WIDTH].y);
			}
		}
	}
	
	public void setSelected(int xx, int yy){
		int index = (((xx) % 32) + (yy / 8) * WIDTH) - WIDTH;
		if(selectedIndex != -1) tiles[selectedIndex].selected = false;
		System.out.println((xx%32)+","+(yy/8)+"  "+xx+","+yy+"   "+index);
		tiles[index].selected = true;
		selectedIndex = index;
	}
}
