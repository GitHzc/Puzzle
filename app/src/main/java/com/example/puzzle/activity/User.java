package com.example.puzzle.activity;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.puzzle.R;
import com.example.puzzle.model.LogoutBean;
import com.example.puzzle.model.RankBean;
import com.example.puzzle.model.UserInfoBean;
import com.example.puzzle.utils.HttpUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

class User extends AppCompatActivity implements View.OnClickListener{
    private TextView user_rank;
    private TextView user_name;
    private ImageButton back_button;
    private Button history_button;
    private Button logout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user_rank = (TextView) findViewById(R.id.user_rank);
        user_name = (TextView) findViewById(R.id.user_name);
        back_button = (ImageButton) findViewById(R.id.user_back_button);
        history_button = (Button) findViewById(R.id.user_history_button);
        logout_button = (Button) findViewById(R.id.user_logout_button);

        back_button.setOnClickListener(this);
        history_button.setOnClickListener(this);
        logout_button.setOnClickListener(this);

        //初始化用户信息：用户名 排名
        Retrofit retrofit = HttpUtils.getRetrofit();
        HttpUtils.Myapi api = retrofit.create(HttpUtils.Myapi.class);
        api.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        List<UserInfoBean.ContentBean> contentBeans = userInfoBean.getContent();
                        if(contentBeans != null){
                            UserInfoBean.ContentBean contentBean = contentBeans.get(0);
                            String username = contentBean.getUsername();
                            String rank = contentBean.getRank();
                            user_name.setText(username);
                            user_rank.setText(rank);
                        }
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_back_button:
                User.this.finish();
                break;
            case R.id.user_history_button:
                Intent intentHistory = new Intent(User.this, History.class);
                startActivity(intentHistory);
                break;
            case R.id.user_logout_button:
                //登出
                Retrofit retrofit = HttpUtils.getRetrofit();
                HttpUtils.Myapi api = retrofit.create(HttpUtils.Myapi.class);
                api.logout("logout")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LogoutBean>() {
                            @Override
                            public void onSubscribe(Disposable d) { }

                            @Override
                            public void onNext(LogoutBean logoutBean) {
                                if (logoutBean.getError().equals("")) {
                                    //跳转到登陆界面
                                    Intent intent = new Intent(User.this, LoginActivity.class);
                                    startActivity(intent);
                                }else return;
                            }

                            @Override
                            public void onError(Throwable e) { }

                            @Override
                            public void onComplete() { }
                        });
                break;
            default:
                break;
        }
    }
}
