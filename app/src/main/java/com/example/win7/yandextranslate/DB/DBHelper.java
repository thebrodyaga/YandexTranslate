package com.example.win7.yandextranslate.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.win7.yandextranslate.BaseFragment;


public class DBHelper extends SQLiteOpenHelper {

    private static final String tableName = "myTable";
    public static SQLiteDatabase db;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MyLog", "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table " + tableName + " ("
                + "id integer primary key autoincrement,"
                + "isFavorite integer,"
                + "inText text,"
                + "outText text,"
                + "code text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void put(String inText, String outText) {//запись в таблицу
        String code = (BaseFragment.codeInLang + "-" + BaseFragment.codeOutLang).toUpperCase();
        ContentValues cv = new ContentValues();
        if (checkIsDataAlreadyInDBorNot(inText, code)) {
            cv.put("inText", inText);
            cv.put("outText", outText);
            cv.put("code", code);
            cv.put("isFavorite", 0);
            db.insert(tableName, null, cv);
            Log.d("MyLog", "новая запись в истории");
        } else Log.d("MyLog", "Запись есть в истории");

    }

    public static boolean checkIsDataAlreadyInDBorNot(String inText, String code) {//проверка на существование записи
        String Query = "Select * from " + tableName + " where inText = '" + inText + "' and code = '" + code + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    public static boolean isFavoriteImage(String inText, String code) {//проверка на избранную запись
        String Query = "Select * from " + tableName + " where inText = '" + inText + "' and code = '" + code + "'";
        Cursor cursor = db.rawQuery(Query, null);
        Log.d("MyLog", String.valueOf(cursor.getCount()));
        cursor.moveToFirst();
        int isFavorite = cursor.getInt(cursor.getColumnIndex("isFavorite"));
        if (isFavorite == 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public static void addFavoriteImage(String inText, String code) {//изменение записи на избранную
        String Query = "Select * from " + tableName + " where inText = '" + inText + "' and code = '" + code + "'";
        ContentValues cv = new ContentValues();
        cv.put("isFavorite", 1);
        db.update(tableName, cv, "inText = '" + inText + "' and code = '" + code + "'", null);
        Log.d("Mylog", inText + " добавили в избранное");
    }

    public static void removeFavoriteImage(String inText, String code) {//удаление записи из избранных
        String Query = "Select * from " + tableName + " where inText = '" + inText + "' and code = '" + code + "'";
        ContentValues cv = new ContentValues();
        cv.put("isFavorite", 0);
        db.update(tableName, cv, "inText = '" + inText + "' and code = '" + code + "'", null);
        Log.d("Mylog", inText + " убрали из избранного");
    }

    public static void clear() {//отчистка таблицы, сделано не очень. надо бы отдельно историю и избранное сохранять. не успел доделать
        db.delete("myTable", null, null);
    }
}