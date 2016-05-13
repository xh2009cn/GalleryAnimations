package org.howe.galleryanimations.effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.howe.galleryanimations.DisplayUtils;
import org.howe.galleryanimations.Configs;
import org.howe.galleryanimations.PhotoFrameUtil;
import org.howe.galleryanimations.BitmapCollections;
import org.howe.galleryanimations.particle.ParticleController;
import org.howe.galleryanimations.particle.model.Point;
import org.howe.galleryanimations.math.BezierCurLogic;
import org.howe.galleryanimations.math.HeartCurve;
import org.howe.galleryanimations.particle.model.Fall;
import org.howe.galleryanimations.particle.model.HeartPathStar;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class PhotoInFrame implements IEffect {

    private static final int NUM = 5; //照片数量
    private static final int PHOTO_FRAME = 30; //照片动画帧数
    private static final int FALL_FRAME = 30; //照片动画完成以后，五角星、心形坠落物下落帧数
    private static final int MOVE_FRAME = 15; //蝴蝶从屏幕外飞到圆形起始点的帧数
    private static final int HEART_FRAME = 60; //心形帧数
    private static final int ANGLE = (int) (360 / (float)HEART_FRAME); //心形曲线每帧累加的角度
    private static final int ZOOM_FRAME = 20; //图片放大到全屏帧数
    private static final int PAUSE_FRAME = 20; //图片全屏停留帧数
    private static final int FRAME_COUNT = PHOTO_FRAME * NUM + FALL_FRAME + 2 * MOVE_FRAME + HEART_FRAME + (int)(1 / HeartPathStar.FADE) + ZOOM_FRAME + PAUSE_FRAME; //总帧数
    private static final float HEART_RADIUS = Configs.getWidth() / 48; //心形半径大小
    private static final float HEART_CENTER_X = Configs.getWidth() / 2f; //心形中间点Y坐标
    private static final float HEART_CENTER_Y = 2 * Configs.getHeight() / 5f; //心形中间点Y坐标
    private static final float HEART_BEGIN_DEGREES = 180; //心形开始角度，从最下面开始
    private float mRatioX = 0.383f; //照片占屏幕宽度比
    private float mRatioY = 0.428f; //照片占屏幕高度比
    private float mScaleX; //照片缩放比例
    private float mScaleY;

    private float mScaleBgX; //背景照片缩放比例
    private float mScaleBgY;

    /** 第1张照片动画属性 **/
    private float mFirstRotate = -20;
    private float mFirstBeginScaleX;
    private float mFirstBeginScaleY;
    private float mFirstBeginTranslateX;
    private float mFirstBeginTranslateY;
    private float mFirstEndTranslateX;
    private float mFirstEndTranslateY;

    /** 第2张照片动画属性 **/
    private float mSecondRotate = 10;
    private float mSecondBeginTranslateX;
    private float mSecondBeginTranslateY;
    private float mSecondEndTranslateX;
    private float mSecondEndTranslateY;

    /** 第3张照片动画属性 **/
    private float mThirdBeginTranslateX;
    private float mThirdBeginTranslateY;
    private float mThirdEndTranslateX;

    /** 第4张照片动画属性 **/
    private float mFourthRotate = 5;
    private float mFourthBeginTranslateX;
    private float mFourthBeginTranslateY;
    private float mFourthEndTranslateX;
    private float mFourthEndTranslateY;

    /** 第5张照片动画属性 **/
    private float mFifthRotate = -10;
    private float mFifthBeginTranslateX;
    private float mFifthBeginTranslateY;
    private float mFifthEndTranslateX;
    private float mFifthEndTranslateY;

    /** 照片放大到全屏 **/
    private float mZoomBeginTranslateX;
    private float mZoomBeginTranslateY;
    private float mZoomEndTranslateX;
    private float mZoomEndTranslateY;
    private float mZoomEndScaleX;
    private float mZoomEndScaleY;

    private Bitmap mFrameBg;
    private Bitmap mFirstPhoto;
    private Bitmap mSecondPhoto;
    private Bitmap mThirdPhoto;
    private Bitmap mFourthPhoto;
    private Bitmap mFifthPhoto;

    private List<Fall> mFallList = new LinkedList<Fall>(); //落下的五角星、心形
    private List<HeartPathStar> mStarList = new LinkedList<HeartPathStar>(); //心形路径星星
    private Random mRandom = new Random();
    private int mMoveLineFrame = PHOTO_FRAME * NUM + FALL_FRAME;
    private int mHeartFrame = mMoveLineFrame + MOVE_FRAME;
    private int mPhotoZoomOutFrame = mHeartFrame + HEART_FRAME + MOVE_FRAME + (int)(1 / HeartPathStar.FADE);

    private float mLeftBufferFlyBeginX;
    private float mLeftBufferFlyBeginY;
    private float mRightBufferFlyBeginX;
    private float mRightBufferFlyBeginY;
    private float mLeftBufferFlyEndX;
    private float mLeftBufferFlyEndY;
    private float mRightBufferFlyEndX;
    private float mRightBufferFlyEndY;

    private List<Point> mLeftMoveLinePointList;
    private List<Point> mRightMoveLinePointList;

    private final ParticleController mParticleController;

    public PhotoInFrame(ParticleController controller) {
        mParticleController = controller;
    }

    @Override
    public void run(int frame, Canvas canvas, Matrix matrix, Paint paint) {
        if (frame < 0 || frame >= FRAME_COUNT) {
            return;
        }
        if (frame == 0) {
            /** 停止粒子效果--下雪，气泡 **/
            mParticleController.stopParticle();

            mFrameBg = BitmapCollections.getInstance().getPhotoFrameBg();

            /** 第1张照片属性初始化 **/
            mFirstPhoto = BitmapCollections.getInstance().getCurrentFrameBitmap();
            mFirstBeginScaleX = Configs.getWidth() / (mFirstPhoto.getWidth() * PhotoFrameUtil.SCALEX);
            mFirstBeginScaleY = Configs.getHeight() / (mFirstPhoto.getHeight() * PhotoFrameUtil.SCALEY);
            mScaleX = mRatioX * Configs.getWidth() / (float) mFirstPhoto.getWidth();
            mScaleY = mRatioY * Configs.getHeight() / (float) mFirstPhoto.getHeight();
            mScaleBgX =  Configs.getWidth() / (float) mFrameBg.getWidth();
            mScaleBgY =  Configs.getHeight() / (float) mFrameBg.getHeight();
            mFirstBeginTranslateX = -DisplayUtils.getDensity() * (PhotoFrameUtil.LEFT * Configs.getWidth() / mFirstBeginScaleX + 2.2f);
            mFirstBeginTranslateY = -DisplayUtils.getDensity() * (PhotoFrameUtil.TOP * Configs.getHeight() / mFirstBeginScaleY + 3.2f);
            mFirstEndTranslateX = 0;
            mFirstEndTranslateY = (float) (mRatioY * Configs.getHeight() * Math.sin(Math.abs(mFirstRotate * Math.PI / 180d)));

            /** 第2张照片属性初始化 **/
            BitmapCollections.getInstance().moveNextGallery();
            mSecondPhoto = BitmapCollections.getInstance().getCurrentFrameBitmap();
            mSecondBeginTranslateX = (1 + mRatioX) * Configs.getWidth();
            mSecondBeginTranslateY = -mRatioY * Configs.getHeight();
            mSecondEndTranslateX = (1 - mRatioX) * Configs.getWidth();
            mSecondEndTranslateY = 0.03f * Configs.getHeight();

            /** 第3张照片属性初始化 **/
            BitmapCollections.getInstance().moveNextGallery();
            mThirdPhoto = BitmapCollections.getInstance().getCurrentFrameBitmap();
            mThirdBeginTranslateX = -mRatioX * Configs.getWidth();
            mThirdBeginTranslateY = (1 - mRatioY) * Configs.getHeight() / 2f;
            mThirdEndTranslateX = (1 - mRatioX) * Configs.getWidth() / 2f;

            /** 第4张照片属性初始化 **/
            BitmapCollections.getInstance().moveNextGallery();
            mFourthPhoto = BitmapCollections.getInstance().getCurrentFrameBitmap();
            mFourthBeginTranslateX = (1 + mRatioX) * Configs.getWidth();
            mFourthBeginTranslateY = Configs.getHeight();
            mFourthEndTranslateX = (1 - mRatioX) * Configs.getWidth();
            mFourthEndTranslateY = (1 - mRatioY) * Configs.getHeight();

            /** 第5张照片属性初始化 **/
            BitmapCollections.getInstance().moveNextGallery();
            mFifthPhoto = BitmapCollections.getInstance().getCurrentFrameBitmap();
            mFifthBeginTranslateX = -mRatioX * Configs.getWidth();
            mFifthBeginTranslateY = Configs.getHeight();
            mFifthEndTranslateX = 0;
            mFifthEndTranslateY = (1 - mRatioY) * Configs.getHeight();

            /** 蝴蝶从左边飞出 **/
            mLeftBufferFlyBeginX = -BitmapCollections.getInstance().getBufferSize() / 2;
            mLeftBufferFlyBeginY = Configs.getHeight() - BitmapCollections.getInstance().getBufferSize() / 2;
            mLeftBufferFlyEndX = HeartCurve.counterClockwiseX(HEART_CENTER_X, HEART_RADIUS, HEART_BEGIN_DEGREES);
            mLeftBufferFlyEndY = HeartCurve.getY(HEART_CENTER_Y, HEART_RADIUS, HEART_BEGIN_DEGREES);

            Point[] leftCtrlPoints = new Point[1];
            leftCtrlPoints[0] = new Point(mLeftBufferFlyBeginX + 4 * (mLeftBufferFlyEndX - mLeftBufferFlyBeginX) / 5f
                    , mLeftBufferFlyBeginY + (mLeftBufferFlyEndY - mLeftBufferFlyBeginY) / 4f);

            mLeftMoveLinePointList = BezierCurLogic.createBezierPoints(new Point(mLeftBufferFlyBeginX, mLeftBufferFlyBeginY)
                    , new Point(mLeftBufferFlyEndX, mLeftBufferFlyEndY), 1 / (float) MOVE_FRAME, leftCtrlPoints);

            /** 蝴蝶从右边飞出 **/
            mRightBufferFlyBeginX = Configs.getWidth() - BitmapCollections.getInstance().getBufferSize() / 2;
            mRightBufferFlyBeginY = Configs.getHeight() - BitmapCollections.getInstance().getBufferSize() / 2;
            mRightBufferFlyEndX = mLeftBufferFlyEndX;
            mRightBufferFlyEndY = mLeftBufferFlyEndY;

            Point[] rightCtrlPoints = new Point[1];
            rightCtrlPoints[0] = new Point(mRightBufferFlyBeginX + 4 * (mRightBufferFlyEndX - mRightBufferFlyBeginX) / 5f
                    , mRightBufferFlyBeginY + (mRightBufferFlyEndY - mRightBufferFlyBeginY) / 4f);

            mRightMoveLinePointList = BezierCurLogic.createBezierPoints(new Point(mRightBufferFlyBeginX, mRightBufferFlyBeginY)
                    , new Point(mRightBufferFlyEndX, mRightBufferFlyEndY), 1 / (float) MOVE_FRAME, rightCtrlPoints);

            /** 照片放大到全屏 **/
            mZoomBeginTranslateX = mFifthEndTranslateX;
            mZoomBeginTranslateY = mFifthEndTranslateY;
            mZoomEndTranslateX = mFirstBeginTranslateX;
            mZoomEndTranslateY = mFirstBeginTranslateY;
            mZoomEndScaleX = mFirstBeginScaleX;
            mZoomEndScaleY = mFirstBeginScaleY;
        }
        matrix.reset();
        matrix.postScale(mScaleBgX, mScaleBgY);
        canvas.drawBitmap(mFrameBg, matrix, null);

        canvas.drawColor(Color.parseColor("#44000000"));

        if (frame <= PHOTO_FRAME) {
            drawRunningAnimation(0, frame, canvas, matrix, paint);
        } else if (frame <= 2 * PHOTO_FRAME) {
            drawCompletePhoto(0, frame, canvas, matrix, paint);
            drawRunningAnimation(1, frame, canvas, matrix, paint);
        } else if (frame <= 3 * PHOTO_FRAME) {
            drawCompletePhoto(0, frame, canvas, matrix, paint);
            drawCompletePhoto(1, frame, canvas, matrix, paint);
            drawRunningAnimation(2, frame, canvas, matrix, paint);
        } else if (frame <= 4 * PHOTO_FRAME) {
            drawCompletePhoto(0, frame, canvas, matrix, paint);
            drawCompletePhoto(1, frame, canvas, matrix, paint);
            drawCompletePhoto(2, frame, canvas, matrix, paint);
            drawRunningAnimation(3, frame, canvas, matrix, paint);
        } else if (frame <= 5 * PHOTO_FRAME) {
            drawCompletePhoto(0, frame, canvas, matrix, paint);
            drawCompletePhoto(1, frame, canvas, matrix, paint);
            drawCompletePhoto(2, frame, canvas, matrix, paint);
            drawCompletePhoto(3, frame, canvas, matrix, paint);
            drawRunningAnimation(4, frame, canvas, matrix, paint);
        } else {
            drawCompletePhoto(0, frame, canvas, matrix, paint);
            drawCompletePhoto(1, frame, canvas, matrix, paint);
            drawCompletePhoto(2, frame, canvas, matrix, paint);
            drawCompletePhoto(3, frame, canvas, matrix, paint);
            drawCompletePhoto(4, frame, canvas, matrix, paint);
        }

        if (frame < NUM * PHOTO_FRAME) {
            if (mFallList.size() <= 5) {
                addFallList(frame);
            }
        } else if (frame > NUM * PHOTO_FRAME + FALL_FRAME) {
            int moveFrame = frame - mMoveLineFrame;
            if (moveFrame < MOVE_FRAME) {
                drawLeftBufferFlyStartLine(frame, matrix, paint, canvas);
                drawRightBufferFlyStartLine(frame, matrix, paint, canvas);
                drawPathStar(frame, canvas, matrix, paint);
            } else {
                int heartFrame = frame - mHeartFrame;
                if (heartFrame < HEART_FRAME) {
                    drawLeftBufferFlyHeart(frame, canvas, matrix);
                    drawRightBufferFlyHeart(frame, canvas, matrix);
                } else if (heartFrame < HEART_FRAME + MOVE_FRAME) {
                    drawLeftBufferFlyBackLine(frame, matrix, paint, canvas);
                    drawRightBufferFlyBackLine(frame, matrix, paint, canvas);
                }

                if (mStarList.size() > 0) {
                    drawPathStar(frame, canvas, matrix, paint);
                } else {
                    if (frame - mPhotoZoomOutFrame <= ZOOM_FRAME) {
                        drawPhotoZoomOutAnimation(frame, canvas, matrix);
                    } else {
                        if (frame == mPhotoZoomOutFrame + ZOOM_FRAME) {
                            releaseBitmap();
                        }
                        drawEndPhoto(canvas, matrix);
                    }
                }
            }
        }

        if (frame == FRAME_COUNT - 1) {
            mParticleController.startSnow();
        }

        Iterator<Fall> iterator = mFallList.iterator();
        while (iterator.hasNext()) {
            Fall fall = iterator.next();
            if (frame - fall.mBeginFrame - fall.mMoveDelay <= 0) {
                continue;
            } else {
                float t = frame - fall.mBeginFrame - fall.mMoveDelay;
                float y = fall.mStarY + fall.mFirstSpeed * t + 0.098f * 3.8f * t * t;
                if (y > Configs.getWidth()) {
                    iterator.remove();
                } else {
                    matrix.reset();
                    matrix.postScale(fall.mScale, fall.mScale);
                    matrix.postTranslate(fall.mStarX, y);
                    canvas.drawBitmap(fall.mType == Fall.LOVE ? BitmapCollections.getInstance().getLoveBitmap() : BitmapCollections.getInstance().getStarBitmap(), matrix, null);
                }
            }
        }
    }

    private void drawEndPhoto(Canvas canvas, Matrix matrix) {
        matrix.reset();
        Bitmap bitmap = BitmapCollections.getInstance().getCurrentBitmap();
        matrix.postScale(Configs.getWidth() / (float) bitmap.getWidth(), Configs.getHeight() / (float) bitmap.getHeight());
        canvas.drawBitmap(bitmap, matrix, null);
    }

    private void releaseBitmap() {
        if (mFrameBg != null && !mFrameBg.isRecycled()) {
            mFrameBg.recycle();
            mFrameBg = null;
        }
        if (mFirstPhoto != null && !mFirstPhoto.isRecycled()) {
            mFirstPhoto.recycle();
            mFirstPhoto = null;
        }
        if (mSecondPhoto != null && !mSecondPhoto.isRecycled()) {
            mSecondPhoto.recycle();
            mSecondPhoto = null;
        }
        if (mThirdPhoto != null && !mThirdPhoto.isRecycled()) {
            mThirdPhoto.recycle();
            mThirdPhoto = null;
        }
        if (mFourthPhoto != null && !mFourthPhoto.isRecycled()) {
            mFourthPhoto.recycle();
            mFourthPhoto = null;
        }
        if (mFifthPhoto != null && !mFifthPhoto.isRecycled()) {
            mFifthPhoto.recycle();
            mFifthPhoto = null;
        }
    }

    private void drawPhotoZoomOutAnimation(int frame, Canvas canvas, Matrix matrix) {
        int deltaFrame = frame - mPhotoZoomOutFrame;
        matrix.reset();
        float scaleX = mScaleX + (mZoomEndScaleX - mScaleX) * deltaFrame / (float) ZOOM_FRAME;
        float scaleY = mScaleX + (mZoomEndScaleY - mScaleY) * deltaFrame / (float) ZOOM_FRAME;
        matrix.postScale(scaleX, scaleY);
        if (deltaFrame >= ZOOM_FRAME / 2) {
            int rotateFrame = deltaFrame - ZOOM_FRAME / 2;
            float degrees = mFifthRotate * 2 * (ZOOM_FRAME / 2 - rotateFrame) / (float) ZOOM_FRAME;
            matrix.postRotate(degrees);
        } else {
            matrix.postRotate(mFifthRotate);
        }
        matrix.postTranslate(mZoomBeginTranslateX + (mZoomEndTranslateX - mZoomBeginTranslateX) * deltaFrame / (float) ZOOM_FRAME
                , mZoomBeginTranslateY + (mZoomEndTranslateY - mZoomBeginTranslateY) * deltaFrame / (float) ZOOM_FRAME);
        canvas.drawBitmap(mFifthPhoto, matrix, null);
    }

    /** 蝴蝶从左侧飞到心形起点 **/
    private void drawLeftBufferFlyStartLine(int frame, Matrix matrix, Paint paint, Canvas canvas) {
        matrix.reset();
        paint.reset();
        canvas.drawColor(Color.parseColor("#66000000"));
        int deltaFrame = frame - mMoveLineFrame;
        if (deltaFrame < mLeftMoveLinePointList.size()) {
            float x = mLeftMoveLinePointList.get(deltaFrame).x;
            float y = mLeftMoveLinePointList.get(deltaFrame).y;
            if (deltaFrame < mLeftMoveLinePointList.size() - 1) {
                float dx = mLeftMoveLinePointList.get(deltaFrame + 1).x - x;
                float dy = mLeftMoveLinePointList.get(deltaFrame + 1).y - y;
                float degrees = 90 + (float) (Math.atan2(dy, dx) * 180f / Math.PI);
                matrix.postTranslate(x, y);
                int bufferSize = BitmapCollections.getInstance().getBufferSize();
                matrix.postRotate(degrees, x + bufferSize / 2, y + bufferSize / 2);
            } else {
                matrix.postTranslate(x, y);
            }
            canvas.drawBitmap(BitmapCollections.getInstance().getBufferFly(), matrix, null);
            HeartPathStar star = new HeartPathStar();
            star.mFrame = frame;
            star.x = x;
            star.y = y;
            mStarList.add(star);
        }
    }

    /** 蝴蝶从心形起点飞回到左侧 **/
    private void drawLeftBufferFlyBackLine(int frame, Matrix matrix, Paint paint, Canvas canvas) {
        matrix.reset();
        paint.reset();
        canvas.drawColor(Color.parseColor("#66000000"));
        int deltaFrame = MOVE_FRAME - (frame - mHeartFrame - HEART_FRAME);
        if (deltaFrame < mLeftMoveLinePointList.size()) {
            float x = mLeftMoveLinePointList.get(deltaFrame).x;
            float y = mLeftMoveLinePointList.get(deltaFrame).y;
            if (deltaFrame < mLeftMoveLinePointList.size() - 1) {
                float dx = mLeftMoveLinePointList.get(deltaFrame + 1).x - x;
                float dy = mLeftMoveLinePointList.get(deltaFrame + 1).y - y;
                float degrees = 270 + (float) (Math.atan2(dy, dx) * 180f / Math.PI);
                matrix.postTranslate(x, y);
                int bufferSize = BitmapCollections.getInstance().getBufferSize();
                matrix.postRotate(degrees, x + bufferSize / 2, y + bufferSize / 2);
            } else {
                matrix.postTranslate(x, y);
            }
            canvas.drawBitmap(BitmapCollections.getInstance().getBufferFly(), matrix, null);
            HeartPathStar star = new HeartPathStar();
            star.mFrame = frame;
            star.x = x;
            star.y = y;
            mStarList.add(star);
        }
    }

    /** 蝴蝶从右边飞到心形起点 **/
    private void drawRightBufferFlyStartLine(int frame, Matrix matrix, Paint paint, Canvas canvas) {
        matrix.reset();
        paint.reset();
        canvas.drawColor(Color.parseColor("#66000000"));
        int deltaFrame = frame - mMoveLineFrame;
        if (deltaFrame < mRightMoveLinePointList.size()) {
            float x = mRightMoveLinePointList.get(deltaFrame).x;
            float y = mRightMoveLinePointList.get(deltaFrame).y;
            if (deltaFrame < mRightMoveLinePointList.size() - 1) {
                float dx = mRightMoveLinePointList.get(deltaFrame + 1).x - x;
                float dy = mRightMoveLinePointList.get(deltaFrame + 1).y - y;
                float degrees = 90 + (float) (Math.atan2(dy, dx) * 180f / Math.PI);
                matrix.postTranslate(x, y);
                int bufferSize = BitmapCollections.getInstance().getBufferSize();
                matrix.postRotate(degrees, x + bufferSize / 2, y + bufferSize / 2);
            } else {
                matrix.postTranslate(x, y);
            }
            canvas.drawBitmap(BitmapCollections.getInstance().getBufferFly(), matrix, null);
            HeartPathStar star = new HeartPathStar();
            star.mFrame = frame;
            star.x = x;
            star.y = y;
            mStarList.add(star);
        }
    }

    /** 蝴蝶从心形起点飞回到右侧 **/
    private void drawRightBufferFlyBackLine(int frame, Matrix matrix, Paint paint, Canvas canvas) {
        matrix.reset();
        paint.reset();
        canvas.drawColor(Color.parseColor("#66000000"));
        int deltaFrame = MOVE_FRAME - (frame - mHeartFrame - HEART_FRAME);
        if (deltaFrame < mRightMoveLinePointList.size()) {
            float x = mRightMoveLinePointList.get(deltaFrame).x;
            float y = mRightMoveLinePointList.get(deltaFrame).y;
            if (deltaFrame < mRightMoveLinePointList.size() - 1) {
                float dx = mRightMoveLinePointList.get(deltaFrame + 1).x - x;
                float dy = mRightMoveLinePointList.get(deltaFrame + 1).y - y;
                float degrees = 270 + (float) (Math.atan2(dy, dx) * 180f / Math.PI);
                matrix.postTranslate(x, y);
                int bufferSize = BitmapCollections.getInstance().getBufferSize();
                matrix.postRotate(degrees, x + bufferSize / 2, y + bufferSize / 2);
            } else {
                matrix.postTranslate(x, y);
            }
            canvas.drawBitmap(BitmapCollections.getInstance().getBufferFly(), matrix, null);
            HeartPathStar star = new HeartPathStar();
            star.mFrame = frame;
            star.x = x;
            star.y = y;
            mStarList.add(star);
        }
    }

    private void drawLeftBufferFlyHeart(int frame, Canvas canvas, Matrix matrix) {
        canvas.drawColor(Color.parseColor("#66000000"));
        int bufferFrame = frame - mHeartFrame;
        matrix.reset();
        float x = HeartCurve.counterClockwiseX(HEART_CENTER_X, HEART_RADIUS, HEART_BEGIN_DEGREES + ANGLE * bufferFrame);
        float y = HeartCurve.getY(HEART_CENTER_Y, HEART_RADIUS, HEART_BEGIN_DEGREES + ANGLE * bufferFrame);
        float dx = HeartCurve.counterClockwiseX(HEART_CENTER_X, HEART_RADIUS, HEART_BEGIN_DEGREES + ANGLE * (bufferFrame + 1)) - x;
        float dy = HeartCurve.getY(HEART_CENTER_Y, HEART_RADIUS, HEART_BEGIN_DEGREES + ANGLE * (bufferFrame + 1)) - y;
        float degrees = 90 + (float) (Math.atan2(dy, dx) * 180f / Math.PI);
        matrix.postTranslate(x, y);
        int bufferSize = BitmapCollections.getInstance().getBufferSize();
        matrix.postRotate(degrees, x + bufferSize / 2, y + bufferSize / 2);
        canvas.drawBitmap(BitmapCollections.getInstance().getBufferFly(), matrix, null);
        HeartPathStar star = new HeartPathStar();
        star.mFrame = frame;
        star.x = x;
        star.y = y;
        mStarList.add(star);
    }

    private void drawRightBufferFlyHeart(int frame, Canvas canvas, Matrix matrix) {
        canvas.drawColor(Color.parseColor("#66000000"));
        int bufferFrame = frame - mHeartFrame;
        matrix.reset();
        float x = HeartCurve.clockwiseX(HEART_CENTER_X, HEART_RADIUS, HEART_BEGIN_DEGREES + ANGLE * bufferFrame);
        float y = HeartCurve.getY(HEART_CENTER_Y, HEART_RADIUS, HEART_BEGIN_DEGREES + ANGLE * bufferFrame);
        float dx = HeartCurve.clockwiseX(HEART_CENTER_X, HEART_RADIUS, HEART_BEGIN_DEGREES + ANGLE * (bufferFrame + 1)) - x;
        float dy = HeartCurve.getY(HEART_CENTER_Y, HEART_RADIUS, HEART_BEGIN_DEGREES + ANGLE * (bufferFrame + 1)) - y;
        float degrees = 90 + (float) (Math.atan2(dy, dx) * 180f / Math.PI);
        matrix.postTranslate(x, y);
        int bufferSize = BitmapCollections.getInstance().getBufferSize();
        matrix.postRotate(degrees, x + bufferSize / 2, y + bufferSize / 2);
        canvas.drawBitmap(BitmapCollections.getInstance().getBufferFly(), matrix, null);
        HeartPathStar star = new HeartPathStar();
        star.mFrame = frame;
        star.x = x;
        star.y = y;
        mStarList.add(star);
    }

    /** 画星星路径 **/
    private void drawPathStar(int frame, Canvas canvas, Matrix matrix, Paint paint) {
        Iterator<HeartPathStar> iterator = mStarList.iterator();
        while (iterator.hasNext()) {
            HeartPathStar heartStar = iterator.next();
            heartStar.mLive = 1 - (frame - heartStar.mFrame) * heartStar.FADE;
            if (heartStar.mLive <= 0) {
                iterator.remove();
            } else {
                paint.reset();
                matrix.reset();
                paint.setAlpha((int) (255f * heartStar.mLive));
                float starDegrees = 2 * (frame - heartStar.mFrame) % 360;
                matrix.postTranslate(heartStar.x, heartStar.y);
                matrix.postRotate(starDegrees, heartStar.x + BitmapCollections.getInstance().getHeartBitmap().getWidth() / 2, heartStar.y + BitmapCollections.getInstance().getHeartBitmap().getHeight() / 2);
                canvas.drawBitmap(BitmapCollections.getInstance().getHeartBitmap(), matrix, paint);
            }
        }
    }

    private void drawRunningAnimation(int index, int frame, Canvas canvas, Matrix matrix, Paint paint) {
        switch (index) {
            case 0:
                matrix.reset();
                float scaleX = mFirstBeginScaleX - (mFirstBeginScaleX - mScaleX) * frame / (float) PHOTO_FRAME;
                float scaleY = mFirstBeginScaleY - (mFirstBeginScaleY - mScaleY) * frame / (float) PHOTO_FRAME;
                matrix.postScale(scaleX, scaleY);
                matrix.postRotate(mFirstRotate * frame / (float) PHOTO_FRAME);
                matrix.postTranslate(mFirstBeginTranslateX + (mFirstEndTranslateX - mFirstBeginTranslateX)  * frame / (float) PHOTO_FRAME
                        , mFirstBeginTranslateY + (mFirstEndTranslateY - mFirstBeginTranslateY) * frame / (float) PHOTO_FRAME);
                canvas.drawBitmap(mFirstPhoto, matrix, null);
                break;
            case 1:
                matrix.reset();
                matrix.postScale(mScaleX, mScaleY);
                matrix.postRotate(mSecondRotate);
                matrix.postTranslate(mSecondBeginTranslateX + (mSecondEndTranslateX - mSecondBeginTranslateX) * (frame - PHOTO_FRAME) / (float) PHOTO_FRAME,
                        mSecondBeginTranslateY + (mSecondEndTranslateY - mSecondBeginTranslateY) * (frame - PHOTO_FRAME) / (float) PHOTO_FRAME);
                canvas.drawBitmap(mSecondPhoto, matrix, null);
                break;
            case 2:
                matrix.reset();
                matrix.postScale(mScaleX, mScaleY);
                matrix.postTranslate(mThirdBeginTranslateX + (mThirdEndTranslateX - mThirdBeginTranslateX) * (frame - 2 * PHOTO_FRAME) / (float) PHOTO_FRAME,
                        mThirdBeginTranslateY);
                canvas.drawBitmap(mThirdPhoto, matrix, null);
                break;
            case 3:
                matrix.reset();
                matrix.postScale(mScaleX, mScaleY);
                matrix.postRotate(mFourthRotate);
                matrix.postTranslate(mFourthBeginTranslateX + (mFourthEndTranslateX - mFourthBeginTranslateX) * (frame - 3 * PHOTO_FRAME) / (float) PHOTO_FRAME,
                        mFourthBeginTranslateY + (mFourthEndTranslateY - mFourthBeginTranslateY) * (frame - 3 * PHOTO_FRAME) / (float) PHOTO_FRAME);
                canvas.drawBitmap(mFourthPhoto, matrix, null);
                break;
            case 4:
                matrix.reset();
                matrix.postScale(mScaleX, mScaleY);
                matrix.postRotate(mFifthRotate);
                matrix.postTranslate(mFifthBeginTranslateX + (mFifthEndTranslateX - mFifthBeginTranslateX)  * (frame - 4 * PHOTO_FRAME) / (float) PHOTO_FRAME
                        , mFifthBeginTranslateY + (mFifthEndTranslateY - mFifthBeginTranslateY) * (frame - 4 * PHOTO_FRAME) / (float) PHOTO_FRAME);
                canvas.drawBitmap(mFifthPhoto, matrix, null);
                break;
            default:
                break;
        }
    }

    private void addFallList(int frame) {
        int count = (int) (8 + 10 * mRandom.nextFloat());
        for (int i = 0; i < count; i++) {
            Fall fall = new Fall();
            fall.mBeginFrame = frame;
            fall.mMoveDelay = (int) (50 * mRandom.nextFloat());
            fall.mType = mRandom.nextBoolean() ? Fall.LOVE : Fall.START;
            fall.mScale = 0.7f + 0.7f * mRandom.nextFloat();
            fall.mStarX = Configs.getWidth() * mRandom.nextFloat();
            fall.mStarY = mRandom.nextBoolean() ? 0 : -DisplayUtils.dp2px(40) * mRandom.nextFloat();
            fall.mFirstSpeed = 10 * mRandom.nextFloat();
            mFallList.add(fall);
        }
    }

    private void drawCompletePhoto(int index, int frame, Canvas canvas, Matrix matrix, Paint paint) {
        switch (index) {
            case 0:
                matrix.reset();
                matrix.postScale(mScaleX, mScaleY);
                matrix.postRotate(mFirstRotate);
                matrix.postTranslate(mFirstEndTranslateX, mFirstEndTranslateY);
                canvas.drawBitmap(mFirstPhoto, matrix, null);
                break;
            case 1:
                matrix.reset();
                matrix.postScale(mScaleX, mScaleY);
                matrix.postRotate(mSecondRotate);
                matrix.postTranslate(mSecondEndTranslateX, mSecondEndTranslateY);
                canvas.drawBitmap(mSecondPhoto, matrix, null);
                break;
            case 2:
                matrix.reset();
                matrix.postScale(mScaleX, mScaleY);
                matrix.postTranslate(mThirdEndTranslateX, mThirdBeginTranslateY);
                canvas.drawBitmap(mThirdPhoto, matrix, null);
                break;
            case 3:
                matrix.reset();
                matrix.postScale(mScaleX, mScaleY);
                matrix.postRotate(mFourthRotate);
                matrix.postTranslate(mFourthEndTranslateX, mFourthEndTranslateY);
                canvas.drawBitmap(mFourthPhoto, matrix, null);
                break;
            case 4:
                matrix.reset();
                matrix.postScale(mScaleX, mScaleY);
                matrix.postRotate(mFifthRotate);
                matrix.postTranslate(mFifthEndTranslateX, mFifthEndTranslateY);
                canvas.drawBitmap(mFifthPhoto, matrix, null);
                break;
            default:
                break;
        }
    }

    @Override
    public int getFrameCount() {
        return FRAME_COUNT;
    }
}
