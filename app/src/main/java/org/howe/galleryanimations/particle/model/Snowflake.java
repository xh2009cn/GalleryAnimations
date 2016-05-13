package org.howe.galleryanimations.particle.model;

import android.graphics.Rect;


import org.howe.galleryanimations.DisplayUtils;
import org.howe.galleryanimations.Configs;

import java.util.Random;
import java.util.Set;

public class Snowflake {

    private static final int MAX_BIAS_TIME = 50;
    private static final float MAX_BEGIN_FALL_SPEED = DisplayUtils.dp2px(4);

    public float x;
    public float y;
    public float sy;
    public float g = DisplayUtils.dp2px(0.05f * 0.98f);
    public int bias = 0;
    public int biasTime = 0;
    public boolean interBias = false;
    public float scale;
    public float fadeRate;
    public boolean floor = false;
    private Random random;
    private int bitmapHeight;

    public Snowflake(float x, float y, int height) {
        this.x = x;
        this.y = y;
        bitmapHeight = height;
        random = new Random();
        sy = random.nextFloat() * MAX_BEGIN_FALL_SPEED;
        scale = 1.6f - 0.5f * (sy / MAX_BEGIN_FALL_SPEED);
        fadeRate = (0.35f + 0.3f * random.nextFloat()) * 0.001f;
    }

    public void update(float windX, float windY, Set<Snowflake> floorSnows) {
        if (biasTime < 0) {
            if (interBias) {
                bias = ((Integer) random.nextInt(3)).compareTo(1);
                interBias = false;
            } else {
                bias = 0;
                interBias = true;
            }

            biasTime = random.nextInt(MAX_BIAS_TIME);
        }

        biasTime--;
        sy += g;
        scale -= sy * fadeRate;
        if (scale < 0) {
            scale = 0;
        }

        y += sy + windY;
        if (!floor) {
            x += bias + windX;
            if (y >= Configs.getWidth() - scale * bitmapHeight) {
                y = Configs.getHeight() - scale * bitmapHeight;
                floor = true;
            } else {
                detectCollision(windY, floorSnows);
            }
        } else {
            if (y >= Configs.getWidth() - scale * bitmapHeight) {
                y = Configs.getHeight() - scale * bitmapHeight;
            } else {
                detectCollision(windY, floorSnows);
            }
        }
    }

    private void detectCollision(float windY, Set<Snowflake> floorSnows) {
        for (Snowflake snow : floorSnows) {
            if (snow != this) {
                Rect rect = new Rect((int)snow.x, (int)snow.y, (int)(snow.x + snow.scale * bitmapHeight), (int)(snow.y + snow.scale * bitmapHeight));
                Rect rectFlake = new Rect((int)x, (int)(y - sy - windY + scale * bitmapHeight), (int)(x + scale * bitmapHeight), (int)(y + scale * bitmapHeight));
                if (rect.intersect(rectFlake)) {
                    y = snow.y - scale * bitmapHeight;
                    floor = true;
                    break;
                }
            }
        }
    }
}