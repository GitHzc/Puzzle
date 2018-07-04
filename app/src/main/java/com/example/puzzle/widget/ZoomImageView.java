package com.example.puzzle.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/7/3 0003.
 */

public class ZoomImageView extends View {
    public static final int STATUS_INIT = 1;
    private Matrix matrix = new Matrix();
    private Bitmap sourceBitmap;
    private int currentStatus;
    private int width;
    private int height;
    private float ratio;

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentStatus = STATUS_INIT;
    }

    public void setImageBitmap(Bitmap bitmap) {
        sourceBitmap = bitmap;
        invalidate();
    }

    public Bitmap getSourceBitmap() {
        return sourceBitmap;
    }

    public float getRatio() {
        return ratio;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            width = getWidth();
            height = getHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initBitmap(canvas);
        canvas.drawBitmap(sourceBitmap, matrix, null);
    }

    public float translateY;
    public float translateX;

    public float getTranslateY() {
        return translateY;
    }

    public float getTranslateX() {
        return translateX;
    }

    private void initBitmap(Canvas canvas) {
        if (sourceBitmap != null) {
            matrix.reset();
            int bitmapWidth = sourceBitmap.getWidth();
            int bitmapHeight = sourceBitmap.getHeight();

            if (bitmapWidth > width || bitmapHeight > height) {
                if (bitmapWidth > width && bitmapHeight > height) {
                    int distanceX = Math.abs(width - bitmapWidth);
                    int distanceY = Math.abs(height - bitmapHeight);
                    if (distanceX >= distanceY) {
                        ratio = width / (bitmapWidth * 1.0f);
                        matrix.postScale(ratio,  ratio);
                        translateY = (height - sourceBitmap.getHeight() * ratio) / 2f;
                        matrix.postTranslate(0, translateY);
                    } else {
                        ratio = height / (bitmapHeight * 1.0f);
                        matrix.postScale(ratio, ratio);
                        translateX = (width - sourceBitmap.getWidth() * ratio) / 2f;
                        matrix.postTranslate(translateX, 0);
                    }
                } else if (bitmapHeight > height) {
                    ratio = height / (bitmapHeight * 1.0f);
                    matrix.postScale(ratio, ratio);
                    translateX = (width - (bitmapWidth * ratio)) / 2f;
                    matrix.postTranslate(translateX, 0);
                } else if (bitmapWidth > width){
                    ratio = width / (bitmapWidth * 1.0f);
                    matrix.postScale(ratio, ratio);
                    translateY = (height - (bitmapHeight * ratio)) / 2f;
                    matrix.postTranslate(0, translateY);
                }
            } else {
                int distanceX = Math.abs(width - bitmapWidth);
                int distanceY = Math.abs(height - bitmapHeight);
                if (distanceX <= distanceY) {
                    ratio = width / (bitmapWidth * 1.0f);
                    matrix.postScale(ratio, ratio);
                    translateY = (height - sourceBitmap.getHeight() * ratio) / 2f;
                    matrix.postTranslate(0, translateY);
                } else {
                    ratio = height / (bitmapHeight * 1.0f);
                    matrix.postScale(ratio, ratio);
                    translateX = (width - sourceBitmap.getWidth() * ratio) / 2f;
                    matrix.postTranslate(translateX, 0);
                }
            }
            canvas.drawBitmap(sourceBitmap, matrix, null);
        }
    }
}
