package com.example.puzzle.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.puzzle.R;

/**
 * Created by Administrator on 2018/7/3 0003.
 */

public class ChooseBorderView extends View {
    private int scale = (int) this.getResources().getDisplayMetrics().density;
    private float borderHeight;
    private float borderWidth;
    private float borderLength = 200 * scale;
    private int RECT_BORDER_WIDTH = 3 * scale;
    private int RECT_CORNER_WIDTH = 6 * scale;
    private int RECT_CORNER_HEIGHT = 20 * scale;
    private static float[][] four_corner_coordinate_positions;
    private static int NOW_MOVE_STATE = 1;
    private static boolean MOVE_OR_ZOOM_STATE = true;

    public ChooseBorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        borderHeight = this.getHeight();
        borderWidth = this.getWidth();
        four_corner_coordinate_positions = new float[][] {
                {(borderWidth - borderLength) / 2, (borderHeight - borderLength) / 2},
                {(borderWidth + borderLength) / 2, (borderHeight - borderLength) / 2},
                {(borderWidth - borderLength) / 2, (borderHeight + borderLength) / 2},
                {(borderWidth + borderLength) / 2, (borderHeight + borderLength) / 2}
        };
    }

    private int temp1 = (RECT_CORNER_WIDTH - RECT_BORDER_WIDTH) / 2;
    private int temp2 = (RECT_CORNER_WIDTH + RECT_BORDER_WIDTH) / 2;

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paintRect = new Paint();
        paintRect.setColor(getResources().getColor(R.color.backgroundWhite));
        paintRect.setStrokeWidth(RECT_BORDER_WIDTH);
        paintRect.setAntiAlias(true);
        paintRect.setStyle(Paint.Style.STROKE);
        canvas.drawRect(four_corner_coordinate_positions[0][0],
                four_corner_coordinate_positions[0][1],
                four_corner_coordinate_positions[3][0],
                four_corner_coordinate_positions[3][1], paintRect);
        paintRect.setColor(Color.WHITE);
        paintRect.setStrokeWidth(RECT_CORNER_WIDTH);
        paintRect.setAntiAlias(true);

        //左上
        canvas.drawLine(four_corner_coordinate_positions[0][0] - temp2,
                four_corner_coordinate_positions[0][1] - temp1,
                four_corner_coordinate_positions[0][0] - temp1 + RECT_CORNER_HEIGHT,
                four_corner_coordinate_positions[0][1] - temp1,
                paintRect);
        canvas.drawLine(four_corner_coordinate_positions[0][0] - temp1,
                four_corner_coordinate_positions[0][1] - temp2,
                four_corner_coordinate_positions[0][0] - temp1,
                four_corner_coordinate_positions[0][1] - temp1 + RECT_CORNER_HEIGHT,
                paintRect);

        //左下
        canvas.drawLine(four_corner_coordinate_positions[2][0] - temp2,
                four_corner_coordinate_positions[2][1] + temp1,
                four_corner_coordinate_positions[2][0] - temp1 + RECT_CORNER_HEIGHT,
                four_corner_coordinate_positions[2][1] + temp1,
                paintRect);
        canvas.drawLine(four_corner_coordinate_positions[2][0] - temp1,
                four_corner_coordinate_positions[2][1] + temp1,
                four_corner_coordinate_positions[2][0] - temp1,
                four_corner_coordinate_positions[2][1] + temp1 - RECT_CORNER_HEIGHT,
                paintRect);

        //右上
        canvas.drawLine(four_corner_coordinate_positions[1][0] + temp1,
                four_corner_coordinate_positions[1][1] - temp1,
                four_corner_coordinate_positions[1][0] + temp1 - RECT_CORNER_HEIGHT,
                four_corner_coordinate_positions[1][1] - temp1,
                paintRect);
        canvas.drawLine(four_corner_coordinate_positions[1][0] + temp1,
                four_corner_coordinate_positions[1][1] - temp2,
                four_corner_coordinate_positions[1][0] + temp1,
                four_corner_coordinate_positions[1][1] - temp1 + RECT_CORNER_HEIGHT,
                paintRect);

        //右下
        canvas.drawLine(four_corner_coordinate_positions[3][0] + temp2,
                four_corner_coordinate_positions[3][1] + temp1,
                four_corner_coordinate_positions[3][0] + temp1 - RECT_CORNER_HEIGHT,
                four_corner_coordinate_positions[3][1] + temp1,
                paintRect);
        canvas.drawLine(four_corner_coordinate_positions[3][0] + temp1,
                four_corner_coordinate_positions[3][1] + temp1,
                four_corner_coordinate_positions[3][0] + temp1,
                four_corner_coordinate_positions[3][1] + temp1 - RECT_CORNER_HEIGHT,
                paintRect);

        //扫描线
        if (IF_SCANNING_SHOW) {
            paintRect.setColor(Color.WHITE);
            paintRect.setStrokeWidth(1);
            paintRect.setAntiAlias(true);
            paintRect.setStyle(Paint.Style.STROKE);
            //竖1
            canvas.drawLine(four_corner_coordinate_positions[0][0] + borderLength / 3,
                    four_corner_coordinate_positions[0][1] + temp1,
                    four_corner_coordinate_positions[2][0] + borderLength / 3,
                    four_corner_coordinate_positions[2][1] - temp1,
                    paintRect);
            //竖2
            canvas.drawLine(four_corner_coordinate_positions[1][0] - borderLength / 3,
                    four_corner_coordinate_positions[1][1] + temp1,
                    four_corner_coordinate_positions[3][0] - borderLength / 3,
                    four_corner_coordinate_positions[3][1] - temp1,
                    paintRect);
            //横1
            canvas.drawLine(four_corner_coordinate_positions[0][0] + temp1,
                    four_corner_coordinate_positions[0][1] + borderLength / 3,
                    four_corner_coordinate_positions[1][0] - temp1,
                    four_corner_coordinate_positions[1][1] + borderLength / 3,
                    paintRect);
            //横2
            canvas.drawLine(four_corner_coordinate_positions[2][0] + temp1,
                    four_corner_coordinate_positions[2][1] - borderLength / 3,
                    four_corner_coordinate_positions[3][0] - temp1,
                    four_corner_coordinate_positions[3][1] - borderLength / 3,
                    paintRect);
        }
    }

    private boolean IF_SCANNING_SHOW = false;
    private int lastX = 0;
    private int lastY = 0;
    private int offsetX = 0;
    private int offsetY = 0;
    static int point = -1;
    private int POINT_STATE = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                IF_SCANNING_SHOW = true;
                if (isInTheCornerCircle(event.getX(), event.getY()) != -1) {
                    MOVE_OR_ZOOM_STATE = false;
                    point = isInTheCornerCircle(event.getX(), event.getY());
                }
                lastX = x;
                lastY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = x - lastX;
                offsetY = y - lastY;
                judgementXandY();
                if (MOVE_OR_ZOOM_STATE) {
                    getoffsetXandoffsetY();
                    for (int i = 0; i < four_corner_coordinate_positions.length; i++) {
                        four_corner_coordinate_positions[i][0] += offsetX;
                        four_corner_coordinate_positions[i][1] += offsetY;

                        mOnImageDetailsSizeChange.onBorderSizeChange(
                                (int) four_corner_coordinate_positions[0][0],
                                (int) four_corner_coordinate_positions[0][1],
                                (int) borderLength
                        );

                        invalidate();
                    }
                } else {
                    max = Math.abs(offsetX) > Math.abs(offsetY) ? Math.abs(offsetX) : Math.abs(offsetY);
                    if (POINT_STATE == 0) {
                        getoffsetXandoffsetY();
                    } else if (POINT_STATE == 1) {
                        if (borderLength - max <= RECT_CORNER_HEIGHT * 2 - temp2) {
                            max = 0;
                        }
                    }

                    changeFourCoodinatePosition(point, offsetX, offsetY);
                    notifyNowBordrLength();
                    mOnImageDetailsSizeChange.onBorderSizeChange(
                            (int) four_corner_coordinate_positions[0][0],
                            (int) four_corner_coordinate_positions[0][1],
                            (int) borderLength
                    );
                    invalidate();
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                IF_SCANNING_SHOW = false;
                MOVE_OR_ZOOM_STATE = true;
                invalidate();
                break;
        }
        return true;
    }

    private void notifyNowBordrLength() {
        float a = four_corner_coordinate_positions[0][0];
        float b = four_corner_coordinate_positions[0][1];
        float c = four_corner_coordinate_positions[1][0];
        float d = four_corner_coordinate_positions[1][1];
        float temp1 = (float) Math.pow(a - c, 2);
        float temp2 = (float) Math.pow(b - d, 2);
        borderLength = (float) Math.sqrt(temp1 + temp2);
    }

    private void judgementXandY() {
        switch(point) {
            case 0:
                if ((offsetX <= 0 && offsetY <= 0) || (offsetX <= 0 && offsetY >= 0)) {
                    POINT_STATE = 0; //扩大
                } else {
                    POINT_STATE = 1; //缩小
                }
                break;
            case 1:
                if ((offsetX >= 0 && offsetY <= 0) || (offsetX >= 0 && offsetY >= 0)) {
                    POINT_STATE = 0;
                } else {
                    POINT_STATE = 1;
                }
                break;
            case 2:
                if ((offsetX <= 0 && offsetY >= 0) || (offsetX <= 0 && offsetY <= 0)) {
                    POINT_STATE = 0;
                } else {
                    POINT_STATE = 1;
                }
                break;
            case 3:
                if ((offsetX >= 0 && offsetY >= 0) || (offsetX >= 0 && offsetY <= 0)) {
                    POINT_STATE = 0;
                } else {
                    POINT_STATE = 1;
                }
                break;
        }
    }

    //防止x和y溢出边界
    private void getoffsetXandoffsetY() {
        if (MOVE_OR_ZOOM_STATE) {
            if ((four_corner_coordinate_positions[0][0] + offsetX <= 0) || (four_corner_coordinate_positions[1][0] + offsetX >= borderWidth)) {
                offsetX = 0;
            }

            if ((four_corner_coordinate_positions[0][1] + offsetY <= 0) || (four_corner_coordinate_positions[2][1] + offsetY >= borderHeight)) {
                offsetY = 0;
            }
        } else {
            switch(point) {
                case 0:
                    if ((four_corner_coordinate_positions[0][0] - max <= 0) || (four_corner_coordinate_positions[0][1] - max <= 0)) {
                        max = 0;
                    }
                    break;
                case 1:
                    if ((four_corner_coordinate_positions[1][0] + max >= borderWidth) || (four_corner_coordinate_positions[1][1] - max <= 0)) {
                        max = 0;
                    }
                    break;
                case 2:
                    if ((four_corner_coordinate_positions[2][0] - max <= 0) || (four_corner_coordinate_positions[2][1] + max >= borderHeight)) {
                        max = 0;
                    }
                    break;
                case 3:
                    if ((four_corner_coordinate_positions[3][0] + max >= borderWidth) || (four_corner_coordinate_positions[3][1] + max >= borderHeight)) {
                        max = 0;
                    }
                    break;
            }
        }
    }

    static int max;

    private void changeFourCoodinatePosition(int point, int offsetX, int offsetY) {
        switch (point) {
            case 0:
                if (offsetX > 0 && offsetY < 0) {
                    four_corner_coordinate_positions[0][0] += max;
                    four_corner_coordinate_positions[0][1] += max;
                    four_corner_coordinate_positions[1][1] += max;
                    four_corner_coordinate_positions[2][0] += max;
                } else if (offsetX < 0 && offsetY > 0) {
                    four_corner_coordinate_positions[0][0] -= max;
                    four_corner_coordinate_positions[0][1] -= max;
                    four_corner_coordinate_positions[1][1] -= max;
                    four_corner_coordinate_positions[2][0] -= max;
                } else if (offsetX < 0 && offsetY < 0) {
                    four_corner_coordinate_positions[0][0] -= max;
                    four_corner_coordinate_positions[0][1] -= max;
                    four_corner_coordinate_positions[1][1] -= max;
                    four_corner_coordinate_positions[2][0] -= max;
                } else if (offsetX > 0 && offsetY > 0) {
                    four_corner_coordinate_positions[0][0] += max;
                    four_corner_coordinate_positions[0][1] += max;
                    four_corner_coordinate_positions[1][1] += max;
                    four_corner_coordinate_positions[2][0] += max;
                }
                break;
            case 1:
                if (offsetX > 0 && offsetY < 0) {
                    four_corner_coordinate_positions[1][0] += max;
                    four_corner_coordinate_positions[1][1] -= max;
                    four_corner_coordinate_positions[0][1] -= max;
                    four_corner_coordinate_positions[3][0] += max;
                } else if (offsetX < 0 && offsetY > 0) {
                    four_corner_coordinate_positions[1][0] -= max;
                    four_corner_coordinate_positions[1][1] += max;
                    four_corner_coordinate_positions[0][1] += max;
                    four_corner_coordinate_positions[3][0] -= max;
                } else if (offsetX < 0 && offsetY < 0) {
                    four_corner_coordinate_positions[1][0] -= max;
                    four_corner_coordinate_positions[1][1] += max;
                    four_corner_coordinate_positions[0][1] += max;
                    four_corner_coordinate_positions[3][0] -= max;
                } else if (offsetX > 0 && offsetY > 0) {
                    four_corner_coordinate_positions[1][0] += max;
                    four_corner_coordinate_positions[1][1] -= max;
                    four_corner_coordinate_positions[0][1] -= max;
                    four_corner_coordinate_positions[3][0] += max;
                }
                break;
            case 2:
                if (offsetX > 0 && offsetY < 0) {
                    four_corner_coordinate_positions[2][0] += max;
                    four_corner_coordinate_positions[2][1] -= max;
                    four_corner_coordinate_positions[0][0] += max;
                    four_corner_coordinate_positions[3][1] -= max;
                } else if (offsetX < 0 && offsetY > 0) {
                    four_corner_coordinate_positions[2][0] -= max;
                    four_corner_coordinate_positions[2][1] += max;
                    four_corner_coordinate_positions[0][0] -= max;
                    four_corner_coordinate_positions[3][1] += max;
                } else if (offsetX < 0 && offsetY < 0) {
                    four_corner_coordinate_positions[2][0] -= max;
                    four_corner_coordinate_positions[2][1] += max;
                    four_corner_coordinate_positions[0][0] -= max;
                    four_corner_coordinate_positions[3][1] += max;
                } else if (offsetX > 0 && offsetY > 0) {
                    four_corner_coordinate_positions[2][0] += max;
                    four_corner_coordinate_positions[2][1] -= max;
                    four_corner_coordinate_positions[0][0] += max;
                    four_corner_coordinate_positions[3][1] -= max;
                }
                break;
            case 3:
                if (offsetX > 0 && offsetY < 0) {
                    four_corner_coordinate_positions[3][0] += max;
                    four_corner_coordinate_positions[3][1] += max;
                    four_corner_coordinate_positions[1][0] += max;
                    four_corner_coordinate_positions[2][1] += max;
                } else if (offsetX < 0 && offsetY > 0) {
                    four_corner_coordinate_positions[3][0] -= max;
                    four_corner_coordinate_positions[3][1] -= max;
                    four_corner_coordinate_positions[1][0] -= max;
                    four_corner_coordinate_positions[2][1] -= max;
                } else if (offsetX < 0 && offsetY < 0) {
                    four_corner_coordinate_positions[3][0] -= max;
                    four_corner_coordinate_positions[3][1] -= max;
                    four_corner_coordinate_positions[1][0] -= max;
                    four_corner_coordinate_positions[2][1] -= max;
                } else if (offsetX > 0 && offsetY > 0) {
                    four_corner_coordinate_positions[3][0] += max;
                    four_corner_coordinate_positions[3][1] += max;
                    four_corner_coordinate_positions[1][0] += max;
                    four_corner_coordinate_positions[2][1] += max;
                }
                break;
        }
    }

    private int isInTheCornerCircle(float x, float y) {
        for (int i = 0; i < four_corner_coordinate_positions.length; i++) {
            float a = four_corner_coordinate_positions[i][0];
            float b = four_corner_coordinate_positions[i][1];
            float temp1 = (float) Math.pow(x - a, 2);
            float temp2 = (float) Math.pow(y - b, 2);
            if (((float) RECT_CORNER_HEIGHT) >= Math.sqrt(temp1 + temp2)) {
                return i;
            }
        }
        return -1;
    }

    public interface OnImageDetailsSizeChange {
        void onBorderSizeChange(int x, int y, int length);
    }

    public OnImageDetailsSizeChange mOnImageDetailsSizeChange;

    public void setOnImageDetailsSizeChange(OnImageDetailsSizeChange onImageDetailsSizeChange) {
        this.mOnImageDetailsSizeChange = onImageDetailsSizeChange;
    }

}
