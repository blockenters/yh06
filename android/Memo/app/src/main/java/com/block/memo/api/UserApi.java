package com.block.memo.api;

import com.block.memo.model.User;
import com.block.memo.model.UserRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    // 회원가입 API
    // 함수명을 작성해주고, 보낼 데이터는 파라미터에 작성, 받을 데이터는 리턴타입에 작성.
    @POST("/dev/user/register")
    Call<UserRes> register(@Body User user);

    // 로그인 API

    // 로그아웃 API
}
