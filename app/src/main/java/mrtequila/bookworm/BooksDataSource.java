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
        this.open();
        dbHelper.deleteDB();
        this.close();
    }

    public Book createBook(String author, String title, String startDate,
                           String finishDate, int pageNumber) {
        this.open();
        Book book = dbHelper.createBook(author, title, startDate, finishDate, pageNumber);
        this.close();
        return book;
    }

    public Book updateBook(long id, String author, String title, String startDate,
                           String finishDate, int pageNumber){
        this.open();
        Book book = dbHelper.updateBook(id, author, title, startDate, finishDate, pageNumber);
        this.close();
        return book;
    }

    public void deleteBook(Book book) {
        long id = book.getId();
        this.open();
        dbHelper.deleteBook(id);
        this.close();
    }

    public List<Book> getAllBooks() {
        List<Book> books;
        this.open();
        books = dbHelper.getAllBooks();
        this.close();
        return books;

    }

    public ArrayList<Book> getAllBooksArray() {
        ArrayList<Book> books;

        this.open();
        books = dbHelper.getAllBooksArray();
        this.close();

        return books;

    }

    public Book getBook(long id){
        this.open();
        Book book = dbHelper.getBook(id);
        this.close();
        return book;
    }

}
