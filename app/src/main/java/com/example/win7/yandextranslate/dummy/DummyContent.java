package com.example.win7.yandextranslate.dummy;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.win7.yandextranslate.DB.DBHelper.db;

public class DummyContent {
    private List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public List<DummyItem> getAll() {
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("myTable", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int isFavoriteTextColIndex = c.getColumnIndex("isFavorite");
            int inTextColIndex = c.getColumnIndex("inText");
            int outTextColIndex = c.getColumnIndex("outText");
            int codeTextColIndex = c.getColumnIndex("code");
            do {
                ITEMS.add(0, new DummyItem(
                        c.getInt(isFavoriteTextColIndex),
                        c.getString(inTextColIndex),
                        c.getString(outTextColIndex),
                        c.getString(codeTextColIndex)));

            } while (c.moveToNext());
        } else
            Log.d("MyLog", "0 rows");
        c.close();
        //db.close();
        return ITEMS;
    }

    public List<DummyItem> getFavorite() {
        String Query = "Select * from myTable where isFavorite = '1'";
        Cursor c = db.rawQuery(Query, null);
        if (c.moveToFirst()) {
            int isFavoriteTextColIndex = c.getColumnIndex("isFavorite");
            int inTextColIndex = c.getColumnIndex("inText");
            int outTextColIndex = c.getColumnIndex("outText");
            int codeTextColIndex = c.getColumnIndex("code");
            do {
                ITEMS.add(0, new DummyItem(
                        c.getInt(isFavoriteTextColIndex),
                        c.getString(inTextColIndex),
                        c.getString(outTextColIndex),
                        c.getString(codeTextColIndex)));

            } while (c.moveToNext());
        } else
            Log.d("MyLog", "0 rows");
        c.close();
        //db.close();
        return ITEMS;
    }

    public static class DummyItem {
        public final int isFavorite;
        public final String inText;
        public final String outText;
        public final String code;

        public DummyItem(int isFavorite, String inText, String outText, String code) {
            this.isFavorite = isFavorite;
            this.inText = inText;
            this.outText = outText;
            this.code = code;
        }
    }
}
