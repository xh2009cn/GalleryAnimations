package org.howe.galleryanimations.math;


import org.howe.galleryanimations.particle.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class BezierCurLogic {

    public static List<Point> createBezierPoints(Point start, Point end, float d, Point... controls) {
        if (controls == null) {
            return null;
        }

        List<Point> bezierPoint = new ArrayList<Point>();
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

            Point p[] = new Point[n + 1];
            for (i = 0; i <= n; i++) {
                p[i] = new Point(controlPoint[i].x, controlPoint[i].y);
            }

            for (r = 1; r <= n; r++) {
                for (i = 0; i <= n - r; i++) {
                    p[i].x = (1 - u) * p[i].x + u * p[i + 1].x;
                    p[i].y = (1 - u) * p[i].y + u * p[i + 1].y;
                }
            }
            bezierPoint.add(p[0]);
        }
        return bezierPoint;
    }
}
