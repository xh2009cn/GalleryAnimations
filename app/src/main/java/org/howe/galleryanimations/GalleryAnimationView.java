package org.howe.galleryanimations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.howe.galleryanimations.effects.EffectManager;
import org.howe.galleryanimations.effects.TaijiStarPath;
import org.howe.galleryanimations.effects.ZoomFadeIn;
import org.howe.galleryanimations.particle.BubbleEffect;
import org.howe.galleryanimations.particle.ParticleController;
import org.howe.galleryanimations.particle.SnowEffect;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class GalleryAnimationView extends SurfaceView implements ParticleController {

    private static final int STATE_STOPPED = 0x00;
    private static final int STATE_DRAWING = 0x01;
    private static final int STATE_PAUSE = 0x02;
    private static final int STATE_STOPPING = 0x03;
    private static final int STATE_BACKGROUND_WHEN_DRAWING = 0x04;
    private static final int STATE_BACKGROUND_WHEN_PAUSE = 0x05; //When app went background the canvas will clear, we need to redraw the last frame.

    private static final int PARTICLE_NONE = 0;
    private static final int PARTICLE_SNOW = 1;
    private static final int PARTICLE_BUBBLE = 2;
    private int mParticle = PARTICLE_NONE;

    private long mLastRefreshScoreTime;

    private int mState = STATE_STOPPED;

    private final LocalDrawThread mDrawThread = new LocalDrawThread();

    private int mFrame = 0;
    private int mBubbleFrame = 0;

    private EffectManager mEffectManager;
    private SnowEffect mSnowEffect;
    private BubbleEffect mBubbleEffect;

    private Matrix mMatrix;
    private Paint mPaint;

    private float mLastTouchX;
    private float mLastTouchY;
    private long mLastTouchTime;

    private boolean mInit;

    /**
     * 开始绘制
     */
    public void start() {
        if (mState == STATE_PAUSE || mState == STATE_STOPPING || mState == STATE_DRAWING) {
            return;
        }
        if (mDrawThread != null) {
            if (mState == STATE_STOPPED) {
                mEffectManager.addEffect(new TaijiStarPath());
                mEffectManager.addEffect(new ZoomFadeIn(this));
            }
            mState = STATE_DRAWING;
            if (!mDrawThread.isStart) {
                mDrawThread.start();
            } else {
                synchronized (mDrawThread) {
                    mDrawThread.notify();
                }
            }
        }
    }

    /**
     * 暂停
     */
    public boolean pause() {
        if (mState == STATE_DRAWING) {
            mState = STATE_PAUSE;
            return true;
        }
        return false;
    }

    /**
     * 恢复
     */
    public boolean resume() {
        if (mState == STATE_PAUSE) {
            mState = STATE_DRAWING;
            synchronized (mDrawThread) {
                mDrawThread.notify();
            }
            return true;
        }
        return false;
    }

    public boolean isPaused() {
        return mState == STATE_PAUSE;
    }

    public boolean isRunning() {
        return mState == STATE_DRAWING;
    }

    /**
     * 停止绘制
     */
    public void stop() {
        if (mState == STATE_STOPPED) {
            return;
        }
        int oldState = mState;
        mState = STATE_STOPPING;
        if (oldState == STATE_PAUSE) {
            synchronized (mDrawThread) {
                mDrawThread.notify();
            }
        }
        mFrame = 0;
        mEffectManager.clear();
        stopParticle();
    }

    public void onDestroy() {
        mDrawThread.interrupt();
        synchronized (mDrawThread) {
            mDrawThread.notify();
        }
    }

    /**
     * @param context mContext
     */
    public GalleryAnimationView(Context context) {
        super(context);
        init();
    }

    /**
     * @param context mContext
     * @param attrs   attrs
     */
    public GalleryAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * @param context  mContext
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public GalleryAnimationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Configs.setWidth(width);
        Configs.setHeight(height);
        if (!mInit) {
            mInit = true;
            BitmapCollections.getInstance().init(getContext(), width, height);
            mSnowEffect = new SnowEffect(getContext());
            mBubbleEffect = new BubbleEffect(getContext());
        }
    }

    private void init() {
        mMatrix = new Matrix();
        mPaint = new Paint();

        mEffectManager = new EffectManager(this);
        getHolder().addCallback(mCallback);
    }

    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mDrawThread.setSurfaceHolder(holder);
            if (mState == STATE_BACKGROUND_WHEN_DRAWING) {
                mState = STATE_DRAWING;
                synchronized (mDrawThread) {
                    mDrawThread.notify();
                }
            } else if (mState == STATE_PAUSE) {
                mState = STATE_BACKGROUND_WHEN_PAUSE;
                synchronized (mDrawThread) {
                    mDrawThread.notify();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mState == STATE_DRAWING) {
                mState = STATE_BACKGROUND_WHEN_DRAWING;
            }
        }
    };

    @Override
    public void startSnow() {
        mParticle = PARTICLE_SNOW;
    }

    @Override
    public void startBubble() {
        mParticle = PARTICLE_BUBBLE;
    }

    @Override
    public void stopParticle() {
        if (mParticle == PARTICLE_SNOW) {
            mSnowEffect.clear();
        } else if (mParticle == PARTICLE_BUBBLE) {
            mBubbleEffect.clear();
        }
        mParticle = PARTICLE_NONE;
    }

    private class LocalDrawThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean isStart;

        public void setSurfaceHolder(SurfaceHolder holder) {
            surfaceHolder = holder;
        }

        @Override
        public void run() {
            isStart = true;
            try {
                while (!isInterrupted()) {
                    if (mState == STATE_DRAWING || mState == STATE_STOPPING || mState == STATE_BACKGROUND_WHEN_PAUSE) {
                        draw();
                        Thread.sleep(Configs.FRAME_DURATION);
                    } else {
                        synchronized (this) {
                            mDrawThread.wait();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 绘制
         */
        public void draw() {
            Canvas canvas = surfaceHolder.lockCanvas(null);
            if (canvas != null) {
                clearCanvas(canvas);
                if (mState == STATE_STOPPING) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    mFrame = 0;
                    mState = STATE_STOPPED;
                    return;
                }
                synchronized (GalleryAnimationView.this) {
                    if ((System.currentTimeMillis() - mLastRefreshScoreTime) >= Configs.FRAME_DURATION) {
                        mLastRefreshScoreTime = System.currentTimeMillis();
                    } else {
                        try {
                            Thread.sleep(Configs.FRAME_DURATION - System.currentTimeMillis() + mLastRefreshScoreTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                mFrame = mEffectManager.onDraw(mFrame, canvas, mMatrix, mPaint);

                if (mParticle == PARTICLE_BUBBLE) {
                    mBubbleEffect.update(mBubbleFrame, canvas, mMatrix);
                    mBubbleFrame++;
                } else if (mParticle == PARTICLE_SNOW) {
                    mSnowEffect.update(canvas, mMatrix, mPaint);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
                if (mState == STATE_BACKGROUND_WHEN_PAUSE) { //Redraw the last frame when draw thread on pause state and app resume from background
                    mState = STATE_PAUSE;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastTouchX = x;
            mLastTouchY = y;
            mLastTouchTime = System.currentTimeMillis();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float speedX = (x - mLastTouchX) / (System.currentTimeMillis() - mLastTouchTime);
            float speedY = (y - mLastTouchY) / (System.currentTimeMillis() - mLastTouchTime);
            mSnowEffect.windBlow(5 * speedX, 5 * speedY);
        }
        return true;
    }

    private void clearCanvas(Canvas canvas) {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
    }
}

