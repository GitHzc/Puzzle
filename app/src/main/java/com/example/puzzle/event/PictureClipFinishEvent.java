package com.example.puzzle.event;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class PictureClipFinishEvent {
    Bitmap mBitmap;

    public PictureClipFinishEvent(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
