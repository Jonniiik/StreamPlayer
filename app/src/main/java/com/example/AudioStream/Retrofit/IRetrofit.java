package com.example.AudioStream.Retrofit;

import com.example.AudioStream.Common.Common;
import com.example.AudioStream.ModeliTunesApi.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRetrofit {
    @GET(Common.API_SEARCH)
    Observable<Result> getMusic(@Query("term") String search);
}
