package com.gmail.kevinlegros;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends Canvas implements Runnable{
	
	/*
	 * 1:18:49
	 */

	private static final long serialVersionUID = 1L;
	public static final int GAME_HEIGHT = 120;
	public static final int GAME_WIDTH = 160;
	public static final int SCALE = 3;
	private final int picSize = 3;
	private final int framerate = 60;
	private int zoom = 3;

	private boolean running = true;
	private int fps = 0;
	
	private Screen screen;
	private Level level;
	
	private boolean leftpress;
	private boolean rightpress;
	private boolean uppress;
	private boolean downpress;
	
	
	public Game(){
		try {
			screen = new Screen(GAME_WIDTH, GAME_HEIGHT, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/tiles_.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		addKeyListener(new KeyHandler());
		addMouseListener(new MouseHandler());
	}
	
	public class KeyHandler implements KeyListener{
		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_A) screen.zoomIn();
			if(e.getKeyCode() == KeyEvent.VK_Z)	screen.zoomOut();
			if(e.getKeyCode() == KeyEvent.VK_UP) uppress = true; 
			if(e.getKeyCode() == KeyEvent.VK_DOWN) downpress = true;
			if(e.getKeyCode() == KeyEvent.VK_LEFT) leftpress = true; 
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) rightpress = true;
		}

		public void keyReleased(KeyEvent e) {	
			if(e.getKeyCode() == KeyEvent.VK_UP) uppress = false; 
			if(e.getKeyCode() == KeyEvent.VK_DOWN) downpress = false;
			if(e.getKeyCode() == KeyEvent.VK_LEFT) leftpress = false; 
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) rightpress = false;
		}
		
	}
	
	public class MouseHandler implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			//System.out.println(screen.bits[(e.getX() * e.getY())/64]);
			level.setSelected(e.getX(), e.getY());
		}

		public void mousePressed(MouseEvent e) {			
		}

		public void mouseReleased(MouseEvent e) {			
		}

		public void mouseEntered(MouseEvent e) {			
		}

		public void mouseExited(MouseEvent e) {	
		}
		
	}
	
	public void start(){
		new Thread(this).start();
		running = true;
	}
	
	public void stop(){
		running = false;
	}
	
	public void run() {
		long last = System.nanoTime();
		double unproc = 0;
		double nanoPerTick = 1000000000 / framerate;
		int frames = 0;
		int ticks = 0;
		long timer = System.currentTimeMillis();
		
		while(running){
			long now = System.nanoTime();
			unproc += (now - last) / nanoPerTick;
			last = now;		// limits to 60 fps
			boolean shouldRender = false;
			
			while(unproc >= 1){
				ticks++;
				update();
				unproc -= 1;
				shouldRender = true;
			}
			
			try{
				Thread.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
			if(shouldRender){
				frames++;
				render();
			}
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println(ticks+"  "+frames);
				fps = frames;
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void update(){
		if(level == null) level = new Level();
		/*screen.xScroll+=2;
		screen.yScroll++;*/
		if(uppress) screen.yd--;
		if(downpress) screen.yd++;
		if(leftpress) screen.xd--;
		if(rightpress) screen.xd++;
		
		if(screen != null) screen.update();
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		
		//screen.render();
		if(level != null) level.render(screen);
		
		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int ww = GAME_WIDTH * picSize;
		int hh = GAME_HEIGHT * picSize;
		int xo = (getWidth() - ww) /2;
		int yo = (getHeight() - hh) /2;
		g.drawImage(screen.getImg(), xo, yo, ww, hh, null); 
		
		g.setColor(Color.black);
		g.drawString("FPS: "+fps, 11, 16);
		g.setColor(Color.blue);
		g.drawString("FPS: "+fps, 10, 15);
		
		g.drawString("Arrows to move", 10, 30);
		g.drawString("A/Z to zoom", 10, 45);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args){
		Game game = new Game();
		game.setMinimumSize(new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE));
		
		JFrame frame = new JFrame("aaa");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		game.start();
	}

}
