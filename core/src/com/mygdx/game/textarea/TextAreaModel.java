package com.mygdx.game.textarea;


public class TextAreaModel {
	private String text;
	private Caret caret;
	
	public TextAreaModel() {
		text = "";
		caret = new Caret();
	}

	public Caret caret() {
		return caret;
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
		int index = getCaretIndex();
		text = text.substring(0, index) + character + text.substring(index, text.length());
	}

	public void deleteCharacter() {
		int index = getCaretIndex();
		if (index > 0) {
			text = text.substring(0, index - 1) + text.substring(index, text.length());
			positionCaret(index - 1);
		}
	}

	private void positionCaret(int textIndex) {
		int row = 0;
		int index = 0;
		while (true) {
			int newlineIndex = text.indexOf('\n', index);
			if (newlineIndex == -1 || newlineIndex >= textIndex) {
				break;
			}
			row++;
			index = newlineIndex + 1;
		}
		caret.setX(textIndex - index);
		caret.setY(row);
	}

	private int getCaretIndex() {
		int index = getIndexForRow(caret.getY());
		index += caret.getX();
		return index;
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
			changeXIfBeyondEndOfLine();
		}

		public void moveLeft() {
			if (x != 0) {
				setX(x - 1);
			}
		}
		
		public void moveLeftAndWrap() {
			if (x != 0) {
				setX(x - 1);
			} else if (y > 0){
				moveUp();
				x = 999;
				changeXIfBeyondEndOfLine();
			}
		}
		
		public void moveRight() {
			setX(x + 1);
		}

		public void moveUp() {
			if (y != 0) {
				setY(y - 1);
			}
		}
		
		public void moveDown() {
			setY(y + 1);
		}
		
		private void changeXIfBeyondEndOfLine() {
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
	}
}
