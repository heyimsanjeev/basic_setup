package com.app.motiv.ui.main.normalUser.myMoTivDesc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.motiv.R;
import com.app.motiv.data.model.BadgeModel;
import com.app.motiv.data.model.EventModel;
import com.app.motiv.data.shared.DataResponse;
import com.app.motiv.ui.authenticate.base.BaseAuthenticateActivity;
import com.app.motiv.ui.main.chat.ChatInboxActivity;
import com.app.motiv.ui.main.eventOrganizer.guests.GuestsFragment;
import com.app.motiv.ui.main.eventOrganizer.loremEvent.LoremEventActivity;
import com.app.motiv.ui.main.eventOrganizer.myMoTiv.MyMoTivActivity;
import com.app.motiv.ui.main.normalUser.addFriends.AddFriendsActivity;
import com.app.motiv.ui.main.normalUser.applicationEvents.ApplicationEventFragment;
import com.app.motiv.ui.main.normalUser.createMoTivFragment.CreateMoTivNormalUserFragment;
import com.app.motiv.ui.main.normalUser.inviteUsers.InviteUserActivity;
import com.app.motiv.ui.main.normalUser.main.MainNormalUserActivity;
import com.app.motiv.ui.main.normalUser.myMoTiv.MyMoTivNormalUserFragment;
import com.app.motiv.ui.main.normalUser.notification.NotificationNormalUserFragment;
import com.app.motiv.utils.Constants;
import com.app.motiv.utils.ToastUtils;
import com.app.motiv.utils.Utils;
import com.app.motiv.utils.commonActivity.ReportEventActivity;
import com.app.motiv.utils.commonApi.contract.BadgeContract;
import com.app.motiv.utils.commonApi.presentorImpl.BadgePresentorImpl;
import com.app.motiv.utils.helpers.SharedPreferenceHelper;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyMoTivDescNormalUserActivity extends BaseAuthenticateActivity implements EventInfoNormalUserFragment.OnFragmentInteractionListener
        , GuestsFragment.OnFragmentInteractionListener,BadgeContract.View {

    ButterKnife butterKnife;

    @BindView(R.id.iv_back2)
    public ImageView ivBack;
    @BindView(R.id.iv_edit_event)
    public ImageView ivEditEvent;
    @BindView(R.id.iv_theme)
    public ImageView ivTheme;
    @BindView(R.id.iv_report_event)
    public ImageView ivReportEvent;

    @BindView(R.id.rl_main_2)
    public RelativeLayout rlMainDesc;
    @BindView(R.id.rl_main_1)
    public RelativeLayout rlMainGuest;
    @BindView(R.id.iv_back1)
    public ImageView ivBackGuest;
    @BindView(R.id.tv_header)
    public TextView tvHeader;


    @BindView(R.id.tv_date)
    public TextView tvDateHeader;
    @BindView(R.id.tv_event_name)
    public TextView tvEventNameHeader;
    @BindView(R.id.tv_event_price)
    public TextView tvEventPriceHeader;
    @BindView(R.id.tv_approval)
    public TextView tvApprovalHeader;


    @BindView(R.id.iv_share)
    public ImageView ivShare;

    @BindView(R.id.btn_event_info)
    public Button btnEventInfo;
    @BindView(R.id.btn_guest)
    public Button btnGuest;
    @BindView(R.id.btn_post)
    public Button btnPost;

    @BindView(R.id.iv_down_arrow_one)
    public ImageView ivDownArrowOne;
    @BindView(R.id.iv_down_arrow_two)
    public ImageView ivDownArrowTwo;
    @BindView(R.id.iv_down_arrow_three)
    public ImageView ivDownArrowThree;

    @BindView(R.id.ll_main)
    public LinearLayout llSharePost;
    @BindView(R.id.rl_end)
    public RelativeLayout rlEnd;

    @BindView(R.id.iv_chat)
    public ImageView ivChat;
    @BindView(R.id.iv_add_friend)
    public ImageView ivAddFriend;

    @BindView(R.id.rl_notification_counter_chat)
    public RelativeLayout rlNOtificaitonCounterChat;

    List<Button> upperTabList;
    List<ImageView> downArrowList;
    EventModel eventModel;
    String btnClick = "1";

    String eventId = "", eventDate = "", eventTime = "";

    BadgeContract.Presentor presentor;

    public static void start(Context context, EventModel eventModel) {
        Intent intent = new Intent(context, MyMoTivDescNormalUserActivity.class);
        intent.putExtra(Constants.EVENT_MODEL, eventModel);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_my_mo_tiv_desc_normal_user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        butterKnife.bind(this);
        initToolbar();
        addDataToList();
        initViews();
//        checkNotif();
        initFragment();
    }

//    private void checkNotif() {
//        Intent intent = getIntent();
//        String type1 = intent.getStringExtra("From");
//        if (type1 != null) {
//            if (type1.equals("notifyFragEvent")) {
//                eventId = intent.getStringExtra("EventId");
//                eventDate = intent.getStringExtra("EventDate");
//                eventTime = intent.getStringExtra("EventTime");
//
//                eventModel = new EventModel();
//                eventModel.setId(eventId);
//                eventModel.setEvent_date(eventDate);
//                eventModel.setEvent_time(eventTime);
//                eventModel.setEvent_name(intent.getStringExtra("EventName"));
//
//                String date = eventModel.getEvent_date();
//                String time = eventModel.getEvent_time();
//                String dateTime = date + " " + time;
//                String currentDate = Utils.getCurrentDate();
//                String eventDate = Utils.stringToDate2(dateTime);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                try {
//                    Date date1 = sdf.parse(currentDate);
//                    Date date2 = sdf.parse(eventDate);
//                    sdf.format(date1);
//                    sdf.format(date2);
//
//                    if (date1.equals(date2)) {  // current
//                        btnClick = "1";
//                        eventModel.setBtnClick(btnClick);
//                    } else if (date1.before(date2)) {  // upcoming
//                        btnClick = "2";
//                        eventModel.setBtnClick(btnClick);
//                    } else {                          // past
//                        btnClick = "3";
//                        eventModel.setBtnClick(btnClick);
//                    }
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        } else {
//            eventId = eventModel.getId();
//        }
//    }

    private void initViews() {
        eventModel = (EventModel) getIntent().getSerializableExtra(Constants.EVENT_MODEL);
        onUpperTabSelected(btnEventInfo, ivDownArrowOne);
        ivChat.setVisibility(View.VISIBLE);
        ivAddFriend.setVisibility(View.GONE);

        if (Integer.parseInt(SharedPreferenceHelper.getInstance().getUserId()) == Integer.parseInt(eventModel.getUser_id())) {
            ivReportEvent.setVisibility(View.GONE);
        } else {
            ivReportEvent.setVisibility(View.VISIBLE);
        }
    }


    private void initFragment() {
        rlMainDesc.setVisibility(View.VISIBLE);
        rlMainGuest.setVisibility(View.GONE);
        ivEditEvent.setVisibility(View.GONE);
        switchFragment(R.id.frame_layout, EventInfoNormalUserFragment.newInstance(eventModel));

        if (eventModel.getBtnClick().equals("3")) {
            llSharePost.setVisibility(View.GONE);
            ivEditEvent.setVisibility(View.GONE);    // for past event 01 june

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlEnd.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rlEnd.setLayoutParams(params);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rlEnd.getLayoutParams();
            lp.setMargins(0, 0, 50, 0);
            rlEnd.setLayoutParams(lp);

        } else {
            llSharePost.setVisibility(View.VISIBLE);
        }

        if (eventModel.getShareOption() != null) {
            if (eventModel.getShareOption().equals("1")) {
                llSharePost.setVisibility(View.GONE);
  /*              RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlEnd.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                rlEnd.setLayoutParams(params);

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rlEnd.getLayoutParams();
                lp.setMargins(0, 0, 50, 0);
                rlEnd.setLayoutParams(lp);*/

            }

            if (eventModel.getShareOption().equals("1")) {
                llSharePost.setVisibility(View.VISIBLE);
            }
        }
        setData();
    }

    private void setData() {

        if (eventModel.getEvent_theme_url() != null
                && !eventModel.getEvent_theme_url().isEmpty()) {
            Picasso.with(this).load(eventModel.getEvent_theme_url())
                    .placeholder(R.mipmap.img_background).error(R.mipmap.img_background).into(ivTheme);
        }

        if (eventModel.getTicket_price().equals("0")) {
            tvEventPriceHeader.setText(getString(R.string.free));
        } else {
            if (eventModel.getTicket_price().contains(".")) {
                tvEventPriceHeader.setText("£" + eventModel.getTicket_price());
            } else {
                tvEventPriceHeader.setText("£" + eventModel.getTicket_price());
            }
        }
        tvEventNameHeader.setText(eventModel.getEvent_name());
        tvDateHeader.setText(Utils.stringToDateDay(eventModel.getEvent_date2()));
        tvApprovalHeader.setText("Approved");
    }

    private void initToolbar() {
        downArrowList = new ArrayList<>();
        upperTabList = new ArrayList<>();
        presentor = new BadgePresentorImpl(this);
    }

    private void addDataToList() {
        downArrowList.add(ivDownArrowOne);
        downArrowList.add(ivDownArrowTwo);
        downArrowList.add(ivDownArrowThree);

        upperTabList.add(btnEventInfo);
        upperTabList.add(btnGuest);
        upperTabList.add(btnPost);
    }

    @OnClick(R.id.btn_event_info)
    public void onClickCurrent() {
        rlMainDesc.setVisibility(View.VISIBLE);
        rlMainGuest.setVisibility(View.GONE);
        ivEditEvent.setVisibility(View.GONE);
        initViews();
        initFragment();
    }

    @OnClick(R.id.btn_guest)
    public void onClickUpcoming() {

        rlMainDesc.setVisibility(View.GONE);
        rlMainGuest.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvHeader.setText(getString(R.string.guests));

        ivEditEvent.setVisibility(View.GONE);
        onUpperTabSelected(btnGuest, ivDownArrowTwo);
//        AttendingActivity.start(this, eventModel);
        switchFragment(R.id.frame_layout, GuestsFragment.newInstance(eventModel));
    }

    @OnClick(R.id.iv_back1)
    public void onClickBack2() {
        onClickBack();
    }

    @OnClick(R.id.btn_post)
    public void onClickPast() {
        ivEditEvent.setVisibility(View.GONE);
        onUpperTabSelected(btnPost, ivDownArrowThree);
        LoremEventActivity.start(this, Constants.NORMAL_USER, eventModel);
    }

    // for changing color of tabs
    private void onUpperTabSelected(Button selectedBtn, ImageView imageViewSelected) {

        for (Button button : upperTabList) {
            button.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }

        for (ImageView imageView : downArrowList) {
            imageView.setVisibility(View.GONE);
        }

        for (Button btn : upperTabList) {
            if (btn == selectedBtn) {
                selectedBtn.setBackgroundColor(getResources().getColor(R.color.color_texi_yellow));
                imageViewSelected.setVisibility(View.VISIBLE);
                return;
            }
            btn.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
    }

    @OnClick(R.id.iv_back2)
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
        showProgress();
        presentor.getBadge();
        initFragment();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @OnClick(R.id.iv_invite_user)
    public void onClickInviteUser() {
        InviteUserActivity.start(this, eventModel);
    }

    @OnClick(R.id.iv_share)
    public void onClickShare() {

        String date = eventModel.getEvent_date();
        String time = eventModel.getEvent_time();

        String formatedTime = Utils.stringToTime(time);
        formatedTime = formatedTime.replace("a.m.", "AM").replace("p.m.", "PM");

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Please attend " + eventModel.getEvent_name() + " on " + Utils.stringToDate(date) + " | " + formatedTime + " at location "
                + eventModel.getEvent_location() + "\n" + "Managed by Motiv.\n";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @OnClick(R.id.iv_report_event)
    public void onClickReportEvent() {
        ReportEventActivity.start(eventModel.getId(), this);
    }

    @OnClick(R.id.iv_chat)
    public void onClickChat() {
        rlNOtificaitonCounterChat.setVisibility(View.GONE);
        Intent intent = new Intent(this, ChatInboxActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_add_friend)
    public void onClickAddFriend() {
        AddFriendsActivity.start(this);
    }

    @Override
    public void onSuccessBadge(DataResponse<BadgeModel> response) {
        hideProgress();
        if (isFinishing()) {
            return;
        }

        if (response.getData().getMsg_count() != 0) {
            rlNOtificaitonCounterChat.setVisibility(View.VISIBLE);
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
}
