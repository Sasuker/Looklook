package com.example.administrator.look.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Property;

import com.example.administrator.look.R;
import com.example.administrator.look.Util.AnimUtils;
import com.example.administrator.look.Util.ColorUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ParallaxScrimageView extends FourTreeImageView {
    private static final int[] STATE_PINNED = {R.attr.state_pinned};
    private final Paint scrimPaint;
    private int imageOffset;
    private int minOffset;
    private float scrimAlpha = 0f;
    private float maxScrimAlpha = 1f;
    private int scrimColor = 0x00000000;
    private float parallaxFactor = -0.5f;
    private boolean isPinned = false;
    private boolean immediatePin = false;
    public static final Property<ParallaxScrimageView, Float> OFFSET = new AnimUtils
            .FloatProperty<ParallaxScrimageView>("offset") {

        @Override
        public void setValue(ParallaxScrimageView parallaxScrimageView, float value) {
            parallaxScrimageView.setOffset(value);
        }

        @Override
        public Float get(ParallaxScrimageView parallaxScrimageView) {
            return parallaxScrimageView.getOffset();
        }
    };

    public ParallaxScrimageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable
                .ParallaxScrimageView);
        if (a.hasValue(R.styleable.ParallaxScrimageView_scrimColor)) {
            scrimColor = a.getColor(R.styleable.ParallaxScrimageView_scrimColor, scrimColor);
        }
        if (a.hasValue(R.styleable.ParallaxScrimageView_scrimAlpha)) {
            scrimAlpha = a.getFloat(R.styleable.ParallaxScrimageView_scrimAlpha, scrimAlpha);
        }
        if (a.hasValue(R.styleable.ParallaxScrimageView_maxScrimAlpha)) {
            maxScrimAlpha = a.getFloat(R.styleable.ParallaxScrimageView_maxScrimAlpha,
                    maxScrimAlpha);
        }
        if (a.hasValue(R.styleable.ParallaxScrimageView_parallaxFactor)) {
            parallaxFactor = a.getFloat(R.styleable.ParallaxScrimageView_parallaxFactor,
                    parallaxFactor);
        }
        a.recycle();

        scrimPaint = new Paint();
        scrimPaint.setColor(ColorUtils.modifyAlpha(scrimColor, scrimAlpha));
    }

    public float getOffset() {
        return getTranslationY();
    }

    public void setOffset(float offset) {
        offset = Math.max(minOffset, offset);
        if (offset != getTranslationY()) {
            setTranslationY(offset);
            imageOffset = (int) (offset * parallaxFactor);
            setScrimAlpha(Math.min((-offset / getMinimumHeight()) * maxScrimAlpha, maxScrimAlpha));
            ViewCompat.postInvalidateOnAnimation(this);
        }
        setPinned(offset == minOffset);
    }

    public void setScrimColor(@ColorInt int scrimColor) {
        if (this.scrimColor != scrimColor) {
            this.scrimColor = scrimColor;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setScrimAlpha(@FloatRange(from = 0f, to = 1f) float alpha) {
        if (scrimAlpha != alpha) {
            scrimAlpha = alpha;
            scrimPaint.setColor(ColorUtils.modifyAlpha(scrimColor, scrimAlpha));
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h > getMinimumHeight()) {
            minOffset = getMinimumHeight() - h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (imageOffset != 0) {
            canvas.save();
            canvas.translate(0f, imageOffset);
            canvas.clipRect(0f, 0f, canvas.getWidth(), canvas.getHeight() + imageOffset);
            super.onDraw(canvas);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), scrimPaint);
            canvas.restore();
        } else {
            super.onDraw(canvas);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), scrimPaint);
        }
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isPinned) {
            mergeDrawableStates(drawableState, STATE_PINNED);
        }
        return drawableState;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean isPinned) {
        if (this.isPinned != isPinned) {
            this.isPinned = isPinned;
            refreshDrawableState();
            if (isPinned && immediatePin) {
                jumpDrawablesToCurrentState();
            }
        }
    }

    public boolean isImmediatePin() {
        return immediatePin;
    }

    /**
     * As the pinned state is designed to work with a {@see StateListAnimator}, we may want to short
     * circuit this animation in certain situations e.g. when flinging a list.
     */
    public void setImmediatePin(boolean immediatePin) {
        this.immediatePin = immediatePin;
    }
}
