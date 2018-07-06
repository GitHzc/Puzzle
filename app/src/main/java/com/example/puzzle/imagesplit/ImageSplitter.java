package com.example.puzzle.imagesplit;

/**
 * Created by Administrator on 2016/3/27.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.example.puzzle.dish.DishManager;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class ImageSplitter {

    private static final String TAG = "puzzle";

    public static List<ImagePiece> split(Bitmap bitmap, int level, int dishWidth, int dishHeight)
            throws FileNotFoundException {
        List<ImagePiece> pieces = new ArrayList<>(level * level);

        float pieceWidth = (float) dishWidth / level;
        float pieceHeight = (float) dishHeight / level;
        PorterDuffXfermode pdf = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        Log.d(TAG, "pieceWidth: " + pieceWidth);

        int k;
        Log.d(TAG, "circle begin");
        for (int i = 0; i < level; i++) {
            for (int j = 0; j < level; j++) {
                if(i == 0) {
                    if (j == 0) {
                        k = DishManager.COVER_TOP_LEFT;
                    } else if(j == level - 1) {
                        k = DishManager.COVER_TOP_RIGHT;
                    }
                    else  {
                        k = DishManager.COVER_TOP;
                    }
                }
                else if(i == level - 1) {
                    if(j == 0) {
                        k = DishManager.COVER_BOTTOM_LEFT;
                    } else if (j == level - 1) {
                        k = DishManager.COVER_BOTTOM_RIGHT;
                    } else{
                        k = DishManager.COVER_BOTTOM;
                    }
                }
                else if(j == 0) {
                    k = DishManager.COVER_LEFT;
                }else if(j == level - 1) {
                    k = DishManager.COVER_RIGHT;
                }else  {
                    k = DishManager.COVER_CENTER;
                }

                Log.d(TAG, "piece " + k + " compute completed");

                ImagePiece piece = new ImagePiece();
                piece.index = j + i * level;
                float xValue, yValue;

                if(j != 0) {
                    //xValue = (int) (j * (pieceWidth - pieceWidth * 0.25));
                    xValue = (int) (j * pieceWidth - pieceWidth * 0.25);
                }
                else {
                    xValue = j * pieceWidth;
                }
                if(i != 0) {
                    yValue = i * pieceHeight;
                    if(i != level - 1){
                        yValue += 0.25f * pieceHeight;
                    }
                }
                else {
                    yValue = i * pieceHeight;
                }

                Bitmap drawingBitmap = Bitmap.createBitmap((int) pieceWidth,
                        (int) pieceHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(drawingBitmap);
                Paint paint = new Paint();

                Bitmap cover = DishManager.getCover(k);
                final float scale = pieceWidth / cover.getWidth();
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                canvas.drawBitmap(cover, matrix, paint);
                cover.recycle();

                float width = pieceWidth;
                float height = pieceHeight;
                if(i != level - 1){
                    height += pieceHeight * 0.25f;
                }
                if(j != 0){
                    width += pieceWidth * 0.25f;
                }

                paint.setXfermode(pdf);
                piece.bitmap = Bitmap.createBitmap(
                        bitmap,
                        (int) xValue, (int) yValue,
                        (int) width, (int) height);

                canvas.drawBitmap(piece.bitmap, 0, 0, paint);
                piece.bitmap.recycle();
                piece.bitmap = drawingBitmap;
                pieces.add(piece);
                Log.d(TAG, "piece " + k + " draw completed");
            }
        }
        System.gc();

        return pieces;
    }

}