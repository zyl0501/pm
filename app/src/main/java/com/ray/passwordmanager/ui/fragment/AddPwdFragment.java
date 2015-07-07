package com.ray.passwordmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.passwordmanager.R;
import com.ray.passwordmanager.db.entity.PasswordEntity;
import com.ray.passwordmanager.manager.PasswordManager;
import com.ray.passwordmanager.ui.widget.InputIconLayout;
import com.ray.passwordmanager.util.ToastUtil;

/**
 * Created by Ray on 15/7/2.
 */
public class AddPwdFragment extends Fragment {

    private InputIconLayout usernameLayout, pwdLayout, categoryLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_add_pwd, container, false);
        usernameLayout = (InputIconLayout) view.findViewById(R.id.username_input_layout);
        pwdLayout = (InputIconLayout) view.findViewById(R.id.password_input_layout);
        categoryLayout = (InputIconLayout) view.findViewById(R.id.category_input_layout);

        return view;
    }

    public void doConfirm() {
        String username = usernameLayout.getEditText().getText().toString();
        String password = pwdLayout.getEditText().getText().toString();
        String category = categoryLayout.getEditText().getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.showS(getActivity(), R.string.username_not_empty);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showS(getActivity(), R.string.password_not_empty);
            return;
        }
        PasswordEntity entity = new PasswordEntity();
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setCategory(category);
        PasswordManager.addPassword(entity);
        ToastUtil.showS(getActivity(), R.string.add_success);
        getActivity().finish();
    }
}
