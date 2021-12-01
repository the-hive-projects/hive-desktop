package org.thehive.hivedesktop.util;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static byte[] scaleImageContent(byte[] content, int width, int height) throws IOException {
        var imageByteArrayInputStream = new ByteArrayInputStream(content);
        var bufferedImage = ImageIO.read(imageByteArrayInputStream);
        var scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        var scaledBufferedImage = convertImageToBufferedImage(scaledImage);
        return extractByteArrayFromBufferedImage(scaledBufferedImage);
    }

    public static BufferedImage convertImageToBufferedImage(Image image) {
        return convertImageToBufferedImage(image, "png");
    }

    public static BufferedImage convertImageToBufferedImage(Image img, String format) {
        if (img instanceof BufferedImage)
            return (BufferedImage) img;
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bufferedImage;
    }

    public static byte[] extractByteArrayFromBufferedImage(BufferedImage image) throws IOException {
        var imageByteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", imageByteArrayOutputStream);
        return imageByteArrayOutputStream.toByteArray();
    }

}
