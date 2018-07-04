package com.example.puzzle.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.puzzle.R;
import com.example.puzzle.utils.Utility;
import com.example.puzzle.widget.ChooseDifficultyFragment;

import org.w3c.dom.Text;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);
        ButterKnife.bind(this);
    }

    @OnClick({
                R.id.activity_game_start_picture1, R.id.activity_game_start_picture2,
                R.id.activity_game_start_picture3, R.id.activity_game_start_choose_from_album,
                R.id.activity_game_start_choose_difficulty, R.id.activity_game_start_button,
                R.id.activity_game_start_back
    })
    void onClickPicture(View view) {
        switch(view.getId()) {
            case R.id.activity_game_start_picture1:
                break;
            case R.id.activity_game_start_picture2:
                break;
            case R.id.activity_game_start_picture3:
                break;
            case R.id.activity_game_start_choose_from_album:
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
                    }
                });
                dialog.show(manager,"singleDialog");
                break;
            case R.id.activity_game_start_button:
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
}
