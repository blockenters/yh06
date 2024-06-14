package com.block.memo.api;

import com.block.memo.model.Memo;
import com.block.memo.model.MemoList;
import com.block.memo.model.Res;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MemoApi {

    // 내 메모 리스트 가져오는 API
    @GET("/dev/memo")
    Call<MemoList> getMemoList(@Header("Authorization") String token);

    // 메모 생성 API
    @POST("/dev/memo")
    Call<Res> addMemo(@Header("Authorization") String token, @Body Memo memo);

}




