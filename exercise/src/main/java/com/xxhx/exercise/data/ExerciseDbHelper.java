package com.xxhx.exercise.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xxhx on 2017/8/31.
 */

public class ExerciseDbHelper extends DaoMaster.OpenHelper {
    public ExerciseDbHelper(Context context, String name) {
        super(context, name);
    }

    public ExerciseDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
}
