package com.example.puzzle.widget;

/**
 * Created by Jiang太白 on 2018/7/4.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.puzzle.R;

public class Pausedialog extends Dialog implements android.view.View.OnClickListener {

    private Context context;
    private TextView txt;

    private Button retu;
    private Button again;
    private Button goon;
    private Pausedialog.LeaveMyDialogListener listener;

    public interface LeaveMyDialogListener{
        void onClick(View view);
    }

    public Pausedialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public Pausedialog(Context context, int theme, Pausedialog.LeaveMyDialogListener listener) {
        super(context,theme);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.pause);

        goon = findViewById(R.id.goon);
        retu = findViewById(R.id.retu);
        again = findViewById(R.id.again);
        goon.setOnClickListener(this);
        retu.setOnClickListener(this);
        again.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        listener.onClick(v);
    }
}

