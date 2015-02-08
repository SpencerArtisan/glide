package com.mygdx.game.textarea;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector;
import com.mygdx.game.ResourceManager;

public class TextAreaControllerTest {
	private TextAreaController subject;
	private TextAreaModel model;
	
	@Before
	public void before() {
		model = new TextAreaModel();
		model.clear();
		subject = new TextAreaController(model);
	}
	
	@Test
	public void clickingInsideTextAreaSetsCaret() throws Exception {
//		model.setText("Hello\nWorld");
//		XY<Integer> clickPosition = new TextArea(model, new ResourceManager().getSkin()).caretToPosition(new XY(3, 1));
//		subject.touchUp(clickPosition.x, clickPosition.y, 0, 0);
//		assertThat(model.caret().getX(), is(3));
//		assertThat(model.caret().getY(), is(1));		
	}
	
	@Test
	public void clickingBeyondLineSetsCaretAtLineEnd() throws Exception {
		
		
	}
	
	@Test
	public void clickingBeyondRowsInsertsRowsAndSetsCaret() throws Exception {
		
	}
	
	@Test
	public void returnMovesToStartOfNextLine() throws Exception {
		subject.keyTyped(Key.Return.asChar());
		assertThat(model.getText(), is("\n"));
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(1));
	}
	
	@Test
	public void returnPushesNextLineDown() throws Exception {
		model.setText("Hello");
		subject.keyTyped(Key.Return.asChar());
		assertThat(model.getText(), is("\nHello"));
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(1));
	}
	
	@Test
	public void returnInMiddleOfLineSplitsIt() throws Exception {
		model.setText("Hello");
		model.caret().setX(2);
		subject.keyTyped(Key.Return.asChar());
		assertThat(model.getText(), is("He\nllo"));
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(1));
	}
	
	@Test
	public void clearRemovesText() {
		model.clear();
		assertThat(model.getText(), is(""));
	}
	
	@Test
	public void keyPressAddsText() {
		subject.keyTyped('a');
		assertThat(model.getText(), is("a"));
	}
	
	@Test
	public void multipleKeyPressesAddText() {
		subject.keyTyped('a');
		subject.keyTyped('b');
		subject.keyTyped('c');
		assertThat(model.getText(), is("abc"));
	}
	
	@Test
	public void deleteAtEndOfLineRemovesText() {
		subject.keyTyped('a');
		subject.keyTyped(Key.Delete.asChar());
		assertThat(model.getText(), is(""));
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void deleteInMiddleLineSquashesText() {
		model.setText("Hello\nThere");
		model.caret().setX(2);
		subject.keyTyped(Key.Delete.asChar());
		assertThat(model.getText(), is("Hllo\nThere"));
		assertThat(model.caret().getX(), is(1));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void deleteInMiddleSecondLineSquashesText() {
		model.setText("Hello\nThere");
		model.caret().setX(2);
		model.caret().setY(1);
		subject.keyTyped(Key.Delete.asChar());
		assertThat(model.getText(), is("Hello\nTere"));
		assertThat(model.caret().getX(), is(1));
		assertThat(model.caret().getY(), is(1));
	}
	
	@Test
	public void deleteBeyondStartOfLineMovesUp() {
		model.setText("a\n");
		model.caret().setY(1);
		subject.keyTyped(Key.Delete.asChar());
		assertThat(model.getText(), is("a"));
		assertThat(model.caret().getX(), is(1));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void deleteBeyondStartOfLineWithEmptyLinesMovesUp() {
		model.setText("a\n\n\nb");
		model.caret().setY(3);
		subject.keyTyped(Key.Delete.asChar());
		assertThat(model.getText(), is("a\n\nb"));
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(2));
	}
	
	@Test
	public void deleteBeyondStartOfBringsExistingLineUp() {
		model.setText("a\nb");
		model.caret().setY(1);
		subject.keyTyped(Key.Delete.asChar());
		assertThat(model.getText(), is("ab"));
		assertThat(model.caret().getX(), is(1));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void deleteBeyondStartOfFirstLineDoesNothing() {
		model.setText("a");
		subject.keyTyped(Key.Delete.asChar());
		assertThat(model.getText(), is("a"));
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void caretStartsAtOrigin() {
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void visibleKeyPressMovesCaretRight() {
		subject.keyTyped('a');
		assertThat(model.caret().getX(), is(1));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void invisibleKeyPressDoesNotMoveCaret() {
		subject.keyTyped(Key.Shift.asChar());
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void deleteMovesCaretLeft() {
		subject.keyTyped('a');
		subject.keyTyped(Key.Delete.asChar());
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}	
	
	@Test
	public void downArrowMovesDown() throws Exception {
		subject.keyTyped(Key.Down.asChar());
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(1));
	}
	
	@Test
	public void downArrowWhenLowerLineShorterGoesToEndOfLine() throws Exception {
		model.setText("Hello\nYou");
		model.caret().setX(5);
		subject.keyTyped(Key.Down.asChar());
		assertThat(model.caret().getX(), is(3));
		assertThat(model.caret().getY(), is(1));
	}
	
	@Test
	public void upArrowWhenHigherLineShorterGoesToEndOfLine() throws Exception {
		model.setText("Hi\nThere");
		model.caret().setX(5);
		model.caret().setY(1);
		subject.keyTyped(Key.Up.asChar());
		assertThat(model.caret().getX(), is(2));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void upArrowMovesUp() throws Exception {
		model.caret().setY(1);
		subject.keyTyped(Key.Up.asChar());
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void upArrowStopsAtTop() throws Exception {
		subject.keyTyped(Key.Up.asChar());
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void leftArrowMovesLeft() throws Exception {
		model.caret().setX(1);
		subject.keyTyped(Key.Left.asChar());
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void leftArrowStopsAtLeft() throws Exception {
		subject.keyTyped(Key.Left.asChar());
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void rightArrowMovesRight() throws Exception {
		model.setText("a");
		subject.keyTyped(Key.Right.asChar());
		assertThat(model.caret().getX(), is(1));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void rightArrowStopsAtEndOfLine() throws Exception {
		subject.keyTyped(Key.Right.asChar());
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void textEnteredAtCaretXPosition() throws Exception {
		model.setText("Hello\nWorld");
		model.caret().setX(2);
		subject.keyTyped('a');
		assertThat(model.getText(), is("Heallo\nWorld"));
	}
	
	@Test
	public void textEnteredAtCaretYPositionWhenYGreaterThanNumberOfLines() throws Exception {
		model.caret().setX(0);
		model.caret().setY(2);
		subject.keyTyped('a');
		assertThat(model.getText(), is("\n\na"));
	}
	
	@Test
	public void textEnteredAtCaretYPositionWhenYLessThanNumberOfLines() throws Exception {
		model.setText("Hello\nThere\nWorld");
		model.caret().setX(0);
		model.caret().setY(2);
		subject.keyTyped('a');
		assertThat(model.getText(), is("Hello\nThere\naWorld"));
	}
}