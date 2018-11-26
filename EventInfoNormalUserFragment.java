package com.app.motiv.ui.main.normalUser.myMoTivDesc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.motiv.R;
import com.app.motiv.data.model.EachEventDataModel;
import com.app.motiv.data.model.EventModel;
import com.app.motiv.data.shared.DataResponse;
import com.app.motiv.ui.base.BaseFragment;
import com.app.motiv.ui.main.eventOrganizer.myMoTiv.GetDirectionActivity;
import com.app.motiv.ui.main.normalUser.attending.AttendingActivity;
import com.app.motiv.ui.main.normalUser.editEvent.EditEventNormalUserActivity;
import com.app.motiv.ui.main.normalUser.myMoTivDesc.contract.AttendEventContract;
import com.app.motiv.ui.main.normalUser.myMoTivDesc.presentorImpl.AttendEventPresentorImpl;
import com.app.motiv.utils.Constants;
import com.app.motiv.utils.ToastUtils;
import com.app.motiv.utils.Utils;
import com.app.motiv.utils.commonApi.contract.EachEventDataContract;
import com.app.motiv.utils.commonApi.presentorImpl.EachEventDataPresentorImpl;
import com.app.motiv.utils.helpers.SharedPreferenceHelper;
import com.app.motiv.utils.imageFullView.TicketWebActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 123 on 07-Feb-18.
 */

public class EventInfoNormalUserFragment extends BaseFragment implements EachEventDataContract.View, AttendEventContract.View {

    private OnFragmentInteractionListener mListener;
    private Unbinder binder;

    @BindView(R.id.tv_user_info)
    public TextView tvGuestInfo;
    @BindView(R.id.tv_address)
    public TextView tvAddress;
    @BindView(R.id.tv_event_time)
    public TextView tvEventTime;
    @BindView(R.id.tv_category)
    public TextView tvCategory;
    @BindView(R.id.tv_music)
    public TextView tvMusic;
    @BindView(R.id.tv_desc)
    public TextView tvEventDesc;
    @BindView(R.id.rl_attend)
    public RelativeLayout rlAttend;
    @BindView(R.id.rl_location)
    public RelativeLayout rlLocation;

    @BindView(R.id.tv_viewer)
    public TextView tvViewers;

    @BindView(R.id.rl_category)
    public RelativeLayout rlCategory;
    @BindView(R.id.rl_music)
    public RelativeLayout rlMusic;

    @BindView(R.id.tv_ticker_url)
    public TextView tvTicketUrl;
    @BindView(R.id.tv_contact_number)
    public TextView tvContactNumber;
    @BindView(R.id.iv_ticket_url)
    public ImageView ivTicketUrl;

    @BindView(R.id.rl_ticket_url)
    public RelativeLayout rlTicketUrl;
    @BindView(R.id.view_music)
    public View viewMusic;
    @BindView(R.id.rl_contact_number)
    public RelativeLayout rlContactNumber;
    @BindView(R.id.view_ticket)
    public View viewTicket;
    @BindView(R.id.tv_attend)
    public TextView tvAttend;

    EachEventDataContract.Presentor presentorEachEventData;
    EventModel eventModel;
    AttendEventContract.Presentor presentorAttendEvent;

    public ImageView ivTheme;
    public TextView tvEventPriceHeader;
    public TextView tvEventNameHeader;
    public TextView tvDateHeader;
    public TextView tvApprovalHeader;
    public ImageView ivEditEvent;
    EachEventDataModel eachEventDataModel;

    public EventInfoNormalUserFragment() {

    }

    public static EventInfoNormalUserFragment newInstance(EventModel eventModel) {
        EventInfoNormalUserFragment fragment = new EventInfoNormalUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.EVENT_MODEL, eventModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_info_normal_user, container, false);
        initViews(view);
        getDescAcitivityView();
        initToolbar();
        onClickEditEvent();
        return view;
    }

    private void getDescAcitivityView() {

        ivTheme = (ImageView) getActivity().findViewById(R.id.iv_theme);
        tvEventPriceHeader = (TextView) getActivity().findViewById(R.id.tv_event_price);
        tvEventNameHeader = (TextView) getActivity().findViewById(R.id.tv_event_name);
        tvDateHeader = (TextView) getActivity().findViewById(R.id.tv_date);
        tvApprovalHeader = (TextView) getActivity().findViewById(R.id.tv_approval);
        ivEditEvent = (ImageView) getActivity().findViewById(R.id.iv_edit_event);
//        ivEditEvent.setVisibility(View.VISIBLE);
//        ivEditEvent.setEnabled(false);
    }


    private void initViews(View view) {
        binder = ButterKnife.bind(this, view);
        rlLocation.setEnabled(false);
        String text1 = "Event will start at ";
        String text2 = "<font color='#000000'><b>1:00PM</b></font>";

        Bundle bundle = getArguments();
        eventModel = (EventModel) bundle.getSerializable(Constants.EVENT_MODEL);
    }

    private void initToolbar() {
        presentorEachEventData = new EachEventDataPresentorImpl(this);
        presentorAttendEvent = new AttendEventPresentorImpl(this);
    }

    // for edit event
    private void onClickEditEvent() {
        ivEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eachEventDataModel != null) {
                    EditEventNormalUserActivity.start(getActivity(), eachEventDataModel);
                }
            }
        });
    }

    @OnClick(R.id.rl_location)
    public void onClickLocation() {
        GetDirectionActivity.start(getActivity(), eventModel);
    }

    @Override
    public void onSuccessEachEventData(DataResponse<EachEventDataModel> response) {
        hideProgress();
        if (!isVisible()) {
            return;
        }

        eachEventDataModel = new EachEventDataModel();
        eachEventDataModel = response.getData();
        rlLocation.setEnabled(true);

        if (eachEventDataModel.getPost_type().equals("1")) {   // 1 for organiser (public), 2 for normalUser (private)
            rlCategory.setVisibility(View.VISIBLE);
            rlMusic.setVisibility(View.VISIBLE);
            rlContactNumber.setVisibility(View.GONE);
            viewTicket.setVisibility(View.GONE);
            ivTicketUrl.setImageResource(R.mipmap.ic_ticket);
        } else if (eachEventDataModel.getPost_type().equals("2")) {
            rlCategory.setVisibility(View.GONE);
            rlMusic.setVisibility(View.GONE);
            rlContactNumber.setVisibility(View.VISIBLE);
            viewTicket.setVisibility(View.VISIBLE);
        }

        if (eventModel.getBtnClick().equals("3")) {
            rlAttend.setVisibility(View.GONE);
        } else if (response.getData().getAttend_status().equals("1")) {
            rlAttend.setVisibility(View.VISIBLE);
            tvAttend.setText(getString(R.string.un_attend));
        } else if (response.getData().getAttend_status().equals("2")) {
            rlAttend.setVisibility(View.VISIBLE);
            tvAttend.setText(getString(R.string.attend));
        }

        if (SharedPreferenceHelper.getInstance().getUserId().equals(eachEventDataModel.getUser_id())) {
            rlAttend.setVisibility(View.GONE);
        }

        if (eachEventDataModel.getUser_id().equals(SharedPreferenceHelper.getInstance().getUserId())) {
            ivEditEvent.setVisibility(View.VISIBLE);
            ivEditEvent.setEnabled(true);
        }

        if (eachEventDataModel.getEdit_status().equals("1")) {   // 1 for edit 2 for cannot edit
            ivEditEvent.setVisibility(View.VISIBLE);
            ivEditEvent.setEnabled(true);
        }

        if (eventModel.getBtnClick().equals("3")) {   // for past event 01 june
            ivEditEvent.setVisibility(View.GONE);
        }

        tvTicketUrl.setText(eachEventDataModel.getWebsite());
        tvContactNumber.setText(eachEventDataModel.getContact_number());

        if (eachEventDataModel.getUrl() == null || eachEventDataModel.getUrl().isEmpty()) {
            viewMusic.setVisibility(View.VISIBLE);
        } else {
            viewMusic.setVisibility(View.VISIBLE);
        }

        if (eachEventDataModel.getWebsite() == null || eachEventDataModel.getWebsite().isEmpty()) {
            rlTicketUrl.setEnabled(false);
            tvTicketUrl.setText("N/A");
        }

        if (eachEventDataModel.getPost_type().equals("1")) {
            tvTicketUrl.setText(eachEventDataModel.getUrl());
        }

        if (eachEventDataModel.getUrl() != null && !eachEventDataModel.getUrl().isEmpty()) {
            rlTicketUrl.setVisibility(View.VISIBLE);
            rlTicketUrl.setEnabled(true);
            rlTicketUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TicketWebActivity.start(getActivity(), eachEventDataModel.getUrl());
                }
            });
        }

        setEventInfoData(response.getData());
        setDataForDescActivity(response.getData());

        eventModel.setAttend_status(response.getData().getAttend_status());
    }

    @Override
    public void onSuccessAttendEvent(DataResponse response) {
        hideProgress();
        if (!isVisible()) {
            return;
        }
        ToastUtils.longToast(response.getMessage());
        getActivity().finish();
    }

    @Override
    public void onFailure(String message) {
        hideProgress();
        if (!isVisible()) {
            return;
        }
        ivEditEvent.setEnabled(false);
        rlLocation.setEnabled(false);
        ToastUtils.longToast(message);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        binder.unbind();
        binder = null;
    }

    @OnClick({R.id.rl_attend, R.id.tv_attend})
    public void onClickAttend() {
        showProgress();
        presentorAttendEvent.attendEvent(eventModel.getId());
    }

    @OnClick(R.id.rl_user_info)
    public void onClickUserInformation() {
        AttendingActivity.start(getActivity(), eventModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgress();
        presentorEachEventData.getEachEventData(eventModel.getId());
    }

    private void setEventInfoData(EachEventDataModel eachEventDataModel) {

        if (eachEventDataModel.getGuest_count().equals("0")) {
            tvGuestInfo.setText(getString(R.string.no_user));
        } else {
            tvGuestInfo.setText(eachEventDataModel.getGuest_count() + " Users attending this event");
        }

        tvAddress.setText(eachEventDataModel.getEvent_location());

        String formatedTime = Utils.stringToTime(eachEventDataModel.getEvent_time());
        formatedTime = formatedTime.replace("a.m.", "AM").replace("p.m.", "PM");
        formatedTime = formatedTime.replace("am", "AM").replace("pm", "PM");

        String formatedEndTime = Utils.stringToTime(eachEventDataModel.getEnd_time());
        formatedEndTime = formatedEndTime.replace("a.m.", "AM").replace("p.m.", "PM");
        formatedEndTime = formatedEndTime.replace("am", "AM").replace("pm", "PM");

        String sourceString = getString(R.string.event_will_start) + " " + "<font color='#000000'><b>"
                + formatedTime + "</b></font>";

        String eventStartTime = eachEventDataModel.getEvent_date2() + " " + formatedTime + "-" + formatedEndTime;

//        tvEventTime.setText(Html.fromHtml(sourceString));

        tvEventTime.setText(eventStartTime);

        tvViewers.setText(eachEventDataModel.getView_count()+" Users view this Events");

        if (eachEventDataModel.getView_count() == null || eachEventDataModel.getView_count().equals("")){
            tvViewers.setText("0 Users view this Events");
        }

        if (eventModel.getPublic_interest_id() != null
                && !eventModel.getPublic_interest_id().isEmpty()) {
            tvCategory.setText(eventModel.getPublic_interest_id());
        } else {
            tvCategory.setText(getString(R.string.no_category));
        }
        if (eventModel.getMusic_interest_id() != null
                && !eventModel.getMusic_interest_id().isEmpty()) {
            tvMusic.setText(eventModel.getMusic_interest_id());
        } else {
            tvMusic.setText(getString(R.string.no_music_category));
        }

        // for find motiv
        if (eventModel.getPublic_interest_id() == null) {
            if (eventModel.getPublic_interest_name() != null
                    && !eventModel.getPublic_interest_name().isEmpty()) {
                tvCategory.setText(eventModel.getPublic_interest_name());
            } else {
                tvCategory.setText(getString(R.string.no_category));
            }
        }

        if (eventModel.getMusic_interest_id() == null) {
            if (eventModel.getMusic_interest_name() != null
                    && !eventModel.getMusic_interest_name().isEmpty()) {
                tvMusic.setText(eventModel.getMusic_interest_name());
            } else {
                tvMusic.setText(getString(R.string.no_music_category));
            }
        }

        tvEventDesc.setText(eachEventDataModel.getDescription());

    }


    //     set data from fragment to activity
    private void setDataForDescActivity(EachEventDataModel eachEventDataModel) {

        if (eachEventDataModel.getEvent_theme_url() != null
                && !eachEventDataModel.getEvent_theme_url().isEmpty()) {
            Picasso.with(getActivity()).load(eachEventDataModel.getEvent_theme_url())
                    .placeholder(R.mipmap.img_background).error(R.mipmap.img_background).into(ivTheme);
        }

        if (eachEventDataModel.getTicket_price().equals("0")) {
            tvEventPriceHeader.setText(getString(R.string.free));
        } else {
            if (eachEventDataModel.getTicket_price().contains(".")) {
                tvEventPriceHeader.setText("£" + eachEventDataModel.getTicket_price());
            } else {
                tvEventPriceHeader.setText("£" + eachEventDataModel.getTicket_price());
            }
        }
        tvEventNameHeader.setText(eachEventDataModel.getEvent_name());
//        tvDateHeader.setText(Utils.stringToDateDay(eachEventDataModel.getEvent_date()));
        tvDateHeader.setText(eachEventDataModel.getEvent_date2());
        tvApprovalHeader.setText("Approved");

    }

}
