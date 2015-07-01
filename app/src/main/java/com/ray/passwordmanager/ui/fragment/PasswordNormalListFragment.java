package com.ray.passwordmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.passwordmanager.R;
import com.ray.passwordmanager.db.service.PasswordSvc;
import com.ray.passwordmanager.ui.adapter.recycler.PwdNormalAdapter;

/**
 * Created by Ray on 15/6/19.
 */
public class PasswordNormalListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView lv = (RecyclerView) inflater.inflate(
                R.layout.fragment_pwd_list, container, false);
        setupListView(lv);
        return lv;
    }

    private void setupListView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        PwdNormalAdapter adapter = new PwdNormalAdapter(recyclerView);
        adapter.setData(PasswordSvc.getAllPassword());
        recyclerView.setAdapter(adapter);
    }
}
