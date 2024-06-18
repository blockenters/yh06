package com.block.postingtabbar.api;


import com.block.postingtabbar.model.User;
import com.block.postingtabbar.model.UserRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    // 로그인 API
    @POST("/user/login")
    Call<UserRes> login(@Body User user);

    // 회원가입 API
    @POST("/user/register")
    Call<UserRes> register(@Body User user);
}







