package com.example.win7.yandextranslate;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {

    private static YandexTranslateApi yaTranslate;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        yaTranslate = retrofit.create(YandexTranslateApi.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static YandexTranslateApi getApi() {
        return yaTranslate;
    }
}
