package com.example.puzzle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.puzzle.PuzzleApplication;
import com.example.puzzle.R;
import com.example.puzzle.bgm.MusicServer;
import com.example.puzzle.dish.DishManager;
import com.example.puzzle.dish.DragImageView;
import com.example.puzzle.event.DishManagerInitFinishEvent;
import com.example.puzzle.event.DragStartEvent;
import com.example.puzzle.event.GameSuccessEvent;
import com.example.puzzle.event.PieceMoveSuccessEvent;
import com.example.puzzle.event.TimeEvent;
import com.example.puzzle.imagesplit.ImagePiece;
import com.example.puzzle.imagesplit.ImageSplitter;
import com.example.puzzle.model.SubmitScoreBean;
import com.example.puzzle.utils.BitmapUtils;
import com.example.puzzle.utils.DensityUtil;
import com.example.puzzle.utils.GameTimer;
import com.example.puzzle.utils.GlobalUtils;
import com.example.puzzle.utils.HttpUtils;
import com.example.puzzle.widget.Pausedialog;
import com.example.puzzle.widget.Windialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class GameActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dish)
    ImageView dish;
    @BindView(R.id.layViewContainer)
    GridLayout layViewContainer;
    @BindView(R.id.gameContainer)
    LinearLayout gameContainer;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.viewContainer)
    ScrollView viewContainer;
    @BindView(R.id.gameview)
    RelativeLayout gameview;
    @BindView(R.id.pa)
    ImageView pa;
    @BindView(R.id.puzzleHint)
    TextView puzzleHint;

    private AlertDialog alertDialog = null;
    private AlertDialog.Builder dialogBuilder = null;
    private Context mContext;

    private StaticHandler timeHandler = new StaticHandler(GameActivity.this);
    private GameTimer gameTimer;
    private int time = 0;
    private String startTime;

    private DishManager dm;
    private Bitmap mBitmap;

    private Windialog dialogwin;
    private Pausedialog dialogpause;

    private Map<String, String> mapright = new HashMap<String, String>();
    private Map<String, String> mapleft = new HashMap<String, String>();
    private int left = 0;
    private int right = 0;
    private int rl = 1;
    private Stack stack = new Stack();
    private int st = 0;

    private final int DISH_WIDTH = 300;
    private final int DISH_HEIGHT = 300;
    private HashMap<Integer, View> pieceList = new HashMap<>();

    private List<ImagePiece> IPL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);
        gameTimer = new GameTimer(timeHandler);

        mContext = this;

        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pause();
            }
        });

        initialization();
        EventBus.getDefault().post(new DishManagerInitFinishEvent());

        final Button r = findViewById(R.id.right);
        final Button l = findViewById(R.id.left);
        final Button o = findViewById(R.id.original);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right = 1;
                left = 0;
                rl = 0;
                view.setBackgroundResource(R.drawable.right1);
                o.setBackgroundResource(R.drawable.original1);
                l.setBackgroundResource(R.drawable.left);
            }
        });


        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right = 0;
                left = 1;
                rl = 0;
                view.setBackgroundResource(R.drawable.left1);
                o.setBackgroundResource(R.drawable.original1);
                r.setBackgroundResource(R.drawable.right);
            }
        });

        o.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        o.setBackgroundResource(R.drawable.original1);
                        alertDialog.dismiss();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        o.setBackgroundResource(R.drawable.original);
                        r.setBackgroundResource(R.drawable.right);
                        l.setBackgroundResource(R.drawable.left);
                        yuantutu();
                        break;
                }
                return true;
            }
        });
    }

    private void yuantutu() {
        dialogBuilder = new AlertDialog.Builder(mContext, R.style.dialog);
        alertDialog = dialogBuilder
                .setView(R.layout.yuantu)
                .create();
        alertDialog.show();
        ImageView im = alertDialog.findViewById(R.id.yuan);
        im.setImageBitmap(mBitmap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TimeEvent event) {
        time++;

        int curminute = time / 60;
        int cursecond = time % 60;

        String curTimeString = String.format("%02d:%02d", curminute, cursecond);
        timeText.setText(curTimeString);

        if (st == 1) {
            while (!stack.empty()) {
                ImageView v = findViewById((int) stack.pop());
                Matrix matrix = new Matrix();
                matrix.postRotate((float) 90, v.getWidth() / 2, v.getHeight() / 2);
                v.setImageMatrix(matrix);
            }
            st = 2;
        }
        if (st == 0) st = 1;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(GameActivity.this, MusicServer.class);
        startService(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(GameActivity.this, MusicServer.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }

        dm.recycle();

        if (IPL != null) {
            for (int i = 0; i < IPL.size(); i++) {
                IPL.get(i).recycleBitmap();
            }
            IPL.clear();
            IPL = null;
        }

        if (gameTimer != null) {
            gameTimer.recycle();
            gameTimer = null;
        }

        System.gc();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PieceMoveSuccessEvent event) {
        int index = event.getIndex();
        DragImageView view = (DragImageView) pieceList.get(index);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GameSuccessEvent event) {
        gameTimer.stopTimer();
        showSuccess();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DragStartEvent event) {
        puzzleHint.setVisibility(View.GONE);
    }

    private void showSuccess() {
        gameview.setVisibility(View.GONE);
        pa.setImageResource(R.drawable.star);
        dialogwin = new Windialog(mContext, R.style.dialog,
                new Windialog.LeaveMyDialogListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.returnButton:
                                Intent intent = MainPageActivity.getIntent(GameActivity.this);
                                startActivity(intent);
                                dialogwin.dismiss();
                                finish();
                            case R.id.againButton:
                                dialogwin.dismiss();
                                finish();
                            default:
                                break;
                        }
                    }
                });

        Window window = dialogwin.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.5f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogwin.setCanceledOnTouchOutside(false);
        dialogwin.show();
        submitScore();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DishManagerInitFinishEvent event) {
        gameTimer.startTimer();
    }

    private void initialization() {
        Log.d(TAG, "init begin");
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        startTime = simpleDateFormat.format(now);
        layViewContainer.removeAllViews();
        pieceList.clear();

        String picturePath = getIntent().getStringExtra("picturePath");
        mBitmap = BitmapUtils.decodeSampleBitmapFromPath(picturePath, DISH_WIDTH, DISH_HEIGHT);

        dm = PuzzleApplication.getDishManager();

        if (dm == null) return;

        dm.initNewGame(mBitmap, dish);

        Log.d(TAG, "DishManager init finish");

        try {

            // 裁剪算法优化基本完成，尚有几像素的偏差，可能是int到float强制转换的精度损失
            int dishWidth = DensityUtil.dip2px(PuzzleApplication.getAppContext(), DISH_WIDTH);
            int dishHeight = DensityUtil.dip2px(PuzzleApplication.getAppContext(), DISH_HEIGHT);
            Bitmap tempBitmap = BitmapUtils.createNoRecycleScaleBitmap(
                    mBitmap,
                    dishWidth,
                    dishHeight);
            IPL = ImageSplitter.split(tempBitmap, PuzzleApplication.getLevel(),
                    dishWidth,
                    dishHeight);
            tempBitmap.recycle();
            Log.d(TAG, "split finish");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 10);

            int mLevel = PuzzleApplication.getLevel();
            layViewContainer.setColumnCount(mLevel);

            for (int i = 0; i < mLevel; i++) {
                for (int j = 0; j < mLevel; j++) {
                    DragImageView imageView = new DragImageView(this);
                    imageView.setLayoutParams(layoutParams);

                    imageView.setScaleType(ImageView.ScaleType.MATRIX);   //required

                    imageView.setImageBitmap(IPL.get(j + i * mLevel).bitmap);

                    imageView.setIndex(j + i * mLevel);

                    int id = View.generateViewId();
                    mapright.put(String.valueOf(id), "0");
                    mapleft.put(String.valueOf(id), "0");

                    imageView.setId(id);

                    pieceList.put(imageView.getIndex(), imageView);
                }
            }

            int[] list = GlobalUtils.getRamdomList(mLevel * mLevel);

            if (layViewContainer != null) {
                for (final int aList : list) {

                    pieceList.get(aList).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int id = view.getId();
                            ImageView imageView = findViewById(id);
                            Matrix matrix = view.getMatrix();

                            int cright = Integer.valueOf(mapright.get(String.valueOf(id))).intValue();
                            int cleft = Integer.valueOf(mapleft.get(String.valueOf(id))).intValue();
                            if (right == 1) {
                                cright = cright + 1;
                            }
                            if (left == 1) {
                                cleft = cleft + 1;
                            }
                            if (rl == 1) {
                                cright = 0;
                                cleft = 0;
                            }

                            matrix.setRotate(90 + 90 * cright - 90 * cleft, view.getWidth() / 2, view.getHeight() / 2);
                            mapright.put(String.valueOf(view.getId()), String.valueOf(cright));
                            mapleft.put(String.valueOf(view.getId()), String.valueOf(cleft));
                            imageView.setImageMatrix(matrix);
                        }

                    });
                    layViewContainer.addView(pieceList.get(aList), layoutParams);
                    stack.push(pieceList.get(aList).getId());
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "init finish");
    }

    public void rotate(ImageView v) {
        Matrix matrix = new Matrix();
        v.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate((float) 90, 75, 75);
        v.setImageMatrix(matrix);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class StaticHandler extends Handler {

        private final WeakReference<Activity> mActivity;

        public StaticHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GameTimer.MESSAGE_TIMER:
                    EventBus.getDefault().post(new TimeEvent());
                    break;

            }
        }
    }

    private void pause() {
        gameTimer.stopTimer();

        dialogpause = new Pausedialog(mContext, R.style.dialog,
                new Pausedialog.LeaveMyDialogListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {

                            case R.id.goon:
                                dialogpause.dismiss();
                                gameTimer = new GameTimer(timeHandler);
                                EventBus.getDefault().post(new DishManagerInitFinishEvent());
                                break;

                            case R.id.retu:
                                dialogpause.dismiss();
                                finish();
                                break;

                            case R.id.again:
                                dialogpause.dismiss();
                                time = 0;
                                gameTimer = new GameTimer(timeHandler);
                                //onDestroy();
                                initialization();
                                EventBus.getDefault().post(new DishManagerInitFinishEvent());
                                break;
                            default:
                                break;
                        }
                    }
                });

        Window window = dialogpause.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.5f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialogpause.setCanceledOnTouchOutside(false);
        dialogpause.show();

        int curminute = time / 60;
        int cursecond = time % 60;

        TextView timpause = dialogpause.findViewById(R.id.timepause);
        timpause.setText(String.format("%02d:%02d", curminute, cursecond));
    }

    static public Intent getIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }

    private void submitScore() {
        Retrofit retrofit = HttpUtils.getRetrofit();
        HttpUtils.Myapi api = retrofit.create(HttpUtils.Myapi.class);
        api.submit(PuzzleApplication.getmUser().getCookie(), time, startTime, PuzzleApplication.getLevel())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<SubmitScoreBean>() {
                    @Override
                    public void accept(SubmitScoreBean submitScoreBean) throws Exception {
                        if (submitScoreBean.getError().equals("")) {
                            Log.d(TAG, "accept: " + "提交成功!");
                        }
                    }
                });
    }
}


