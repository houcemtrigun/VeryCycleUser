package com.verycycle.retrofit;



import com.verycycle.model.SignupModel;

import java.util.Map;

import kotlin.jvm.internal.MagicApiIntrinsics;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VeryCycleUserInterface {



 /*   @Multipart
    @POST("signup")
    Call<SignupModel> signupUser(
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("register_id") RequestBody register_id,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part file);*/


    @FormUrlEncoded
    @POST("signup")
    Call<SignupModel> signupUser(@FieldMap Map<String, String> params);


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



}
