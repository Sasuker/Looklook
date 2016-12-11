package com.example.administrator.look.Util;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.administrator.look.R;
import com.example.administrator.look.widget.BadgedFourThreeImageView;

/**
 * Created by Administrator on 2016/11/25.
 */
public class DribbbleTarget extends GlideDrawableImageViewTarget implements Palette.PaletteAsyncListener{
    private final boolean autoplayGifs;
    public DribbbleTarget(ImageView view, boolean autoplayGifs) {
        super(view);
        this.autoplayGifs = autoplayGifs;
    }

    @Override
    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable>
            animation) {
        super.onResourceReady(resource, animation);
        if (!autoplayGifs) {
            resource.stop();
        }

        BadgedFourThreeImageView badgedImageView = (BadgedFourThreeImageView) getView();
        if (resource instanceof GlideBitmapDrawable) {
            Palette.from(((GlideBitmapDrawable) resource).getBitmap())
                    .clearFilters()
                    .generate(this);
        } else if (resource instanceof GifDrawable) {
            Bitmap image = ((GifDrawable) resource).getFirstFrame();
            Palette.from(image).clearFilters().generate(this);

            // look at the corner to determine the gif badge color
            int cornerSize = (int) (56 * getView().getContext().getResources().getDisplayMetrics
                    ().scaledDensity);
            Bitmap corner = Bitmap.createBitmap(image,
                    image.getWidth() - cornerSize,
                    image.getHeight() - cornerSize,
                    cornerSize, cornerSize);
            boolean isDark = ColorUtils.isDark(corner);
            corner.recycle();
            badgedImageView.setBadgeColor(ContextCompat.getColor(getView().getContext(),
                    isDark ? R.color.gif_badge_dark_image : R.color.gif_badge_light_image));
        }
    }

    @Override
    public void onStart() {
        if (autoplayGifs) {
            super.onStart();
        }
    }

    @Override
    public void onStop() {
        if (autoplayGifs) {
            super.onStop();
        }
    }

    @Override
    public void onGenerated(Palette palette) {
        ((BadgedFourThreeImageView) getView()).setForeground(
                ViewUtils.createRipple(palette, 0.25f, 0.5f,
                        ContextCompat.getColor(getView().getContext(), R.color.mid_grey), true));
    }
}
