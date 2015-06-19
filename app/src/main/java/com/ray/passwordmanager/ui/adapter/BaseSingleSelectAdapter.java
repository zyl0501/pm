package com.ray.passwordmanager.ui.adapter;

import android.content.Context;

import java.util.List;

public abstract class BaseSingleSelectAdapter<T> extends BasePMAdapter<T> {
    protected int selectPos;

    public BaseSingleSelectAdapter(Context context) {
        super(context);
        selectPos = -1;
    }

    @Override
    public void setData(List<T> list) {
        clearSelect();
        super.setData(list);
    }

    public T setSelect(int pos) {
        selectPos = pos;
        return data.get(pos);
    }

    public void clearSelect() {
        selectPos = -1;
    }

    public T getSelect() {
        return data.get(selectPos);
    }
}
