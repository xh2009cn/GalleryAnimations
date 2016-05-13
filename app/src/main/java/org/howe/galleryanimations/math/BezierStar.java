package org.howe.galleryanimations.math;


import org.howe.galleryanimations.DisplayUtils;
import org.howe.galleryanimations.effects.TaijiStarPath;
import org.howe.galleryanimations.particle.model.Point;
import org.howe.galleryanimations.particle.model.Star;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class BezierStar {

    private static final float MAX_DLETA = DisplayUtils.dp2px(65);

    public static List<Star> createStars(Point start, Point end, float d, Point... controls) {
        if (controls == null) {
            return null;
        }
        Random random = new Random();
        List<Star> bezierPoint = new ArrayList<Star>();
        int n = controls.length + 1;
        Point[] controlPoint = new Point[controls.length + 2];
        controlPoint[0] = start;
        System.arraycopy(controls, 0, controlPoint, 1, controls.length);
        controlPoint[controlPoint.length - 1] = end;
        int i, r;
        float u;

        bezierPoint.clear();
        // u的步长决定了曲线点的精度
        for (u = 0; u <= 1; u += d) {

            Star p[] = new Star[n + 1];
            for (i = 0; i <= n; i++) {
                p[i] = new Star();
                p[i].x = controlPoint[i].x;
                p[i].y = controlPoint[i].y;
            }

            for (r = 1; r <= n; r++) {
                for (i = 0; i <= n - r; i++) {
                    p[i].x = (1 - u) * p[i].x + u * p[i + 1].x;
                    p[i].y = (1 - u) * p[i].y + u * p[i + 1].y;
                }
            }
            bezierPoint.add(p[0]);
            for (int m = 0; m < TaijiStarPath.PATH_STAR_COUNT - 1; m++) {
                Star star = p[0].clone();
                star.rotate = 360 * u;
                star.x += (random.nextBoolean() ? 1 : -1) * (1 - u) * (1 - u) * MAX_DLETA;
                star.y += (random.nextBoolean() ? 1 : -1) * (1 - u) * (1 - u) * MAX_DLETA;
                bezierPoint.add(star);
            }
        }
        return bezierPoint;
    }
}
