package ru.fbtw.navigator.map_builder.canvas;



public final class ToolGroup {
	public static final int DECORATION = 0;
	public static final int TOOL = 1;
	public static final int NODE  = 2;

	public static final int OTHER = 3;

	public static int getToolGroupById(int id){
		return id < 5 ? 0 : id < 8 ? 1 : id < 12 ? 2 : 3;
	}
}
