package com.husseinsilver.store.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    /** Fetches XAG and ILS rates relative to USD base. */
    @GET("latest")
    Call<SilverPriceResponse> getSilverPrice(
            @Query("access_key") String accessKey,
            @Query("base") String base,
            @Query("symbols") String symbols
    );
}
