package com.xxhx.xome.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.xxhx.xome.data.CheckinDao;
import com.xxhx.xome.data.CreditBillDao;
import com.xxhx.xome.data.DaoMaster;
import com.xxhx.xome.data.DateMarkDao;
import com.xxhx.xome.data.TodoDao;
import com.xxhx.xome.data.TripDao;
import com.xxhx.xome.ui.disc.checkin.data.DateMark;
import org.greenrobot.greendao.database.Database;

/**
 * Created by xxhx on 2017/4/11.
 */

public class OpenDbHelper extends DaoMaster.OpenHelper {
    public OpenDbHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if(newVersion == oldVersion)
            return;
        if(oldVersion < 3) {
            updateToVersion3(db);
        }
        if(oldVersion < 4) {
            CreditBillDao.createTable(db, false);
        }
        if(oldVersion < 5) {
            CreditBillDao.dropTable(db, true);
            CreditBillDao.createTable(db, false);
        }
        if(oldVersion < 6) {
            updateToVersion6(db);
        }
        if(oldVersion < 7) {
            TripDao.createTable(db, false);
        }
        if(oldVersion < 8) {
            //ExerciseDao.createTable(db, false);
        }
        if(oldVersion < 9) {
            //ExerciseDao.dropTable(db, true);
            //ExerciseDao.createTable(db, false);
        }
        if(oldVersion < 10) {
            CheckinDao.createTable(db, false);
        }
        if(oldVersion < 11) {
            DateMarkDao.createTable(db, false);
        }
        if (oldVersion < 12) {
            TodoDao.createTable(db, false);
        }
        if (oldVersion < 13) {
            TodoDao.dropTable(db, true);
            TodoDao.createTable(db, false);
        }
    }

    private void updateToVersion3(Database db) {
        db.execSQL("ALTER TABLE \"TURNOVER\" ADD COLUMN \"TURNOVER_TYPE\" TEXT;");
    }

    private void updateToVersion6(Database db) {
        db.execSQL("ALTER TABLE \"CREDIT_BILL\" ADD COLUMN \"DEADLINE\" TEXT;");
    }

}
