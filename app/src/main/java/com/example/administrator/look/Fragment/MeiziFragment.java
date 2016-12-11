package com.example.administrator.look.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.look.Bean.Meizi.Gank;
import com.example.administrator.look.Bean.Meizi.Meizi;
import com.example.administrator.look.Presents.Presents.implPresents.MeiziPresenterImpl;
import com.example.administrator.look.Presents.Presents.implView.IMeiziFragment;
import com.example.administrator.look.R;
import com.example.administrator.look.Util.Once;
import com.example.administrator.look.adapter.MeiziAdapter;
import com.example.administrator.look.widget.WrapContentLinearLayoutManager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/11/26.
 */

public class MeiziFragment extends Fragment implements IMeiziFragment{

    @InjectView(R.id.recyle_meizi)
    RecyclerView mRecycleMeizi;
    @InjectView(R.id.prograss)
    ProgressBar mProgress;

    WrapContentLinearLayoutManager linearLayoutManager;
    MeiziAdapter meiziAdapter;
    RecyclerView.OnScrollListener loadmoreListener;

    MeiziPresenterImpl mMeiziPresenter;

    private boolean loading;

    private int index=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meizi_framlayout, container , false);
        ButterKnife.inject(this ,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        intialDate();

        initialView();
        super.onViewCreated(view, savedInstanceState);
    }
    private void intialDate() {
        mMeiziPresenter=new MeiziPresenterImpl(getContext(), this);

    }
    private void initialView(){
        meiziAdapter = new MeiziAdapter(getContext());
        linearLayoutManager = new WrapContentLinearLayoutManager(getContext());
        intialListener();
        mRecycleMeizi.setLayoutManager(linearLayoutManager);
        mRecycleMeizi.setAdapter(meiziAdapter);
        mRecycleMeizi.addOnScrollListener(loadmoreListener);
        new Once(getContext()).show("tip_guide_6", new Once.OnceCallback() {
            @Override
            public void onOnce() {
                Snackbar.make(mRecycleMeizi,"长按保存妹子" , Snackbar.LENGTH_SHORT).show();
            }
        });
        mRecycleMeizi.setItemAnimator(new DefaultItemAnimator());
        loadDate();
    }
    private void loadDate() {
        if (meiziAdapter.getItemCount() > 0) {
            meiziAdapter.clearData();
        }
        mMeiziPresenter.getMeiziData(index);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void intialListener(){
        loadmoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 ){
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        index += 1;
                        loadMoreDate();
                    }
                }
            }
        };
    }
    private void loadMoreDate() {
        meiziAdapter.loadingStart();
        mMeiziPresenter.getMeiziData(index);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMeiziPresenter.unsubcrible();
        ButterKnife.reset(this);
    }
    @Override
    public void updateMeiziData(ArrayList<Meizi> list) {
        meiziAdapter.loadingfinish();
        loading = false;
        meiziAdapter.addItems(list);
        mMeiziPresenter.getVedioData(index);
    }

    @Override
    public void updateVedioData(ArrayList<Gank> list) {
        meiziAdapter.addVedioDes(list);
    }

    @Override
    public void showProgressDialog() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidProgressDialog() {
        mProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String error) {
        mProgress.setVisibility(View.INVISIBLE);
        if(mRecycleMeizi != null){
            Snackbar.make(mRecycleMeizi , "请检查网络" ,Snackbar.LENGTH_SHORT).setAction("重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMeiziPresenter.getMeiziData(index);
                }
            }).show();
        }
    }
}
