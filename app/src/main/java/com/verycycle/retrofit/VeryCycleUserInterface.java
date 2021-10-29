package com.verycycle.retrofit;



import com.verycycle.NotificationAct;
import com.verycycle.adapter.AdapterAssemble;
import com.verycycle.model.AddCard;
import com.verycycle.model.AssembleModel;
import com.verycycle.model.BookingDetailModel;
import com.verycycle.model.CycleModel;
import com.verycycle.model.EditCardModel;
import com.verycycle.model.GetCardModel;
import com.verycycle.model.HistoryModel;
import com.verycycle.model.NotificationModel;
import com.verycycle.model.PaymentModel;
import com.verycycle.model.PaymentSummaryModel;
import com.verycycle.model.ProblemModel;
import com.verycycle.model.ProviderModel;
import com.verycycle.model.RequestModel;
import com.verycycle.model.SignupModel;
import com.verycycle.model.SubProblmModel;

import java.util.Map;

import kotlin.jvm.internal.MagicApiIntrinsics;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VeryCycleUserInterface {



    @Multipart
    @POST("signup")
    Call<SignupModel> signupUser(
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("country_code") RequestBody country_code,
            @Part("password") RequestBody password,
            @Part("register_id") RequestBody register_id,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part file);






   /* @FormUrlEncoded
    @POST("signup")
    Call<SignupModel> signupUser(@FieldMap Map<String, String> params);*/


    @FormUrlEncoded
    @POST("login")
    Call<SignupModel> userLogin (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("forgot_password")
    Call<Map<String,String>> forgotPass (@FieldMap Map<String, String> params);




    @Multipart
    @POST("update_profile")
    Call<SignupModel> profileUpdate(
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("country_code") RequestBody country_code,
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("change_password")
    Call<Map<String,String>> changePassword(@FieldMap Map<String, String> params);




    @GET("get_cycle")
    Call<CycleModel> getCycleList();


    @FormUrlEncoded
    @POST("get_provider_list_nearbuy")
    Call<ProviderModel> getNearByProvider(@FieldMap Map<String, String> params);



    @Multipart
    @POST("booking_request")
    Call<Map<String,String>> sendRequest(
            @Part("cycle_id") RequestBody cycle_id,
            @Part("problem") RequestBody problem,
            @Part("sub_problem_id") RequestBody sub_problem_id,
            @Part("date") RequestBody date,
            @Part("time") RequestBody time,
            @Part("address") RequestBody address,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon,
            @Part("user_id") RequestBody user_id,
            @Part("provider_id") RequestBody provider_id,
            @Part("booktype") RequestBody service_type,
            @Part("amount") RequestBody amount,
            @Part("vat_amount") RequestBody vat_amount,
            @Part MultipartBody.Part file, @Part MultipartBody.Part file1);


    @FormUrlEncoded
    @POST("get_accept_user_request")
    Call<RequestModel> getAcceptReq(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_current_user_request")
    Call<RequestModel> sendReq(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_booking_detail")
    Call<BookingDetailModel> bookingDetails(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("chat_notification")
    Call<Map<String, String>> sendPushNotification(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_chat_count")
    Call<Map<String, String>> getChatCount(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("reset_chat_count")
    Call<Map<String, String>> resetChatCount(@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("get_latlon_driver")
    Call<Map<String,String>>  getDriverLocation(@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("logout")
    Call<Map<String,String>>  logout(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("get_payment_detail")
    Call<PaymentSummaryModel>  getPaymentSummary(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("add_rating")
    Call<Map<String,String>> giveRate(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_user_detail")
    Call<HistoryModel> getMyHistorty(@FieldMap Map<String, String> params);



    @GET("get_problem_details")
    Call<ProblemModel> getProblemList();


    @FormUrlEncoded
    @POST("get_sub_problem_details")
    Call<SubProblmModel>  getSubProblemList(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("edit_card_details")
    Call<EditCardModel> editCard(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("add_card_details")
    Call<AddCard> addCardss(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_card_details")
    Call<GetCardModel> getCardList(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("stripe_payment")
    Call<PaymentModel> payment(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("user_receive_request")
    Call<Map<String,String>>  estimateMethod(@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("get_notification")
    Call<NotificationModel> getNotification(@FieldMap Map<String, String> params);


    @GET("get_bike_list")
    Call<AssembleModel> getAllTypess();


    @Multipart
    @POST("add_bike_request")
    Call<Map<String,String>> addCarAsmReq(
            @Part("user_id") RequestBody user_id,
            @Part("bike_id") RequestBody bike_id,
            @Part("bike_name") RequestBody bike_name,
            @Part MultipartBody.Part file);




}
