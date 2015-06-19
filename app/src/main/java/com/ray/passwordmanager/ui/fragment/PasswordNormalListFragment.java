package com.ray.passwordmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ray.passwordmanager.R;
import com.ray.passwordmanager.db.service.PasswordSvc;
import com.ray.passwordmanager.ui.adapter.PwdNormalAdapter;

/**
 * Created by Ray on 15/6/19.
 */
public class PasswordNormalListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView lv = (ListView) inflater.inflate(
                R.layout.fragment_pwd_list, container, false);
        setupListView(lv);
        return lv;
    }

    private void setupListView(ListView lv) {
        PwdNormalAdapter adapter = new PwdNormalAdapter(getActivity());
        adapter.setData(PasswordSvc.getAllPassword());
        lv.setAdapter(adapter);
    }
}
