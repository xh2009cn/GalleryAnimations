package org.howe.galleryanimations.effects;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.howe.galleryanimations.particle.ParticleController;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class EffectManager {

    private static final int PHOTO_ANIMATION_COUNT = 7;
    private List<IEffect> mEffectList = new LinkedList<IEffect>();
    private int mAnimationIndex;
    private ParticleController mParticleController;

    public EffectManager(ParticleController controller) {
        mParticleController = controller;
    }

    public IEffect getCurrentEffect() {
        return mEffectList.size() > 0 ? mEffectList.get(0) : null;
    }

    public IEffect addEffect(IEffect effect) {
        mEffectList.add(effect);
        return effect;
    }

    public boolean removeEffect(IEffect effect) {
        return mEffectList.remove(effect);
    }

    public IEffect generateEffect() {
        IEffect effect = null;
        switch (mAnimationIndex) {
            case 0:
                effect = addEffect(new TranslateToBottom());
                break;
            case 1:
                effect = addEffect(new TranslateToLeft());
                break;
            case 2:
                effect = addEffect(new TranslateToTop());
                break;
            case 3:
                effect = addEffect(new TranslateToRight());
                break;
            case 4:
                effect = addEffect(new TransFromLT2RB());
                break;
            case 5:
                effect = addEffect(new TransToRightFadeOut());
                break;
            case 6:
                effect = addEffect(new TurnPage());
                addEffect(new PhotoInFrame(mParticleController));
                break;
            default:
                break;
        }
        mAnimationIndex = ++mAnimationIndex % PHOTO_ANIMATION_COUNT;
        return effect;
    }

    public int onDraw(int frame, Canvas canvas, Matrix matrix, Paint paint) {
        IEffect effect = getCurrentEffect();
        if (effect != null) {
            if (tryRemoveEndEffect(frame, effect)) {
                effect = getCurrentEffect();
                frame = 0;
            }
        }
        if (effect == null) {
            effect = generateEffect();
            frame = 0;
        }
        effect.run(frame, canvas, matrix, paint);
        return ++frame;
    }

    private boolean tryRemoveEndEffect(int frame, IEffect effect) {
        boolean isEnd = frame >= effect.getFrameCount();
        if (isEnd) {
            removeEffect(effect);
        }
        return isEnd;
    }

    public void clear() {
        mEffectList.clear();
    }
}
