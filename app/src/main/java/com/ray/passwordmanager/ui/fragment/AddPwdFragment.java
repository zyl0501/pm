package com.ray.passwordmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ray.passwordmanager.R;

/**
 * Created by Ray on 15/7/2.
 */
public class AddPwdFragment extends Fragment {

    private EditText usernameEdt, pwdEdt, categoryEdt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_add_pwd, container, false);
        usernameEdt = (EditText) view.findViewById(R.id.username_input_edt);
        pwdEdt = (EditText) view.findViewById(R.id.password_input_edt);
        categoryEdt = (EditText) view.findViewById(R.id.category_input_edt);

        return view;
    }
}
