package com.example.administrator.look.Util;

import android.util.Property;
import android.graphics.ColorMatrix;

/**
 * Created by Administrator on 2016/11/25.
 */
public class ObservableColorMatrix extends ColorMatrix{

    private float saturation = 1f;

    public ObservableColorMatrix() {
        super();
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
        super.setSaturation(saturation);
    }

    public static final Property<ObservableColorMatrix, Float> SATURATION
            = new AnimUtils.FloatProperty<ObservableColorMatrix>("saturation") {

        @Override
        public void setValue(ObservableColorMatrix cm, float value) {
            cm.setSaturation(value);
        }

        @Override
        public Float get(ObservableColorMatrix cm) {
            return cm.getSaturation();
        }
    };
}
