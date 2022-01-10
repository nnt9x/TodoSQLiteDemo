package com.bkacad.nnt.todosqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bkacad.nnt.todosqlitedemo.adapter.TodoAdapter;
import com.bkacad.nnt.todosqlitedemo.db.DAO;
import com.bkacad.nnt.todosqlitedemo.db.DBHelper;
import com.bkacad.nnt.todosqlitedemo.model.Todo;
import com.bkacad.nnt.todosqlitedemo.model.TodoDAO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private EditText edtTodo;
    private ListView lvTodo;
    private List<Todo> todos;

    private TodoAdapter todoAdapter;

    // DBHelper và TodoDAO
    private DBHelper dbHelper;
    private DAO<Todo> todoDAO;

    private void initUI(){
        btnAdd = findViewById(R.id.btn_main_add);
        edtTodo = findViewById(R.id.edt_main);
        lvTodo = findViewById(R.id.lv_main_todo);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        // init db
        dbHelper = new DBHelper(this);
        todoDAO = new TodoDAO(dbHelper);

        todos = todoDAO.all();
        if(todos.size() == 0) Toast.makeText(this, "Danh sách rỗng", Toast.LENGTH_SHORT).show();
        todoAdapter = new TodoAdapter(this,todos);
        lvTodo.setAdapter(todoAdapter);
        // Xử lý sự kiện khi thêm vào listview
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTodo.getText().toString();
                if(title.isEmpty()) {
                    edtTodo.setError("Hãy nhập dữ liệu");
                    return;
                }
                edtTodo.clearFocus();
                edtTodo.setText("");
                // Thêm dữ liệu vào SQLite
                Todo item = new Todo(title);
                long id = todoDAO.create(item);
                item.setId(id);
                // Push dữ liệu vào todo
                todos.add(item);
                // Thông báo cho Adapter -> render lại view
                todoAdapter.notifyDataSetChanged();
            }
        });
        lvTodo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, todos.get(position).toString(), Toast.LENGTH_SHORT ).show();
            }
        });
        lvTodo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Xoa du lieu tai vi tri position
                // Xoa trong SQLite
                Todo item = todos.get(position);
                if(todoDAO.delete(item.getId()) == 1){
                    // XOas thanh cong -> xoa du lieu trong bo nho (RAM)
                    todos.remove(position);
                    todoAdapter.notifyDataSetChanged();
                }

                // Xoa o list => cap lai listview
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}