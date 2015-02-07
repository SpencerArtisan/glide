package com.mygdx.game.textarea;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector;

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
	public void deleteRemovesText() {
		subject.keyTyped('a');
		subject.keyTyped('\b');
		assertThat(model.getText(), is(""));
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
		subject.keyTyped('\u007F');
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}
	
	@Test
	public void deleteMovesCaretLeft() {
		subject.keyTyped('a');
		subject.keyTyped('\b');
		assertThat(model.caret().getX(), is(0));
		assertThat(model.caret().getY(), is(0));
	}	
}