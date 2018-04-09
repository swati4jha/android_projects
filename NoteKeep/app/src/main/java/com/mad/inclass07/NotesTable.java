package com.mad.inclass07;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mihai on 2/27/17.
 */

public class NotesTable {


    static final String  TABLENAME="nodes";
    static final String COLUMN_ID="id";
    static final String COLUMN_SUBJECT="subject";
    static final String COLUMN_PRIORITY="priority";
    static final String COLUMN_TIME="time";
    static final String COLUMN_STATUS="completed_pending";

    static public  void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+TABLENAME+" (");
        sb.append(COLUMN_ID+" integer primary key autoincrement, ");
        sb.append(COLUMN_SUBJECT+" text not null, ");
        sb.append(COLUMN_PRIORITY+" text not null, ");
        sb.append(COLUMN_TIME+" text not null, ");
        sb.append(COLUMN_STATUS+" text not null);");

        try {
            db.execSQL(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+ TABLENAME);
            NotesTable.onCreate(db);
    }
}
