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

    @Before
    public void setUp() throws Exception{
        booksDataSource = new BooksDataSource(InstrumentationRegistry.getTargetContext());

        //deleting database first to be sure we create new one again.
        booksDataSource.deleteDB(getTargetContext());
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




}