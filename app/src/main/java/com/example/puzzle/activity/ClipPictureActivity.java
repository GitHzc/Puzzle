package com.example.puzzle.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.puzzle.R;
import com.example.puzzle.event.PictureClipFinishEvent;
import com.example.puzzle.widget.ChooseBorderView;
import com.example.puzzle.widget.ZoomImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.puzzle.utils.Utility.saveBitmap;

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
                try {
                    mClipPicture = Bitmap.createBitmap(mPicture.getSourceBitmap(), x, y, length, length);
                    String picturePath = saveBitmap(ClipPictureActivity.this, "picture6.jpg", mClipPicture);
                    PictureClipFinishEvent event = new PictureClipFinishEvent(mClipPicture, picturePath);
                    EventBus.getDefault().post(event);
                    finish();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(ClipPictureActivity.this, "图像裁剪超出边界", Toast.LENGTH_SHORT).show();
                }
        }
    }

    static public Intent getIntent(Context context) {
        return new Intent(context, ClipPictureActivity.class);
    }
}
