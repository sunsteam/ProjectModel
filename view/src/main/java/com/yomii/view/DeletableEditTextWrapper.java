package com.yomii.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Yomii on 2017/7/2.
 *
 * 用来包裹EditText 在左或右设置一个清除文字按钮，点击可以恢复到默认文字。必须包含一个EditText子控件，否则报错。
 */

public class DeletableEditTextWrapper extends LinearLayout {

    private static final int ORIENTATION_RIGHT = 1;

    private String defaultText;
    private Drawable buttonDrawable;
    private int buttonOrientation;

    //view
    private EditText editText;
    private ImageView buttonView;

    //logical
    private boolean currentEquals;


    public DeletableEditTextWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeletableEditTextWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        obtainAttr(context, attrs);
    }


    private void obtainAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DeletableEditTextWrapper);

        defaultText = a.getString(R.styleable.DeletableEditTextWrapper_defaultText);
        if (defaultText == null)
            defaultText = "";

        buttonDrawable = a.getDrawable(R.styleable.DeletableEditTextWrapper_button);

        buttonOrientation = a.getInt(R.styleable.DeletableEditTextWrapper_buttonOrientation, 1);

        a.recycle();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof EditText) {
                editText = (EditText) childAt;
                removeAllViews();
                generateView();
                return;
            }
        }

        throw new IllegalStateException("must has one EditText child");
    }

    private void generateView() {
        editText.setText(defaultText);
        currentEquals = true;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean equals = TextUtils.isEmpty(s) || TextUtils.equals(defaultText, s);
                if (currentEquals != equals) {
                    currentEquals = equals;
                    buttonView.setVisibility(currentEquals ? GONE : VISIBLE);
                }

            }
        });

        LayoutParams layoutParams = new LayoutParams(0, -1);
        layoutParams.weight = 1;
        addView(editText, layoutParams);

        //the delete button
        buttonView = new ImageView(getContext());
        buttonView.setImageDrawable(buttonDrawable);
        buttonView.setVisibility(GONE);
        buttonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(defaultText);
            }
        });
        measure(0,0);
        int bound = (int) Math.min(getMeasuredHeight(), dp2px(22f));
        if (buttonOrientation == ORIENTATION_RIGHT)
            addView(buttonView, bound, bound);
        else
            addView(buttonView, 0, new LayoutParams(bound, bound));
    }


    private float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }
}
