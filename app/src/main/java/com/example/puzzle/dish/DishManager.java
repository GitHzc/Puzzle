package com.example.puzzle.dish;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.puzzle.PuzzleApplication;
import com.example.puzzle.event.DragStartEvent;
import com.example.puzzle.event.GameSuccessEvent;
import com.example.puzzle.event.PieceMoveSuccessEvent;
import com.example.puzzle.utils.DensityUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * puzzle
 * 拼盘管理类，用于更新拼盘的显示
 * Created by ZQ on 2016/4/3.
 */
public class DishManager{

    private static final String TAG = "puzzle";

    //拼盘
    private ImageView mImageView;
    //游戏难度
    private int mLevel;
    //拼图图片
    private Bitmap mBitmap;
    //拼图数组
    private boolean[] mIndex;
    //拼块数目
    private int mSize;
    //距离游戏胜利还需移动的拼块数目
    private int mLeftSize;
    //拼盘大小
    private static int DISH_HEIGHT = 300;
    private static int DISH_WIDTH = 300;

    //基本款遮罩拼块
    //private static Bitmap[] bmcover = new Bitmap[9];
    public static final int COVER_CENTER = 0;
    public static final int COVER_TOP = 1;
    public static final int COVER_BOTTOM = 2;
    public static final int COVER_LEFT = 3;
    public static final int COVER_RIGHT = 4;
    public static final int COVER_TOP_LEFT = 5;
    public static final int COVER_TOP_RIGHT = 6;
    public static final int COVER_BOTTOM_LEFT = 7;
    public static final int COVER_BOTTOM_RIGHT = 8;
    //拼块凹凸半径
    private static float r;
    //拼块内的矩形长宽
    private static float rectWidth;
    private static float rectHeight;
    //拼块的长宽
    static float width;
    static float height;

    private boolean firstDrag;

    public DishManager(int level){
        mLevel = level;
        mSize = level * level;
        mLeftSize = mSize;
        mIndex = new boolean[mSize];
        for(int i = 0; i < mSize; i++) mIndex[i] = false;

        p = new Paint();
        p.setColor(Color.RED);

        pOut = new Paint();
        pOut.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        pOut.setAntiAlias(true);
        pOut.setColor(Color.RED);

        pOver = new Paint();
        pOver.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        pOver.setAntiAlias(true);
        pOver.setColor(Color.RED);
        
        initMask();
    }

    /**
     * 游戏结束后调用，释放资源
     */
    public void recycle(){
        if(mImageView != null) {
            mImageView = null;
        }
        if(mBitmap != null && !mBitmap.isRecycled()){
            mBitmap.recycle();
        }

    }

    /**
     * 初始化新游戏
     * @param bitmap 新游戏图片
     * @param imageView 新游戏的拼盘
     */
    public void initNewGame(Bitmap bitmap, ImageView imageView){
        firstDrag = true;
        if(bitmap == null || imageView == null) return;

        if(mBitmap != null && !mBitmap.isRecycled()){
            mBitmap.recycle();
        }

        mImageView = imageView;
        mBitmap = bitmap;
        mLeftSize = mSize;
        for(int i = 0; i < mIndex.length; i++){
            mIndex[i] = false;
        }

        imageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (firstDrag) {
                    EventBus.getDefault().post(new DragStartEvent());
                    firstDrag = false;
                }
                int x = (int) event.getX();
                int y = (int) event.getY();
                int index = (int) event.getLocalState();

                switch (event.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:

                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:

                        break;
                    case DragEvent.ACTION_DRAG_EXITED:

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:

                        break;
                    case DragEvent.ACTION_DROP:

                        if(judgeDrag(index, x, y)){
                            updatePiece(index);
                        }

                        break;
                    default:
                        break;
                }

                return true;
            }
        });

        refreshDish();
    }

    public void setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
    }

    public void setLevel(int level){
        mLevel = level;
    }

    public static Bitmap getCover(int index){
        Bitmap bitmap = Bitmap.createBitmap(
                    (int) width,
                    (int) height,
                    Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        switch (index){
            case COVER_TOP_LEFT:
                canvas.drawRect(0, 0, rectWidth, rectHeight, p);
                canvas.drawArc(ovalRight, 0, 360, true, pOut);
                canvas.drawArc(ovalBottom, 0, 360, true, pOver);
                break;
            case COVER_TOP:
                canvas.drawRect(r, 0, r + rectWidth, rectHeight, p);
                canvas.drawArc(ovalLeft, 0, 360, true, pOver);
                canvas.drawArc(ovalBottomRight, 0, 360, true, pOver);
                canvas.drawArc(ovalRightRight, 0, 360, true, pOut);
                break;
            case COVER_TOP_RIGHT:
                canvas.drawRect(r, 0, r + rectWidth, rectHeight, p);
                canvas.drawArc(ovalLeft, 0, 360, true, pOver);
                canvas.drawArc(ovalBottomRight, 0, 360, true, pOver);
                break;
            case COVER_LEFT:
                canvas.drawRect(0, 0, rectWidth, rectHeight, p);
                canvas.drawArc(ovalBottom, 0, 360, true, pOver);
                canvas.drawArc(ovalTop, 0, 360, true, pOut);
                canvas.drawArc(ovalRight, 0, 360, true, pOut);
                break;
            case COVER_CENTER:
                canvas.drawRect(r, 0, r + rectWidth, rectHeight, p);
                canvas.drawArc(ovalTopRight, 0, 360, true, pOut);
                canvas.drawArc(ovalRightRight, 0, 360, true, pOut);
                canvas.drawArc(ovalLeft, 0, 360, true, pOver);
                canvas.drawArc(ovalBottomRight, 0, 360, true, pOver);
                break;
            case COVER_BOTTOM_LEFT:
                canvas.drawRect(0, 0, rectWidth, rectHeight, p);
                canvas.drawArc(ovalTop, 0, 360, true, pOut);
                canvas.drawArc(ovalRight, 0, 360, true, pOut);
                break;
            case COVER_BOTTOM:
                canvas.drawRect(r, 0, rectWidth + r, rectHeight, p);
                canvas.drawArc(ovalLeft, 0, 360, true, pOver);
                canvas.drawArc(ovalTopRight, 0, 360, true, pOut);
                canvas.drawArc(ovalRightRight, 0, 360, true, pOut);
                break;
            case COVER_BOTTOM_RIGHT:
                canvas.drawRect(r, 0, rectWidth + r, rectHeight, p);
                canvas.drawArc(ovalLeft, 0, 360, true, pOver);
                canvas.drawArc(ovalTopRight, 0, 360, true, pOut);
                break;
            case COVER_RIGHT:
                canvas.drawRect(r, 0, rectWidth + r, rectHeight, p);
                canvas.drawArc(ovalTopRight, 0, 360, true, pOut);
                canvas.drawArc(ovalLeft, 0, 360, true, pOver);
                canvas.drawArc(ovalBottomRight, 0, 360, true, pOver);
        }

        canvas.save();
        return bitmap;
    }

    /**
     * 判断拼块是否被拖动到正确的位置
     * @param i 拼块下标
     * @param x 拖动事件x坐标
     * @param y 拖动事件y坐标
     * @return 判断结果
     */
    public boolean judgeDrag(int i, int x, int y){
        RectF rect = getRectF(i);
        return rect.contains(x, y);
    }

    /**
     * 根据拼块下标计算拼块实际占据的方形大小
     * @param i 拼块下标
     * @return 拼块实际占据的方形大小
     */
    private RectF getRectF(int i){

        return new RectF(
                (i % mLevel) * rectWidth,
                (i / mLevel) * rectHeight,
                (i % mLevel + 1) * rectWidth,
                (i / mLevel + 1) * rectHeight);
    }

    /**
     * 当玩家将拼块移动到正确位置时调用，更新拼盘显示状态
     * @param i 正确的拼块下标
     */
    public void updatePiece(int i){
        if(i < 0 || i > mLevel * mLevel) return;

        mIndex[i] = true;
        refreshDish();
        EventBus.getDefault().post(new PieceMoveSuccessEvent(i));
        mLeftSize--;
        if(mLeftSize == 0){
            EventBus.getDefault().post(new GameSuccessEvent());
        }
    }

    /**
     * 根据拼盘大小和游戏难度初始化基本遮罩拼块
     */
    private void initMask(){

        rectWidth = DensityUtil.dip2px(PuzzleApplication.getAppContext(), (float) DISH_WIDTH / mLevel);
        rectHeight = DensityUtil.dip2px(PuzzleApplication.getAppContext(), (float) DISH_HEIGHT / mLevel);
        r = rectWidth / 4;

        Log.d(TAG, "rectWidth and Height: " + rectWidth);

        width = rectWidth + r;
        height = rectHeight + r;

        ovalLeft = new RectF(0, rectHeight / 2 - r, 2 * r, rectHeight / 2 + r);
        ovalTop = new RectF(rectWidth / 2 - r, - r, rectWidth / 2 + r, r);
        ovalTopRight = new RectF(rectWidth / 2, - r, rectWidth / 2 + 2 * r, r);
        ovalRight = new RectF(rectWidth - r, rectHeight / 2 - r, rectWidth + r, rectHeight / 2 + r);
        ovalBottom = new RectF(rectWidth / 2 - r, rectHeight - r, rectWidth / 2 + r, rectHeight + r);
        ovalRightRight = new RectF(rectWidth, rectHeight / 2 - r, rectWidth + 2 * r, rectHeight / 2 + r);
        ovalBottomRight = new RectF(rectWidth / 2, rectHeight - r, rectWidth / 2 + 2 * r, rectHeight + r);
    }
    private static Paint p, pOut, pOver;
    private static RectF ovalLeft, ovalTop, ovalTopRight, ovalRight, ovalBottom, ovalRightRight, ovalBottomRight;

    /**
     * 根据当前游戏进度生成遮罩层
     * @return 遮罩层Bitmap
     */
    private Bitmap getMask(){
        int width = DensityUtil.dip2px(PuzzleApplication.getAppContext(), (float) DISH_WIDTH);
        int height = DensityUtil.dip2px(PuzzleApplication.getAppContext(), (float) DISH_HEIGHT);
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.RED);

        for(int i = 0; i < mSize; i += mLevel){
            for(int j = i; j < i + mLevel; j++){
                if(!mIndex[j]) continue;

                Bitmap cover;

                //最右一列
                if((j + 1) % mLevel == 0) {
                    //右上角
                    if(j == mLevel - 1) {
                        cover = getCover(COVER_TOP_RIGHT);
                        canvas.drawBitmap(cover, rectWidth * (mLevel - 1) - r, 0, p);
                    }
                        //右下角
                    else if (j == mSize - 1){
                        cover = getCover(COVER_BOTTOM_RIGHT);
                        canvas.drawBitmap(
                                cover,
                                rectWidth * (mLevel - 1) - r,
                                rectHeight * (mLevel - 1), p);
                    }
                    else{
                        cover = getCover(COVER_RIGHT);
                        canvas.drawBitmap(cover,
                                rectWidth * (mLevel - 1) - r,
                                rectHeight * (i / mLevel), p);
                    }

                }
                //最左一列
                else if(j % mLevel == 0) {
                    //左上角
                    if(j == 0){
                        cover = getCover(COVER_TOP_LEFT);
                        canvas.drawBitmap(cover, 0, 0, p);
                    }
                    //左下角
                    if(j == mSize - mLevel + 1){
                        cover = getCover(COVER_BOTTOM_LEFT);
                        canvas.drawBitmap(cover,
                                0,
                                rectHeight * (mLevel - 1), p);
                    }

                    else{
                        cover = getCover(COVER_LEFT);
                        canvas.drawBitmap(cover,
                                0,
                                rectHeight * (i / mLevel), p);
                    }
                }
                //最上一行
                else if(j < mLevel){
                    cover = getCover(COVER_TOP);
                    canvas.drawBitmap(cover,
                            rectWidth * j - r,
                            0, p);
                }
                //最下一行
                else if(j > (mSize - mLevel)){
                    cover = getCover(COVER_BOTTOM);
                    canvas.drawBitmap(cover,
                            rectWidth * (j % mLevel) - r,
                            rectHeight * (mLevel - 1), p);
                }
                //中间
                else{
                    cover = getCover(COVER_CENTER);
                    canvas.drawBitmap(cover,
                            rectWidth * (j % mLevel) - r,
                            rectHeight * (i / mLevel), p);
                }

                cover.recycle();

            }

        }

        canvas.save();

        return mask;
    }

    private Bitmap output;
    private Bitmap mask;
    /**
     * 刷新拼盘显示
     * 首先获取遮罩层，然后将遮罩层与原始图片混合并显示
     */
    private void refreshDish(){
        if(mBitmap == null) return;
        if(mask != null) mask.recycle();
        if(output != null) output.recycle();
        //获取遮罩层
        mask = getMask();

        //将遮罩层与原始图片混合并显示在ImageView
        int width = DensityUtil.dip2px(PuzzleApplication.getAppContext(), (float) DISH_WIDTH);
        int height = DensityUtil.dip2px(PuzzleApplication.getAppContext(), (float) DISH_HEIGHT);
        output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Paint p = new Paint();

        p.setAntiAlias(true);
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(mask, 0, 0, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        RectF rect = new RectF(0, 0, width, height);
        canvas.drawBitmap(mBitmap, null, rect, p);
        mImageView.setImageBitmap(output);
    }
}
