package org.howe.galleryanimations;

/**
 * @author xionghao01
 * @version 2.2.0
 */
public class Configs {

    public static final int FPS = 60;
    public static final int FRAME_DURATION = 1000 / FPS;
    private static int sWidth;
    private static int sHeight;

    public static int getWidth() {
        return sWidth;
    }

    public static void setWidth(int width) {
        sWidth = width;
    }

    public static int getHeight() {
        return sHeight;
    }

    public static void setHeight(int height) {
        sHeight = height;
    }
}
