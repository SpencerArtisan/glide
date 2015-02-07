package com.mygdx.game.textarea;

public class Caret {
	private int x, y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void moveLeft() {
		x--;
	}
	
	public void moveRight() {
		x++;
	}
}