package org.howe.galleryanimations.math;

public class HeartCurve {

    public static float counterClockwiseX(float centerX, float radius, float t) {
        return centerX - radius * (float)  (16 * Math.sin(angle(t)) * Math.sin(angle(t)) * Math.sin(angle(t)));
    }

    public static float clockwiseX(float centerX, float radius, float t) {
        return centerX + radius * (float)  (16 * Math.sin(angle(t)) * Math.sin(angle(t)) * Math.sin(angle(t)));
    }

    public static float getY(float centerY,  float radius, float t) {
        return centerY - radius * (float) (13 * Math.cos(angle(t)) - 5 * Math.cos(angle(2 * t)) - 2 * Math.cos(angle(3 * t)) - Math.cos(angle(4 * t)));
    }

    private static double angle(float x) {
        return 2 * x * Math.PI / 360d;
    }
}








