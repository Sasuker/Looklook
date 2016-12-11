package com.example.administrator.look.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.look.Bean.Meizi.Gank;
import com.example.administrator.look.Bean.Meizi.Meizi;
import com.example.administrator.look.MainActivity;
import com.example.administrator.look.R;
import com.example.administrator.look.Util.DensityUtil;
import com.example.administrator.look.Util.DribbbleTarget;
import com.example.administrator.look.Util.Help;
import com.example.administrator.look.Util.ObservableColorMatrix;
import com.example.administrator.look.activity.MeiziPhotoDescribeActivity;
import com.example.administrator.look.widget.BadgedFourThreeImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/25.
 */

public class MeiziAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MainActivity.LoadingMore{
    private static final int TYPE_LOADING_MORE = -1;
    private static final int NOMAL_ITEM = 1;
    boolean showLoadingMore;

    int[] decsi;
    private ArrayList<Meizi> meiziItemes = new ArrayList<>();
    private Context mContext;

    public MeiziAdapter(Context context){
        this.mContext = context;
        decsi = DensityUtil.getDeviceInfo(mContext);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case NOMAL_ITEM:
                return new MeiziViewHolder(LayoutInflater.from(mContext).inflate(R.layout.meizi_layout_itrm , parent , false));
            case TYPE_LOADING_MORE:
                return new LoadingMoreHolder(LayoutInflater.from(mContext).inflate(R.layout.loading , parent , false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type){
            case NOMAL_ITEM:
                bindViewHolderNormal((MeiziViewHolder) holder, position);
                break;
            case TYPE_LOADING_MORE:
                bindLoadingViewHold((LoadingMoreHolder) holder, position);
                break;
        }
    }
    private void bindLoadingViewHold(LoadingMoreHolder holder, int position) {
        holder.progressBar.setVisibility(showLoadingMore? View.VISIBLE : View.INVISIBLE);
    }
    private void bindViewHolderNormal(final MeiziViewHolder holder , final int position){
        final Meizi meizi = meiziItemes.get(holder.getAdapterPosition());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDescribeActivity(meizi,holder);
            }
        });
        Glide.with(mContext)
                .load(meizi.getUrl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if(!meizi.hasFadedIn){
                            holder.imageView.setHasTransientState(true);
                            final ObservableColorMatrix matrix = new ObservableColorMatrix();
                            final ObjectAnimator animator = ObjectAnimator.ofFloat(matrix , ObservableColorMatrix.SATURATION , 0f,1f);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    holder.imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
                                }
                            });
                            animator.setDuration(2000L);
                            animator.setInterpolator(new AccelerateInterpolator());
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    holder.imageView.clearColorFilter();
                                    holder.imageView.setHasTransientState(false);
                                    animator.start();
                                    meizi.hasFadedIn = true;
                                }
                            });
                        }

                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(new DribbbleTarget(holder.imageView , false));
    }

    private void startDescribeActivity(Meizi meizi,RecyclerView.ViewHolder holder){

        Intent intent = new Intent(mContext, MeiziPhotoDescribeActivity.class);
        intent.putExtra("image",meizi.getUrl());
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            final android.support.v4.util.Pair<View, String>[] pairs = Help.createSafeTransitionParticipants
                    ((Activity) mContext, false,new android.support.v4.util.Pair<>(((MeiziViewHolder)holder).imageView, mContext.getString(R.string.meizi)));
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pairs);
            mContext.startActivity(intent, options.toBundle());
        }else {
            mContext.startActivity(intent);
        }

    }

    @Override
    public int getItemCount() {
        return meiziItemes.size();
    }
    public void addItems(ArrayList<Meizi> list) {
        meiziItemes.addAll(list);
        notifyDataSetChanged();
    }
    public int getItemViewType(int position){
        if(position < getDataItemCount()
                && getDataItemCount() > 0){
            return NOMAL_ITEM;
        }else {
            return TYPE_LOADING_MORE;
        }
    }
    private int getDataItemCount() {

        return meiziItemes.size();
    }
    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    @Override
    public void loadingStart() {
        if (showLoadingMore) return;
        showLoadingMore = true;
        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void loadingfinish() {
        if(!showLoadingMore) return;
        final int loadingPos = getLoadingMoreItemPosition();
        showLoadingMore = false;
        notifyItemRemoved(loadingPos);
    }
    public void addVedioDes(ArrayList<Gank> list){

    }

    public void clearData() {
        meiziItemes.clear();
        notifyDataSetChanged();
    }
    public static class LoadingMoreHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView;
        }
    }
    class MeiziViewHolder extends RecyclerView.ViewHolder{
        BadgedFourThreeImageView imageView;

        public MeiziViewHolder(View itemView) {
            super(itemView);
            imageView = (BadgedFourThreeImageView) itemView.findViewById(R.id.item_image_id);
        }
    }
}
