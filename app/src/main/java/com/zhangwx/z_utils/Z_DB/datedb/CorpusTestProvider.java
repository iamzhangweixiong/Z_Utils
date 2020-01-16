package com.zhangwx.z_utils.Z_DB.datedb;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhangwx.z_utils.BuildConfig;

public class CorpusTestProvider extends ContentProvider {

    public static final String UL_CORPUS_AUTHORITY = BuildConfig.APPLICATION_ID + ".CorpusTestProvider";
    public static final Uri CORPUS_URI = Uri.parse("content://" + UL_CORPUS_AUTHORITY);

    private CorpusTestDBHelper mCorpusTestDBHelper;

    @Override
    public boolean onCreate() {
        mCorpusTestDBHelper = new CorpusTestDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(CorpusTestDBHelper.TABLE_CORPUS);
        final SQLiteDatabase db = mCorpusTestDBHelper.getReadableDatabase();
        return sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mCorpusTestDBHelper.getWritableDatabase();
        db.insertWithOnConflict(CorpusTestDBHelper.TABLE_CORPUS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mCorpusTestDBHelper.getWritableDatabase();
        return db.delete(CorpusTestDBHelper.TABLE_CORPUS, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}