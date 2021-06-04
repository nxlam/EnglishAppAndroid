package com.example.englishapp.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.englishapp.Model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategorySQLHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "EnglishDB.db";
    static final int DB_VERSION = 1;

    public CategorySQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tbl_category(" +
                "id integer primary key autoincrement," +
                "name text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Category> getAll(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("tbl_category", null, null, null, null, null, null);
        List<Category> list = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Category category = new Category(id, name);
            list.add(category);
        }
        return list;
    }

    public long addCategory(Category category){
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("tbl_category", null, values);
    }

    public int updateCategory(Category category){
        String whereClause = "id = ?";
        String[] whereArgs = {category.getId()+""};
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.update("tbl_category", values, whereClause, whereArgs);
    }

    public int deleteCategory(int id){
        String whereClause = "id = ?";
        String[] whereArgs = {id+""};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("tbl_category", whereClause, whereArgs);
    }
}
