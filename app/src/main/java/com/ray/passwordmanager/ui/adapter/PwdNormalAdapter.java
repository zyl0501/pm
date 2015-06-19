package com.ray.passwordmanager.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ray.passwordmanager.R;
import com.ray.passwordmanager.db.entity.PasswordEntity;

public class PwdNormalAdapter extends BasePMAdapter<PasswordEntity> {
    public PwdNormalAdapter(Context context) {
        super(context);
    }

    @Override
    protected View newView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pwd_normal, parent, false);
        assert view != null;
        ViewHolder holder = new ViewHolder();
        holder.icon = (ImageView) view.findViewById(R.id.pwd_icon);
        holder.username = (TextView) view.findViewById(R.id.pwd_username);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(int position, View view, ViewGroup parent) {
        PasswordEntity entity = getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.icon.setImageResource(R.drawable.cheese_1);
        holder.username.setText(entity.getUsername());
    }

    private class ViewHolder{
        ImageView icon;
        TextView username;
    }
}
