package com.app.motiv.data;

import com.app.motiv.data.model.BadgeModel;
import com.app.motiv.data.model.CommentModel;
import com.app.motiv.data.model.EachEventDataModel;
import com.app.motiv.data.model.EventDataModel;
import com.app.motiv.data.model.EventModel;
import com.app.motiv.data.model.FriendCountModel;
import com.app.motiv.data.model.FriendListModel;
import com.app.motiv.data.model.FriendModel;
import com.app.motiv.data.model.GuestsModel;
import com.app.motiv.data.model.InboxChatModel.InboxChatModel;
import com.app.motiv.data.model.InterestModel;
import com.app.motiv.data.model.InvitationListModel;
import com.app.motiv.data.model.LoginModel;
import com.app.motiv.data.model.NotificationListModel;
import com.app.motiv.data.model.PendingModel;
import com.app.motiv.data.model.SearchFriendModel;
import com.app.motiv.data.model.SearchInviteModel;
import com.app.motiv.data.model.chatListModel.ChatListModel;
import com.app.motiv.data.model.createEventDocList.CreateEventDocListModel;
import com.app.motiv.data.shared.CommonResponse;
import com.app.motiv.data.shared.DataResponse;
import com.app.motiv.data.shared.PageResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by 123 on 12-Feb-18.
 */

public interface InterfaceApi {

    //        String BASE_URL = "http://14.141.82.38/webservices/sim/public/api/"; // local url
    String BASE_URL = "http://175.176.186.116/webservices/sim_admin/public/api/v2/";  // local url new
//    String BASE_URL = "http://54.206.13.240/sim_admin/public/api/v2/";  // live url

    @Multipart
    @POST("signup")
    Call<DataResponse> signupEventOrganizer(@Part("name") RequestBody name, @Part("user_name") RequestBody userName,
                                            @Part("email") RequestBody email, @Part("password") RequestBody password,
                                            @Part("phone_number") RequestBody phoneNum,
                                            @Part("age") RequestBody age, @Part("signup_type") RequestBody signupType,
                                            @Part("user_type") RequestBody userType, @Part MultipartBody.Part... args);

    @Multipart
    @POST("signup")
    Call<DataResponse> signupNormalUser(@Part("name") RequestBody name, @Part("user_name") RequestBody userName,
                                        @Part("email") RequestBody email,
                                        @Part("password") RequestBody password, @Part("phone_number") RequestBody phoneNum,
                                        @Part("age") RequestBody age, @Part("signup_type") RequestBody signupType,
                                        @Part("user_type") RequestBody userType, @Part("public_interest") RequestBody publicInterest,
                                        @Part("music_interest") RequestBody musicInterest, @Part("refferal_user_code") RequestBody referralCode,
                                        @Part MultipartBody.Part... args);

    @FormUrlEncoded
    @POST("signup")
    Call<DataResponse> socialSignupNormalUser(@Field("name") String name, @Field("user_name") String userName,
                                              @Field("email") String email,
                                              @Field("password") String password, @Field("phone_number") String phoneNum,
                                              @Field("age") String age, @Field("signup_type") String signupType,
                                              @Field("social_signup_type") String socialSignupType, @Field("social_id") String socialId,
                                              @Field("user_type") String userType, @Field("public_interest") String publicInterest,
                                              @Field("music_interest") String musicInterest,
                                              @Field("image") String image, @Field("refferal_user_code") String referralCode);

    @FormUrlEncoded
    @POST("signup")
    Call<DataResponse> socialSignupEventOrganizer(@Field("name") String name, @Field("user_name") String userName,
                                                  @Field("email") String email,
                                                  @Field("password") String password, @Field("phone_number") String phoneNum,
                                                  @Field("age") String age, @Field("signup_type") String signupType,
                                                  @Field("social_signup_type") String socialSignupType, @Field("social_id") String socialId,
                                                  @Field("user_type") String userType, @Field("image") String image);

    @FormUrlEncoded
    @POST("signin")
        Call<DataResponse<LoginModel>> signin(@Field("email") String email, @Field("password") String password,
                                          @Field("device_token") String deviceToken, @Field("device_type") String deviceType);

    @FormUrlEncoded
    @POST("socialLogin")
    Call<DataResponse<LoginModel>> socialSignin(@Field("social_id") String socialId, @Field("social_signup_type") String socialSignupType,
                                                @Field("device_token") String deviceToken, @Field("device_type") String deviceType);

    @FormUrlEncoded
    @POST("logout")
    Call<DataResponse> logout(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("checkEmailExist")
    Call<DataResponse> checkExistEmail(@Field("email") String email);

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<DataResponse> forgotPassword(@Field("email") String email);

    @POST("musicInterestList")
    Call<PageResponse<InterestModel>> getMusicInterestList();

    @POST("publicInterestList")
    Call<PageResponse<InterestModel>> getPublicInterestList();

    @Multipart
    @POST("update_profile")
    Call<DataResponse<LoginModel>> updateProfile(@Part("name") RequestBody name, @Part("email") RequestBody email,
                                                 @Part("phone_number") RequestBody phoneNumber, @Part MultipartBody.Part... args);


    @Multipart
    @POST("createPost")
    Call<DataResponse> createPost(@Part("user_type") RequestBody userType, @Part("text") RequestBody text,
                                  @Part("event_id") RequestBody eventId, @Part("post_media_type") RequestBody postMediaType,
                                  @Part MultipartBody.Part image, @Part MultipartBody.Part video);

    @Multipart
    @POST("createEvent")
    Call<DataResponse> createEventNormalUser(@Part("user_type") RequestBody userType, @Part("event_name") RequestBody event_name,
                                             @Part("location") RequestBody location, @Part("event_lat") RequestBody eventLat,
                                             @Part("event_long") RequestBody eventLong, @Part("date") RequestBody date,
                                             @Part("time") RequestBody time, @Part("event_detail") RequestBody eventDetail,
                                             @Part("event_media_type") RequestBody eventMediaType,
                                             @Part("post_type") RequestBody postType, @Part("event_date") RequestBody eventDate,
                                             @Part("end_time") RequestBody endTime,
                                             @Part("repeat_interval") RequestBody repeatInterval, @Part("friend_id") RequestBody friendId,
                                             @Part("website") RequestBody websiteUrl, @Part("contact_number") RequestBody contactNumber,
                                             @Part MultipartBody.Part image1, @Part MultipartBody.Part eventTheme,
                                             @Part MultipartBody.Part image, @Part MultipartBody.Part video);

    @Multipart
    @POST("createEvent")
    Call<DataResponse> createEventEventOrganizer(@Part("user_type") RequestBody userType, @Part("event_name") RequestBody event_name,
                                                 @Part("location") RequestBody location, @Part("event_lat") RequestBody eventLat,
                                                 @Part("event_long") RequestBody eventLong, @Part("date") RequestBody date,
                                                 @Part("time") RequestBody time, @Part("event_detail") RequestBody eventDetail,
                                                 @Part("event_media_type") RequestBody eventMediaType,
                                                 @Part("post_type") RequestBody postType, @Part("ticket_price") RequestBody ticketPrice,
                                                 @Part("dress_code") RequestBody dressCode, @Part("age_restrictions") RequestBody ageRestriction,
                                                 @Part("id_Required") RequestBody idRequired, @Part("public_interest") RequestBody publicInterest,
                                                 @Part("music_interest") RequestBody musicInterest, @Part("url") RequestBody url,
                                                 @Part("event_date") RequestBody eventDate, @Part("end_time") RequestBody endTime,
                                                 @Part("repeat_interval") RequestBody repeatInterval,
                                                 @Part MultipartBody.Part image1,
                                                 @Part MultipartBody.Part image, @Part MultipartBody.Part video);

    @FormUrlEncoded
    @POST("contact_us")
    Call<DataResponse> sendTextToContact(@Field("text") String text);

    @FormUrlEncoded
    @POST("change_password")
    Call<DataResponse> changePassword(@Field("new_password") String newPassword, @Field("old_password") String oldPassword);

    @POST("friend_list")
    Call<PageResponse<FriendListModel>> getFriendList();

    @FormUrlEncoded
    @POST("comment_list")
    Call<PageResponse<CommentModel>> getCommentList(@Field("post_id") String postId);

    @FormUrlEncoded
    @POST("add_comment")
    Call<DataResponse> addComment(@Field("post_id") String postId, @Field("comment") String comment);

    @FormUrlEncoded
    @POST("event_list")
    Call<DataResponse<EventDataModel>> getEventList(@Field("event_time") String eventTime, @Query("page") int currentPage);

    @POST("get_music_interest_list")
    Call<PageResponse<InterestModel>> getUserMusicInterest();

    @FormUrlEncoded
    @POST("update_music_interest_list")
    Call<DataResponse> updateMusicInterest(@Field("music_interest_id") String musicId);

    @POST("get_public_interest_list")
    Call<PageResponse<InterestModel>> getUserPublicInterest();

    @FormUrlEncoded
    @POST("update_public_interest_list")
    Call<DataResponse> updatePublicInterest(@Field("public_interest_id") String publicId);

    @FormUrlEncoded
    @POST("each_event_list")
    Call<DataResponse<EachEventDataModel>> getEachEventData(@Field("event_id") String eventId);

    @FormUrlEncoded
    @POST("guest_list")
    Call<PageResponse<GuestsModel>> getGuestList(@Field("event_id") String eventId);

    @FormUrlEncoded
    @POST("like_post")
    Call<DataResponse> likeUnlikePost(@Field("post_id") String postId, @Field("like_status") String likeStatus);

    @FormUrlEncoded
    @POST("post_list")
    Call<DataResponse<EventDataModel>> getPostList(@Field("event_id") String eventId, @Query("page") int currentPage);

    @FormUrlEncoded
    @POST("attend_event")
    Call<DataResponse> attendEvent(@Field("event_id") String eventId);

    @POST("invitation_list")
    Call<PageResponse<InvitationListModel>> getInvitationList();

    @FormUrlEncoded
    @POST("accept_decline_invitation")
    Call<DataResponse> acceptDeclineInvitation(@Field("invitation_id") String invitationId, @Field("accept_status") String status);

    @FormUrlEncoded
    @POST("send_invitation")
    Call<DataResponse> sendInvitation(@Field("receiver_id") String receiverId, @Field("event_id") String eventId);

    @FormUrlEncoded
    @POST("search_invite")
    Call<DataResponse<SearchInviteModel>> searchInvite(@Field("name") String name, @Field("event_id") String eventId, @Query("page") int currentPage);

    @FormUrlEncoded
    @POST("cancel_invitation")
    Call<DataResponse> cancelInvitation(@Field("invitation_id") String invitationId);

    @FormUrlEncoded
    @POST("search_friends")
    Call<DataResponse<SearchFriendModel>> searchFriend(@Field("name") String name, @Query("page") int currentPage);

    @FormUrlEncoded
    @POST("send_request")
    Call<DataResponse> sendFriendRequest(@Field("receiver_id") String receiverId);

    @FormUrlEncoded
    @POST("cancel_request")
    Call<DataResponse> cancelFriendRequest(@Field("request_id") String requestId);

    @POST("notification_list")
    Call<PageResponse<NotificationListModel>> getNotificationList();

    @POST("pending_list")
    Call<PageResponse<PendingModel>> getPendingList();

    @FormUrlEncoded
    @POST("accept_decline")
    Call<DataResponse> acceptDecline(@Field("request id") String requestId, @Field("accept_status") String acceptStatus);


    @FormUrlEncoded
    @POST("search_event")
    Call<PageResponse<EventModel>> searchEvent(@Field("name") String name, @Field("filter") String filter, @Field("miles") String miles,
                                               @Field("lat") String lat, @Field("long") String longt, @Field("public_interest") String publicInterest,
                                               @Field("music_interest") String musicInterest);

    @POST("get_profile_counts")
    Call<DataResponse<FriendCountModel>> getFriendCount();

    @POST("create_event_doc_list")
    Call<DataResponse<CreateEventDocListModel>> getCreateEventDocList();

    @FormUrlEncoded
    @POST("get_profile")
    Call<DataResponse<LoginModel>> getProfile(@Field("friend_id") String id);

    @Multipart
    @POST("edit_event")
    Call<DataResponse> editEventEventOrganizer(@Part("event_id") RequestBody eventId, @Part("user_type") RequestBody userType, @Part("event_name") RequestBody event_name,
                                               @Part("location") RequestBody location, @Part("event_lat") RequestBody eventLat,
                                               @Part("event_long") RequestBody eventLong, @Part("date") RequestBody date,
                                               @Part("time") RequestBody time, @Part("event_detail") RequestBody eventDetail,
                                               @Part("event_media_type") RequestBody eventMediaType,
                                               @Part("ticket_price") RequestBody ticketPrice,
                                               @Part("dress_code") RequestBody dressCode, @Part("age_restrictions") RequestBody ageRestriction,
                                               @Part("id_Required") RequestBody idRequired, @Part("public_interest") RequestBody publicInterest,
                                               @Part("music_interest") RequestBody musicInterest, @Part("url") RequestBody url,
                                               @Part("event_date") RequestBody eventDate, @Part("end_time") RequestBody endTime,
                                               @Part("repeat_interval") RequestBody repeatInterval,
                                               @Part MultipartBody.Part image1,
                                               @Part MultipartBody.Part image, @Part MultipartBody.Part video);
//    , @Part MultipartBody.Part video ,  @Part("post_type") RequestBody postType,

    @Multipart
    @POST("edit_event")
    Call<DataResponse> editEventNormalUser(@Part("event_id") RequestBody eventId, @Part("user_type") RequestBody userType, @Part("event_name") RequestBody event_name,
                                           @Part("location") RequestBody location, @Part("event_lat") RequestBody eventLat,
                                           @Part("event_long") RequestBody eventLong, @Part("date") RequestBody date,
                                           @Part("time") RequestBody time, @Part("event_detail") RequestBody eventDetail,
                                           @Part("event_media_type") RequestBody eventMediaType,
                                           @Part("event_date") RequestBody eventDate, @Part("end_time") RequestBody endTime,
                                           @Part("repeat_interval") RequestBody repeatInterval, @Part("friend_id") RequestBody friendId,
                                           @Part("website") RequestBody websiteUrl, @Part("contact_number") RequestBody contactNumber,
                                           @Part MultipartBody.Part image, @Part MultipartBody.Part eventTheme, @Part MultipartBody.Part video);

    @FormUrlEncoded
    @POST("get_chat")
    Call<PageResponse<ChatListModel>> getChatList(@Field("user_id") String userId, @Field("friend_id") String friendId);

    @FormUrlEncoded
    @POST("send_message")
    Call<CommonResponse> sendMessage(@Field("user_id") String userId, @Field("friend_id") String friendId,
                                     @Field("message") String message, @Field("msg_type") String msg_type);

    @FormUrlEncoded
    @POST("recent_chat")
    Call<PageResponse<InboxChatModel>> inboxChat(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("add_co_admin")
    Call<DataResponse> addCoAdmin(@Field("event_id") String eventId, @Field("admin_id") String adminId);

    @POST("badge_count")
    Call<DataResponse<BadgeModel>> getBadge();

    @FormUrlEncoded
    @POST("report_event")
    Call<DataResponse> reportEvent(@Field("event_id") String eventId, @Field("message") String message);

    @POST("block_user_list")
    Call<PageResponse<FriendModel>> getBlockUserList();

    @FormUrlEncoded
    @POST("block_unblock_user")
    Call<DataResponse> blockUnblockUser(@Field("user_id") String userId, @Field("friend_id") String friendId);
}
