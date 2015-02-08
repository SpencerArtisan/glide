package com.mygdx.game.textarea;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.mygdx.game.XY;

public class AtXY extends TypeSafeMatcher<TextAreaModel.Caret> {
	private XY<Integer> location;

	public AtXY(XY<Integer> location) {
		this.location = location;
	}

	@Override
	public boolean matchesSafely(TextAreaModel.Caret caret) {
		return this.location.equals(caret.location());
	}

	@Factory
	public static <T> Matcher<TextAreaModel.Caret> at(int x, int y) {
		return new AtXY(new XY<Integer>(x, y));
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(location.toString());
	}
}