package com.app.motiv.ui.authenticate.intractorImpl;

import com.app.motiv.data.Injector;
import com.app.motiv.data.InterfaceApi;
import com.app.motiv.data.shared.DataResponse;
import com.app.motiv.ui.authenticate.contract.SignupNormalUserContract;
import com.app.motiv.utils.App;
import com.app.motiv.utils.Constants;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 123 on 13-Feb-18.
 */

public class SignupNormalUserIntractorImpl implements SignupNormalUserContract.Intracter {

    // signup_type = ((1 => normal , 2 => Social ))
    // user_type = ( 2 => Normal , 3 => Organizer )

    private InterfaceApi api;

    public SignupNormalUserIntractorImpl() {
        api = Injector.provideApi();
    }

    @Override
    public void signupNormalUser(String user_image, String name,String userName, String email, String password, String phoneNum, String age,
                                 String publicInterest, String musicInterest,String referralCode,
                                 final SignupNormalUserContract.OnCompleteListener callback) {

        if (!App.hasNetwork()) {
            callback.onError(Constants.NETWORK_ERROR);
            return;
        }

        RequestBody nameBody = null;
        RequestBody emailBody = null;
        RequestBody passwrodBody = null;
        RequestBody phoneNumBody = null;
        RequestBody ageBody = null;
        RequestBody signupTypeBody = null;
        RequestBody userTypeBody = null;
        RequestBody publicInterestTypeBody = null;
        RequestBody musicInterestTypeBody = null;
        RequestBody userNameTypeBody = null;
        RequestBody referralCodeTypeBody = null;

        MultipartBody.Part imageFileBody = null;

        nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        passwrodBody = RequestBody.create(MediaType.parse("text/plain"), password);
        phoneNumBody = RequestBody.create(MediaType.parse("text/plain"), phoneNum);
        ageBody = RequestBody.create(MediaType.parse("text/plain"), age);
        signupTypeBody = RequestBody.create(MediaType.parse("text/plain"), "1");
        userTypeBody = RequestBody.create(MediaType.parse("text/plain"), "2");
        publicInterestTypeBody = RequestBody.create(MediaType.parse("text/plain"), publicInterest);
        musicInterestTypeBody = RequestBody.create(MediaType.parse("text/plain"), musicInterest);
        userNameTypeBody = RequestBody.create(MediaType.parse("text/plain"), userName);
        referralCodeTypeBody = RequestBody.create(MediaType.parse("text/plain"), referralCode);

        if (!user_image.isEmpty() && !user_image.equals("")) {
            File file = new File(user_image);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            imageFileBody = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        }

        api.signupNormalUser(nameBody,userNameTypeBody, emailBody, passwrodBody, phoneNumBody, ageBody, signupTypeBody, userTypeBody,
                publicInterestTypeBody, musicInterestTypeBody,referralCodeTypeBody, imageFileBody).enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccessNormalUserSignup(response.body());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        callback.onError(jObjError.getString("message"));
                    } catch (Exception e) {
                        callback.onError(Constants.SERVER_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
