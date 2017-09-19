package com.yomii.view;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


/**
 * 包含删除文字按钮的EditText
 * <p>
 * 原理: 文字长度大于0则显示DrawableRight, 点击区域处于最右边的60px范围则清空文字
 */
public class EditTextWithDel extends android.support.v7.widget.AppCompatEditText {

    private final static String TAG = "EditTextWithDel";
    private Drawable iconDelete;

    //logical
    private boolean currentEmpty;


    public EditTextWithDel(Context context) {
        this(context, null);
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        currentEmpty = true;
        Drawable[] d = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(d[0], d[1], null, d[3]);
        iconDelete = d[2];
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = length() < 1;
                if (currentEmpty != isEmpty) {
                    currentEmpty = isEmpty;
                      Drawable[] d = getCompoundDrawables();
                    setCompoundDrawablesWithIntrinsicBounds(d[0], d[1], currentEmpty ? null : iconDelete, d[3]);
                }
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (iconDelete != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.i(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 60;
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }
}
