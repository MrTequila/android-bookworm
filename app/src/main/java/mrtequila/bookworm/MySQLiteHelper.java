package mrtequila.bookworm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Michal on 2017-04-04.
 */

public class MySQLiteHelper  extends SQLiteOpenHelper{

    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STARTDATE = "startDate";
    public static final String COLUMN_FINISHDATE = "finishDate";
    public static final String COLUMN_PAGENUMBER = "pageNumber";

    private static final String DATABASE_NAME = "test_bookworm.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_BOOKS
            + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_AUTHOR + " text not null, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_STARTDATE + " text not null, "
            + COLUMN_FINISHDATE + " text not null, "
            + COLUMN_PAGENUMBER + " integer not null"
            + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }
}
