package com.example.puzzle.imagesplit;

/**
 * Created by Administrator on 2016/3/27.
 */
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