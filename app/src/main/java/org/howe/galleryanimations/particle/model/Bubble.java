package org.howe.galleryanimations.particle.model;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class Bubble {
    public float x;
    public float y;
    public float beginX;
    public float beginY;
    public float sx;
    public float sy;
    public float scale;
    public float radius;
    private int beginFrame;

    public Bubble(int beginFrame, float x, float y, float beginX, float beginY, float sx, float sy, float scale, float radius) {
        this.beginFrame = beginFrame;
        this.x = x;
        this.y = y;
        this.beginX = beginX;
        this.beginY = beginY;
        this.sx = sx;
        this.sy = sy;
        this.scale = scale;
        this.radius = radius;
    }

    public Bubble(int beginFrame) {
        this.beginFrame = beginFrame;
    }

    public void update(int frame) {
        int deltaFrame = frame - beginFrame;
        x = beginX + radius * (float) Math.sin(deltaFrame * Math.PI / 180d) + sx * deltaFrame;
        y = beginY + sy * deltaFrame;
    }
}
