package com.example.puzzle.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.puzzle.PuzzleApplication;
import com.example.puzzle.R;
import com.example.puzzle.model.LoginBean;
import com.example.puzzle.model.User;
import com.example.puzzle.utils.HttpUtils;
import com.example.puzzle.utils.Utility;
import com.example.puzzle.widget.KeyboardLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.puzzle.utils.Utility.REQUEST_PERMISSION_CODE;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.activity_login_username)
    EditText mUsername;
    @BindView(R.id.activity_login_password)
    EditText mPassword;
    @BindView(R.id.activity_login_login_button)
    Button mLoginButton;
    @BindView(R.id.activity_login_register_button)
    Button mRegisterButton;
    @BindView(R.id.error_message)
    TextView mErrorMessage;
    @BindView(R.id.close_button)
    ImageView mCloseButton;
    @BindView(R.id.error_message_layout)
    LinearLayout mLinearLayout;
    @BindView(R.id.keyboard_layout)
    KeyboardLayout mKeyboardLayout;
    @BindView(R.id.activity_login_scroll_view)
    ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getPermission();
        mKeyboardLayout.setKeyboardListener(new KeyboardLayout.KeyboardLayoutListener() {
            @Override
            public void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
                if (isActive) {
                    scrollToBottom();
                }
            }
        });
        setUsernameAndPassword();
    }

    private void scrollToBottom() {
        mScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollTo(0, mScrollView.getBottom());
            }
        }, 100);
    }

    @OnClick({R.id.activity_login_login_button, R.id.activity_login_register_button, R.id.close_button})
    public void onButtonClick(View view) {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        switch (view.getId()) {
            case R.id.close_button:
                mLinearLayout.setVisibility(View.GONE);
                break;
            case R.id.activity_login_login_button:
                if (username.equals("") || password.equals("")) {
                    mErrorMessage.setText("用户名或密码不能为空");
                    mLinearLayout.setVisibility(View.VISIBLE);
                    return;
                }
                login(username, password);
                break;
            case R.id.activity_login_register_button:
                if (username.equals("") || password.equals("")) {
                    mErrorMessage.setText("用户名或密码不能为空");
                    mLinearLayout.setVisibility(View.VISIBLE);
                    return;
                }
                register(username, password);
        }
    }

    private void login(final String username, final String password) {
        Retrofit retrofit = HttpUtils.getRetrofit();
        HttpUtils.Myapi api = retrofit.create(HttpUtils.Myapi.class);
        api.login(username, password, "login")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LoginBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response<LoginBean> response) {
                        LoginBean loginBean = response.body();
                        if (loginBean.getError().equals("")) {
                            User user = new User(username, password, response.headers().get("Set-Cookie"));
                            PuzzleApplication.setmUser(user);
                            Intent intent = MainPageActivity.getIntent(LoginActivity.this);
                            startActivity(intent);
                        } else {
                            mLinearLayout.setVisibility(View.VISIBLE);
                            mErrorMessage.setText(loginBean.getError());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void register(final String username, final String password) {
        Retrofit retrofit = HttpUtils.getRetrofit();
        HttpUtils.Myapi api = retrofit.create(HttpUtils.Myapi.class);
        api.register(username, password, "register")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        if (loginBean.getError().equals("")) {
                            mLinearLayout.setVisibility(View.GONE);
                            login(username, password);
                        } else {
                            mLinearLayout.setVisibility(View.VISIBLE);
                            mErrorMessage.setText(loginBean.getError());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void getPermission() {
        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }
        if (!permissionList.isEmpty()) {
            permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LoginActivity.this, permissions, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Utility.REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        finish();
                    }
                }
            }
        }
    }

    static public Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private void saveUsernameAndPassword() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", mUsername.getText().toString());
        editor.putString("password", mPassword.getText().toString());
        editor.apply();
    }

    private void setUsernameAndPassword() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        mUsername.setText(sharedPreferences.getString("username", ""));
        mPassword.setText(sharedPreferences.getString("password", ""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveUsernameAndPassword();
    }
}
