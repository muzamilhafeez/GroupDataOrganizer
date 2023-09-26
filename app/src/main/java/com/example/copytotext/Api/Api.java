package com.example.copytotext.Api;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.copytotext.model.UserDetail;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    public static String
            IMAGE_BASE_URL="https://script.google.com/macros/s/AKfycbzUzA5whKq9_386v0sRsIfo0XsfZepZ_jziQK37-lcUkPL5CjnF-ThFvR9gdBA-RURcqQ/";
    public static String
            BASE_URL="https://script.google.com/macros/s/AKfycbzKtKay07beoBfTHDKRrH1aj1Q7U8_3CkU4l-KfAg6oymIZN6wvuZ5eiOaI6CZ2uARq/";

//    @POST("Customer/SaveCustomer")
//    public Call<String> saveCustomer(@Body Customer c);
//
//    @GET("math/getuser")
//    public Call<User> getUser();
//
//    @GET("math/getusers")
//    public Call<List<User>> getUsers();
//
    @POST("exec")
    public Call<String> NewUserAdd(@Query("action") String action, @Query("name") String name, @Query("city") String city, @Query("start") String start, @Query("end") String end, @Query("duratio") String duration, @Query("fee") String fee, @Query("phone") String phone,@Body RequestBody requestBodyBinary);

    @POST("exec")
    public Call<String> NewUserAdd(@Query("action") String action, @Query("name") String name, @Query("city") String city, @Query("start") String start, @Query("end") String end, @Query("duratio") String duration, @Query("fee") String fee, @Query("phone") String phone);

    @POST("exec")
    public Call<String> UserAddUpdate(@Query("action") String action,@Query("id") String id, @Query("name") String name, @Query("city") String city, @Query("start") String start, @Query("end") String end, @Query("duratio") String duration, @Query("fee") String fee, @Query("phone") String phone,@Body RequestBody requestBodyBinary);

    @POST("exec")
    public Call<String> UserAddUpdate(@Query("action") String action,@Query("id") String id, @Query("name") String name, @Query("city") String city, @Query("start") String start, @Query("end") String end, @Query("duratio") String duration, @Query("fee") String fee, @Query("phone") String phone);

    @POST("exec")
    public Call<String> ImageSend(@Query("action") String action, @Body RequestBody requestBody);
    @GET("exec")
    public Call<String> getAllUserDeatils(@Query("action") String action);

    @GET("exec")
    public Call<String> UserDelete(@Query("action") String action,@Query("id") String id);
//
//    @GET("Customer/getAllProfiles")
//    public Call<ArrayList<UserProfileInfo>> getAllProfile();
//
//    @Multipart
//    @POST("api/mark-attendance")
//    public Call<ArrayList<Attendance>> mark_attendance(
//            @Part MultipartBody.Part file);
//
//    @Multipart
//    @POST("Customer/uploadImage")
//    public Call<String> saveUserProfile
//            (
//                    @Part ArrayList<MultipartBody.Part> images,
//                    @Part("id") RequestBody id,
//                    @Part("lname") RequestBody lname,
//                    @Part("fname") RequestBody fname,
//                    @Part("address") RequestBody address,
//                    @Part("gender") RequestBody g
//            );
    @NonNull
    public default MultipartBody.Part prepareFilePart(String partName, Uri fileUri, Context context) throws IOException {
        File file = FileUtil.from(context, fileUri);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(context.getContentResolver().getType(fileUri)),
                        file
                );
        return MultipartBody.Part.createFormData(partName,
                file.getName(),
                requestFile);
    }
    public default RequestBody createPartFromString(String descriptionString){
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);
        return  description;
    }
}