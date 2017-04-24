package com.example.win7.yandextranslate;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.win7.yandextranslate.DB.DBHelper;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyTextWatcher implements TextWatcher {

    private String key = "trnsl.1.1.20170407T160021Z.695b525a130b6a4e.63537242677666d108ccc81fe6364f3d8f5eab18";

    private EditText inText;
    private TextView outText;
    private ImageButton clear;
    private ImageButton favoriteImage;

    private Timer timer = new Timer();

    public MyTextWatcher(EditText inText, TextView outText, ImageButton clear, ImageButton favoriteImage) {
        super();
        this.inText = inText;
        this.outText = outText;
        this.clear = clear;
        this.favoriteImage = favoriteImage;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        clear.setVisibility(View.VISIBLE);
        timer.cancel();
        favoriteImage.setVisibility(View.INVISIBLE);
        favoriteImage.setImageResource(R.drawable.ic_star_border_black_36dp);
        timer = new Timer();
        long DELAY = 1000;
        if (inText.length() > 1) {//ограничил т.к. при inText.length()=0 срабатывает
            timer.schedule( //задержка на 1сек, чтоб не спамило каждую букву
                    new TimerTask() {
                        @Override
                        public void run() {
                            Log.d("My_log", inText.getText().toString());
                            //retrofit начал запрос
                            App.getApi().getData(key, inText.getText().toString(),
                                    BaseFragment.codeInLang + "-" + BaseFragment.codeOutLang).enqueue(new Callback<TranslateModel>() {
                                @Override
                                public void onResponse(Call<TranslateModel> call, Response<TranslateModel> response) {
                                    outText.setText(response.body().getText().get(0));
                                    Log.d("My_log", response.body().getText().get(0));
                                    DBHelper.put(inText.getText().toString(), response.body().getText().get(0));//запись в бд
                                    favoriteImage.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onFailure(Call<TranslateModel> call, Throwable t) {
                                    //Произошла ошибка
                                    Log.d("My_log", t.toString());
                                }
                            });
                        }
                    },
                    DELAY
            );
        }

        if (inText.getText().length() == 0) {
            clear.setVisibility(View.INVISIBLE);
            favoriteImage.setVisibility(View.INVISIBLE);
        }
    }
}
