package com.example.puzzle.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.puzzle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainPageActivity extends AppCompatActivity {
    @BindView(R.id.activity_main_page_game_start)
    Button mGameStartButton;
    @BindView(R.id.activity_main_page_rank)
    Button mRankButton;
    @BindView(R.id.activity_main_page_personal)
    Button mPersonalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.activity_main_page_game_start, R.id.activity_main_page_rank, R.id.activity_main_page_personal})
    void onClick(View view) {
        switch(view.getId()) {
            case R.id.activity_main_page_game_start:
                Intent intent = GameStartActivity.getIntent(MainPageActivity.this);
                startActivity(intent);
                break;
            case R.id.activity_main_page_rank:
                break;
            case R.id.activity_main_page_personal:
                break;
        }
    }

    static public Intent getIntent(Context context) {
        return new Intent(context, MainPageActivity.class);
    }
}
