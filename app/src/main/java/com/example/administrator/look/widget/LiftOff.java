package com.example.administrator.look.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.look.R;

/**
 * Created by Administrator on 2016/11/22.
 */

public class LiftOff extends Transition {
    private static final String PROPNAME_ELEVATION = "plaid:liftoff:elevation";

    private static final String[] transitionProperties = {
            PROPNAME_ELEVATION
    };

    private final float lift;

    public LiftOff(float lift) {
        this.lift = lift;
    }

    public LiftOff(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LiftOff);
        lift = ta.getDimensionPixelSize(R.styleable.LiftOff_android_elevation, 0);
        ta.recycle();
    }

    @Override
    public String[] getTransitionProperties() {
        return transitionProperties;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_ELEVATION, 0f);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_ELEVATION, lift);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        return ObjectAnimator.ofFloat(endValues.view, View.TRANSLATION_Z, lift, 0f);
    }

}
