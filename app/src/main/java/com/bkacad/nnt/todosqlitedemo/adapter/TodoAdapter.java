package com.bkacad.nnt.todosqlitedemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bkacad.nnt.todosqlitedemo.R;
import com.bkacad.nnt.todosqlitedemo.model.Todo;

import java.util.List;

public class TodoAdapter extends BaseAdapter {

    private Context context;
    private List<Todo> todoList;

    public TodoAdapter(Context context, List<Todo> todoList){
        this.context = context;
        this.todoList = todoList;
    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        }
        // Bind id
        TextView tvTodo = convertView.findViewById(R.id.tv_item_todo);
        Todo item = todoList.get(position);
        tvTodo.setText(String.format("%d. %s", getItemId(position), item.getTitle()));

        return convertView;
    }
}
