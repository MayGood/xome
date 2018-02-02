package com.xxhx.xome.ui.disc.todos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.data.TodoDao;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.todos.data.Todo;
import com.xxhx.xome.ui.disc.todos.view.TodoTextView;
import java.util.Date;
import java.util.List;

public class TodosMainActivity extends BaseActivity implements AdapterView.OnItemLongClickListener {
    private ListView mTodoList1;    // 第一象限：重要 不紧急
    private ListView mTodoList2;    // 第二象限：重要 紧急
    private ListView mTodoList3;    // 第三象限：不重要 紧急
    private ListView mTodoList4;    // 第四象限：不重要 不紧急

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_todos_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTodoList1 = (ListView) findViewById(R.id.list_1);
        mTodoList2 = (ListView) findViewById(R.id.list_2);
        mTodoList3 = (ListView) findViewById(R.id.list_3);
        mTodoList4 = (ListView) findViewById(R.id.list_4);

        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todos_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, NewTodoActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        // 第一象限
        List<Todo> todos1 = App.getInstance().getDaoSession().getTodoDao().queryBuilder()
                .where(TodoDao.Properties.Important.eq(true),
                        TodoDao.Properties.Urgent.eq(false),
                        TodoDao.Properties.Done.eq(false))
                .orderDesc(TodoDao.Properties.Done)
                .orderAsc(TodoDao.Properties.DueDate)
                .list();
        MyAdapter adapter1 = new MyAdapter(this, todos1);
        mTodoList1.setAdapter(adapter1);
        mTodoList1.setOnItemLongClickListener(this);

        // 第二象限
        List<Todo> todos2 = App.getInstance().getDaoSession().getTodoDao().queryBuilder()
                .where(TodoDao.Properties.Important.eq(true),
                        TodoDao.Properties.Urgent.eq(true),
                        TodoDao.Properties.Done.eq(false))
                .orderDesc(TodoDao.Properties.Done)
                .orderAsc(TodoDao.Properties.DueDate)
                .list();
        MyAdapter adapter2 = new MyAdapter(this, todos2);
        mTodoList2.setAdapter(adapter2);
        mTodoList2.setOnItemLongClickListener(this);

        // 第三象限
        List<Todo> todos3 = App.getInstance().getDaoSession().getTodoDao().queryBuilder()
                .where(TodoDao.Properties.Important.eq(false),
                        TodoDao.Properties.Urgent.eq(true),
                        TodoDao.Properties.Done.eq(false))
                .orderDesc(TodoDao.Properties.Done)
                .orderAsc(TodoDao.Properties.DueDate)
                .list();
        MyAdapter adapter3 = new MyAdapter(this, todos3);
        mTodoList3.setAdapter(adapter3);
        mTodoList3.setOnItemLongClickListener(this);

        // 第四象限
        List<Todo> todos4 = App.getInstance().getDaoSession().getTodoDao().queryBuilder()
                .where(TodoDao.Properties.Important.eq(false),
                        TodoDao.Properties.Urgent.eq(false),
                        TodoDao.Properties.Done.eq(false))
                .orderDesc(TodoDao.Properties.Done)
                .orderAsc(TodoDao.Properties.DueDate)
                .list();
        MyAdapter adapter4 = new MyAdapter(this, todos4);
        mTodoList4.setAdapter(adapter4);
        mTodoList4.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, final View view, int position, final long id) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("确认删除此条待办项吗？")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getInstance().getDaoSession().getTodoDao().deleteByKey(id);
                        ((MyAdapter) parent.getAdapter()).remove(id);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
        return true;
    }

    class MyAdapter extends ArrayAdapter<Todo> {

        public MyAdapter(@NonNull Context context, @NonNull List<Todo> objects) {
            super(context, R.layout.list_item_todo, objects);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_todo, parent, false);
                holder = new ViewHolder();
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                holder.text = (TodoTextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Todo todo = getItem(position);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.text.setTextColor(getResources().getColor(R.color.disc));
                        holder.text.setDeleteLineEnabled(true);
                        if (!todo.getDone()) {
                            todo.setDone(true);
                            App.getInstance().getDaoSession().getTodoDao().update(todo);
                        }
                    }
                    else {
                        if (todo.getDueDate().before(new Date())) {
                            holder.text.setTextColor(getResources().getColor(R.color.googleRed));
                        }
                        else {
                            holder.text.setTextColor(Color.parseColor("#333333"));
                        }
                        holder.text.setDeleteLineEnabled(false);
                        if (todo.getDone()) {
                            todo.setDone(false);
                            App.getInstance().getDaoSession().getTodoDao().update(todo);
                        }
                    }
                }
            });
            holder.checkBox.setChecked(todo.getDone());
            if (todo.getDone()) {
                holder.text.setTextColor(getResources().getColor(R.color.disc));
                holder.text.setDeleteLineEnabled(true);
            }
            else {
                if (todo.getDueDate().before(new Date())) {
                    holder.text.setTextColor(getResources().getColor(R.color.googleRed));
                }
                else {
                    holder.text.setTextColor(Color.parseColor("#333333"));
                }
                holder.text.setDeleteLineEnabled(false);
            }
            holder.text.setText(todo.getContent());
            return convertView;
        }

        public void remove(long id) {
            for (int i = 0; i < getCount(); i++) {
                Todo todo = getItem(i);
                if (todo.getId() == id) {
                    remove(todo);
                    return;
                }
            }
        }
    }

    class ViewHolder {
        CheckBox checkBox;
        TodoTextView text;
    }
}
