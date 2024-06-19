package com.block.placeapp.api;

import com.block.placeapp.model.PlaceList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceApi {

    // 키워드 기반으로 장소 가져오는 API
    @GET("/maps/api/place/nearbysearch/json")
    Call<PlaceList> getPlaceList(@Query("language") String language,
                                 @Query("location") String location,
                                 @Query("radius") int radius,
                                 @Query("key") String key,
                                 @Query("keyword") String keyword);

}


