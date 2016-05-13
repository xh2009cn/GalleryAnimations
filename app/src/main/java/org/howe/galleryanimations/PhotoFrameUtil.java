package org.howe.galleryanimations;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class PhotoFrameUtil {

    public static float LEFT = 0.044588f;
    public static float TOP = 0.035f;
    public static float SCALEX = 0.90949f;
    public static float SCALEY = 0.80633f;

    public static Bitmap addPhotoFrame(Bitmap photo) {
        Bitmap frame = BitmapCollections.getInstance().getPhotoFrame();
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int photoWidth = photo.getWidth();
        int photoHeight = photo.getHeight();
        Bitmap newmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(frame, 0, 0, null);
        Matrix matrix = new Matrix();
        matrix.postScale(frameWidth * SCALEX / (float)photoWidth, frameHeight * SCALEY / (float) photoHeight);
        matrix.postTranslate(LEFT * frameWidth, TOP * frameHeight);
        canvas.drawBitmap(photo, matrix, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newmap;
    }
}
