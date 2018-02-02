package com.xxhx.xome.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.MenuItem;
import android.view.Window;
import com.xxhx.xome.App;
import com.xxhx.xome.config.Identity;
import com.xxhx.xome.helper.ToastHelper;

/**
 * Created by xxhx on 2016/9/19.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Identity identity = App.getInstance().getIdentity();
        if(identity.getSecurityLevel() < getRequiredIdentity().getSecurityLevel()) {
            finish();
            ToastHelper.toast("Unauthorized visit!!!");
            return;
        }
        onBaseCreate(savedInstanceState);
    }

    protected abstract void onBaseCreate(@Nullable Bundle savedInstanceState);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    protected Identity getRequiredIdentity() {
        return Identity.Visitor;
    }
}
