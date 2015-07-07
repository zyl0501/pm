package com.ray.passwordmanager.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ray.passwordmanager.R;

/**
 * 带icon的输入框
 * Created by Ray on 15/7/6.
 */
public class InputIconLayout extends FrameLayout {

    private ViewGroup contentView;
    private ImageView iconImg;
    private View dividerView;
    private EditText mEditText;

    private int iconResId;
    private boolean showIcon, showDivider;

    public InputIconLayout(Context context) {
        this(context, null);
    }

    public InputIconLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputIconLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        contentView = (ViewGroup) ((ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.v_input_icon_layout, this, true)).getChildAt(0);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.InputIconLayoutStyle);
        showIcon = t.getBoolean(R.styleable.InputIconLayoutStyle_showIcon, true);
        showDivider = t.getBoolean(R.styleable.InputIconLayoutStyle_showDivider, true);
        iconResId = t.getResourceId(R.styleable.InputIconLayoutStyle_iconSrc, -1);
        t.recycle();
    }

    @Override
    protected void onFinishInflate() {
        iconImg = (ImageView) findViewById(R.id.icon_img);
        dividerView = findViewById(R.id.divider);

        if (iconResId != -1) {
            iconImg.setImageResource(iconResId);
        }
        iconImg.setVisibility(showIcon ? View.VISIBLE : View.GONE);
        dividerView.setVisibility(showDivider ? View.VISIBLE : View.GONE);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            setEditText((EditText) child, params);
//            super.addView(child, index, params);
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    private void setEditText(EditText editText, ViewGroup.LayoutParams params) {
        // If we already have an EditText, throw an exception
        if (mEditText != null) {
            throw new IllegalArgumentException("We already have an EditText, can only have one");
        }
        mEditText = editText;

        // Add focus listener to the EditText so that we can notify the label that it is activated.
        // Allows the use of a ColorStateList for the text color on the label
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
            }
        });

        Context context = getContext();
//        int height = (int) context.getResources().getDimension(R.dimen.input_icon_view_edt_height);
        int leftMargin = showIcon ? (int) context.getResources().getDimension(R.dimen.input_icon_view_edt_margin) : 0;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(params);
        lp.leftMargin = leftMargin;
        lp.gravity = GravityCompat.START | Gravity.TOP;
        contentView.addView(editText, lp);
    }

    public EditText getEditText() {
        return mEditText;
    }
}
