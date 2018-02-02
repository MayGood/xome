package com.xxhx.xome.ui.disc;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import com.xxhx.exercise.ExerciseDisplayActivity;
import com.xxhx.exercise.ExerciseRecordActivity;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ContextHelper;
import com.xxhx.xome.ui.activity.WebViewActivity;
import com.xxhx.xome.ui.disc.checkin.CheckinActivity;
import com.xxhx.xome.ui.disc.todos.TodosMainActivity;
import com.xxhx.xome.ui.disc.trip.MyTripActivity;
import com.xxhx.xome.ui.disc.wealth.MyWealthActivity;
import com.xxhx.xome.ui.disc.wealth.util.WealthUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by xxhx on 2016/9/22.
 */
public class DiscPresenter implements DiscContract.Presenter, View.OnClickListener {

    private static final int sDrawingboardId = 0x10;
    private static final int sClipboardId = 0x11;
    private static final int sTodolistId = 0x12;
    private static final int sCookbookId = 0x13;
    private static final int sTripId = 0x14;
    private static final int sExerciseId = 0x15;
    private static final int sNoteId = 0x16;

    private static final int sAAId = 0x20;
    private static final int sCheckinId = 0x21;
    private static final int sWealthId = 0x22;
    private static final int sPasswordId = 0x23;

    @NonNull
    private final DiscContract.View mDiscView;

    public DiscPresenter(@NonNull DiscContract.View discView) {
        mDiscView = checkNotNull(discView);
    }

    @Override
    public void subscribe() {
        //mDiscView.addPublicItem(sDrawingboardId, R.drawable.ic_drawingboard, ContextHelper.getString(R.string.title_drawingboard), null, 0, this);
        //mDiscView.addPublicItem(sClipboardId, R.drawable.ic_clipboard, ContextHelper.getString(R.string.title_clipboard), null, 0, this);
        mDiscView.addPublicItem(sTodolistId, R.drawable.ic_todolist, ContextHelper.getString(R.string.title_todolist), null, 0, this);
        //mDiscView.addPublicItem(sCookbookId, R.drawable.ic_cookbook, ContextHelper.getString(R.string.title_cookbook), null, 0, this);
        mDiscView.addPublicItem(sTripId, R.drawable.ic_trip, ContextHelper.getString(R.string.title_trip), null, 0, this);
        mDiscView.addPublicItem(sExerciseId, R.drawable.ic_exercise, ContextHelper.getString(R.string.title_exercise), null, 0, this);
        mDiscView.addPublicItem(sNoteId, R.drawable.ic_module_note, ContextHelper.getString(R.string.title_note), null, 0, this);

        //mDiscView.addPrivateItem(sAAId, R.drawable.ic_aa, ContextHelper.getString(R.string.title_aa), null, 0, this);
        mDiscView.addPrivateItem(sCheckinId, R.drawable.ic_checkin, ContextHelper.getString(R.string.title_checkin), null, 0, this);
        mDiscView.addPrivateItem(sWealthId, R.drawable.ic_wealth, ContextHelper.getString(R.string.title_wealth), null,
                WealthUtil.getWarningCount(), this);
        //mDiscView.addPrivateItem(sPasswordId, R.drawable.ic_password, ContextHelper.getString(R.string.title_password), null, 0, this);
    }

    @Override
    public void unsubscribe() {

    }

    private boolean flag = false;

    private void gotoActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case sDrawingboardId:
                break;
            case sAAId:
                break;
            case sClipboardId:
                break;
            case sTodolistId:
                gotoActivity(v.getContext(), TodosMainActivity.class);
                break;
            case sCookbookId:
                break;
            case sTripId:
                gotoActivity(v.getContext(), MyTripActivity.class);
                break;
            case sExerciseId:
                if(flag) {
                    gotoActivity(v.getContext(), ExerciseRecordActivity.class);
                }
                else {
                    gotoActivity(v.getContext(), ExerciseDisplayActivity.class);
                }
                flag = !flag;
                break;
            case sCheckinId:
                gotoActivity(v.getContext(), CheckinActivity.class);
                break;
            case sWealthId:
                gotoActivity(v.getContext(), MyWealthActivity.class);
                break;
            case sPasswordId:
                break;
        }
    }
}
