package org.howe.galleryanimations;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.howe.galleryanimations.R;

/**
 * 固定宽高比的线性布局
 * @author xh2009cn
 * @version 1.0.0
 */
public class FixedAspectRatioFrameLayout extends FrameLayout {

    private static final float DEFAULT_ASPECT_RATIO = 1;
    private static final int FIX_WIDTH = 0;
    private static final int FIX_HEIGHT = 1;

    private float mAspectRatio = DEFAULT_ASPECT_RATIO;
    private int mOrientation = FIX_WIDTH;

    /**
     * 构造函数
     * @param context 上下文对象
     */
    public FixedAspectRatioFrameLayout(Context context) {
        this(context, null);
    }

    /**
     * 构造函数
     * @param context 上下文对象
     * @param attrs 属性
     */
    public FixedAspectRatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioFrameLayout);
        mOrientation = a.getInt(R.styleable.FixedAspectRatioFrameLayout_orientation, FIX_WIDTH);
        mAspectRatio = a.getFloat(R.styleable.FixedAspectRatioFrameLayout_aspectRatio, DEFAULT_ASPECT_RATIO);
    }

    /**
     * 设置宽度和高度的比例
     * @param ratio 宽度和高度的比例
     */
    public void setAspectRatio(float ratio) {
        mAspectRatio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAspectRatio > 0) {
            float width = 0;
            float height = 0;
            switch (mOrientation) {
                case FIX_WIDTH:
                    width = MeasureSpec.getSize(widthMeasureSpec);
                    height = width / mAspectRatio;
                    break;
                case FIX_HEIGHT:
                    height = MeasureSpec.getSize(heightMeasureSpec);
                    width = height * mAspectRatio;
                    break;
                default:
                    break;
            }
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
