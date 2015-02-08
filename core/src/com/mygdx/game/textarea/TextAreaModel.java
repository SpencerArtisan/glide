package com.mygdx.game.textarea;

import java.awt.datatransfer.StringSelection;

public class TextAreaModel {
	private String text;
	private Caret caret;
	
	public TextAreaModel() {
		text = "";
		caret = new Caret();
	}

	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}

	public void clear() {
		text = "";
	}

	public void insert(char character) {
		int index = getIndexForRow(caret.getY());
		index += caret.getX();
		text = text.substring(0, index) + character + text.substring(index, text.length());
	}

	private int getIndexForRow(int row) {
		int index = 0;
		for (int y = 0; y < row; y++) {
			index = text.indexOf('\n', index);
			if (index == -1) {
				text = text.concat("\n");
				index = text.length() - 1;
			}
			index++;
		}
		return index;
	}

	public void deleteCharacter() {
		text = text.substring(0, text.length() - 1);
	}

	public Caret caret() {
		return caret;
	}

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
			int startRowIndex = getIndexForRow(y);
			int endOfRowIndex = text.indexOf('\n', startRowIndex);
			if (endOfRowIndex == -1) {
				endOfRowIndex = text.length();
			}
			int rowLength = endOfRowIndex - startRowIndex;
			if (x > rowLength) {
				x = rowLength;
			}
		}

		public void moveLeft() {
			setX(x - 1);
		}
		
		public void moveRight() {
			setX(x + 1);
		}

		public void moveUp() {
			setY(y - 1);
		}
		
		public void moveDown() {
			setY(y + 1);
		}
		
	}
}
