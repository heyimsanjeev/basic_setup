package com.app.motiv.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.motiv.R;

import permissions.dispatcher.PermissionRequest;

public class CommonMethods {

    public static void openDialog(final String type, String title, String msg, String okListenerText, String noListenerText,
                                  String yesListenerText, final YesNoDialogListener yesNoDialogListener, Context context) {
        final Dialog mDialog = new Dialog(context, R.style.MyDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setGravity(Gravity.CENTER_VERTICAL);
        window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent_image)));
        mDialog.setCancelable(false);

        mDialog.setContentView(R.layout.custom_dialog);

        TextView tvTitle = (TextView) mDialog.findViewById(R.id.tv_title);
        TextView tvMsg = (TextView) mDialog.findViewById(R.id.tv_msg);

        TextView tvOk = (TextView) mDialog.findViewById(R.id.tv_ok);

        LinearLayout llYesNo = (LinearLayout) mDialog.findViewById(R.id.ll_yes_no);
        TextView tvNo = (TextView) mDialog.findViewById(R.id.tv_no);
        TextView tvYes = (TextView) mDialog.findViewById(R.id.tv_yes);

        tvMsg.setText(msg);


        if (type.equalsIgnoreCase(Constants.ONE_LISTENER)) {
            tvTitle.setVisibility(View.VISIBLE);
            llYesNo.setVisibility(View.GONE);
            tvOk.setVisibility(View.VISIBLE);
            tvOk.setText(okListenerText);
            tvTitle.setText(title);

            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    yesNoDialogListener.onClickYes();
                    mDialog.dismiss();
                }
            });

        } else if (type.equalsIgnoreCase(Constants.TWO_LISTENER)) {
            llYesNo.setVisibility(View.VISIBLE);
            tvOk.setVisibility(View.GONE);
            tvNo.setText(noListenerText);
            tvYes.setText(yesListenerText);
            tvTitle.setVisibility(View.GONE);

            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    yesNoDialogListener.onClickNo();
                }
            });

            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    yesNoDialogListener.onClickYes();
                }
            });
        }

        mDialog.show();
    }

    public static void showRotaionalPermissionDialog(Context mContext, final PermissionRequest request) {
        new AlertDialog.Builder(mContext)
                .setMessage(R.string.app_need_perm)
                .setPositiveButton(mContext.getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    public static void showSettingActivityPermissionDialog(final Context mContext) {
        new AlertDialog.Builder(mContext)
                .setMessage(mContext.getString(R.string.app_need_perm))
                .setPositiveButton(mContext.getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", mContext.getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                })
                .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setCancelable(false)
                .show();
    }
}
