package com.example.puzzle.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.puzzle.R;
import com.example.puzzle.event.PictureClipFinishEvent;
import com.example.puzzle.widget.ChooseBorderView;
import com.example.puzzle.widget.ZoomImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClipPictureActivity extends AppCompatActivity {
    private static final String TAG = "ClipPictureActivity";
    @BindView(R.id.activity_clip_picture)
    ZoomImageView mPicture;
    @BindView(R.id.activity_clip_picture_back)
    ImageView mBackButton;
    @BindView(R.id.activity_clip_picture_choose_border)
    ChooseBorderView mChooseBorder;
    @BindView(R.id.activity_clip_picture_complete)
    Button mCompleteButton;
    Bitmap mClipPicture;
    int mStartX = 0;
    int mStartY = 0;
    int mLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_picture);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(intent.getData()));
            mPicture.setImageBitmap(bitmap);
            mChooseBorder.setOnImageDetailsSizeChange(new ChooseBorderView.OnImageDetailsSizeChange() {
                @Override
                public void onBorderSizeChange(int x, int y, int length) {
                    mStartX = x;
                    mStartY = y;
                    mLength = length;
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.activity_clip_picture_back, R.id.activity_clip_picture_complete})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_clip_picture_back:
                finish();
                break;
            case R.id.activity_clip_picture_complete:
                if (mLength == 0) {
                    mStartX = (int) mChooseBorder.getTopLeftX();
                    mStartY = (int) mChooseBorder.getTopLeftY();
                    mLength = (int) mChooseBorder.getBorderLength();
                }
                float ratio = mPicture.getRatio();
                int x = (int) ((mStartX - mPicture.getTranslateX()) / ratio);
                int y = (int) ((mStartY - (int) mPicture.getTranslateY()) / ratio);
                int length = (int) (mLength / ratio);
                mClipPicture = Bitmap.createBitmap(mPicture.getSourceBitmap(), x, y, length, length);
                saveClipPicture();
                PictureClipFinishEvent event = new PictureClipFinishEvent(mClipPicture);
                EventBus.getDefault().post(event);
                finish();
        }
    }

    static public Intent getIntent(Context context) {
        return new Intent(context, ClipPictureActivity.class);
    }

    void saveClipPicture() {
        String path = getCacheDir().getAbsolutePath() + File.separator + "temp.jpg";
        File file = new File(path);
         FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            mClipPicture.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
