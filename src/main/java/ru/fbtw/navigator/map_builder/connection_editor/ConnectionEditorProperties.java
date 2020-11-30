package ru.fbtw.navigator.map_builder.connection_editor;

public class ConnectionEditorProperties {
	private int tool;
	private ConnectionEditorController sourse;

	public ConnectionEditorProperties(int tool) {
		this.tool = tool;
		sourse = null;
	}

	public static final ConnectionEditorProperties DEFAULT_PROPERTIES =
			new ConnectionEditorProperties(0);
}
