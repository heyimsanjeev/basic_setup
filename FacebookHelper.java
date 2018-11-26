package com.app.motiv.utils.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.net.URL;
import java.util.Collections;

/**
 * Created by 123 on 26-09-2017.
 */

public class FacebookHelper implements FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {


    public final static String FACEBOOK_ID = "idFacebook";
    public final static String FIRST_NAME = "first_name";
    public final static String LAST_NAME = "last_name";
    public final static String EMAIL = "email";
    public final static String BIRTHDAY = "birthday";
    public final static String AGE_RANGE = "age_range";
    public final static String LOCATION = "location";
    public final static String GENDER = "gender";

    public final static String PROFILE_PIC = "profile_pic";

    public interface FacebookHelperCallback {
        void onSuccessFacebook(Bundle bundle);

        void onCancelFacebook();

        void onErrorFacebook(FacebookException ex);
    }

    private Context context;
    private FacebookHelperCallback callback;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ProgressDialog progressDialog;

    public FacebookHelper() {
    }

    public FacebookHelper(Context context, FacebookHelperCallback callback) {
        this.context = context;
        this.callback = callback;
        callbackManager = CallbackManager.Factory.create();
    }

    public void login(Context context) {
        loginButton = new LoginButton(context);
        loginButton.setReadPermissions(Collections.singletonList("public_profile, email,user_birthday"));
        loginButton.performClick();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {

        SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance();
        helper.saveFacebookAccessToken(loginResult.getAccessToken().getToken());
        helper.saveFacebookUserId(loginResult.getAccessToken().getUserId());

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        getFaceBookProfile(loginResult);
    }

    private void getFaceBookProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday,age_range, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
            Log.i("profile_pic", profile_pic + "");
            bundle.putString(PROFILE_PIC, profile_pic.toString());
//            Log.e("Dob", "getFacebookData: " + object.getString("birthday"));

            bundle.putString(FACEBOOK_ID, id);

            if (object.has(FIRST_NAME))
                bundle.putString(FIRST_NAME, object.getString("first_name"));
            if (object.has(LAST_NAME))
                bundle.putString(LAST_NAME, object.getString("last_name"));
            if (object.has(EMAIL))
                bundle.putString(EMAIL, object.getString("email"));
            if (object.has(GENDER))
                bundle.putString(GENDER, object.getString("gender"));
            if (object.has(BIRTHDAY))
                bundle.putString(BIRTHDAY, object.getString("birthday"));
            if (object.has(AGE_RANGE))
                bundle.putString(AGE_RANGE, object.getString("age_range"));
            if (object.has(LOCATION))
                bundle.putString(LOCATION, object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCancel() {
        callback.onCancelFacebook();
    }

    @Override
    public void onError(FacebookException error) {
        callback.onErrorFacebook(error);
    }

    public void logout() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        Bundle facebookData = getFacebookData(object);

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        callback.onSuccessFacebook(facebookData);
    }
}
