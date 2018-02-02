package com.xxhx.xome.ui.mess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ContextHelper;
import com.xxhx.xome.ui.BaseFragment;

/**
 * Created by xxhx on 2016/9/19.
 */
public class MessFragment extends BaseFragment {
    public static MessFragment newInstance(int sectionNumber) {
        MessFragment fragment = new MessFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String retrieveTitle() {
        return ContextHelper.getString(R.string.mess);
    }

    @Override
    public int retrieveThemeColor() {
        return ContextHelper.getColor(R.color.mess);
    }

    @Override
    public int retrieveThemeDarkColor() {
        return ContextHelper.getColor(R.color.darkMess);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt("section_number")));
        return rootView;
    }
}
