package edu.oregonstate.cope.clientRecorder;

public interface ChangeOrigin {

	//change origin types
	public static final String CHANGE_ORIGIN_USER = "user";
	public static final String CHANGE_ORIGIN_REFACTORING = "refactoring";
	public static final String CHANGE_ORIGIN_UI_EVENT = "ui-event";
	public static final String CHANGE_ORIGIN_PASTE = "paste";
	public static final String CHANGE_ORIGIN_CUT = "cut";
	public static final String CHANGE_ORIGIN_UNDO = "undo";
	public static final String CHANGE_ORIGIN_REDO = "redo";

}
