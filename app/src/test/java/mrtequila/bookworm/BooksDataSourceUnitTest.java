package mrtequila.bookworm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.mock.MockContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Michal on 2017-08-23.
 */
@RunWith(MockitoJUnitRunner.class)
public class BooksDataSourceUnitTest {


    @Mock
    BooksDataSource booksDataSource;


    @Before
    public void setUp(){

    }

    @After
    public void finish(){
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

        Book mockBook = new Book(1, author, title, startDate, finishDate, pages);
        ArrayList<Book> mockBooks =new ArrayList<>();
        mockBooks.add(mockBook);
        when(booksDataSource.createBook(author, title, startDate, finishDate, pages)).thenReturn(mockBook);
        when(booksDataSource.getAllBooksArray()).thenReturn(mockBooks);

        booksDataSource.createBook(author, title, startDate, finishDate, pages);
        ArrayList<Book> books = booksDataSource.getAllBooksArray();

        Mockito.verify(booksDataSource, times(1)).createBook(Matchers.matches(author),Matchers.matches(title), Matchers.matches(startDate), Matchers.matches(finishDate), Matchers.eq(pages));
        Mockito.verify(booksDataSource, times(1)).getAllBooksArray();

        assertThat(books.size(), is(1));

        assertTrue(books.get(0).getAuthor().equals("Martin"));
        assertTrue(books.get(0).getTitle().equals("Game of Thrones"));
        assertTrue(books.get(0).getStartDate().equals("2017-01-19"));
        assertTrue(books.get(0).getFinishDate().equals("2017-02-03"));
        assertEquals(851, books.get(0).getPageNumber());
    }




}