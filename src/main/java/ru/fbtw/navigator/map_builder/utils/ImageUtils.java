package ru.fbtw.navigator.map_builder.utils;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ImageUtils {
    public static final String DEFAULT_TYPE = "png";
    public static final String DEFAULT_FOLDER = "C:\\Users\\nikmo\\IdeaProjects\\navigator\\map_builder\\tmp\\";

    public static File getEmptyImage(int width, int height, int color) {
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < image.getWidth(); x++)
                for (int y = 0; y < image.getHeight(); y++)
                    image.setRGB(x, y, color);


            File output = new File(nextFilename());


            ImageIO.write(image, DEFAULT_TYPE, output);

            return output;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Image fromBase64(String base64) {
        byte[] bytes = Base64.decodeBase64(base64);
        ByteInputStream inputStream = new ByteInputStream(bytes, bytes.length);
        return new Image(inputStream);
    }

    public static String nextFilename() {
        return DEFAULT_FOLDER
                + UUID.randomUUID().toString().substring(0, 8)
                + "." + DEFAULT_TYPE;
    }

    public static ImageView loadImage(String filename) throws IOException {

        InputStream imageIO = ResourceLoader.loadIS(filename);
        Image image = new Image(imageIO);
        return new ImageView(image);

    }


    public static byte[] getImageFromNode(Node node, int width, int height, boolean async) {
        WritableImage image = new WritableImage(width, height);
        if(async){
            Platform.runLater(() -> node.snapshot(null, image));
        }else{
            node.snapshot(null, image);
        }

        RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            ImageIO.write(renderedImage, DEFAULT_TYPE, output);

            File file = new File(nextFilename());
            ImageIO.write(renderedImage, DEFAULT_TYPE, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toByteArray();
    }
}
