package ru.fbtw.navigator.map_builder.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class EmptyImageMaker {
	public static final String DEFAULT_TYPE = "png";
	public static final String DEFAULT_FOLDER = "tmp/";

	public static File getEmptyImage(int width, int height, int color) {
		try {
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < image.getWidth(); x++)
				for (int y = 0; y < image.getHeight(); y++)
					image.setRGB(x,y,color);


			File output = new File(/*DEFAULT_FOLDER
					+ */UUID.randomUUID().toString().substring(0, 8)
					+ "." + DEFAULT_TYPE);

			ImageIO.write(image,DEFAULT_TYPE,output);

			return output;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
