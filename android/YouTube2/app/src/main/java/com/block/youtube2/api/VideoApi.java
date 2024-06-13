package com.block.youtube2.api;

import com.block.youtube2.model.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoApi {

    // 유튜브에 검색하는 API

    // HTTP Method 써주고, 그 안에는 경로를 써준다.
    @GET("/youtube/v3/search")
    Call<VideoList> searchVideo(@Query("key") String key,
                                @Query("part") String part,
                                @Query("maxResults") int maxResults,
                                @Query("order") String order,
                                @Query("type") String type,
                                @Query("q") String q);
    // 함수의 리턴 데이터 타입은, Call 안에, 응답으로 받을 클래스를 넣어준다.
}






