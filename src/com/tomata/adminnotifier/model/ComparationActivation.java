package com.tomata.adminnotifier.model;

public enum ComparationActivation {

	Equal(0), 	LessEqual(1), 	GreaterEqual(2), 	Greater(3), 	Less(4);
	private final int  field;
	private ComparationActivation(int  f) {
		field = f;
	}
	public int  getField() {
		return field;
	}
}
