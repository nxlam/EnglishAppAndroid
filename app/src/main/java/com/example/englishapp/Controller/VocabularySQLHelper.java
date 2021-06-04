package com.example.englishapp.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.englishapp.Model.Vocabulary;

import java.util.ArrayList;
import java.util.List;

public class VocabularySQLHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "EnglishWordDB.db";
    static final int DB_VERSION = 1;

    public VocabularySQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tbl_vocabulary(" +
                "id integer primary key autoincrement," +
                "categoryID integer," +
                "engsub text," +
                "vietsub text," +
                "img blob)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Vocabulary> getAll(int categoryID){
        String whereClause = "categoryID = ?";
        String[] whereArgs = {categoryID+""};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("tbl_vocabulary", null, whereClause, whereArgs, null, null, null);
        List<Vocabulary> list = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String engsub = cursor.getString(2);
            String vietsub = cursor.getString(3);
            byte[] img = cursor.getBlob(4);
            Vocabulary vocabulary = new Vocabulary(id, categoryID, engsub, vietsub, img);
            list.add(vocabulary);
        }
        return list;
    }

    public int countVocabulary(int categoryID){
        List<Vocabulary> list = getAll(categoryID);
        if (list == null)
            return 0;
        else return list.size();
    }

    public long addVocabulary(Vocabulary vocabulary){
        ContentValues values = new ContentValues();
        values.put("categoryID", vocabulary.getCategoryID());
        values.put("img", vocabulary.getImg());
        values.put("engsub", vocabulary.getEngsub());
        values.put("vietsub", vocabulary.getVietsub());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("tbl_vocabulary", null, values);
    }

    public int updateVocabulary(Vocabulary vocabulary){
        String whereClause = "id = ?";
        String[] whereArgs = {vocabulary.getId()+""};
        ContentValues values = new ContentValues();
        values.put("categoryID", vocabulary.getCategoryID());
        values.put("img", vocabulary.getImg());
        values.put("engsub", vocabulary.getEngsub());
        values.put("vietsub", vocabulary.getVietsub());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.update("tbl_vocabulary", values, whereClause, whereArgs);
    }

    public int deleteVocabulary(int id){
        String whereClause = "id = ?";
        String[] whereArgs = {id+""};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("tbl_vocabulary", whereClause, whereArgs);
    }

    public List<Vocabulary> searchVocabulary(int categoryID, String key){
        String whereClause = "engsub like ?";
        String[] whereArgs = {"%"+key+"%"};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("tbl_vocabulary", null, whereClause, whereArgs, null, null, null);
        List<Vocabulary> list = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            categoryID = cursor.getInt(1);
            String engsub = cursor.getString(2);
            String vietsub = cursor.getString(3);
            byte[] img = cursor.getBlob(4);
            Vocabulary vocabulary = new Vocabulary(id, categoryID, engsub, vietsub, img);
            list.add(vocabulary);
        }
        return list;
    }
}
