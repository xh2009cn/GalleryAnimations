package org.howe.galleryanimations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import org.howe.galleryanimations.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class BitmapCollections {

    private volatile static BitmapCollections sInstance;
    private int mBufferSize;
    private int mIndex;
    private int mBufferFlyIndex;
    private List<Bitmap> mGalleryBitmapList = new ArrayList<Bitmap>(0);
    private List<Bitmap> mFrameBitmapList = new ArrayList<Bitmap>(0);
    private Bitmap mPhotoFrame;
    private Bitmap mPhotoFrameBg;
    private Bitmap mStarBitmap;
    private Bitmap mSplashStarBitmap;
    private Bitmap mLoveBitmap;
    private Bitmap mHeartBitmap;
    private Bitmap[] mBufferFlyBitmap;
    private Bitmap mRotateBitmap;
    private Bitmap mSpotLightBitmap;
    private Bitmap mVerticalLightBitmap;
    private Bitmap mHorizontalLightBitmap;
    private Bitmap mStarBackgroundBitmap;

    public static BitmapCollections getInstance() {
        if (sInstance == null) {
            synchronized(BitmapCollections.class) {
                if (sInstance == null) {
                    sInstance = new BitmapCollections();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        Bitmap bitmap01 = createScaledBitmap(context, R.drawable.animation_photo_01, width, height);
        mGalleryBitmapList.add(bitmap01);
        Bitmap bitmap02 = createScaledBitmap(context, R.drawable.animation_photo_02, width, height);
        mGalleryBitmapList.add(bitmap02);
        Bitmap bitmap03 = createScaledBitmap(context, R.drawable.animation_photo_03, width, height);
        mGalleryBitmapList.add(bitmap03);
        Bitmap bitmap04 = createScaledBitmap(context, R.drawable.animation_photo_04, width, height);
        mGalleryBitmapList.add(bitmap04);
        Bitmap bitmap05 = createScaledBitmap(context, R.drawable.animation_photo_05, width, height);
        mGalleryBitmapList.add(bitmap05);

        mPhotoFrame = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_photo_frame, options);
        mPhotoFrameBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_photo_bg, options);

        mFrameBitmapList.add(PhotoFrameUtil.addPhotoFrame(bitmap01));
        mFrameBitmapList.add(PhotoFrameUtil.addPhotoFrame(bitmap02));
        mFrameBitmapList.add(PhotoFrameUtil.addPhotoFrame(bitmap03));
        mFrameBitmapList.add(PhotoFrameUtil.addPhotoFrame(bitmap04));
        mFrameBitmapList.add(PhotoFrameUtil.addPhotoFrame(bitmap05));

        mStarBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_star, options);
        mLoveBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_love_heart, options);
        mHeartBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_heart_path_star, options);

        Bitmap[] bufferFly = new Bitmap[9];
        bufferFly[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_buffer_01, options);
        bufferFly[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_buffer_02, options);
        bufferFly[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_buffer_03, options);
        bufferFly[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_buffer_04, options);
        bufferFly[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_buffer_05, options);
        bufferFly[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_buffer_06, options);
        bufferFly[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_buffer_07, options);
        bufferFly[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_buffer_08, options);
        bufferFly[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_buffer_09, options);
        mBufferSize = bufferFly[0].getWidth();
        mBufferFlyBitmap = bufferFly;

        mRotateBitmap =
                BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_title_forebg, options);
        mSplashStarBitmap =
                BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_heart_path_star, options);
        mSpotLightBitmap = BitmapFactory
                .decodeResource(context.getResources(), R.drawable.animation_title_master_light_spot, options);
        mVerticalLightBitmap = BitmapFactory
                .decodeResource(context.getResources(), R.drawable.animation_title_small_light_line, options);
        mHorizontalLightBitmap = BitmapFactory
                .decodeResource(context.getResources(), R.drawable.animation_title_big_light_line, options);
        mStarBackgroundBitmap =
                BitmapFactory.decodeResource(context.getResources(), R.drawable.animation_title_bg, options);
    }

    private Bitmap createScaledBitmap(Context context, int resId, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        Matrix matrix = new Matrix();
        matrix.postScale(width / (float)bitmap.getWidth(), height / (float)bitmap.getHeight());
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap getBufferFly() {
        mBufferFlyIndex++;
        mBufferFlyIndex = mBufferFlyIndex % mBufferFlyBitmap.length;
        return mBufferFlyBitmap[mBufferFlyIndex];
    }

    public int getBufferSize() {
        return mBufferSize;
    }

    public Bitmap getHeartBitmap() {
        return mHeartBitmap;
    }

    public Bitmap getLoveBitmap() {
        return mLoveBitmap;
    }

    public Bitmap getStarBitmap() {
        return mStarBitmap;
    }

    public Bitmap getPhotoFrame() {
        return mPhotoFrame;
    }

    public Bitmap getPhotoFrameBg() {
        return mPhotoFrameBg;
    }

    public Bitmap getCurrentBitmap() {
        if (mGalleryBitmapList != null && mGalleryBitmapList.size() > 0) {
            mIndex = mIndex % mGalleryBitmapList.size();
            return mGalleryBitmapList.get(mIndex);
        }
        return null;
    }

    public Bitmap getCurrentFrameBitmap() {
        if (mFrameBitmapList != null && mFrameBitmapList.size() > 0) {
            mIndex = mIndex % mFrameBitmapList.size();
            return mFrameBitmapList.get(mIndex);
        }
        return null;
    }

    public List<Bitmap> getGalleryBitmapList() {
        return mGalleryBitmapList;
    }

    public List<Bitmap> getFrameBitmapList() {
        return mFrameBitmapList;
    }

    public Bitmap getRotateBitmap() {
        return mRotateBitmap;
    }

    public Bitmap getSpotLightBitmap() {
        return mSpotLightBitmap;
    }

    public Bitmap getSplashStarBitmap() {
        return mSplashStarBitmap;
    }

    public Bitmap getVerticalLightBitmap() {
        return mVerticalLightBitmap;
    }

    public Bitmap getHorizontalLightBitmap() {
        return mHorizontalLightBitmap;
    }

    public Bitmap getStarBackgroundBitmap() {
        return mStarBackgroundBitmap;
    }

    public Bitmap getNextBitmap() {
        if (mGalleryBitmapList != null && mGalleryBitmapList.size() > 0) {
            int index = mIndex + 1;
            index = index % mGalleryBitmapList.size();
            return mGalleryBitmapList.get(index);
        }
        return null;
    }

    public Bitmap getLastPhotoBitmap() {
        if (mGalleryBitmapList != null && mGalleryBitmapList.size() > 0) {
            int index = mIndex + mGalleryBitmapList.size() - 1;
            index = index % mGalleryBitmapList.size();
            return mGalleryBitmapList.get(index);
        }
        return null;
    }

    public void moveNextGallery() {
        mIndex++;
    }

    public void release() {
        if (mGalleryBitmapList != null) {
            mGalleryBitmapList.clear();
            mGalleryBitmapList = null;
        }
    }

    public int getCurrentIndex() {
        return mIndex;
    }
}
