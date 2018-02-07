package com.xxhx.xome.ui.disc.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.config.Action;
import com.xxhx.xome.data.MemoDao;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.note.view.NoteTextView;
import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends BaseActivity {
    private static final int REQUEST_EDIT = 0x01;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    private List<Memo> mData;

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_memo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoActivity.this, MemoEditActivity.class);
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });

        mRecyclerView = findViewById(R.id.content);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK && data != null) {
            String action = data.getAction();
            if (Action.ADD.equals(action)) {
                long id = data.getLongExtra("id", 0);
                Memo memo = App.getInstance().getDaoSession().getMemoDao().load(id);
                if (memo != null) {
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getModifyTime().before(memo.getModifyTime())) {
                            mData.add(i, memo);
                            mAdapter.notifyItemInserted(i);
                            mRecyclerView.scrollToPosition(i);
                            return;
                        }
                    }
                    mData.add(memo);
                    mAdapter.notifyItemInserted(mData.size() - 1);
                    mRecyclerView.scrollToPosition(mData.size() - 1);
                }
            }
            else if (Action.DELETE.equals(action)) {
                long id = data.getLongExtra("id", 0);
                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i).getId() == id) {
                        Memo memo = mData.get(i);
                        mData.remove(i);
                        mAdapter.notifyItemRemoved(i);
                        showDeleteSnackbar(i, memo);
                        return;
                    }
                }
            }
        }

        mData = App.getInstance().getDaoSession().getMemoDao().queryBuilder().orderDesc(MemoDao.Properties.ModifyTime).list();
        mAdapter.notifyDataSetChanged();
    }

    private void init() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mData = App.getInstance().getDaoSession().getMemoDao().queryBuilder().orderDesc(MemoDao.Properties.ModifyTime).list();
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showDeleteSnackbar(final int position, final Memo memo) {
        Snackbar.make(mRecyclerView, "删除成功", Snackbar.LENGTH_LONG)
                .setAction("撤回", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.getInstance().getDaoSession().getMemoDao().insert(memo);
                        mData.add(position, memo);
                        mAdapter.notifyItemInserted(position);
                    }
                }).show();
    }

    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MemoActivity.this).inflate(R.layout.grid_item_memo, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Memo memo = mData.get(position);
            String content = memo.getContent();
            if (!TextUtils.isEmpty(content)) {
                String[] paras = content.split("\n", 2);
                holder.headView.setAlignText(paras[0]);
                holder.headView.setVisibility(View.VISIBLE);
                if (paras.length > 1) {
                    holder.contentView.setAlignText(paras[1]);
                    holder.contentView.setVisibility(View.VISIBLE);
                }
                else {
                    holder.contentView.setVisibility(View.GONE);
                }
            }
            else {
                holder.headView.setVisibility(View.GONE);
                holder.contentView.setVisibility(View.GONE);
            }
            holder.timeView.setText(DateUtils.getRelativeTimeSpanString(memo.getModifyTime().getTime()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MemoActivity.this, MemoEditActivity.class);
                    intent.putExtra(MemoEditActivity.EXTRA_MEMO_ID, memo.getId());
                    startActivityForResult(intent, REQUEST_EDIT);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeleteDialog(holder.getAdapterPosition(), memo);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        private void showDeleteDialog(final int position, final Memo memo) {
            AlertDialog dialog = new AlertDialog.Builder(MemoActivity.this)
                    .setTitle("删除便笺")
                    .setMessage("确认要删除所选的便笺吗？")
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.getInstance().getDaoSession().getMemoDao().delete(memo);
                            mData.remove(position);
                            notifyItemRemoved(position);
                            showDeleteSnackbar(position, memo);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureView;
        NoteTextView headView;
        NoteTextView contentView;
        TextView timeView;

        public ViewHolder(View itemView) {
            super(itemView);
            pictureView = itemView.findViewById(R.id.picture);
            headView = itemView.findViewById(R.id.head);
            contentView = itemView.findViewById(R.id.content);
            timeView = itemView.findViewById(R.id.time);
        }
    }
}
