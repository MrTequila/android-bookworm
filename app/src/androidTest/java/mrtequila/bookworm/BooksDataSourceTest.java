package mrtequila.bookworm;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by Michal on 2017-08-23.
 */

@RunWith(AndroidJUnit4.class)
public class BooksDataSourceTest extends AndroidTestCase {

    private BooksDataSource booksDataSource;
    private MySQLiteHelper helper;

    @Before
    public void setUp() throws Exception{
        helper = new MySQLiteHelper(InstrumentationRegistry.getTargetContext());
        booksDataSource = new BooksDataSource(helper);

        //deleting database first to be sure we create new one again.
        booksDataSource.deleteDB();
        booksDataSource.open();
    }

    @After
    public void tearDown() throws Exception{
        booksDataSource.close();
    }

    @Test
    public void dbShouldBeCreated() throws Exception {
        assertNotNull(booksDataSource);
    }

    @Test
    public void bookShouldBeAdded() throws Exception {
        String author = "Martin";
        String title = "Game of Thrones";
        String startDate = "2017-01-19";
        String finishDate = "2017-02-03";
        int pages = 851;

        booksDataSource.createBook(author, title, startDate, finishDate, pages);
        ArrayList<Book> books = booksDataSource.getAllBooksArray();


        assertThat(books.size(), is(1));

        assertTrue(books.get(0).getAuthor().equals("Martin"));
        assertTrue(books.get(0).getTitle().equals("Game of Thrones"));
        assertTrue(books.get(0).getStartDate().equals("2017-01-19"));
        assertTrue(books.get(0).getFinishDate().equals("2017-02-03"));
        assertEquals(851, books.get(0).getPageNumber());
    }

    @Test
    public void bookShouldBeDeleted() throws Exception{
        String author1 = "Martin";
        String title1 = "Game of Thrones";
        String startDate1 = "2017-01-19";
        String finishDate1 = "2017-02-03";
        int pages1 = 851;

        String author2 = "Jordan";
        String title2 = "Wheel of Time";
        String startDate2 = "2017-03-21";
        String finishDate2 = "2017-05-07";
        int pages2 = 629;

        booksDataSource.createBook(author1, title1, startDate1, finishDate1, pages1);
        booksDataSource.createBook(author2, title2, startDate2, finishDate2, pages2);
        ArrayList<Book> books = booksDataSource.getAllBooksArray();

        assertThat(books.size(), is(2));

        assertTrue(books.get(0).getAuthor().equals("Martin"));
        assertFalse(books.get(0).getAuthor().equals("Jordan"));
        assertTrue(books.get(1).getAuthor().equals("Jordan"));
        assertFalse(books.get(1).getAuthor().equals("Martin"));

        booksDataSource.deleteBook(books.get(0));
        books = booksDataSource.getAllBooksArray();

        assertThat(books.size(), is(1));

        assertTrue(books.get(0).getAuthor().equals("Jordan"));
    }




}