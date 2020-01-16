package com.zhangwx.z_utils.Z_DB.datedb;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.zhangwx.z_utils.MyApplication;
import com.zhangwx.z_utils.Z_Utils.AesUtil;

import java.util.ArrayList;
import java.util.List;

public class CorpusTestDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UnionLearning.db";
    static final String TABLE_CORPUS = "Corpus";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_DATE = "date";// YYYY-MM-DD HH:MM:SS
    static final String COLUMN_CONTENT = "content";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_CORPUS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATE + " INTEGER NOT NULL, " +
            COLUMN_CONTENT + " TEXT NOT NULL " + ")";

    CorpusTestDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public static List<String> query() {
        List<String> QUERY = new ArrayList<>();
        final Context context = MyApplication.getContext();
        final ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(CorpusTestProvider.CORPUS_URI,
                null,
                null,
                null,
                null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    QUERY.add(cursor.getString(cursor.getColumnIndex(CorpusTestDBHelper.COLUMN_ID)) + "  " +
                            cursor.getString(cursor.getColumnIndex(CorpusTestDBHelper.COLUMN_DATE)) + "  " +
                            AesUtil.getDecryptCorpus(cursor.getString(cursor.getColumnIndex(CorpusTestDBHelper.COLUMN_CONTENT))));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            safeClose(cursor);
        }
        return QUERY;
    }

    public static void insert(String content) {
        final Context context = MyApplication.getContext();
        final ContentResolver contentResolver = context.getContentResolver();
        ContentValues value = new ContentValues();
        value.put(CorpusTestDBHelper.COLUMN_CONTENT, AesUtil.getEncryptCorpus(content));
        value.put(CorpusTestDBHelper.COLUMN_DATE, System.currentTimeMillis());
        contentResolver.insert(CorpusTestProvider.CORPUS_URI, value);
    }

    public static int delete(long time) {
        final Context context = MyApplication.getContext();
        final ContentResolver contentResolver = context.getContentResolver();

        return contentResolver.delete(CorpusTestProvider.CORPUS_URI,
                CorpusTestDBHelper.COLUMN_DATE + " < " + time,
                null);
    }

    private static void safeClose(Cursor cursor) {
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
