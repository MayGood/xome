package com.xxhx.moduleclosedatabase;

import android.content.Context;
import com.xxhx.xome.data.close.DaoMaster;
import org.greenrobot.greendao.database.Database;

/**
 * Created by xxhx on 2018/1/23.
 */

public class CloseDbHelper extends DaoMaster.OpenHelper {
    public CloseDbHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if (newVersion == oldVersion)
            return;
    }
}
