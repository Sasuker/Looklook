package com.example.administrator.look.Presents.Presents.implView;

import com.example.administrator.look.Bean.Meizi.Gank;
import com.example.administrator.look.Bean.Meizi.Meizi;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/24.
 */

public interface IMeiziFragment extends IBaseFragment {
    void updateMeiziData(ArrayList<Meizi> list);
    void updateVedioData(ArrayList<Gank> list);
}
