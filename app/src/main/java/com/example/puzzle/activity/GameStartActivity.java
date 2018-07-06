package com.example.puzzle.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.puzzle.PuzzleApplication;
import com.example.puzzle.R;
import com.example.puzzle.event.PictureClipFinishEvent;
import com.example.puzzle.utils.Utility;
import com.example.puzzle.widget.ChooseDifficultyFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameStartActivity extends AppCompatActivity {
    @BindView(R.id.activity_game_start_back)
    ImageButton mBackButton;
    @BindView(R.id.activity_game_start_button)
    Button mGameStartButton;
    @BindView(R.id.activity_game_start_choose_difficulty)
    LinearLayout mChooseDifficultyButton;
    @BindView(R.id.activity_game_start_choose_from_album)
    LinearLayout mChooseFromAlbum;
    @BindView(R.id.activity_game_start_picture1)
    ImageView mPicture1;
    @BindView(R.id.activity_game_start_picture2)
    ImageView mPicture2;
    @BindView(R.id.activity_game_start_picture3)
    ImageView mPicture3;
    @BindView(R.id.activity_game_start_level)
    TextView mLevel;
    Intent mIntent;
    int pictureId;
    LinearLayout mSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }


    @OnClick({
                R.id.activity_game_start_picture1, R.id.activity_game_start_picture2,
                R.id.activity_game_start_picture3, R.id.activity_game_start_choose_from_album,
                R.id.activity_game_start_choose_difficulty, R.id.activity_game_start_button,
                R.id.activity_game_start_back
    })
    void onClickPicture(View view) {
        mIntent = GameActivity.getIntent(GameStartActivity.this);
        switch(view.getId()) {
            case R.id.activity_game_start_picture1:
                resetSelected();
                setSelected((LinearLayout) mPicture1.getParent());
                pictureId = R.raw.puzzle1;
                break;
            case R.id.activity_game_start_picture2:
                resetSelected();
                setSelected((LinearLayout) mPicture2.getParent());
                pictureId = R.raw.puzzle2;
                break;
            case R.id.activity_game_start_picture3:
                resetSelected();
                setSelected((LinearLayout) mPicture3.getParent());
                pictureId = R.raw.puzzle3;
                break;
            case R.id.activity_game_start_choose_from_album:
                pictureId = -1;
                choosePicture();
                break;
            case R.id.activity_game_start_choose_difficulty:
                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                ChooseDifficultyFragment dialog = new ChooseDifficultyFragment();
                dialog.setChooseDifficultyFragmentListener(new ChooseDifficultyFragment.ChooseDifficultyFragmentListener() {
                    @Override
                    public void onChooseDifficulty(int index) {
                        String[] levels = getResources().getStringArray(R.array.difficulty);
                        mLevel.setText(levels[index]);
                        PuzzleApplication.setLevel(index + 3);
                    }
                });
                dialog.show(manager,"singleDialog");
                break;
            case R.id.activity_game_start_button:
                mIntent.putExtra("pictureId", pictureId);
                startActivity(mIntent);
                break;
            case R.id.activity_game_start_back:
                finish();
                break;
        }
    }

    private void choosePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Utility.REQUEST_CHOOSE_PICTURE_CODE);
    }

    static public Intent getIntent(Context context) {
        return new Intent(context, GameStartActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utility.REQUEST_CHOOSE_PICTURE_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = ClipPictureActivity.getIntent(GameStartActivity.this);
                intent.setData(data.getData());
                startActivity(intent);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onPictureClipFinish(PictureClipFinishEvent event) {
        mChooseFromAlbum.removeAllViews();
        ImageView clipPicture = new ImageView(GameStartActivity.this);
        clipPicture.setImageBitmap(event.getBitmap());
        mChooseFromAlbum.addView(clipPicture);
        resetSelected();
        setSelected((LinearLayout) mChooseFromAlbum.getParent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void resetSelected() {
        if (mSelected != null) {
            mSelected.setBackgroundResource(0);
        }
    }

    void setSelected(LinearLayout selected) {
        mSelected = selected;
        mSelected.setBackgroundResource(R.drawable.blue_shadow);
    }
}
