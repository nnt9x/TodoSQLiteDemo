package com.bkacad.nnt.todosqlitedemo.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bkacad.nnt.todosqlitedemo.db.DAO;
import com.bkacad.nnt.todosqlitedemo.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoDAO implements DAO<Todo> {

    private DBHelper dbHelper;

    public TodoDAO(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @Override
    public List<Todo> all() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM todos";
        Cursor cursor = db.rawQuery(sql, null);
        // Đưa con trỏ về đầu hàng
        List<Todo> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex("id");
            int titleIndex = cursor.getColumnIndex("title");
            do{
                Todo item = new Todo();
                // Lấy dữ liệu từ Sqlite -> set giá trị item
                item.setId(cursor.getLong(idIndex));
                item.setTitle(cursor.getString(titleIndex));

                // Push item này vào arraylist (todo)
                list.add(item);
            }
            while(cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    @Override
    public Todo get(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM todos WHERE id = "+id;
        Cursor cursor = db.rawQuery(sql, null);
        Todo item = null;
        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex("id");
            int titleIndex = cursor.getColumnIndex("title");
            item  = new Todo();
            item.setId(cursor.getLong(idIndex));
            item.setTitle(cursor.getString(titleIndex));
        }
        return item;
    }

    @Override
    public long create(Todo item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title",item.getTitle());
        // Lấy id bản ghi mới
        long id = db.insert("todos", null, contentValues );

        return id;
    }

    @Override
    public int update(Todo item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",item.getTitle());
        // VD: update cho bản ghi 1 => UPDATE todos SET title = "Hello" WHERE id = 1
        int rs = db.update("todos",contentValues,"id = " + item.getId(), null);
        return rs;
    }

    @Override
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rs = db.delete("todos", "id = "+id, null);
        return rs;
    }
}
