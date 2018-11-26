package com.app.motiv.ui.authenticate.presentorImpl;

import com.app.motiv.data.shared.DataResponse;
import com.app.motiv.ui.authenticate.contract.SignupNormalUserContract;
import com.app.motiv.ui.authenticate.intractorImpl.SignupNormalUserIntractorImpl;

/**
 * Created by 123 on 13-Feb-18.
 */

public class SignupNormalUserPresentorImpl implements SignupNormalUserContract.Presentor, SignupNormalUserContract.OnCompleteListener {

    SignupNormalUserContract.View view;
    SignupNormalUserContract.Intracter intracter;

    public SignupNormalUserPresentorImpl(SignupNormalUserContract.View view) {
        this.view = view;
        intracter = new SignupNormalUserIntractorImpl();
    }

    @Override
    public void attachView(Object view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void signupNormalUser(String user_image, String name, String userName, String email, String password, String phoneNum, String age,
                                 String publicInterest, String musicInterest,String referralCode) {
        intracter.signupNormalUser(user_image, name, userName, email, password, phoneNum, age, publicInterest, musicInterest,referralCode, this);
    }

    @Override
    public void onSuccessNormalUserSignup(DataResponse dataResponse) {
        view.onSuccessNormalUserSignup(dataResponse);
    }

    @Override
    public void onError(String error) {
        view.onFailure(error);
    }
}
