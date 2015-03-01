package com.mygdx.game.textarea;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.XY;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class TextAreaControllerIntegrationTest {
	private TextAreaController subject;
	private TextAreaModel model;
    private InputEvent event;
    @Mock private ScrollableTextArea view;
    @Mock private Actor actor;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		model = new TextAreaModel(null);
		model.clear();
		subject = spy(new TextAreaController(model, view));
        event = new InputEvent();
        event.setListenerActor(actor);
        doReturn(true).when(subject).isOver((Actor)anyObject(), anyFloat(), anyFloat());
	}
	
	@Test
	public void clickingInsideTextAreaSetsCaret() throws Exception {
		model.setText("Hello\nWorld");
		XY<Integer> caretLocation = new XY<Integer>(3, 1);
		XY<Integer> clickPosition = new XY<Integer>(42, 84);
		when(view.worldPositionToCaretLocation(clickPosition)).thenReturn(caretLocation);
		subject.touchUp(event, clickPosition.x, clickPosition.y, 0, 0);
		XYAssert.assertThat(model.caret()).at(3, 1);
	}
	
	@Test
	public void clickingBeyondLineSetsCaretAtLineEnd() throws Exception {
		model.setText("Hello\nWorld");
		XY<Integer> caretLocation = new XY<Integer>(99, 1);
		XY<Integer> clickPosition = new XY<Integer>(42, 84);
		when(view.worldPositionToCaretLocation(clickPosition)).thenReturn(caretLocation);
		subject.touchUp(event, clickPosition.x, clickPosition.y, 0, 0);
        XYAssert.assertThat(model.caret()).at(5, 1);
	}
	
	@Test
	public void clickingBeyondRowsInsertsRowsAndSetsCaret() throws Exception {
		XY<Integer> caretLocation = new XY<Integer>(10, 3);
		XY<Integer> clickPosition = new XY<Integer>(42, 84);
		when(view.worldPositionToCaretLocation(clickPosition)).thenReturn(caretLocation);
		subject.touchUp(event, clickPosition.x, clickPosition.y, 0, 0);
		assertThat(model.getText()).isEqualTo(("\n\n\n"));
        XYAssert.assertThat(model.caret()).at(0, 3);
	}

	@Test
	public void clickingBeyondRowsAndEnteringText() throws Exception {
		XY<Integer> caretLocation = new XY<Integer>(10, 3);
		XY<Integer> clickPosition = new XY<Integer>(42, 84);
		when(view.worldPositionToCaretLocation(clickPosition)).thenReturn(caretLocation);
		subject.touchUp(event, clickPosition.x, clickPosition.y, 0, 0);
        subject.keyTyped(event, 'h');
		assertThat(model.getText()).isEqualTo(("\n\n\nh"));
        XYAssert.assertThat(model.caret()).at(1, 3);
	}

    @Test
    public void selectArea() {
        XY<Integer> areaStartScreen = new XY<Integer>(10, 300);
        XY<Integer> areaStart = new XY<Integer>(0, 0);
        XY<Integer> areaEndScreen = new XY<Integer>(10, 200);
        XY<Integer> areaEnd = new XY<Integer>(0, 5);
        when(view.worldPositionToCaretLocation(areaStartScreen)).thenReturn(areaStart);
        when(view.worldPositionToCaretLocation(areaEndScreen)).thenReturn(areaEnd);
        subject.touchDown(event, areaStartScreen.x, areaStartScreen.y, 0, 0);
        subject.touchDragged(event, areaEndScreen.x, areaEndScreen.y, 0);
        XYAssert.assertThat(model.caret().selection().getLeft()).at(0, 0);
        XYAssert.assertThat(model.caret().selection().getRight()).at(0, 5);
    }

    @Test
    public void typeWhenAreaSelectedReplacesText() {
        model.setText("hello\nthere");
        XY<Integer> selectionStart = new XY<Integer>(3, 0);
        XY<Integer> selectionEnd = new XY<Integer>(2, 1);
        model.caret().setSelection(selectionStart, selectionEnd);
        model.insert("Z");
        assertThat(model.getText()).isEqualTo("helZere");
    }

    @Test
    public void typeWhenAreaSelectedBeyondText() {
        model.setText("");
        XY<Integer> selectionStart = new XY<Integer>(3, 0);
        XY<Integer> selectionEnd = new XY<Integer>(2, 1);
        model.caret().setSelection(selectionStart, selectionEnd);
        model.insert("Z");
        assertThat(model.getText()).isEqualTo("\nZ");
    }

    @Test
    public void deletedWhenAreaSelectedDeletedSelectedText() {
        model.setText("hello\nthere");
        XY<Integer> selectionStart = new XY<Integer>(3, 0);
        XY<Integer> selectionEnd = new XY<Integer>(2, 1);
        model.caret().setSelection(selectionStart, selectionEnd);
        model.deleteCharacter();
        assertThat(model.getText()).isEqualTo("helere");
    }

	@Test
	public void returnMovesToStartOfNextLine() throws Exception {
		subject.keyTyped(event, Key.Return.asChar());
		assertThat(model.getText()).isEqualTo(("\n"));
        XYAssert.assertThat(model.caret()).at(0, 1);
	}
	
	@Test
	public void returnPushesNextLineDown() throws Exception {
		model.setText("Hello");
		subject.keyTyped(event, Key.Return.asChar());
		assertThat(model.getText()).isEqualTo(("\nHello"));
        XYAssert.assertThat(model.caret()).at(0, 1);
	}
	
	@Test
	public void returnInMiddleOfLineSplitsIt() throws Exception {
		model.setText("Hello");
		model.caret().moveRight(2);
		subject.keyTyped(event, Key.Return.asChar());
		assertThat(model.getText()).isEqualTo(("He\nllo"));
        XYAssert.assertThat(model.caret()).at(0, 1);
	}
	
	@Test
	public void clearRemovesText() {
		model.clear();
		assertThat(model.getText()).isEqualTo((""));
	}
	
	@Test
	public void keyPressAddsText() {
		subject.keyTyped(event, 'a');
		assertThat(model.getText()).isEqualTo(("a"));
	}
	
	@Test
	public void tabAddsSpaces() {
		subject.keyTyped(event, '\t');
		assertThat(model.getText()).isEqualTo(("    "));
	}

	@Test
	public void undoBeyondBeginningDoesNothing() {
        doReturn(true).when(subject).isControlDown();
        doReturn(false).when(subject).isShiftDown();
        subject.keyDown(event, Input.Keys.Z);

        assertThat(model.getText()).isEqualTo("");
	}

	@Test
	public void undoKeyPressRemovesText() {
        doReturn(true).when(subject).isControlDown();
        doReturn(false).when(subject).isShiftDown();
		subject.keyTyped(event, 'a');
		subject.keyTyped(event, 'b');
        subject.keyDown(event, Input.Keys.Z);

        assertThat(model.getText()).isEqualTo("a");
	}

	@Test
	public void redoKeyPressRestoresText() {
        doReturn(true).when(subject).isControlDown();
        doReturn(false).when(subject).isShiftDown();
		subject.keyTyped(event, 'a');
		subject.keyTyped(event, 'b');
        subject.keyDown(event, Input.Keys.Z);
        doReturn(true).when(subject).isShiftDown();
        subject.keyDown(event, Input.Keys.Z);

        assertThat(model.getText()).isEqualTo("ab");
	}

	@Test
	public void redoBeyondDoesNothing() {
        doReturn(true).when(subject).isControlDown();
        doReturn(true).when(subject).isShiftDown();
        subject.keyDown(event, Input.Keys.Z);

        assertThat(model.getText()).isEqualTo("");
	}

	@Test
	public void undoTypeThenRedoDoesNothing() {
        subject.keyTyped(event, 'a');
        doReturn(true).when(subject).isControlDown();
        doReturn(false).when(subject).isShiftDown();
        subject.keyDown(event, Input.Keys.Z);
        subject.keyTyped(event, 'b');
        doReturn(true).when(subject).isShiftDown();
        subject.keyDown(event, Input.Keys.Z);

        assertThat(model.getText()).isEqualTo("b");
	}

	@Test
	public void multipleUndoKeyPressesRemoveText() {
        doReturn(true).when(subject).isControlDown();
        doReturn(false).when(subject).isShiftDown();
		subject.keyTyped(event, 'a');
		subject.keyTyped(event, 'b');
		subject.keyTyped(event, 'c');
        subject.keyDown(event, Input.Keys.Z);
        subject.keyDown(event, Input.Keys.Z);

        assertThat(model.getText()).isEqualTo("a");
	}

	@Test
	public void multipleKeyPressesAddText() {
		subject.keyTyped(event, 'a');
		subject.keyTyped(event, 'b');
		subject.keyTyped(event, 'c');
		assertThat(model.getText()).isEqualTo(("abc"));
	}
	
	@Test
	public void deleteAtEndOfLineRemovesText() {
		subject.keyTyped(event, 'a');
		subject.keyTyped(event, Key.Delete.asChar());
		assertThat(model.getText()).isEqualTo((""));
        XYAssert.assertThat(model.caret()).at(0, 0);
	}
	
	@Test
	public void deleteInMiddleLineSquashesText() {
		model.setText("Hello\nThere");
		model.caret().moveRight(2);
		subject.keyTyped(event, Key.Delete.asChar());
		assertThat(model.getText()).isEqualTo(("Hllo\nThere"));
        XYAssert.assertThat(model.caret()).at(1, 0);
	}
	
	@Test
	public void deleteInMiddleSecondLineSquashesText() {
		model.setText("Hello\nThere");
		model.caret().moveRight(2);
		model.caret().moveDown();
		subject.keyTyped(event, Key.Delete.asChar());
		assertThat(model.getText()).isEqualTo(("Hello\nTere"));
        XYAssert.assertThat(model.caret()).at(1, 1);
	}
	
	@Test
	public void deleteBeyondStartOfLineMovesUp() {
		model.setText("a\n");
		model.caret().moveDown();
		subject.keyTyped(event, Key.Delete.asChar());
		assertThat(model.getText()).isEqualTo(("a"));
        XYAssert.assertThat(model.caret()).at(1, 0);
	}
	
	@Test
	public void deleteBeyondStartOfLineWithEmptyLinesMovesUp() {
		model.setText("a\n\n\nb");
        model.caret().moveDown();
        model.caret().moveDown();
        model.caret().moveDown();
		subject.keyTyped(event, Key.Delete.asChar());
		assertThat(model.getText()).isEqualTo(("a\n\nb"));
        XYAssert.assertThat(model.caret()).at(0, 2);
	}
	
	@Test
	public void deleteBeyondStartOfBringsExistingLineUp() {
		model.setText("a\nb");
        model.caret().moveDown();
		subject.keyTyped(event, Key.Delete.asChar());
		assertThat(model.getText()).isEqualTo(("ab"));
        XYAssert.assertThat(model.caret()).at(1, 0);
	}
	
	@Test
	public void deleteBeyondStartOfFirstLineDoesNothing() {
		model.setText("a");
		subject.keyTyped(event, Key.Delete.asChar());
		assertThat(model.getText()).isEqualTo(("a"));
        XYAssert.assertThat(model.caret()).at(0, 0);
	}
	
	@Test
	public void caretStartsAtOrigin() {
        XYAssert.assertThat(model.caret()).at(0, 0);
	}
	
	@Test
	public void visibleKeyPressMovesCaretRight() {
		subject.keyTyped(event, 'a');
        XYAssert.assertThat(model.caret()).at(1, 0);
	}
	
	@Test
	public void invisibleKeyPressDoesNotMoveCaret() {
		subject.keyTyped(event, Key.Shift.asChar());
        XYAssert.assertThat(model.caret()).at(0, 0);
	}
	
	@Test
	public void deleteMovesCaretLeft() {
		subject.keyTyped(event, 'a');
		subject.keyTyped(event, Key.Delete.asChar());
        XYAssert.assertThat(model.caret()).at(0, 0);
	}	
	
	@Test
	public void downArrowMovesDown() throws Exception {
		subject.keyTyped(event, Key.Down.asChar());
        XYAssert.assertThat(model.caret()).at(0, 1);
	}
	
	@Test
	public void downArrowWhenLowerLineShorterGoesToEndOfLine() throws Exception {
		model.setText("Hello\nYou");
		model.caret().moveRight(5);
		subject.keyTyped(event, Key.Down.asChar());
        XYAssert.assertThat(model.caret()).at(3, 1);
	}
	
	@Test
	public void upArrowWhenHigherLineShorterGoesToEndOfLine() throws Exception {
		model.setText("Hi\nThere");
        model.caret().moveDown();
        model.caret().moveRight(5);
		subject.keyTyped(event, Key.Up.asChar());
        XYAssert.assertThat(model.caret()).at(2, 0);
	}
	
	@Test
	public void upArrowMovesUp() throws Exception {
		model.caret().moveDown();
		subject.keyTyped(event, Key.Up.asChar());
        XYAssert.assertThat(model.caret()).at(0, 0);
	}
	
	@Test
	public void upArrowStopsAtTop() throws Exception {
		subject.keyTyped(event, Key.Up.asChar());
        XYAssert.assertThat(model.caret()).at(0, 0);
	}
	
	@Test
	public void leftArrowMovesLeft() throws Exception {
		model.caret().moveRight();
		subject.keyTyped(event, Key.Left.asChar());
        XYAssert.assertThat(model.caret()).at(0, 0);
	}
	
	@Test
	public void leftArrowStopsAtLeft() throws Exception {
		subject.keyTyped(event, Key.Left.asChar());
        XYAssert.assertThat(model.caret()).at(0, 0);
	}
	
	@Test
	public void rightArrowMovesRight() throws Exception {
		model.setText("a");
		subject.keyTyped(event, Key.Right.asChar());
        XYAssert.assertThat(model.caret()).at(1, 0);
	}
	
	@Test
	public void rightArrowStopsAtEndOfLine() throws Exception {
		subject.keyTyped(event, Key.Right.asChar());
        XYAssert.assertThat(model.caret()).at(0, 0);
	}
	
	@Test
	public void textEnteredAtCaretXPosition() throws Exception {
		model.setText("Hello\nWorld");
		model.caret().moveRight(2);
		subject.keyTyped(event, 'a');
		assertThat(model.getText()).isEqualTo(("Heallo\nWorld"));
	}
	
	@Test
	public void textEnteredAtCaretYPositionWhenYGreaterThanNumberOfLines() throws Exception {
		model.caret().moveDown();
		model.caret().moveDown();
		subject.keyTyped(event, 'a');
		assertThat(model.getText()).isEqualTo(("\n\na"));
	}
	
	@Test
	public void textEnteredAtCaretYPositionWhenYLessThanNumberOfLines() throws Exception {
		model.setText("Hello\nThere\nWorld");
        model.caret().moveDown();
        model.caret().moveDown();
		subject.keyTyped(event, 'a');
		assertThat(model.getText()).isEqualTo(("Hello\nThere\naWorld"));
	}
}