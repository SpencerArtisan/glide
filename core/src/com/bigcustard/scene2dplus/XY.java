package com.bigcustard.scene2dplus;

import com.google.common.base.Objects;

public class XY {
	public final int x, y;

	public XY(float x, float y) {
		this((int) x, (int) y);
	}

	public XY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public XY add(XY deltaXY) {
		return new XY(x + deltaXY.x, y + deltaXY.y);
	}

	public XY subtract(XY deltaXY) {
		return new XY(x - deltaXY.x, y - deltaXY.y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		XY xy = (XY) o;
		return Objects.equal(x, xy.x) &&
				Objects.equal(y, xy.y);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(x, y);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
