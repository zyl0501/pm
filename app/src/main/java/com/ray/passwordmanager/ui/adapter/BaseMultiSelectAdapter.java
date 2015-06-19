package com.ray.passwordmanager.ui.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMultiSelectAdapter<T> extends BasePMAdapter<T> {

    protected List<Integer> selPositions;

    public BaseMultiSelectAdapter(Context context) {
        super(context);
        selPositions = new ArrayList<>();
    }

    @Override
    public void setData(List<T> list) {
        clearSelect();
        super.setData(list);
    }

    public void handleClick(int pos) {
        if (selPositions.contains(pos)) {
            removeSel(pos);
        } else {
            addSelect(pos);
        }
    }

    public T addSelect(int pos) {
        boolean needRefresh = false;
        if (!selPositions.contains(pos)) {
            selPositions.add(pos);
            needRefresh = true;
        }
        if (needRefresh) {
            notifyDataSetChanged();
        }
        return getItem(pos);
    }

    public void addSelect(List<T> list) {
        if (list == null) {
            return;
        }
        boolean needRefresh = false;
        for (T item : list) {
            if (data.contains(item)) {
                int pos = data.indexOf(item);
                if (!selPositions.contains(pos)) {
                    selPositions.add(pos);
                    needRefresh = true;
                }
            }
        }
        if (needRefresh) {
            notifyDataSetChanged();
        }
    }

    public T removeSel(int pos) {
        T item = getItem(pos);
        if (selPositions.contains(pos)) {
            selPositions.remove((Integer) pos);
            notifyDataSetChanged();
        }
        return item;
    }

    public boolean removeSel(T item) {
        boolean rmSuccess = false;
        int pos = data.indexOf(item);
        if (selPositions.contains(pos)) {
            selPositions.remove((Integer) pos);
            notifyDataSetChanged();
            rmSuccess = true;
        }
        return rmSuccess;
    }

    public List<Integer> getSelect() {
        return selPositions;
    }

    public List<T> getSelects() {
        List<T> list = new ArrayList<>(selPositions.size());
        for (int pos : selPositions) {
            T item = getItem(pos);
            if (item != null) {
                list.add(item);
            }
        }
        return list;
    }

    private void clearSelect() {
        selPositions.clear();
    }

}
