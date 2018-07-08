package com.example.puzzle.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.puzzle.PuzzleApplication;
import com.example.puzzle.R;
import com.example.puzzle.event.PictureClipFinishEvent;
import com.example.puzzle.model.PhotoBean;
import com.example.puzzle.utils.HttpUtils;
import com.example.puzzle.utils.Utility;
import com.example.puzzle.widget.ChooseDifficultyFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

import static com.example.puzzle.utils.Utility.savePicture;

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
    @BindView(R.id.activity_game_start_picture4)
    ImageView mPicture4;
    @BindView(R.id.activity_game_start_picture5)
    ImageView mPicture5;
    @BindView(R.id.activity_game_start_level)
    TextView mLevel;
    Intent mIntent;
    int pictureIndex;
    LinearLayout mSelected = null;
    ArrayList<String> mPicturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setLevel();
        if ((mPicturePath = PuzzleApplication.getPicturePath()) != null) {
            setPicture();
        } else {
            mPicturePath = new ArrayList<>();
            pictureIndex = 0;
            getPhotos();
        }
    }

    void setLevel() {
        String[] levels = getResources().getStringArray(R.array.difficulty);
        mLevel.setText(levels[PuzzleApplication.getLevel() - 3]);
    }

    void setPicture() {
        File f1 = new File(mPicturePath.get(0));
        mPicture1.setImageURI(Uri.fromFile(f1));
        File f2 = new File(mPicturePath.get(1));
        mPicture2.setImageURI(Uri.fromFile(f2));
        File f3 = new File(mPicturePath.get(2));
        mPicture3.setImageURI(Uri.fromFile(f3));
        File f4 = new File(mPicturePath.get(3));
        mPicture4.setImageURI(Uri.fromFile(f4));
        File f5 = new File(mPicturePath.get(4));
        mPicture5.setImageURI(Uri.fromFile(f5));
    }

    @OnClick({
            R.id.activity_game_start_picture1, R.id.activity_game_start_picture2,
            R.id.activity_game_start_picture3, R.id.activity_game_start_picture4,
            R.id.activity_game_start_picture5, R.id.activity_game_start_choose_from_album,
            R.id.activity_game_start_choose_difficulty, R.id.activity_game_start_button,
            R.id.activity_game_start_back
    })
    void onClickPicture(View view) {
        mIntent = GameActivity.getIntent(GameStartActivity.this);
        switch (view.getId()) {
            case R.id.activity_game_start_picture1:
                resetSelected();
                setSelected((LinearLayout) mPicture1.getParent());
                pictureIndex = 0;
                break;
            case R.id.activity_game_start_picture2:
                resetSelected();
                setSelected((LinearLayout) mPicture2.getParent());
                pictureIndex = 1;
                break;
            case R.id.activity_game_start_picture3:
                resetSelected();
                setSelected((LinearLayout) mPicture3.getParent());
                pictureIndex = 2;
                break;
            case R.id.activity_game_start_picture4:
                resetSelected();
                setSelected((LinearLayout) mPicture4.getParent());
                pictureIndex = 3;
                break;
            case R.id.activity_game_start_picture5:
                resetSelected();
                setSelected((LinearLayout) mPicture5.getParent());
                pictureIndex = 4;
                break;
            case R.id.activity_game_start_choose_from_album:
                choosePicture();
                pictureIndex = 5;
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
                dialog.show(manager, "singleDialog");
                break;
            case R.id.activity_game_start_button:
                mIntent.putExtra("picturePath", mPicturePath.get(pictureIndex));
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
        mPicturePath.add(event.getPicturePath());
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
        PuzzleApplication.setPicturePath(mPicturePath);
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

    private void getPhotos() {
        Retrofit retrofit = HttpUtils.getRetrofit();
        final HttpUtils.Myapi api = retrofit.create(HttpUtils.Myapi.class);
        api.getPictureUrl()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<PhotoBean, Observable<PhotoBean.ContentBean>>() {
                    @Override
                    public Observable<PhotoBean.ContentBean> apply(PhotoBean photoBean) throws Exception {
                        return Observable.fromIterable(photoBean.getContent());
                    }
                })
                .flatMap(new Function<PhotoBean.ContentBean, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(PhotoBean.ContentBean contentBean) throws Exception {
                        return api.getPicture(contentBean.getSrc());
                    }
                })
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        String name = "picture" + (pictureIndex + 1) + ".jpg";
                        String path = savePicture(GameStartActivity.this, name, responseBody);
                        if (path != null) {
                            mPicturePath.add(path);
                            pictureIndex++;
                        }
                        return path;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        File file = new File(s);
                        switch (s.charAt(s.length() - 5)) {
                            case '1':
                                mPicture1.setImageURI(Uri.fromFile(file));
                                break;
                            case '2':
                                mPicture2.setImageURI(Uri.fromFile(file));
                                break;
                            case '3':
                                mPicture3.setImageURI(Uri.fromFile(file));
                                break;
                            case '4':
                                mPicture4.setImageURI(Uri.fromFile(file));
                                break;
                            case '5':
                                mPicture5.setImageURI(Uri.fromFile(file));
                                break;
                        }
                    }
                });
    }
}
