package com.ray.passwordmanager.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ray.passwordmanager.R;
import com.ray.passwordmanager.ui.fragment.AddPwdFragment;

/**
 * Created by Ray on 15/7/2.
 */
public class AddPwdActivity extends AppCompatActivity {

    private AddPwdFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pwd);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(R.string.create);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fragment = (AddPwdFragment) getSupportFragmentManager().findFragmentById(R.id.frag_add_pwd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_pwd, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.confirm).setEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm:
                fragment.doConfirm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
