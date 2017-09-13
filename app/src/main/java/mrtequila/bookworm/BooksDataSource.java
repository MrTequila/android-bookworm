package mrtequila.bookworm;


import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal on 2017-04-04.
 */

public class BooksDataSource {

    // Database fields
    private MySQLiteHelper dbHelper;

    public BooksDataSource(MySQLiteHelper helper) {
        dbHelper = helper;
    }

    public void open() throws SQLException {
        dbHelper.open();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteDB() {
        dbHelper.deleteDB();
    }

    public Book createBook(String author, String title, String startDate,
                           String finishDate, int pageNumber) {

        return dbHelper.createBook(author, title, startDate, finishDate, pageNumber);
    }

    public Book updateBook(long id, String author, String title, String startDate,
                           String finishDate, int pageNumber){
        return dbHelper.updateBook(id, author, title, startDate, finishDate, pageNumber);
    }

    public void deleteBook(Book book) {

        long id = book.getId();
        dbHelper.deleteBook(id);
    }

    public List<Book> getAllBooks() {
        List<Book> books;

        books = dbHelper.getAllBooks();
        return books;

    }

    public ArrayList<Book> getAllBooksArray() {
        ArrayList<Book> books;

        books = dbHelper.getAllBooksArray();
        return books;

    }

}
