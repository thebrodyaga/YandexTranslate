package com.example.win7.yandextranslate;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface YandexTranslateApi {
    @GET("/api/v1.5/tr.json/translate")
    Call<TranslateModel> getData(@Query("key") String key, @Query("text") String text, @Query("lang") String lang);
}
