package com.mygdx.game.textarea;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.XY;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TextAreaModel {
	private String text;
	private Caret caret;
	private ColorCoder colorCoder;
    private List<Runnable> listeners = new ArrayList<>();

	
	public TextAreaModel(String text, ColorCoder colorCoder) {
		this.text = text;
		this.colorCoder = colorCoder;
		caret = new Caret();
        caret.moveToBottom();
    }

	public TextAreaModel(ColorCoder colorCoder) {
		this("", colorCoder);
	}
	
	public Caret caret() {
		return caret;
	}

    public State getState() {
        return new State();
    }

    public void setState(State state) {
        text = state.text;
        caret().setLocation(state.caretLocation);
        if (state.caretSelection != null) {
            caret().setSelection(state.caretSelection.getLeft(), state.caretSelection.getRight());
        }
        informListeners();
    }

    public String getText() {
		return text;
	}
	
	public String getColoredText() {
		return colorCoder.encode(text);
	}

    public Map<Integer, Color> getColoredLines() {
        return colorCoder.colorLines(text);
    }

	public void setText(String text) {
		this.text = text;
        informListeners();
	}

	public void clear() {
		setText("");
	}

	public String insert(String characters) {
        int fromIndex, toIndex;
        if (caret().isAreaSelected()) {
            fromIndex = getIndex(caret().selection().getLeft());
            toIndex = getIndex(caret().selection().getRight());
            caret().clearSelection();
        } else {
            fromIndex = getIndex(caret.location());
            toIndex = fromIndex;
        }
        String deleted = text.substring(fromIndex, toIndex);
        setText(text.substring(0, fromIndex) + characters + text.substring(toIndex, text.length()));
        positionCaret(fromIndex + characters.length());
        return deleted;
	}

	public String deleteCharacter() {
        int fromIndex, toIndex;
        if (caret().isAreaSelected()) {
            fromIndex = getIndex(caret().selection().getLeft());
            toIndex = getIndex(caret().selection().getRight());
            positionCaret(fromIndex);
        } else {
            toIndex = getIndex(caret.location());
            fromIndex = Math.max(0, toIndex - 1);
            positionCaret(fromIndex);
        }
        String deleted = text.substring(fromIndex, toIndex);
        setText(text.substring(0, fromIndex) + text.substring(toIndex, text.length()));
        return deleted;
	}

    public String getSelection() {
        if (caret().isAreaSelected()) {
            int fromIndex = getIndex(caret().selection().getLeft());
            int toIndex = getIndex(caret().selection().getRight());
            return text.substring(fromIndex, toIndex);
        }
        return null;
    }

    public String getCurrentLine() {
        XY<Integer> location = caret().location();
        int fromIndex = getIndex(new XY<>(0, location.y));
        int toIndex = fromIndex + currentLineLength();
        return text.substring(fromIndex, toIndex);
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
		caret.setLocation(textIndex - index, row);
	}

    private int getIndex(XY<Integer> location) {
        int index = getIndexForRow(location.y);
        index += location.x;
        return Math.min(index, text.length());
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
	
	private int numberOfRows() {
		return StringUtils.countMatches(text, "\n");
	}

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void informListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public class Caret {
		private XY<Integer> location;
        private Pair<XY<Integer>, XY<Integer>> selection;
		
		private Caret() {
			this(0, 0);
		}

        private Caret(int x, int y) {
			this.location = new XY<>(x, y);
		}

		public XY<Integer> location() {
			return location;
		}

        public Pair<XY<Integer>, XY<Integer>> selection() {
            return selection;
        }

        public void setLocation(XY<Integer> caretLocation) {
			this.location = caretLocation;
            changeXIfBeyondEndOfLine();
            clearSelection();
		}

        private void setLocation(int x, int y) {
			setLocation(new XY<>(x, y));
		}

        public void clearSelection() {
            selection = null;
            informListeners();
        }

        public void setSelection(XY<Integer> start, XY<Integer> end) {
            setLocation(end);
            if (start.y < end.y || (start.y == end.y && start.x < end.x)) {
                selection = Pair.of(start, end);
            } else {
                selection = Pair.of(end, start);
            }
            informListeners();
        }

		private int getX() {
			return location.x;
		}

		private void setX(int x) {
			setLocation(x, location.y);
		}

		private int getY() {
			return location.y;
		}

		private void setY(int y) {
            setLocation(location.x, y);
			changeXIfBeyondEndOfLine();
		}

		public void moveLeft() {
			if (getX() != 0) {
				setX(getX() - 1);
			}
		}

		public void moveRight() {
            moveRight(1);
		}

		public void moveRight(int n) {
            setX(getX() + n);
			if (getX() > currentLineLength()) {
                setX(currentLineLength());
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
		

		public void moveToBottom() {
			setY(numberOfRows());
		}

		private void changeXIfBeyondEndOfLine() {
			int lineLength = currentLineLength();
			if (getX() > lineLength) {
				setX(lineLength);
			}
		}

		@Override
		public String toString() {
			return "Caret " + location;
		}

        public boolean isAreaSelected() {
            return selection != null;
        }
    }

    public class State {
        private String text;
        private XY<Integer> caretLocation;
        private Pair<XY<Integer>, XY<Integer>> caretSelection;

        private State() {
            this.text = getText();
            this.caretLocation = caret().location();
            this.caretSelection = caret().selection();
        }
    }
}
