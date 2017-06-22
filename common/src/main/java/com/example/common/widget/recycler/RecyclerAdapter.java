package com.example.common.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 范超 on 2017/6/21.
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<T>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<T> {
    private final List<T> list;
    private AdapterListener listener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener listener) {
        this(new ArrayList<T>(),listener);
    }

    public RecyclerAdapter(List<T> list, AdapterListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setListener(AdapterListener<T> listener) {
        this.listener = listener;
    }

    public AdapterListener getListener() {
        return listener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, list.get(position));
    }

    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param t        数据
     * @return XML文件的ID，用于创建ViewHolder
     */
    public abstract int getItemViewType(int position, T t);

    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //把XML id为viewType的文件初始化为一个rootView
        View root = inflater.inflate(viewType, parent, false);
        //通过子类重写的方法获得ViewHolder
        ViewHolder<T> holder = onCreateViewHolder(root, viewType);
        //设置View的Tag位ViewHolder进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        //设置点击事件
        root.setOnClickListener(this);
        //设置长按事件
        root.setOnLongClickListener(this);
        //界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        //绑定callback
        holder.callback = this;
        return holder;
    }

    //创建ViewHolder
    protected abstract ViewHolder<T> onCreateViewHolder(View root, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        T t = list.get(position);
        holder.bind(t);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //增加一条数据
    public void add(T t) {
        list.add(t);
        notifyItemInserted(list.size() - 1);
    }

    //增加一堆数据
    public void add(T... ts) {
        if (ts != null && ts.length > 0) {
            int startPosition = list.size();
            Collections.addAll(list, ts);
            notifyItemRangeInserted(startPosition, ts.length);
        }
    }

    //增加一堆数据
    public void add(Collection<T> ts) {
        if (ts != null && ts.size() > 0) {
            int startPosition = list.size();
            list.addAll(ts);
            notifyItemRangeInserted(startPosition, ts.size());
        }
    }

    //清空
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    //替换数据(会清空以前的数据)
    public void replace(Collection<T> ts) {
        if (ts == null || ts.size() == 0) {
            return;
        }
        list.clear();
        list.addAll(ts);
        notifyDataSetChanged();
    }

    //设置监听器
    public interface AdapterListener<T> {
        void onItemClick(RecyclerAdapter.ViewHolder holder, T t);

        void onItemLongClick(RecyclerAdapter.ViewHolder holder, T t);
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (listener != null) {
            int pos = holder.getAdapterPosition();
            listener.onItemClick(holder, list.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View view) {ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (listener != null) {
            int pos = holder.getAdapterPosition();
            listener.onItemLongClick(holder, list.get(pos));
            return true;
        }
        return false;
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
        protected T t;
        private Unbinder unbinder;
        private AdapterCallback<T> callback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param t 绑定的数据
         */
        void bind(T t) {
            this.t = t;
        }

        /**
         * 当触发绑定数据的时候回调
         */
        protected abstract void onBind();

        //holder自己对自己对应的data更新
        public void updateData(T t) {
            if (callback != null) {
                callback.update(t, this);
            }
        }
    }
}
