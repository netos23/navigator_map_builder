package ru.fbtw.navigator.map_builder.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Printer {
	private PrintStream out;


	public Printer(File output) throws FileNotFoundException {
		out = new PrintStream(output);
	}


	public void write(String s){
		out.println(s);
	}
}
