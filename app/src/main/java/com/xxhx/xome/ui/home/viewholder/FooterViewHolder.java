package com.xxhx.xome.ui.home.viewholder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.xxhx.xome.R;

/**
 * Created by xxhx on 2017/2/17.
 */

public class FooterViewHolder extends ViewHolder {

    public ProgressBar mProgressBar;
    public TextView mLabel;

    public FooterViewHolder(View itemView) {
        super(itemView);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        mLabel = (TextView) itemView.findViewById(R.id.label);
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE_FOOTER;
    }
}
