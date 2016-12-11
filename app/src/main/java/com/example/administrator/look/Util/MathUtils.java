package com.example.administrator.look.Util;

/**
 * Created by Administrator on 2016/11/22.
 */
public class MathUtils {
    private MathUtils() { }

    public static float constrain(float min, float max, float v) {
        return Math.max(min, Math.min(max, v));
    }
}
