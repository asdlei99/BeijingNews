package com.bobo.beijingnews.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 求知自学网 on 2019/7/7. Copyright © Leon. All rights reserved.
 * Functions: 基本的fragment，LeftmenuFragment 和 ContentFragment 将继承本类
 * 注意：一定要继承android.support.v4.app.Fragment;
 */
public abstract class BaseFragment extends Fragment {

    //MainActivity 当上下文用
    protected Activity context;

    /**
     * 当fragment被创建的时候调用这个方法  第1顺序被调用
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    /**
     * 当视图被创建的时候回调这个方法   第2顺序被调用
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //视图被创建的时候会调用这个方法
        return initView(inflater, container, savedInstanceState);
    }

    //让子类实现 视图被创建的时候会调用这个方法
    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 当（宿主）activity被创建好了回调这个方法  第3顺序被调用
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 1.如果子页面没有数据可以联网请求，并且绑定到由initView初始化的视图上
     * 2.如果有数据直接绑定到由initView初始化的视图上
     */
    public void initData(){

    }
}
