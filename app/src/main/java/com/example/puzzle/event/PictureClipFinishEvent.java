package com.example.puzzle.event;

import android.graphics.Bitmap;

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
