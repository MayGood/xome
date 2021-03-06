package com.xxhx.xome.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.xxhx.xome.R;
import com.xxhx.xome.http.weibo.entity.Comment;

/**
 * Created by xxhx on 2017/7/19.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_status_reply, parent, false);
            holder = new ViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Comment comment = getItem(position);
        holder.content.setText(comment.getText());
        return convertView;
    }

    class ViewHolder {
        TextView content;
    }
}
