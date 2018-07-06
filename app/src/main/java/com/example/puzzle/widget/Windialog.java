package com.example.puzzle.widget;

/**
 * Created by Jiang太白 on 2018/7/4.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.puzzle.R;


public class Windialog extends Dialog implements android.view.View.OnClickListener {

    private Context context;
    private TextView txt;
    private ImageButton re;

    private Windialog.LeaveMyDialogListener listener;

    public interface LeaveMyDialogListener{
        public void onClick(View view);
    }

    public Windialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public Windialog(Context context, int theme, Windialog.LeaveMyDialogListener listener) {
        super(context,theme);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.game_success);

        re   = (ImageButton) findViewById(R.id.againButton);

        re.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        listener.onClick(v);
    }
}

