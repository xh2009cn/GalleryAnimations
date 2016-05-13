package org.howe.galleryanimations.effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import org.howe.galleryanimations.DisplayUtils;
import org.howe.galleryanimations.BitmapCollections;
import org.howe.galleryanimations.Configs;
import org.howe.galleryanimations.particle.model.Point;
import org.howe.galleryanimations.math.BezierStar;
import org.howe.galleryanimations.particle.model.Star;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class TaijiStarPath implements IEffect {

    private static final int FRAME_COUNT = 100;
    private static final int STAR_FRAME = 30;
    private static final int SPLASH_LIGHT_FRAME = 15;
    private static final int TEXT_FRAME = 20;
    public static final int PATH_STAR_COUNT = 1;
    private static final int PATH_CREATE_STAR_COUNT = 4;
    private static final int MAX_TEXT_SIZE = DisplayUtils.dp2px(14);
    private static final int MAX = DisplayUtils.dp2px(35);
    private Random mRandom = new Random();
    private List<Star> mTopPathStarList = new ArrayList<Star>();
    private List<Star> mBottomPathStarList = new ArrayList<Star>();
    private List<Star> mTopCreateStarList = new LinkedList<Star>();
    private List<Star> mBottomCreateStarList = new LinkedList<Star>();

    @Override
    public void run(int frame, Canvas canvas, Matrix matrix, Paint paint) {
        if (frame < 0 || frame >= FRAME_COUNT) {
            return;
        }
        if (frame == 0) {
            initBezierCureStar();
        }

        drawBackground(matrix, canvas);
        if (frame < STAR_FRAME + SPLASH_LIGHT_FRAME + TEXT_FRAME) {
            drawRotateBitmap(frame, matrix, canvas);
        }

        if (frame <= STAR_FRAME) {
            drawTopPathStar(frame, matrix, canvas);
            drawBottomPathStar(frame, matrix, canvas);
        }

        drawTopCreateStar(frame, matrix, paint, canvas);
        drawBottomCreateStar(frame, matrix, paint, canvas);

        if (frame >= STAR_FRAME - 4 && frame < STAR_FRAME + SPLASH_LIGHT_FRAME) {
            drawSplashLight(frame, matrix, paint, canvas);
        }

        if (frame > STAR_FRAME + SPLASH_LIGHT_FRAME) {
            drawAnimationText(frame, canvas, paint);
        }
    }

    private void drawAnimationText(int frame, Canvas canvas, Paint paint) {
        float ratio = (frame - STAR_FRAME - SPLASH_LIGHT_FRAME) / (float) TEXT_FRAME;
        ratio = Math.min(1, ratio);
        int width = Configs.getWidth();
        String title = "xh2009cn";
        String content = "GalleryAnimations";
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setTextSize(MAX_TEXT_SIZE * ratio);
        float songWidth = paint.measureText(title);
        float singerWidth = paint.measureText(content);
        canvas.drawText(title, width / 2 - ratio * songWidth / 2, width / 2 - getFontHeight(paint) / 2, paint);
        canvas.drawText(content, width / 2  - ratio * singerWidth / 2, width / 2 + getFontHeight(paint) / 2, paint);
    }

    @Override
    public int getFrameCount() {
        return FRAME_COUNT;
    }

    private int getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    private void initBezierCureStar() {
        int w = Configs.getWidth();
        Point[] topCtrlPoints = new Point[5];
        topCtrlPoints[0] = new Point(0.757f * w, 0.331f * w);
        topCtrlPoints[1] = new Point(0.559f * w, 0.182f * w);
        topCtrlPoints[2] = new Point(0.41f * w, 0.231f * w);
        topCtrlPoints[3] = new Point(0.286f * w, 0.409f * w);
        topCtrlPoints[4] = new Point(0.435f * w, 0.558f * w);
        mTopPathStarList = BezierStar.createStars(new Point(0.837f * w, 0.5f * w), new Point(0.47f * w, 0.45f * w), 1 / (float) STAR_FRAME, topCtrlPoints);

        Point[] bottomCtrlPoints = new Point[4];
        bottomCtrlPoints[0] = new Point(0.4f * w, 0.844f * w);
        bottomCtrlPoints[1] = new Point(0.551f * w, 0.818f * w);
        bottomCtrlPoints[2] = new Point(0.735f * w, 0.633f * w);
        bottomCtrlPoints[3] = new Point(0.604f * w, 0.476f * w);
        mBottomPathStarList = BezierStar.createStars(new Point(0.204f * w, 0.5f * w), new Point(0.47f * w, 0.45f * w), 1 / (float) STAR_FRAME, bottomCtrlPoints);
    }

    private void drawBackground(Matrix matrix, Canvas canvas) {
        int width = Configs.getWidth();
        Bitmap backgroundBitmap = BitmapCollections.getInstance().getStarBackgroundBitmap();
        matrix.reset();
        matrix.postScale(width / (float) backgroundBitmap.getWidth(), width / (float) backgroundBitmap.getHeight());
        canvas.drawBitmap(backgroundBitmap, matrix, null);
    }

    private void drawRotateBitmap(int frame, Matrix matrix, Canvas canvas) {
        Bitmap rotateBitmap = BitmapCollections.getInstance().getRotateBitmap();
        matrix.reset();
        matrix.postRotate(frame % 360, rotateBitmap.getWidth() / 2, rotateBitmap.getHeight() / 2);
        canvas.drawBitmap(rotateBitmap, matrix, null);
    }

    private void drawTopPathStar(int frame, Matrix matrix, Canvas canvas) {
        Bitmap starBitmap = BitmapCollections.getInstance().getHeartBitmap();
        int topSize = mTopPathStarList.size();
        int curFrame = (frame + 1) * PATH_STAR_COUNT;
        if (curFrame < topSize) {
            for (int i = frame * PATH_STAR_COUNT; i < curFrame; i++) {
                Star star = mTopPathStarList.get(i);
                if (star.active) {
                    // star.x += star.xi;
                    // star.y += star.yi;
                    if (star.live <= 0) {
                        star.active = false;
                    }
                    matrix.reset();
                    matrix.postTranslate(star.x, star.y);
                    matrix.postRotate(star.rotate, star.x + starBitmap.getWidth() / 2, star.y + starBitmap.getHeight() / 2);
                    canvas.drawBitmap(starBitmap, matrix, null);
                }

                for (int k = 0; k < PATH_CREATE_STAR_COUNT; k++) {
                    Star rStar = star.clone();
                    float r = (topSize - curFrame * 0.8f) / (float)topSize;
                    rStar.x += Math.min(MAX, mRandom.nextFloat() * (mRandom.nextBoolean() ? 1 : -1) * DisplayUtils.dp2px(60) * r * r);
                    rStar.y += Math.min(MAX, mRandom.nextFloat() * (mRandom.nextBoolean() ? 1 : -1) * DisplayUtils.dp2px(60) * r * r);
                    rStar.xi = (mRandom.nextBoolean() ? 1 : -1) * mRandom.nextFloat() * 0.3f * (topSize - curFrame) / (float) topSize;
                    rStar.yi = (mRandom.nextBoolean() ? 1 : -1) * mRandom.nextFloat() * 0.3f * (topSize - curFrame) / (float) topSize;
                    mTopCreateStarList.add(rStar);
                }
            }
        }
    }

    private void drawBottomPathStar(int frame, Matrix matrix, Canvas canvas) {
        Bitmap starBitmap = BitmapCollections.getInstance().getHeartBitmap();
        int bottomSize = mBottomPathStarList.size();
        int curFrame = (frame + 1) * PATH_STAR_COUNT;
        if (curFrame < bottomSize) {
            for (int i = frame * PATH_STAR_COUNT; i < curFrame; i++) {
                Star star = mBottomPathStarList.get(i);
                if (star.active) {
                    // star.x += star.xi;
                    // star.y += star.yi;
                    if (star.live <= 0) {
                        star.active = false;
                    }
                    matrix.reset();
                    matrix.postTranslate(star.x, star.y);
                    matrix.postRotate(star.rotate, star.x + starBitmap.getWidth() / 2, star.y + starBitmap.getHeight() / 2);
                    canvas.drawBitmap(starBitmap, matrix, null);
                }

                for (int k = 0; k < PATH_CREATE_STAR_COUNT; k++) {
                    Star rStar = star.clone();
                    float r = (bottomSize - curFrame * 0.8f) / (float)bottomSize;
                    rStar.x += Math.min(MAX, mRandom.nextFloat() * (mRandom.nextBoolean() ? 1 : -1) * DisplayUtils.dp2px(60) * r * r);
                    rStar.y += Math.min(MAX, mRandom.nextFloat() * (mRandom.nextBoolean() ? 1 : -1) * DisplayUtils.dp2px(60) * r * r);
                    rStar.xi = (mRandom.nextBoolean() ? 1 : -1) * mRandom.nextFloat() * 0.3f * (bottomSize - curFrame) / (float) bottomSize;
                    rStar.yi = (mRandom.nextBoolean() ? 1 : -1) * mRandom.nextFloat() * 0.3f * (bottomSize - curFrame) / (float) bottomSize;
                    mBottomCreateStarList.add(rStar);
                }
            }
        }
    }

    private void drawTopCreateStar(int frame, Matrix matrix, Paint paint, Canvas canvas) {
        Bitmap starBitmap = BitmapCollections.getInstance().getHeartBitmap();
        Iterator<Star> iterator = mTopCreateStarList.iterator();
        while(iterator.hasNext()) {
            Star star = iterator.next();
            if (star.active) {
                star.rotate += 6 * (STAR_FRAME - frame) / (float) STAR_FRAME;
                star.scale -= star.fade * 2 / 3f;
                //star.x += star.xi;
                //star.y += star.yi;
                star.live -= star.fade;
                if (star.live <= 0) {
                    star.active = false;
                } else {
                    matrix.reset();
                    paint.reset();
                    matrix.postScale(star.scale, star.scale);
                    matrix.postRotate(star.rotate);
                    matrix.postTranslate(star.x, star.y);
                    paint.setAlpha((int) (255 * star.live));
                    canvas.drawBitmap(starBitmap, matrix, paint);
                }
            } else {
                iterator.remove();
            }
        }
    }

    private void drawBottomCreateStar(int frame, Matrix matrix, Paint paint, Canvas canvas) {
        Bitmap starBitmap = BitmapCollections.getInstance().getHeartBitmap();
        Iterator<Star> it = mBottomCreateStarList.iterator();
        while(it.hasNext()) {
            Star star = it.next();
            if (star.active) {
                star.rotate += 6 * (STAR_FRAME - frame)  / (float) STAR_FRAME;
                star.scale -= star.fade * 2 / 3f;
                // star.x += star.xi;
                // star.y += star.yi;
                star.live -= star.fade;
                if (star.live <= 0) {
                    star.active = false;
                } else {
                    matrix.reset();
                    paint.reset();
                    matrix.postScale(star.scale, star.scale);
                    matrix.postRotate(star.rotate);
                    matrix.postTranslate(star.x, star.y);
                    paint.setAlpha((int) (255 * star.live));
                    canvas.drawBitmap(starBitmap, matrix, paint);
                }
            } else {
                it.remove();
            }
        }
    }

    private void drawSplashLight(int frame, Matrix matrix, Paint paint, Canvas canvas) {
        int width = Configs.getWidth();
        int height = Configs.getHeight();
        Bitmap horizontalLightBitmap = BitmapCollections.getInstance().getHorizontalLightBitmap();
        Bitmap verticalLightBitmap = BitmapCollections.getInstance().getVerticalLightBitmap();
        Bitmap spotLightBitmap = BitmapCollections.getInstance().getSpotLightBitmap();
        matrix.reset();
        paint.reset();
        paint.setAlpha((255 -  2 * (frame - STAR_FRAME + 5)));
        float scale = width / (float) horizontalLightBitmap.getWidth();
        matrix.postScale(scale, scale);
        matrix.postTranslate(0, (height  - scale * horizontalLightBitmap.getHeight()) / 2);
        canvas.drawBitmap(horizontalLightBitmap, matrix, paint);

        matrix.reset();
        float degrees = 90;
        matrix.postRotate(degrees);
        matrix.postTranslate((width - verticalLightBitmap.getHeight()) / 2 + DisplayUtils.dp2px(8), (height - verticalLightBitmap.getWidth()) / 2);
        canvas.drawBitmap(verticalLightBitmap, matrix, paint);

        canvas.drawBitmap(spotLightBitmap, (width - spotLightBitmap.getWidth()) / 2, (height - spotLightBitmap.getHeight()) / 2, paint);
    }
}
