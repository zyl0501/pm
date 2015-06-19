package com.ray.passwordmanager.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BasePMAdapter<T> extends BaseAdapter {
    protected List<T> data;
    protected Context mContext;

    public BasePMAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public T getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = newView(position, convertView, parent);
        } else {
            v = convertView;
        }
        bindView(position, v, parent);
        return v;
    }

    protected abstract View newView(int position, View convertView, ViewGroup parent);

    protected abstract void bindView(int position, View convertView, ViewGroup parent);
}
