package com.bigcustard.scene2dplus.textarea;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.util.Watchable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class TextAreaModel implements Disposable {
	private static final String END = "$END$";
	private String text;
	private Caret caret;
	private ColorCoder colorCoder;
	private Watchable<TextAreaModel> changeWatchable = new Watchable<>();
	private BiFunction<String, TextAreaModel, String> preInsertVetoer;
	private boolean readOnly;

	public TextAreaModel(String text, ColorCoder colorCoder) {
		this.text = text;
		this.colorCoder = colorCoder;
		caret = new Caret();
        caret.moveToBottom();
    }

	public TextAreaModel(ColorCoder colorCoder) {
		this("", colorCoder);
	}

	public void addChangeListener(Consumer<TextAreaModel> listener) {
		changeWatchable.watch(listener);
	}

	public void preInsertVetoer(BiFunction<String, TextAreaModel, String> preInsertVetoer) {
		this.preInsertVetoer = preInsertVetoer;
	}

	public Caret caret() {
		return caret;
	}

    public State state() {
        return new State();
    }

    public void setState(State state) {
        text = state.text;
        caret().setLocation(state.caretLocation);
        if (state.caretSelection != null) {
            caret().setSelection(state.caretSelection.getLeft(), state.caretSelection.getRight());
        }
        changeWatchable.broadcast(this);
    }

	public void clear() {
		setText("");
	}

    public String text() {
		return text;
	}

	public void setText(String text) {
		if (!readOnly) {
			this.text = text;
			changeWatchable.broadcast(this);
		}
	}

	public String coloredText() {
		return colorCoder.encode(text);
	}

    public Map<Integer, Color> getColoredLines() {
        return colorCoder.colorLines(text);
    }

	public void insert(String characters) {
		if (isReadOnly()) return;
		if (preInsertVetoer != null) characters = preInsertVetoer.apply(characters, this);

		int indexfOfEnd = characters.contains(END) ? characters.indexOf(END) : characters.length();
		characters = characters.replace(END, "");

		int fromIndex, toIndex;
        if (caret().isAreaSelected()) {
            fromIndex = getIndex(caret().selection().getLeft());
            toIndex = getIndex(caret().selection().getRight());
            caret().clearSelection();
        } else {
            fromIndex = getIndex(caret.location());
            toIndex = fromIndex;
        }
        setText(text.substring(0, fromIndex) + characters + text.substring(toIndex, text.length()));
        positionCaret(fromIndex + indexfOfEnd);
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
        XY location = caret().location();
        int fromIndex = getIndex(new XY(0, location.y));
        int toIndex = fromIndex + currentLineLength();
        return text.substring(fromIndex, toIndex);
    }

	public int numberOfRows() {
		return StringUtils.countMatches(text, "\n");
	}

	private int currentLineLength() {
		return lineLength(caret.y());
	}

	private int lineLength(int y) {
		int startRowIndex = getIndexForRow(y);
		int endOfRowIndex = text.indexOf('\n', startRowIndex);
		if (endOfRowIndex == -1) {
			endOfRowIndex = text.length();
		}
		return endOfRowIndex - startRowIndex;
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

	private int getIndexForRow(int row) {
		int index = 0;
		for (int y = 0; y < row; y++) {
			index = text.indexOf('\n', index);
			if (index == -1) {
				setText(text.concat("\n"));
				index = text.length() - 1;
			}
			index++;
		}
		return index;
	}

    private int getIndex(XY location) {
        int index = getIndexForRow(location.y);
        index += location.x;
        return Math.min(index, text.length());
    }

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public void dispose() {
		changeWatchable.dispose();
	}

	public class Caret {
		private XY location;
        private Pair<XY, XY> selection;
		
		private Caret() {
			this(0, 0);
		}

        private Caret(int x, int y) {
			this.location = new XY(x, y);
		}

		public XY location() {
			return location;
		}

        public Pair<XY, XY> selection() {
            return selection;
        }

        public void setLocation(XY caretLocation) {
			this.location = caretLocation;
            changeXIfBeyondEndOfLine();
            clearSelection();
		}

        private void setLocation(int x, int y) {
			setLocation(new XY(x, y));
		}

        public void clearSelection() {
            selection = null;
            changeWatchable.broadcast(TextAreaModel.this);
        }

        public void setSelection(XY start, XY end) {
            setLocation(end);
            if (start.y < end.y || (Objects.equals(start.y, end.y) && start.x < end.x)) {
                selection = Pair.of(start, end);
            } else {
                selection = Pair.of(end, start);
			}
			changeSelectionIfBeyondEndOfLine();
			changeWatchable.broadcast(TextAreaModel.this);
        }

		private int x() {
			return location.x;
		}

		private void setX(int x) {
			setLocation(x, location.y);
		}

		private int y() {
			return location.y;
		}

		private void setY(int y) {
            setLocation(location.x, y);
			changeXIfBeyondEndOfLine();
		}

		public void moveLeft() {
			if (x() != 0) {
				setX(x() - 1);
			}
		}

		public void moveRight() {
            moveRight(1);
		}

		public void moveRight(int n) {
            setX(x() + n);
			if (x() > currentLineLength()) {
                setX(currentLineLength());
			}
		}

		public void moveUp() {
			if (y() != 0) {
				setY(y() - 1);
			}
		}
		
		public void moveDown() {
			setY(y() + 1);
		}

		public void moveToBottom() {
			setY(numberOfRows());
		}

		public void moveToTop() {
			setLocation(new XY(0, 0));
		}

		public boolean isAreaSelected() {
			return selection != null;
		}

		private void changeXIfBeyondEndOfLine() {
			int lineLength = currentLineLength();
			if (x() > lineLength) {
				setX(lineLength);
			}
		}

		private void changeSelectionIfBeyondEndOfLine() {
			selection = Pair.of(
					new XY(Math.min(selection.getLeft().x, lineLength(selection.getLeft().y)), selection.getLeft().y),
					new XY(Math.min(selection.getRight().x, lineLength(selection.getRight().y)), selection.getRight().y));
		}

		@Override
		public String toString() {
			return "Caret " + location;
		}
    }

    public class State {
        private String text;
        private XY caretLocation;
        private Pair<XY, XY> caretSelection;

        private State() {
            this.text = text();
            this.caretLocation = caret().location();
            this.caretSelection = caret().selection();
        }
    }
}
