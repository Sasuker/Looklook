package com.example.administrator.look.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.look.Bean.zhihu.ZhihuStory;
import com.example.administrator.look.Presents.Presents.IZhihuStoryPresent;
import com.example.administrator.look.Presents.Presents.implPresents.ZhihuStoryPresenterImpl;
import com.example.administrator.look.Presents.Presents.implView.IZhihuStory;
import com.example.administrator.look.R;
import com.example.administrator.look.Util.AnimUtils;
import com.example.administrator.look.Util.ColorUtils;
import com.example.administrator.look.Util.DensityUtil;
import com.example.administrator.look.Util.GlideUtils;
import com.example.administrator.look.Util.ViewUtils;
import com.example.administrator.look.Util.WebUtil;
import com.example.administrator.look.config.Config;
import com.example.administrator.look.widget.ElasticDragDismissFrameLayout;
import com.example.administrator.look.widget.ParallaxScrimageView;
import com.example.administrator.look.widget.TranslateYTextView;

import java.lang.reflect.InvocationTargetException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/16.
 */

public class ZhihuDescribeActivity extends BaseActivity implements IZhihuStory{
    private static final float SCRIM_ADJUSTMENT = 0.075f;

    @InjectView(R.id.shot)
    ParallaxScrimageView mShot;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.wv_zhihu)
    WebView wvZhihu;
    @InjectView(R.id.nest)
    NestedScrollView mNest;
    @InjectView(R.id.title)
    TranslateYTextView mTranslateYTextView;

    boolean isEmpty;
    String mBody;
    String[] scc;
    String mImageUrl;

    @InjectView(R.id.draggable_frame)
    ElasticDragDismissFrameLayout mDraggableFrame;

    int[] mDeviceInfo;
    int width;
    int heigh;
    private Transition.TransitionListener zhihuReturnHomeListener;
    private NestedScrollView.OnScrollChangeListener scrollListener;

    private String id;
    private String title;
    private String url;
    private IZhihuStoryPresent mIZhihuStoryPresenter;
    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;
    private Handler mHandler=new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhihu_describle);
        ButterKnife.inject(this);
        mDeviceInfo = DensityUtil.getDeviceInfo(this);
        width = mDeviceInfo[0];
        heigh = width * 3 / 4;
        setSupportActionBar(mToolbar);
        initlistenr();
        initData();
        initView();
        getData();

        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            getWindow().getSharedElementReturnTransition().addListener(zhihuReturnHomeListener);
            getWindow().setSharedElementEnterTransition(new ChangeBounds());
        }

        enterAnimation();

    }

    private void initlistenr() {
        zhihuReturnHomeListener =
                new AnimUtils.TransitionListenerAdapter() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                        super.onTransitionStart(transition);
                        mToolbar.animate()
                                .alpha(0f)
                                .setDuration(100)
                                .setInterpolator(new AccelerateInterpolator());
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                            mShot.setElevation(1f);
                            mToolbar.setElevation(0f);
                        }
                        mNest.animate()
                                .alpha(0f)
                                .setDuration(50)
                                .setInterpolator(new AccelerateInterpolator());
                    }
                };
        scrollListener = new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (oldScrollY<168){
                    mShot.setOffset(-oldScrollY);
                    mTranslateYTextView.setOffset(-oldScrollY);
                }

            }
        };
    }

    private void initData() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        mImageUrl = getIntent().getStringExtra("image");
        mIZhihuStoryPresenter = new ZhihuStoryPresenterImpl(this);
        mNest.setOnScrollChangeListener(scrollListener);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            postponeEnterTransition();
            mShot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mShot.getViewTreeObserver().removeOnPreDrawListener(this);
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                        startPostponedEnterTransition();
                    }
                    return true;
                }
            });
        }


    }

    private void initView() {
        mToolbar.setTitleMargin(20,20,0,10);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNest.smoothScrollTo(0,0);
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandImageAndFinish();
            }
        });
        mTranslateYTextView.setText(title);

        WebSettings settings = wvZhihu.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        //settings.setUseWideViewPort(true);造成文字太小
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvZhihu.setWebChromeClient(new WebChromeClient());

    }


    @Override
    protected void onResume() {
        super.onResume();

        mDraggableFrame.addListener(chromeFader);
        try {
            wvZhihu.getClass().getMethod("onResume").invoke(wvZhihu, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDraggableFrame.removeListener(chromeFader);
        try {
            wvZhihu.getClass().getMethod("onPause").invoke(wvZhihu, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {

            getWindow().getSharedElementReturnTransition().removeListener(zhihuReturnHomeListener);
        }
        //webview内存泄露
        if (wvZhihu != null) {
            ((ViewGroup) wvZhihu.getParent()).removeView(wvZhihu);
            wvZhihu.destroy();
            wvZhihu = null;
        }
        mIZhihuStoryPresenter.unsubcrible();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        expandImageAndFinish();

    }

    private void getData() {
        mIZhihuStoryPresenter.getZhihuStory(id);

    }

    @OnClick(R.id.shot)
    public void onClick() {
        mNest.smoothScrollTo(0, 0);

    }

    @Override
    public void showError(String error) {
        Snackbar.make(wvZhihu, "请检查网络", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        }).show();
    }

    @Override
    public void showZhihuStory(ZhihuStory zhihuStory) {

        Glide.with(this)
                .load(zhihuStory.getImage()).centerCrop()
                .listener(loadListener).override(width,heigh)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mShot);
        url = zhihuStory.getShareUrl();
        isEmpty=TextUtils.isEmpty(zhihuStory.getBody());
        mBody=zhihuStory.getBody();
        scc=zhihuStory.getCss();
        if (isEmpty) {
            wvZhihu.loadUrl(url);
        } else {
            String data = WebUtil.buildHtmlWithCss(mBody, scc, Config.isNight);
            wvZhihu.loadDataWithBaseURL(WebUtil.BASE_URL, data, WebUtil.MIME_TYPE, WebUtil.ENCODING, WebUtil.FAIL_URL);
        }


    }



    private void expandImageAndFinish() {
        if (mShot.getOffset() != 0f) {
            Animator expandImage = ObjectAnimator.ofFloat(mShot, ParallaxScrimageView.OFFSET,
                    0f);
            expandImage.setDuration(80);
            expandImage.setInterpolator(new AccelerateInterpolator());
            expandImage.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    }else {
                        finish();
                    }
                }
            });
            expandImage.start();
        } else {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }else {
                finish();
            }
        }
    }

    private RequestListener loadListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onResourceReady(GlideDrawable resource, String model,
                                       Target<GlideDrawable> target, boolean isFromMemoryCache,
                                       boolean isFirstResource) {
            final Bitmap bitmap = GlideUtils.getBitmap(resource);
            final int twentyFourDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24, ZhihuDescribeActivity.this.getResources().getDisplayMetrics());
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters() /* by default palette ignore certain hues
                        (e.g. pure black/white) but we don't want this. */
                    .setRegion(0, 0, bitmap.getWidth() - 1, twentyFourDip) /* - 1 to work around
                        https://code.google.com/p/android/issues/detail?id=191013 */
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            boolean isDark;
                            @ColorUtils.Lightness int lightness = ColorUtils.isDark(palette);
                            if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                                isDark = ColorUtils.isDark(bitmap, bitmap.getWidth() / 2, 0);
                            } else {
                                isDark = lightness == ColorUtils.IS_DARK;
                            }

                            // color the status bar. Set a complementary dark color on L,
                            // light or dark color on M (with matching status bar icons)
                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){


                                int statusBarColor = getWindow().getStatusBarColor();
                                final Palette.Swatch topColor =
                                        ColorUtils.getMostPopulousSwatch(palette);
                                if (topColor != null &&
                                        (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                                    statusBarColor = ColorUtils.scrimify(topColor.getRgb(),
                                            isDark, SCRIM_ADJUSTMENT);
                                    // set a light status bar on M+
                                    if (!isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ViewUtils.setLightStatusBar(mShot);
                                    }
                                }

                                if (statusBarColor != getWindow().getStatusBarColor()) {
                                    mShot.setScrimColor(statusBarColor);
                                    ValueAnimator statusBarColorAnim = ValueAnimator.ofArgb(
                                            getWindow().getStatusBarColor(), statusBarColor);
                                    statusBarColorAnim.addUpdateListener(new ValueAnimator
                                            .AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            getWindow().setStatusBarColor(
                                                    (int) animation.getAnimatedValue());
                                        }
                                    });
                                    statusBarColorAnim.setDuration(1000L);
                                    statusBarColorAnim.setInterpolator(
                                            new AccelerateInterpolator());
                                    statusBarColorAnim.start();
                                }
                            }

                        }
                    });


            Palette.from(bitmap)
                    .clearFilters()
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {

                            // slightly more opaque ripple on the pinned image to compensate
                            // for the scrim
                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

                                mShot.setForeground(ViewUtils.createRipple(palette, 0.3f, 0.6f,
                                        ContextCompat.getColor(ZhihuDescribeActivity.this, R.color.mid_grey),
                                        true));
                            }
                        }
                    });

            // TODO should keep the background if the image contains transparency?!
            mShot.setBackground(null);
            return false;
        }

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                   boolean isFirstResource) {
            return false;
        }
    };

    private void enterAnimation() {
        float offSet = mToolbar.getHeight();
        LinearInterpolator interpolator=new LinearInterpolator();
        viewEnterAnimation(mShot, offSet, interpolator);
        viewEnterAnimationNest(mNest,0f,interpolator);

    }

    private void viewEnterAnimation(View view, float offset, Interpolator interp) {
        view.setTranslationY(-offset);
        view.setAlpha(0f);
        view.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500L)
                .setInterpolator(interp)
                .setListener(null)
                .start();
    }
    private void viewEnterAnimationNest(View view, float offset, Interpolator interp) {
        view.setTranslationY(-offset);
        view.setAlpha(0.3f);
        view.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500L)
                .setInterpolator(interp)
                .setListener(null)
                .start();
    }
}
