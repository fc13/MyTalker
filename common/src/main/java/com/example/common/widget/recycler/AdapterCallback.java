package com.example.common.widget.recycler;

/**
 * Created by 范超 on 2017/6/21.
 */

public interface AdapterCallback<T> {
    void update(T t, RecyclerAdapter.ViewHolder<T> holder);
}
