package com.bigcustard.scene2dplus.textarea;

import com.bigcustard.scene2dplus.XY;
import org.assertj.core.api.AbstractAssert;
import org.hamcrest.Factory;

public class XYAssert extends AbstractAssert<XYAssert, XY> {
	public XYAssert(XY location) {
		super(location, XYAssert.class);
	}

    public static XYAssert assertThat(TextAreaModel.Caret actual) {
        return new XYAssert(actual.location());
    }

    public static XYAssert assertThat(XY actual) {
        return new XYAssert(actual);
    }

	@Factory
	public XYAssert at(int x, int y) {
        if (!actual.equals(new XY(x, y))) {
            failWithMessage(actual + " is not at (" + x + ", " + y + ")");
        }
		return this;
	}
}