package com.xxhx.xome.ui.home.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.xxhx.xome.R;
import com.xxhx.xome.ui.home.view.StatusView;
import com.xxhx.xome.view.WeiboPicsView;

/**
 * Created by xxhx on 2017/2/17.
 */

public class ContentViewHolder extends ViewHolder {

    public StatusView statusView;

    public ContentViewHolder(View itemView) {
        super(itemView);
        statusView = (StatusView) itemView.findViewById(R.id.status);
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE_CONTENT;
    }
}
