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
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteDB(Context context) {
        context.deleteDatabase(dbHelper.getDatabaseName());
    }

    public Book createBook(String author, String title, String startDate,
                           String finishDate, int pageNumber) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_AUTHOR, author);
        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_STARTDATE, startDate);
        values.put(MySQLiteHelper.COLUMN_FINISHDATE, finishDate);
        values.put(MySQLiteHelper.COLUMN_PAGENUMBER, pageNumber);

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
    }

    public void deleteBook(Book book) {
        long id = book.getId();
        System.out.println("Book deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_BOOKS,
                        MySQLiteHelper.COLUMN_ID + " = " + id,
                        null);
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<Book>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
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

    public ArrayList<Book> getAllBooksArray() {
        ArrayList<Book> books = new ArrayList<Book>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
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
        Book book = new Book(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getInt(5));
        /*book.setId(cursor.getLong(0));
        book.setAuthor(cursor.getString(1));
        book.setTitle(cursor.getString(2));
        book.setStartDate(cursor.getString(3));
        book.setFinishDate(cursor.getString(4));
        book.setPageNumber(cursor.getInt(5));*/
        return book;
    }
}
