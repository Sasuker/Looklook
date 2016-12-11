package com.example.administrator.look.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/11/22.
 */

public class FourTreeImageView extends ForegroundImageView {
    public FourTreeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FourTreeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    protected void onMeasure(int widthSpec, int heightSpec) {
        int fourThreeHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthSpec) * 3 / 4,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, fourThreeHeight);
    }
}
