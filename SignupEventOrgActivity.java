package com.app.motiv.ui.authenticate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.motiv.R;
import com.app.motiv.data.model.SignupModel;
import com.app.motiv.data.shared.DataResponse;
import com.app.motiv.ui.authenticate.base.BaseAuthenticateActivity;
import com.app.motiv.ui.authenticate.contract.EmailExistContract;
import com.app.motiv.ui.authenticate.contract.SignupContract;
import com.app.motiv.ui.authenticate.contract.SocialSigninContract;
import com.app.motiv.ui.authenticate.presentorImpl.EmailExistPresentorImpl;
import com.app.motiv.ui.authenticate.presentorImpl.SignupPresentorImpl;
import com.app.motiv.ui.authenticate.presentorImpl.SocialSigninPresentorImpl;
import com.app.motiv.ui.main.normalUser.publicInterest.PublicActivity;
import com.app.motiv.utils.CommonMethods;
import com.app.motiv.utils.CompressImage;
import com.app.motiv.utils.Constants;
import com.app.motiv.utils.ToastUtils;
import com.app.motiv.utils.Validators;
import com.app.motiv.utils.helpers.SharedPreferenceHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SignupEventOrgActivity extends BaseAuthenticateActivity implements SignupContract.View, SocialSigninContract.View,
        EmailExistContract.View {

    ButterKnife butterKnife;

    @BindView(R.id.iv_back)
    public ImageView ivBack;
    @BindView(R.id.tv_header)
    public TextView tvHeader;

    @BindView(R.id.iv_user)
    public CircleImageView ivUser;
    @BindView(R.id.iv_select_image)
    public ImageView ivSelectImage;
    @BindView(R.id.et_name)
    public EditText etName;
    @BindView(R.id.et_email)
    public EditText etEmail;
    @BindView(R.id.et_pwd)
    public EditText etPwd;
    @BindView(R.id.et_confirm_pwd)
    public EditText etConfirmPwd;
    @BindView(R.id.et_phone_num)
    public EditText etPhoneNum;
    @BindView(R.id.et_age)
    public EditText etAGe;
    @BindView(R.id.rl_referral_code)
    public RelativeLayout rlReferralCode;
    @BindView(R.id.et_referal_code)
    public EditText etReferralCode;

    @BindView(R.id.rl_confirm_pwd)
    public RelativeLayout rlConfirmPwd;
    @BindView(R.id.rl_pwd)
    public RelativeLayout rlPwd;

    @BindView(R.id.et_user_name)
    public EditText etUserName;
    @BindView(R.id.chk_touch_id)
    public CheckBox chkEnableTouchId;

    Validators validators;

    String signupType = "";
    String name = "", userName = "", email = "", password = "", confirmPassword = "", phoneNumber = "", age = "", referralCode = "";

    protected static final int GALLERY_PICTURE = 1;
    protected static final int CAMERA_REQUEST = 0;

    Bitmap bitmap;
    String selectedImagePath;
    String selectedImageUri = "";

    CompressImage compressImage;

    EmailExistContract.Presentor presentorEmailExist;
    boolean isEmailExist;

    private SignupContract.Presentor presentor;
    private SocialSigninContract.Presentor presentorSocialSignIn;

    SignupModel signupModel, modelForSignup;
    String type = "";

    public static void start(Context context, String signupType, String type, SignupModel signupModel) {
        Intent intent = new Intent(context, SignupEventOrgActivity.class);
        intent.putExtra(Constants.SIGNUP_TYPE, signupType);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.SIGNUP_MODEL, signupModel);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_signup_event_org;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        butterKnife.bind(this);
        SharedPreferenceHelper.getInstance().savePref(Constants.TOUCH_ID, "disabled");
        // this is for nougat file uri esposed clipdata exception
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initViews();
        initToolbar();
        getSignupType();

        if (signupType.equalsIgnoreCase(Constants.NORMAL_USER)) {
            rlReferralCode.setVisibility(View.VISIBLE);
        } else {
            rlReferralCode.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.chk_touch_id)
    public void touchId() {
        if (chkEnableTouchId.isChecked()) {
            SharedPreferenceHelper.getInstance().savePref(Constants.TOUCH_ID, "enabled");
        } else {
            SharedPreferenceHelper.getInstance().savePref(Constants.TOUCH_ID, "disabled");
        }
//        setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                if ( isChecked )
//                {
//                    // perform logic
//
//                }
//                else{
//
//                }
//
//            }
//        });
    }

    private void initViews() {

        rlPwd.setVisibility(View.VISIBLE);
        rlConfirmPwd.setVisibility(View.VISIBLE);

        // this is for nougat file uri esposed clipdata exception
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getString(R.string.signup));

        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        if (type.equalsIgnoreCase(Constants.SOCIAL)) {

            rlPwd.setVisibility(View.GONE);
            rlConfirmPwd.setVisibility(View.GONE);

            modelForSignup = (SignupModel) intent.getSerializableExtra(Constants.SIGNUP_MODEL);

            if (!modelForSignup.getName().equals("") && modelForSignup.getName() != null) {
                etName.setText(modelForSignup.getName());
            }

            if (!modelForSignup.getEmail().equals("") && modelForSignup.getEmail() != null) {
                etEmail.setText(modelForSignup.getEmail());
            }
            if (!modelForSignup.getImagePath().equals("") && modelForSignup.getImagePath() != null) {
                SharedPreferenceHelper.getInstance().saveImagePath(modelForSignup.getImagePath());
                Picasso.with(this).load(modelForSignup.getImagePath())
                        .error(R.mipmap.ic_user)
                        .into(ivUser);
            }

//            etEmail.setEnabled(false);
//            etEmail.setFocusable(false);
        }
    }

    private void initToolbar() {
        validators = new Validators(this);
        compressImage = new CompressImage();
        presentorEmailExist = new EmailExistPresentorImpl(this);
        presentor = new SignupPresentorImpl(this);
        presentorSocialSignIn = new SocialSigninPresentorImpl(this);
    }

    private void getSignupType() {
        Intent intent = getIntent();
        signupType = intent.getStringExtra(Constants.SIGNUP_TYPE);
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.iv_select_image)
    public void onClickSelectImage() {
        SignupEventOrgActivityPermissionsDispatcher.selectImageWithPermissionCheck(this);
    }

    @OnClick(R.id.rl_sign_up)
    public void onClickSignup() {

        getInputData();
        if (type.equals(Constants.SOCIAL)) {
            password = "12345678";
            confirmPassword = "12345678";
        }
        if (validators.isValidSignupData(name, userName, email, password, confirmPassword, phoneNumber, age)) {
            showProgress();
            presentorEmailExist.checkEmailExist(email);
        }
    }

    // getting input data
    private void getInputData() {

        name = etName.getText().toString().trim();
        userName = etUserName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPwd.getText().toString().trim();
        confirmPassword = etConfirmPwd.getText().toString().trim();
        phoneNumber = etPhoneNum.getText().toString().trim();
        age = etAGe.getText().toString().trim();

        if (signupType.equalsIgnoreCase(Constants.NORMAL_USER)) {
            referralCode = etReferralCode.getText().toString().trim();
        }
    }

    // for selecting image form gallery and camera
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_library), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), getString(R.string.temp_jpg));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, CAMERA_REQUEST);
                } else if (items[item].equals(getString(R.string.choose_from_library))) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForCamera(final PermissionRequest request) {
        CommonMethods.showRotaionalPermissionDialog(this, request);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForCamera() {
        ToastUtils.longToast(R.string.permission_denied);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForCamera() {
        CommonMethods.showSettingActivityPermissionDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            File file = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : file.listFiles()) {
                if (temp.getName().equals(getString(R.string.temp_jpg))) {
                    file = temp;
                    break;
                }
            }

            if (!file.exists()) {
                Toast.makeText(this, getString(R.string.error_while_capturing_image), Toast.LENGTH_LONG).show();
                return;
            }

            try {
                selectedImagePath = file.getAbsolutePath();

                if (selectedImagePath != null) {
                    File file1 = new File(selectedImagePath);
                    selectedImageUri = Uri.fromFile(file1).toString();

                    compressImage.compressYourImage(selectedImageUri, this);
                }
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ivUser.setImageBitmap(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);

                if (selectedImagePath != null) {
                    File file2 = new File(selectedImagePath);
                    selectedImageUri = Uri.fromFile(file2).toString();

                    compressImage.compressYourImage(selectedImageUri, this);
                }
                c.close();
                bitmap = BitmapFactory.decodeFile(selectedImagePath);
                ivUser.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, getString(R.string.cancelled), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SignupEventOrgActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onSuccessEventOrganizer(DataResponse dataResponse) {
        hideProgress();
        if (isFinishing()) {
            return;
        }
        ToastUtils.longToast(getString(R.string.signup_successfully));
        SharedPreferenceHelper.getInstance().saveImagePath("");
        LoginActivity.start(this);

    }

    @Override
    public void goNext() {
        hideProgress();
        if (isFinishing()) {
            return;
        }
        if (SharedPreferenceHelper.getInstance().getIsSocial()) {
            SharedPreferenceHelper.getInstance().saveIsSocial(false);
            ToastUtils.shortToast(getString(R.string.sign_up_successfully));
            SharedPreferenceHelper.getInstance().saveImagePath("");
            LoginActivity.start(this);
        } else {
            ToastUtils.longToast(getString(R.string.signup_successfully));
            SharedPreferenceHelper.getInstance().saveImagePath("");
            LoginActivity.start(this);
        }
    }

    @Override
    public void onFailure(String message) {
        hideProgress();
        if (isFinishing()) {
            return;
        }
        ToastUtils.longToast(message);
    }

    @Override
    public void onSuccessEmailExist(DataResponse response) {
        hideProgress();
        if (signupType.equalsIgnoreCase(Constants.NORMAL_USER)) {
            getInputData();

            if (type.equals(Constants.SOCIAL)) {
                password = "12345678";
                confirmPassword = "12345678";
            }

            if (validators.isValidSignupData(name, userName, email, password, confirmPassword, phoneNumber, age)) {
                signupModel = new SignupModel(SharedPreferenceHelper.getInstance().getImagePath(), name, userName, email, password,
                        phoneNumber, age);
                signupModel.setReferralCode(referralCode);
                if (type.equals(Constants.SOCIAL)) {
                    signupModel.setSocialId(modelForSignup.getSocialId());
                    signupModel.setSocialSignupType(modelForSignup.getSocialSignupType());
                    signupModel.setUserType(modelForSignup.getUserType());
                    signupModel.setSignupType(modelForSignup.getSignupType());
                }
                PublicActivity.start(this, Constants.NORMAL_USER, type, signupModel);
            }
        } else if (signupType.equalsIgnoreCase(Constants.EVENT_ORGANIZER)) {
            getInputData();

            if (type.equals(Constants.SOCIAL)) {
                password = "12345678";
                confirmPassword = "12345678";
            }

            if (validators.isValidSignupData(name, userName, email, password, confirmPassword, phoneNumber, age)) {
                showProgress();
                if (type.equalsIgnoreCase(Constants.SOCIAL)) {

                    signupModel = new SignupModel(SharedPreferenceHelper.getInstance().getImagePath(), name, userName, email, password,
                            phoneNumber, age);

                    presentorSocialSignIn.facebookLoginEventOrganizer(signupModel.getImagePath(), signupModel.getName(), signupModel.getUserName(),
                            signupModel.getEmail(), signupModel.getPassword(), signupModel.getPhoneNum(), signupModel.getAge(),
                            modelForSignup.getSignupType(), modelForSignup.getUserType(), modelForSignup.getSocialSignupType(), modelForSignup.getSocialId());
                } else if (type.equalsIgnoreCase(Constants.SIMPLE)) {
                    presentor.signupEventOrganizer(SharedPreferenceHelper.getInstance().getImagePath(), name, userName,
                            email, password, phoneNumber, age);
                }
            }
        }
    }

    @Override
    public void onFailreEmailExist(String message) {
        hideProgress();
        ToastUtils.longToast(message);
    }
}
