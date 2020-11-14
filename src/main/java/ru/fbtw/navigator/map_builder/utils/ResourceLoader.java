package ru.fbtw.navigator.map_builder.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ResourceLoader {

	private static ClassLoader loader = ResourceLoader.class.getClassLoader();

	public static File loadFile(String fileName){
		return new File(Objects.requireNonNull(loader.getResource(fileName)).getFile());
	}

	public static InputStream loadIS(String fileName) throws IOException {

		return Objects.requireNonNull(loader.getResource(fileName)).openStream();
	}
}
