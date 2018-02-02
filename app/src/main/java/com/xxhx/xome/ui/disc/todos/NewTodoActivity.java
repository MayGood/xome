package com.xxhx.xome.ui.disc.todos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.todos.data.Todo;
import com.xxhx.xome.ui.disc.trip.data.Trip;
import com.xxhx.xome.ui.disc.trip.data.TripType;
import java.util.Calendar;

public class NewTodoActivity extends BaseActivity {

    private EditText mTodoEditor;
    private TextView mTextAddedDate;
    private TextView mTextDueDate;
    private Switch mSwitchImportant;
    private Switch mSwitchUrgent;

    private Calendar mAddedDate = Calendar.getInstance();
    private Calendar mDueDate = Calendar.getInstance();

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTodoEditor = (EditText) findViewById(R.id.todo_content);
        mTextAddedDate = (TextView) findViewById(R.id.todo_added_date);
        mTextDueDate = (TextView) findViewById(R.id.todo_due_date);
        mSwitchImportant = (Switch) findViewById(R.id.todo_important);
        mSwitchUrgent = (Switch) findViewById(R.id.todo_urgent);

        mTextAddedDate.setText(String.format("%04d-%02d-%02d %02d:%02d",
                mAddedDate.get(Calendar.YEAR),
                mAddedDate.get(Calendar.MONTH) + 1,
                mAddedDate.get(Calendar.DAY_OF_MONTH),
                mAddedDate.get(Calendar.HOUR_OF_DAY),
                mAddedDate.get(Calendar.MINUTE)));
        mTextDueDate.setText(String.format("%04d-%02d-%02d %02d:%02d",
                mDueDate.get(Calendar.YEAR),
                mDueDate.get(Calendar.MONTH) + 1,
                mDueDate.get(Calendar.DAY_OF_MONTH),
                mDueDate.get(Calendar.HOUR_OF_DAY),
                mDueDate.get(Calendar.MINUTE)));
    }

    public void confirm(View view) {
        String content = mTodoEditor.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        Todo todo = new Todo();
        todo.setContent(content);
        todo.setDone(false);
        todo.setAddedDate(mAddedDate.getTime());
        todo.setDueDate(mDueDate.getTime());
        todo.setImportant(mSwitchImportant.isChecked());
        todo.setUrgent(mSwitchUrgent.isChecked());
        App.getInstance().getDaoSession().getTodoDao().insert(todo);
        finish();
    }

    public void chooseAddedDate(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                new TimePickerDialog(NewTodoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mAddedDate.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
                        mTextAddedDate.setText(String.format("%04d-%02d-%02d %02d:%02d", year, monthOfYear + 1, dayOfMonth, hourOfDay, minute));
                    }
                }, mAddedDate.get(Calendar.HOUR_OF_DAY), mAddedDate.get(Calendar.MINUTE), true).show();
            }
        }, mAddedDate.get(Calendar.YEAR), mAddedDate.get(Calendar.MONTH), mAddedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void chooseDueDate(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                new TimePickerDialog(NewTodoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mDueDate.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
                        mTextDueDate.setText(String.format("%04d-%02d-%02d %02d:%02d", year, monthOfYear + 1, dayOfMonth, hourOfDay, minute));
                    }
                }, mDueDate.get(Calendar.HOUR_OF_DAY), mDueDate.get(Calendar.MINUTE), true).show();
            }
        }, mDueDate.get(Calendar.YEAR), mDueDate.get(Calendar.MONTH), mDueDate.get(Calendar.DAY_OF_MONTH)).show();
    }
}
