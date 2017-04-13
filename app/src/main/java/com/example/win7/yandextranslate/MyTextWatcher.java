package com.example.win7.yandextranslate;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyTextWatcher implements TextWatcher {

    private String key = "trnsl.1.1.20170407T160021Z.695b525a130b6a4e.63537242677666d108ccc81fe6364f3d8f5eab18";

    private EditText inText;
    private TextView outText;

    public MyTextWatcher(EditText inText, TextView outText) {
        super();
        this.inText = inText;
        this.outText = outText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (inText.getText().length() > 1) {
            Log.d("My_log",inText.getText().toString());
            App.getApi().getData(key, inText.getText().toString(), "en-ru").enqueue(new Callback<TranslateModel>() {
                @Override
                public void onResponse(Call<TranslateModel> call, Response<TranslateModel> response) {
                    //Данные успешно пришли, но надо проверить response.body() на null
                    outText.setText(response.body().getText().get(0));
                    Log.d("My_log",response.body().getText().get(0));
                }

                @Override
                public void onFailure(Call<TranslateModel> call, Throwable t) {
                    //Произошла ошибка
                    Log.d("My_log",t.toString());
                }
            });
        }
    }
}
