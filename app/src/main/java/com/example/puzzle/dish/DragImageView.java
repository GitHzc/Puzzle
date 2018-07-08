package com.example.puzzle.dish;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.puzzle.R;

import butterknife.BindView;


/**
 * puzzle
 */
public class DragImageView extends ImageView {

    @BindView(R.id.right)
    Button right;
    @BindView(R.id.original)
    Button original;
    @BindView(R.id.left)
    Button left;


    private int mIndex;

    public DragImageView(Context context) {
        super(context);
        init(context);
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.startDrag(null, new View.DragShadowBuilder(v), mIndex, 0);
                return true;
            }
        });

    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }
}
