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

	private int currentLineLength() {
		int startRowIndex = getIndexForRow(caret.getY());
		int endOfRowIndex = text.indexOf('\n', startRowIndex);
		if (endOfRowIndex == -1) {
			endOfRowIndex = text.length();
		}
		return endOfRowIndex - startRowIndex;
	}

	public class Caret {
		private XY<Integer> location;
		
		public Caret() {
			this(0, 0);
		}
		
		public Caret(int x, int y) {
			this.location = new XY<Integer>(x, y);
		}

		public XY<Integer> location() {
			return location;
		}
		
		public void setLocation(XY<Integer> caretLocation) {
			setX(caretLocation.x);
			setY(caretLocation.y);
		}

		public int getX() {
			return location.x;
		}

		public void setX(int x) {
			location.x = x;
		}

		public int getY() {
			return location.y;
		}

		public void setY(int y) {
			location.y = y;
			changeXIfBeyondEndOfLine();
		}

		public void moveLeft() {
			if (getX() != 0) {
				setX(getX() - 1);
			}
		}
		
		public void moveRight() {
			if (getX() < currentLineLength()) {
				setX(getX() + 1);
			}
		}

		public void moveUp() {
			if (getY() != 0) {
				setY(getY() - 1);
			}
		}
		
		public void moveDown() {
			setY(getY() + 1);
		}
		
		private void changeXIfBeyondEndOfLine() {
			int lineLength = currentLineLength();
			if (getX() > lineLength) {
				setX(lineLength);
			}
		}
	}
}
