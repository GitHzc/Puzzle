package com.example.puzzle.imagesplit;

import android.graphics.Bitmap;

public class ImagePiece {

    public int index = 0;

    public Bitmap bitmap = null;

    public void recycleBitmap(){
        if(bitmap != null){
            bitmap.recycle();
        }
    }
}