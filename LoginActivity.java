package com.app.motiv.ui.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.motiv.R;
import com.app.motiv.ui.authenticate.base.BaseAuthenticateActivity;
import com.app.motiv.ui.main.eventOrganizer.ChangePwdOrgActivity;
import com.app.motiv.ui.main.eventOrganizer.createEventOrg.CEImgOrgActivity;
import com.app.motiv.ui.main.eventOrganizer.editProfileOrg.EditProfileOrgActivity;
import com.app.motiv.ui.main.eventOrganizer.eventDetailsOrg.EventDetailsOrgActivity;
import com.app.motiv.ui.main.eventOrganizer.eventNameOrg.EventNameOrgActivity;
import com.app.motiv.ui.main.eventOrganizer.helpOrg.HelpOrgActivity;
import com.app.motiv.ui.main.eventOrganizer.homeOrg.MotivOrgActivity;
import com.app.motiv.ui.main.eventOrganizer.settingOrg.SettingOrgActivity;
import com.app.motiv.ui.main.eventOrganizer.termCondOrg.TermCondOrgActivity;
import com.app.motiv.utils.Constants;
import com.app.motiv.utils.ToastUtils;
import com.app.motiv.utils.Validators;
import com.app.motiv.utils.helpers.FacebookHelper;
import com.app.motiv.utils.helpers.SharedPreferenceHelper;
import com.app.motiv.utils.instagram.ApplicationData;
import com.app.motiv.utils.instagram.InstagramApp;
import com.facebook.FacebookException;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

public class LoginActivity extends BaseAuthenticateActivity implements TextWatcher, FacebookHelper.FacebookHelperCallback {

    @BindView(R.id.et_email)
    public EditText etEmail;
    @BindView(R.id.et_pwd)
    public EditText etPwd;
    @BindView(R.id.tv_forgot_pwd)
    public TextView tvForgotPwd;
    @BindView(R.id.btn_login)
    public Button btnLogin;
    @BindView(R.id.tv_skip_login)
    public TextView tvSkipLogin;
    @BindView(R.id.iv_facebook)
    public ImageView ivFacebook;
    @BindView(R.id.iv_twitter)
    public ImageView ivTwitter;
    @BindView(R.id.twitter_login_btn)
    public TwitterLoginButton twitterLoginButton;
    @BindView(R.id.iv_instagram)
    public ImageView ivInstagram;
    @BindView(R.id.tv_register)
    public TextView tvRegister;

    @BindView(R.id.rl_email)
    public RelativeLayout rlEmail;
    @BindView(R.id.iv_email)
    public ImageView ivEmail;
    @BindView(R.id.rl_pwd)
    public RelativeLayout rlPwd;
    @BindView(R.id.iv_pwd)
    public ImageView ivPwd;

    private FacebookHelper facebookHelper;
    private InstagramApp mApp;
    private boolean isFacebookLogin, isTwitterLogin;
    Validators validators;

    String email, pwd;

    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
                ToastUtils.shortToast(userInfoHashmap.get(InstagramApp.TAG_FULL_NAME));
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                ToastUtils.longToast(Constants.NETWORK_ERROR);
            }
            return false;
        }
    });


    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initToolbar();
        initListener();
    }

    private void initViews() {

    }

    private void initToolbar() {
        facebookHelper = new FacebookHelper(this, this);
        validators = new Validators(this);
    }

    // these listener used for animation on edittext for images and background changes
    private void initListener() {
        etEmail.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
    }

    @OnClick(R.id.tv_forgot_pwd)
    public void onClickForgotPwd() {
        ForgotPwdActivity.start(this);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @OnClick(R.id.btn_login)
    public void onClickLogin() {

        email = etEmail.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        if (validators.isValidLoginData(email, pwd)) {
            MotivOrgActivity.start(this);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

    @OnClick(R.id.tv_skip_login)
    public void onClickSkipLogin() {
        MotivOrgActivity.start(this);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @OnClick(R.id.iv_facebook)
    public void onClickFacebook() {
        SharedPreferenceHelper.getInstance().saveIsFromSocial(true);  // for first time showing sign up toast
        facebookHelper.logout();
        facebookHelper.login(this);
        isFacebookLogin = true;
    }

    @OnClick(R.id.iv_twitter)
    public void onClickTwitter() {
        boolean isAppInstalled = appInstalledOrNot("com.twitter.android");
        if (isAppInstalled) {
            SharedPreferenceHelper.getInstance().saveIsFromSocial(true);  // for first time showing sign up toast
            isTwitterLogin = true;
            twitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {

                    TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = session.getAuthToken();
                    String token = authToken.token;
                    String secret = authToken.secret;

                    Call<User> user = TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(true, false, true);

                    user.enqueue(new Callback<User>() {
                        @Override
                        public void success(Result<User> result) {
                            String name = result.data.name;
                            String email = result.data.email;
                            ToastUtils.shortToast(name);
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            isTwitterLogin = false;
                            ToastUtils.longToast(getString(R.string.twitter_authentication_failed));
                        }
                    });
                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                    ToastUtils.longToast(getString(R.string.twitter_authentication_failed));
                }
            });

            twitterLoginButton.performClick();
        } else {
            ToastUtils.longToast(getString(R.string.please_install_twitter_first_to_login));
        }
    }

    @OnClick(R.id.iv_instagram)
    public void onClickInstagram() {
        SharedPreferenceHelper.getInstance().saveIsFromSocial(true);  // for first time showing sign up toast
        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);

        mApp.resetAccessToken();
        mApp.authorize();
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                mApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error) {
                ToastUtils.longToast(getString(R.string.instagram_authentication_failed));
            }
        });
    }

    @OnClick({R.id.rl_register_now, R.id.tv_register_now})
    public void onClickRegisterNow() {
        SignupSelectorActivity.start(LoginActivity.this);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (editable == etEmail.getEditableText()) {
            if (editable.length() == 0) {
                rlEmail.setBackgroundResource(R.drawable.rec_edit_text_with_stroke);
                ivEmail.setImageResource(R.mipmap.ic_email_deselected);
            } else {
                rlEmail.setBackgroundResource(R.drawable.rec_selected_edt_bg);
                ivEmail.setImageResource(R.mipmap.ic_email);
            }
        } else if (editable == etPwd.getEditableText()) {
            if (editable.length() == 0) {
                rlPwd.setBackgroundResource(R.drawable.rec_edit_text_with_stroke);
                ivPwd.setImageResource(R.mipmap.ic_password_deselected);
            } else {
                rlPwd.setBackgroundResource(R.drawable.rec_selected_edt_bg);
                ivPwd.setImageResource(R.mipmap.ic_password);
                etPwd.setTextColor(getResources().getColor(R.color.tab_selected_color));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isFacebookLogin) {
            facebookHelper.onResult(requestCode, resultCode, data);
        } else if (isTwitterLogin) {
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSuccessFacebook(Bundle bundle) {
        ToastUtils.shortToast(bundle.getString(FacebookHelper.FIRST_NAME));
    }

    @Override
    public void onCancelFacebook() {
        ToastUtils.longToast(getString(R.string.facebook_login_cancel));
        isFacebookLogin = false;
    }

    @Override
    public void onErrorFacebook(FacebookException ex) {
        ToastUtils.longToast(ex.getMessage());
        isFacebookLogin = false;
    }

    // to check any particular app is installed or not
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}
