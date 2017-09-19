package com.yomii.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class EditTextWithPassword extends AppCompatEditText {

    private final static String TAG = "EditTextWithPwd";

    @DrawableRes
    private int iconShow;
    @DrawableRes
    private int iconHide;


    public EditTextWithPassword(Context context) {
        this(context, null);
    }

    public EditTextWithPassword(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditTextWithPassword(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithPassword);

        iconShow = a.getResourceId(R.styleable.EditTextWithPassword_drawableShow, 0);
        iconHide = a.getResourceId(R.styleable.EditTextWithPassword_drawableHide, 0);

        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(0, 0, iconHide, 0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 60;
            if (rect.contains(eventX, eventY)) {
                TransformationMethod type = getTransformationMethod();
                if (PasswordTransformationMethod.getInstance().equals(type)) {
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, iconShow, 0);
                } else {
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, iconHide, 0);
                }
            }

        }
        return super.onTouchEvent(event);
    }

}
