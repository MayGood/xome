package com.xxhx.xome.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.xxhx.xome.R;

/**
 * Created by xxhx on 2017/7/19.
 */

public class SendWeiboFragment extends Fragment implements View.OnClickListener {

    private View mBtnClose;
    private EditText mEditView;
    private ImageView mPhotoView;
    private View mBtnPhoto;
    private Button mBtnLocation;
    private TextView mCountView;
    private View mBtnPublish;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send_weibo, container, false);
        mBtnClose = rootView.findViewById(R.id.btn_close);
        mEditView = (EditText) rootView.findViewById(R.id.edit);
        mPhotoView = (ImageView) rootView.findViewById(R.id.photo);
        mBtnPhoto = rootView.findViewById(R.id.btn_photo);
        mBtnLocation = (Button) rootView.findViewById(R.id.btn_location);
        mCountView = (TextView) rootView.findViewById(R.id.item_count);
        mBtnPublish = rootView.findViewById(R.id.btn_publish);
        initView();
        return rootView;
    }

    private void initView() {
        mBtnClose.setOnClickListener(this);
        mBtnPhoto.setOnClickListener(this);
        mBtnLocation.setOnClickListener(this);
        mBtnPublish.setOnClickListener(this);
        mEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCountView.setText("" + (140 - s.length()));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showSoftInput();
    }

    private void showSoftInput() {
        mEditView.requestFocus();
        InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditView, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void hideFragment() {
        hideSoftInput();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(this);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.commit();
        fragmentManager.popBackStack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                hideFragment();
                break;
            case R.id.btn_photo:
                break;
            case R.id.btn_location:
                break;
            case R.id.btn_publish:
                break;
        }
    }
}
