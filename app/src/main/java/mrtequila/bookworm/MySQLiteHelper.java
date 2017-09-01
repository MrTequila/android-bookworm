package mrtequila.bookworm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal on 2017-04-04.
 */

public class MySQLiteHelper  extends SQLiteOpenHelper{

    private SQLiteDatabase database;
    private Context context;

    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STARTDATE = "startDate";
    public static final String COLUMN_FINISHDATE = "finishDate";
    public static final String COLUMN_PAGENUMBER = "pageNumber";

    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_AUTHOR,
            MySQLiteHelper.COLUMN_TITLE,
            MySQLiteHelper.COLUMN_STARTDATE,
            MySQLiteHelper.COLUMN_FINISHDATE,
            MySQLiteHelper.COLUMN_PAGENUMBER};

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
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public void open() throws SQLException{
        database = this.getWritableDatabase();
    }

    public void deleteDB() {
        this.context.deleteDatabase(this.getDatabaseName());

    }

    public void deleteBook(long id){
        database.delete(MySQLiteHelper.TABLE_BOOKS,
                MySQLiteHelper.COLUMN_ID + " = " + id,
                null);
    }

    public Book createBook(String author, String title, String startDate,
                           String finishDate, int pageNumber){
        ContentValues values = new ContentValues();
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_STARTDATE, startDate);
        values.put(COLUMN_FINISHDATE, finishDate);
        values.put(COLUMN_PAGENUMBER, pageNumber);

        long insertID = database.insert(TABLE_BOOKS, null, values);
        Cursor cursor = database.query(TABLE_BOOKS,
                allColumns,
                COLUMN_ID + " = " + insertID,
                null,
                null,
                null,
                null );
        cursor.moveToFirst();
        Book newBook = cursorToBook(cursor);
        cursor.close();
        return newBook;
    }

    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<Book>();

        Cursor cursor = database.query(TABLE_BOOKS,
                allColumns,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return books;

    }

    public ArrayList<Book> getAllBooksArray(){
        ArrayList<Book> books = new ArrayList<Book>();

        Cursor cursor = database.query(TABLE_BOOKS,
                allColumns,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return books;

    }

    private Book cursorToBook(Cursor cursor) {
        Book book = new Book(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getInt(5));

        return book;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }
}
