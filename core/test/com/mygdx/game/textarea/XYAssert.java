package com.mygdx.game.textarea;

import org.assertj.core.api.AbstractAssert;
import org.hamcrest.Factory;

import com.mygdx.game.XY;

public class XYAssert extends AbstractAssert<XYAssert, XY<Integer>> {
	public XYAssert(XY<Integer> location) {
		super(location, XYAssert.class);
	}

    public static XYAssert assertThat(TextAreaModel.Caret actual) {
        return new XYAssert(actual.location());
    }

    public static XYAssert assertThat(XY<Integer> actual) {
        return new XYAssert(actual);
    }

	@Factory
	public XYAssert at(int x, int y) {
        if (!actual.equals(new XY<Integer>(x, y))) {
            failWithMessage(actual + " is not at " + x + ", " + y);
        }
		return this;
	}
}