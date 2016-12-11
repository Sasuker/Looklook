package com.example.administrator.look.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/11/26.
 */
public class GridItemDividerDecoration extends RecyclerView.ItemDecoration {
    private final float dividerSize;
    private final Paint paint;

    public GridItemDividerDecoration(float dividerSize,
                                     @ColorInt int dividerColor) {

        this.dividerSize = dividerSize;
        paint = new Paint();
        paint.setColor(dividerColor);
        paint.setStyle(Paint.Style.FILL);
    }

    public GridItemDividerDecoration(@NonNull Context context,
                                     @DimenRes int dividerSizeResId,
                                     @ColorRes int dividerColorResId) {
        this(
                context.getResources().getDimensionPixelSize(dividerSizeResId),
                ContextCompat.getColor(context, dividerColorResId));
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.isAnimating()) return;

        final int childCount = parent.getChildCount();
        final RecyclerView.LayoutManager lm = parent.getLayoutManager();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(child);

            if (requiresDivider(viewHolder)) {
                final int right = lm.getDecoratedRight(child);
                final int bottom = lm.getDecoratedBottom(child);
                // draw the bottom divider
                canvas.drawRect(lm.getDecoratedLeft(child),
                        bottom - dividerSize,
                        right,
                        bottom,
                        paint);
                // draw the right edge divider
                canvas.drawRect(right - dividerSize,
                        lm.getDecoratedTop(child),
                        right,
                        bottom - dividerSize,
                        paint);
            }

        }
    }

    private boolean requiresDivider(RecyclerView.ViewHolder viewHolder) {

        return true;
    }
}
