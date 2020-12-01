package ru.fbtw.navigator.map_builder.connection_editor;

public class ConnectionEditorProperties {
	private int tool;
	private ConnectionEditorController source;

	public ConnectionEditorProperties(int tool) {
		this.tool = tool;
		source = null;
	}

	public int getTool() {
		return tool;
	}

	public void setTool(int tool) {
		this.tool = tool;
	}

	public ConnectionEditorController getSource() {
		return source;
	}

	public void setSource(ConnectionEditorController source) {
		this.source = source;
	}

	public static final ConnectionEditorProperties DEFAULT_PROPERTIES =
			new ConnectionEditorProperties(0);
}
