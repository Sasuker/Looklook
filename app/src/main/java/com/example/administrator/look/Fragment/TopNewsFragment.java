package com.example.administrator.look.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.look.Bean.news.NewsList;
import com.example.administrator.look.Presents.Presents.implPresents.TopNewsPrensenterImpl;
import com.example.administrator.look.Presents.Presents.implView.ITopNewsFragment;
import com.example.administrator.look.R;
import com.example.administrator.look.View.GridItemDividerDecoration;
import com.example.administrator.look.adapter.TopNewsAdapter;
import com.example.administrator.look.widget.WrapContentLinearLayoutManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/11/26.
 */

public class TopNewsFragment extends Fragment implements ITopNewsFragment{
    boolean loading;
    boolean connected = true;
    TopNewsAdapter mTopNewsAdapter;

    LinearLayoutManager mLinearLayoutManager;
    RecyclerView.OnScrollListener loadingMoreListener;

    int currentIndex;

    TopNewsPrensenterImpl mTopNewsPrensenter;

    @InjectView(R.id.recycle_topnews)
    RecyclerView recycle;
    @InjectView(R.id.prograss)
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topnews_fragment_layout , container , false);
        ButterKnife.inject(this , view);
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialDate();
        initialView();

    }

    private void initialDate() {

        mTopNewsPrensenter=new TopNewsPrensenterImpl(this);
        mTopNewsAdapter =new TopNewsAdapter(getContext());
    }
    private void initialView() {

        initialListener();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mLinearLayoutManager = new WrapContentLinearLayoutManager(getContext());
        }else {
            mLinearLayoutManager = new LinearLayoutManager(getContext());
        }
        recycle.setLayoutManager(mLinearLayoutManager);
        recycle.setHasFixedSize(true);
        recycle.addItemDecoration(new GridItemDividerDecoration(getContext(), R.dimen.divider_height, R.color.divider));
        // TODO: 16/8/13 add  animation
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(mTopNewsAdapter);
        recycle.addOnScrollListener(loadingMoreListener);
        if (connected) {
            loadDate();
        }


    }

    private void loadDate() {
        if (mTopNewsAdapter.getItemCount() > 0) {
            mTopNewsAdapter.clearData();
        }
        currentIndex = 0;
        mTopNewsPrensenter.getNewsList(currentIndex);

    }
    private void loadMoreDate() {
        mTopNewsAdapter.loadingStart();
        currentIndex+=20;
        mTopNewsPrensenter.getNewsList(currentIndex);
    }
    private void initialListener(){
        loadingMoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    int visibleItemCount = mLinearLayoutManager.getChildCount();
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    int pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        loadMoreDate();
                    }
                }
            }
        };
    }
    @Override
    public void upListItem(NewsList newsList) {
        loading = false;
        progress.setVisibility(View.INVISIBLE);
        mTopNewsAdapter.addItems(newsList.getNewsList());
    }

    @Override
    public void showProgressDialog() {
        if (currentIndex ==0){
            progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hidProgressDialog() {
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String error) {
        if (recycle != null) {
            Snackbar.make(recycle, "请检查网络", Snackbar.LENGTH_SHORT).setAction("重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTopNewsPrensenter.getNewsList(currentIndex);
                }
            }).show();

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTopNewsPrensenter.unsubcrible();
        ButterKnife.reset(this);
    }
}
