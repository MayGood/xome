package com.xxhx.xome.ui.disc.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.config.Action;
import com.xxhx.xome.ui.BaseActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoEditActivity extends BaseActivity {
    public static final String EXTRA_MEMO_ID = "extra_memo_id";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("M/d H:mm");

    private EditText mContentEditor;

    private Memo mMemo;

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_memo_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContentEditor = findViewById(R.id.content_edit);

        if (getIntent().hasExtra(EXTRA_MEMO_ID)) {
            mMemo = App.getInstance().getDaoSession().getMemoDao().load(getIntent().getLongExtra(EXTRA_MEMO_ID, 0));
            if (mMemo != null) {
                setTitle(DATE_FORMAT.format(mMemo.getModifyTime()));
                mContentEditor.setText(mMemo.getContent());
                mContentEditor.setSelection(mContentEditor.getText().length());
            }
        }
        else {
            setTitle(DATE_FORMAT.format(new Date()));
        }
        mContentEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mMemo == null) {
                    mMemo = new Memo();
                    mMemo.setContent(s.toString());
                    mMemo.setCreateTime(new Date());
                    mMemo.setModifyTime(mMemo.getCreateTime());
                    App.getInstance().getDaoSession().getMemoDao().insert(mMemo);
                    Intent intent = new Intent(Action.ADD);
                    intent.putExtra("id", mMemo.getId());
                    setResult(RESULT_OK, intent);
                }
                else {
                    mMemo.setContent(s.toString());
                    mMemo.setModifyTime(new Date());
                    App.getInstance().getDaoSession().getMemoDao().update(mMemo);
                }
            }
        });
    }

    public void shareMemo(View view) {
    }

    public void deleteMemo(View view) {
        if (mMemo != null) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("删除便笺")
                    .setMessage("确认要删除所选的便笺吗？")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.getInstance().getDaoSession().getMemoDao().delete(mMemo);
                            Intent intent = new Intent(Action.DELETE);
                            intent.putExtra("id", mMemo.getId());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        }
        else {
            finish();
        }
    }

}
