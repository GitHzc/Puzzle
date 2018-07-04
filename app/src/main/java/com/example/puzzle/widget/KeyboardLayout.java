package com.example.puzzle.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class KeyboardLayout extends LinearLayout{
    private KeyboardLayoutListener mListener;
    private boolean mIsKeyboardActive = false;
    private int mKeyboardHeight = 0;

    public KeyboardLayout(Context context) {
        this(context, null, 0);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardOnGlobalChangeListener());
    }

    public void setKeyboardListener(KeyboardLayoutListener listener) {
        mListener = listener;
    }

    public KeyboardLayoutListener getKeyboardListener() {
        return mListener;
    }

    public boolean isKeyboardActive() {
        return mIsKeyboardActive;
    }

    public int getKeyboardHeight() {
        return mKeyboardHeight;
    }

    public interface KeyboardLayoutListener {
        void onKeyboardStateChanged(boolean isActive, int keyboardHeight);
    }

    private class KeyboardOnGlobalChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int mScreenHeight = 0;

        private int getScreenHeight() {
            if (mScreenHeight > 0) {
                return mScreenHeight;
            }
            DisplayMetrics outMetrics = new DisplayMetrics();
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
            mScreenHeight = outMetrics.heightPixels;
            return mScreenHeight;
        }

        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int screenHeight = getScreenHeight();
            int keyboardHeight = screenHeight - rect.bottom;
            boolean isActive = false;
            if (Math.abs(keyboardHeight) > screenHeight / 4) {
                isActive = true;
                mKeyboardHeight = keyboardHeight;
            }
            mIsKeyboardActive = isActive;
            if (mListener != null) {
                mListener.onKeyboardStateChanged(isActive, keyboardHeight);
            }
        }
    }
}
