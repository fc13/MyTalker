package com.example.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 范超 on 2017/6/21.
 */

public abstract class BaseFragment extends android.support.v4.app.Fragment {
    private View mRoot;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layoutId = getContentLayout();
            View view = inflater.inflate(layoutId, container, false);
            initWidget(view);
            mRoot = view;
        }else {
            if (mRoot.getParent()!=null){
                //把当前root从其父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    protected abstract int getContentLayout();

    /**
     * 初始化相关参数
     */
    protected void initArgs(Bundle bundle){

    }

    //初始化控件
    protected void initWidget(View root){
        unbinder = ButterKnife.bind(this,root);
    }

    //初始化数据
    protected void initData(){

    }

    /**
     * 返回按键触发时调用
     * 返回true代表fragment以处理返回逻辑，Activity不用finish()
     * 返回false代表fragment没有处理逻辑,Activity自己走自己的逻辑
     */
    public boolean onBackPressed(){
        return false;
    }
}
