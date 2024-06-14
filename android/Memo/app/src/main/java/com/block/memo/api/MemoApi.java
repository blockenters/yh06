package com.block.memo.api;

import com.block.memo.model.MemoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface MemoApi {

    // 내 메모 리스트 가져오는 API
    @GET("/dev/memo")
    Call<MemoList> getMemoList(@Header("Authorization") String token);

}




