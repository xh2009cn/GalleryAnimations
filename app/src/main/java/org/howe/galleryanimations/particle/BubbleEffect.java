package org.howe.galleryanimations.particle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import org.howe.galleryanimations.R;
import org.howe.galleryanimations.DisplayUtils;
import org.howe.galleryanimations.Configs;
import org.howe.galleryanimations.particle.model.Bubble;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class BubbleEffect {

    private static final int SX = DisplayUtils.dp2px(2);
    private static final int SY = DisplayUtils.dp2px(2);
    private static final int RADIUS = DisplayUtils.dp2px(20);
    private Bitmap mBitmap;
    private Random mRandom = new Random();
    private List<Bubble> mBubbleList = new LinkedList<Bubble>();

    public BubbleEffect(Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_bubble);
    }

    public synchronized void update(int frame, Canvas c, Matrix matrix) {
        if (mRandom.nextInt(23) == 0) {
            Bubble bubble = new Bubble(frame);
            bubble.beginX = mBitmap.getWidth() + mRandom.nextFloat() * (Configs.getWidth() - 2 * mBitmap.getWidth());
            bubble.beginY = Configs.getHeight() + mBitmap.getHeight();
            bubble.sx = -SX + mRandom.nextFloat() * 2 * SX;
            bubble.sy = -(SY + SY * mRandom.nextFloat());
            bubble.x = bubble.beginX;
            bubble.y = bubble.beginY;
            bubble.scale = 0.2f + 0.6f * mRandom.nextFloat();
            bubble.radius = RADIUS + RADIUS * mRandom.nextFloat();
            mBubbleList.add(bubble);
        }

        Iterator<Bubble> iterator = mBubbleList.iterator();
        while (iterator.hasNext()) {
            Bubble bubble = iterator.next();
            bubble.update(frame);
            if (bubble.x < -mBitmap.getWidth() || bubble.x > Configs.getWidth() + mBitmap.getWidth() || bubble.y < -mBitmap.getHeight()) {
                iterator.remove();
            } else {
                matrix.reset();
                matrix.postScale(bubble.scale, bubble.scale);
                matrix.postTranslate(bubble.x, bubble.y);
                c.drawBitmap(mBitmap, matrix, null);
            }
        }
    }

    public synchronized void clear() {
        mBubbleList.clear();
    }
}
