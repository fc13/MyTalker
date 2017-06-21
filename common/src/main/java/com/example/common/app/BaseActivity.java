package com.example.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 范超 on 2017/6/21.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (initArgs(getIntent().getExtras())) {
            int layout = getContentLayout();
            setContentView(layout);
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    //初始化窗口
    protected void initWindow() {

    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数的bundle
     * @return 如果参数正确返回true，否则返回false
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    //初始化控件
    protected void initWidget() {
        unbinder = ButterKnife.bind(this);
    }

    //初始化数据
    protected void initData() {

    }

    //得到当前界面的资源文件Id
    protected abstract int getContentLayout();

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面上的返回时，结束当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前Activity下的所有Fragment
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (android.support.v4.app.Fragment fragment : fragments) {
                //判断是否是自己写的Fragment
                if (fragment instanceof BaseFragment){
                    //判断是否拦截的返回按钮
                    if (((BaseFragment) fragment).onBackPressed()){
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}

