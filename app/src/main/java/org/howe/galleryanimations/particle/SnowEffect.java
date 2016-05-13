package org.howe.galleryanimations.particle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.howe.galleryanimations.R;
import org.howe.galleryanimations.DisplayUtils;
import org.howe.galleryanimations.Configs;
import org.howe.galleryanimations.particle.model.Snowflake;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class SnowEffect {
    private Bitmap bitmap;
    private Random random = new Random();
    private Set<Snowflake> flakes = new HashSet<Snowflake>();
    private Set<Snowflake> floorSnows = new HashSet<Snowflake>();
    private float windX = 0;
    private float windY = 0;

    public SnowEffect(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_snow);
        windX = (random.nextBoolean() ? 1 : -1) * random.nextFloat() * DisplayUtils.dp2px(3);
    }

    public synchronized void update(Canvas c, Matrix matrix, Paint paint) {
        if (random.nextBoolean()) {
            flakes.add(new Snowflake(random.nextFloat() * 2f * Configs.getWidth() - 0.5f * Configs.getWidth(), -bitmap.getWidth(), bitmap.getHeight()));
        }

        Iterator<Snowflake> iterator = flakes.iterator();
        while (iterator.hasNext()) {
            Snowflake flake = iterator.next();
            flake.update(windX, windY, floorSnows);
            matrix.reset();
            paint.reset();
            matrix.postScale(flake.scale, flake.scale);
            matrix.postTranslate(flake.x, flake.y);
            int alpha = (int) (Math.min(flake.scale, 1) * 255);
            paint.setAlpha(alpha);
            c.drawBitmap(bitmap, matrix, paint);

            if (flake.x > 1.5f * Configs.getWidth() || flake.x < -bitmap.getWidth() - 0.5f * Configs.getWidth() || alpha <= 0) {
                iterator.remove();
                if (floorSnows.contains(flake)) {
                    floorSnows.remove(flake);
                }
            } else if (flake.floor) {
                floorSnows.add(flake);
            }
        }
    }

    public void windBlow(float x, float y) {
        windX += x;
        windY += y;
        windY = Math.max(windY, 0);
    }

    public void clear() {
        flakes.clear();
        floorSnows.clear();
    }
}
