package com.example.puzzle.event;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class PictureClipFinishEvent {
    Bitmap mBitmap;
    String mPicturePath;

    public PictureClipFinishEvent(Bitmap bitmap, String picturePath) {
        mBitmap = bitmap;
        mPicturePath = picturePath;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getPicturePath() {
        return mPicturePath;
    }
}
