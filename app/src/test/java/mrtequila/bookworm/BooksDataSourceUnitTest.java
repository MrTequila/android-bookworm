package mrtequila.bookworm;

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

    public static final String AUTHOR_1 = "Martin";
    public static final String TITLE_1 = "Game of Thrones";
    public static final String START_DATE_1 = "2017-01-19";
    public static final String FINISH_DATE_1 = "2017-02-03";
    public static final int PAGES_1 = 851;

    public static final String AUTHOR_2 = "Jordan";
    public static final String TITLE_2 = "Wheel of Time";
    public static final String START_DATE_2 = "2017-02-12";
    public static final String FINISH_DATE_2 = "2017-04-06";
    public static final int PAGES_2 = 924;

    BooksDataSource booksDataSource;
    Book mockBook1, mockBook2;
    @Mock MySQLiteHelper sqLiteHelper;




    @Before
    public void setUp(){
        booksDataSource = new BooksDataSource(sqLiteHelper);
        booksDataSource.open();


        mockBook1 = new Book(1, AUTHOR_1, TITLE_1, START_DATE_1, FINISH_DATE_1, PAGES_1);
        mockBook2 = new Book(2, AUTHOR_2, TITLE_2, START_DATE_2, FINISH_DATE_2, PAGES_2);
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

        ArrayList<Book> mockBooks =new ArrayList<>();
        mockBooks.add(mockBook1);
        when(sqLiteHelper.createBook("Martin", "Game of Thrones", "2017-01-19", "2017-02-03", 851)).thenReturn(mockBook1);
        when(sqLiteHelper.getAllBooksArray()).thenReturn(mockBooks);


        booksDataSource.createBook(AUTHOR_1, TITLE_1, START_DATE_1, FINISH_DATE_1, PAGES_1);
        ArrayList<Book> books = booksDataSource.getAllBooksArray();

        Mockito.verify(sqLiteHelper, times(1)).createBook(Matchers.matches(AUTHOR_1),
                                                            Matchers.matches(TITLE_1),
                                                            Matchers.matches(START_DATE_1),
                                                            Matchers.matches(FINISH_DATE_1),
                                                            Matchers.eq(PAGES_1));
        Mockito.verify(sqLiteHelper, times(1)).getAllBooksArray();

        assertThat(books.size(), is(1));

        assertTrue(books.get(0).getAuthor().equals("Martin"));
        assertTrue(books.get(0).getTitle().equals("Game of Thrones"));
        assertTrue(books.get(0).getStartDate().equals("2017-01-19"));
        assertTrue(books.get(0).getFinishDate().equals("2017-02-03"));
        assertEquals(851, books.get(0).getPageNumber());
    }

    @Test
    public void bookShouldBeDeleted() throws Exception{
        ArrayList<Book> mockBooks =new ArrayList<>();

        mockBooks.add(mockBook1);
        mockBooks.add(mockBook2);

        ArrayList<Book> mockDeletedBook = new ArrayList<>();
        mockDeletedBook.add(mockBook1);
        mockDeletedBook.add(mockBook2);
        mockDeletedBook.remove(mockBook1);

        when(sqLiteHelper.createBook("Martin", "Game of Thrones", "2017-01-19", "2017-02-03", 851)).thenReturn(mockBook1);
        when(sqLiteHelper.createBook("Jordan", "Wheel of Time", "2017-02-12", "2017-04-06",  924)).thenReturn(mockBook2);

        when(sqLiteHelper.getAllBooksArray()).thenReturn(mockBooks).thenReturn(mockDeletedBook);

        doNothing().when(sqLiteHelper).deleteBook(1);




        booksDataSource.createBook(AUTHOR_1, TITLE_1, START_DATE_1, FINISH_DATE_1, PAGES_1);
        booksDataSource.createBook(AUTHOR_2, TITLE_2, START_DATE_2, FINISH_DATE_2, PAGES_2);
        ArrayList<Book> books = booksDataSource.getAllBooksArray();

        Mockito.verify(sqLiteHelper, times(2)).createBook(Mockito.anyString(),
                                                            Mockito.anyString(),
                                                            Mockito.anyString(),
                                                            Mockito.anyString(),
                                                            Mockito.anyInt());

        Mockito.verify(sqLiteHelper, times(1)).createBook(Matchers.matches(AUTHOR_1),
                                                            Matchers.matches(TITLE_1),
                                                            Matchers.matches(START_DATE_1),
                                                            Matchers.matches(FINISH_DATE_1),
                                                            Matchers.eq(PAGES_1));

        Mockito.verify(sqLiteHelper, times(1)).createBook(Matchers.matches(AUTHOR_2),
                                                            Matchers.matches(TITLE_2),
                                                            Matchers.matches(START_DATE_2),
                                                            Matchers.matches(FINISH_DATE_2),
                                                            Matchers.eq(PAGES_2));

        Mockito.verify(sqLiteHelper, times(1)).getAllBooksArray();

        assertThat(books.size(), is(2));

        booksDataSource.deleteBook(mockBook1);
        books = booksDataSource.getAllBooksArray();

        assertThat(books.size(), is(1));

        Mockito.verify(sqLiteHelper, times(1)).deleteBook(1);

    }




}