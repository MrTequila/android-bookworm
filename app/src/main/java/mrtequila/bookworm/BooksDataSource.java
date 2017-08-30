package mrtequila.bookworm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal on 2017-04-04.
 */

public class BooksDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
                                    MySQLiteHelper.COLUMN_AUTHOR,
                                    MySQLiteHelper.COLUMN_TITLE,
                                    MySQLiteHelper.COLUMN_STARTDATE,
                                    MySQLiteHelper.COLUMN_FINISHDATE,
                                    MySQLiteHelper.COLUMN_PAGENUMBER};

    public BooksDataSource(MySQLiteHelper helper) {
        dbHelper = helper;
    }

    public void open() throws SQLException {
        dbHelper.open();
       // database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteDB() {
        dbHelper.deleteDB();
        //context.deleteDatabase(dbHelper.getDatabaseName());
    }

    public Book createBook(String author, String title, String startDate,
                           String finishDate, int pageNumber) {

        return dbHelper.createBook(author, title, startDate, finishDate, pageNumber);
    }

    /*public Book createBook2(ContentValues values) {

        long insertID = database.insert(MySQLiteHelper.TABLE_BOOKS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns,
                MySQLiteHelper.COLUMN_ID + " = " + insertID,
                null,
                null,
                null,
                null );
        cursor.moveToFirst();
        Book newBook = cursorToBook(cursor);
        cursor.close();
        return newBook;
    }*/

    public void deleteBook(Book book) {

        long id = book.getId();
        dbHelper.deleteBook(id);
    }

    public List<Book> getAllBooks() {
        List<Book> books;// = new ArrayList<Book>();

        books = dbHelper.getAllBooks();
        return books;

    }

    public ArrayList<Book> getAllBooksArray() {
        ArrayList<Book> books;// = new ArrayList<Book>();

        books = dbHelper.getAllBooksArray();
        return books;

    }

   /* private Book cursorToBook(Cursor cursor) {
        Book book = new Book(cursor.getLong(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getInt(5));

        return book;
    }*/
}
