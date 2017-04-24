package com.example.win7.yandextranslate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win7.yandextranslate.DB.DBHelper;


public class BaseFragment extends Fragment implements View.OnClickListener {
    EditText inText;
    TextView outText;
    TextView langIn;
    TextView langOut;
    ImageButton clear;
    ImageButton favoriteImage;

    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_IN_LANG = "inLang";
    public static final String APP_PREFERENCES_OUT_LANG = "outLang";

    public static String codeInLang = "en";
    public static String codeOutLang = "ru";

    final int REQUEST_CODE_IN_LANG = 1;
    final int REQUEST_CODE_OUT_LANG = 2;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        inText = (EditText) view.findViewById(R.id.inText);
        outText = (TextView) view.findViewById(R.id.outText);
        langIn = (TextView) view.findViewById(R.id.langIn);
        langOut = (TextView) view.findViewById(R.id.langOut);
        clear = (ImageButton) view.findViewById(R.id.clear);
        favoriteImage = (ImageButton) view.findViewById(R.id.favoriteImage);
        MyTextWatcher inputTextWatcher = new MyTextWatcher(inText, outText, clear, favoriteImage);//отслеживание записи текста
        inText.addTextChangedListener(inputTextWatcher);
        inText.setHorizontallyScrolling(false);
        inText.setLines(3);//максимальное кол-во видимых линий

        mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);//достает настройки языка текста и перевода
        if (mSettings.contains(APP_PREFERENCES_IN_LANG)) {
            langIn.setText(mSettings.getString(APP_PREFERENCES_IN_LANG, ""));
            codeInLang = getCodeByLang(mSettings.getString(APP_PREFERENCES_IN_LANG, ""));
        }
        if (mSettings.contains(APP_PREFERENCES_OUT_LANG)) {
            langOut.setText(mSettings.getString(APP_PREFERENCES_OUT_LANG, ""));
            codeOutLang = getCodeByLang(mSettings.getString(APP_PREFERENCES_OUT_LANG, ""));
        }

        view.findViewById(R.id.imageButton).setOnClickListener(this);
        clear.setOnClickListener(this);
        favoriteImage.setOnClickListener(this);
        langIn.setOnClickListener(this);
        langOut.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.langIn://выбор языка текста
                startActivityForResult(new Intent(getActivity(), InLangs.class), REQUEST_CODE_IN_LANG);
                break;
            case R.id.langOut://выбор языка перевода
                startActivityForResult(new Intent(getActivity(), OutLangs.class), REQUEST_CODE_OUT_LANG);
                break;
            case R.id.imageButton://меняет местами языки
                swap();
                break;
            case R.id.clear://отчистка
                inText.setText("");
                outText.setText("");
                clear.setVisibility(View.INVISIBLE);
                break;
            case R.id.favoriteImage://избранное
                String code = (BaseFragment.codeInLang + "-" + BaseFragment.codeOutLang).toUpperCase();
                if (checkImageResource(getActivity(), favoriteImage, R.drawable.ic_star_border_black_36dp))
                    {
                    favoriteImage.setImageResource(R.drawable.ic_star_black_36dp);
                    if (DBHelper.isFavoriteImage(inText.getText().toString(), code))
                        Toast.makeText(getActivity(), "Уже в избранном", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getActivity(), "В избранное", Toast.LENGTH_SHORT).show();
                        DBHelper.addFavoriteImage(inText.getText().toString(), code);
                    }
                }
                else {
                    favoriteImage.setImageResource(R.drawable.ic_star_border_black_36dp);
                    Toast.makeText(getActivity(), "Изъять из избранного", Toast.LENGTH_SHORT).show();
                    DBHelper.removeFavoriteImage(inText.getText().toString(), code);
                }
                break;
        }
    }

    private void swap() {
        String a = langIn.getText().toString();
        String b = langOut.getText().toString();
        a = a + b;
        b = a.substring(0, (a.length() - b.length()));
        a = a.substring(b.length());
        langIn.setText(a);
        langOut.setText(b);
        codeInLang = getCodeByLang(langIn.getText().toString());
        codeOutLang = getCodeByLang(langOut.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//ловим интент после выбора языка
        if (resultCode == Activity.RESULT_OK) {
            String strExtra;
            switch (requestCode) {
                case REQUEST_CODE_IN_LANG:
                    strExtra = data.getStringExtra("inLang");
                    if (strExtra.equals(langOut.getText().toString())) {//проверка на одиковый язык перевода и текста
                        langOut.setText(langIn.getText());
                    }
                    langIn.setText(strExtra);
                    break;
                case REQUEST_CODE_OUT_LANG:
                    strExtra = data.getStringExtra("outLang");
                    if (strExtra.equals(langIn.getText().toString())) {//проверка на одиковый язык перевода и текста
                        langIn.setText(langOut.getText());
                    }
                    langOut.setText(strExtra);
                    break;
            }
        }
        codeInLang = getCodeByLang(langIn.getText().toString());
        codeOutLang = getCodeByLang(langOut.getText().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Cycle", "Base onPause");
        mSettings.edit().putString(APP_PREFERENCES_IN_LANG, langIn.getText().toString()).apply();//сохранение настроек языка
        mSettings.edit().putString(APP_PREFERENCES_OUT_LANG, langOut.getText().toString()).apply();
    }


    private String getCodeByLang(String lang) {//список языков и коды сохранены как string array, тут ищу код
        int i = -1;
        for (String cc : getResources().getStringArray(R.array.langs)) {
            i++;
            if (cc.equals(lang))
                break;
        }
        return getResources().getStringArray(R.array.codes)[i];
    }

    public static boolean checkImageResource(Context ctx, ImageButton imageButton,
                                             int imageResource) {//метод проверка на изображений
        boolean result = false;

        if (ctx != null && imageButton != null && imageButton.getDrawable() != null) {
            Drawable.ConstantState constantState;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = ctx.getResources()
                        .getDrawable(imageResource, ctx.getTheme())
                        .getConstantState();
            } else {
                constantState = ctx.getResources().getDrawable(imageResource)
                        .getConstantState();
            }
            if (imageButton.getDrawable().getConstantState() == constantState) {
                result = true;
            }
        }
        return result;
    }

}
