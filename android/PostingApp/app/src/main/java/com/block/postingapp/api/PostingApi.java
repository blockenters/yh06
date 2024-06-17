package com.block.postingapp.api;

import com.block.postingapp.model.PostingList;
import com.block.postingapp.model.Res;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PostingApi {

    // 포스팅 생성 API
    @Multipart
    @POST("/posting")
    Call<Res> addPosting(@Header("Authorization") String token,
                         @Part MultipartBody.Part image,
                         @Part("content") RequestBody content);

    // 친구들의 포스팅 리스트 가져오는 API
    @GET("/posting")
    Call<PostingList> getPostingList(@Header("Authorization") String token,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit);

}






