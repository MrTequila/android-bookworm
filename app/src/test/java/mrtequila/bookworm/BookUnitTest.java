package mrtequila.bookworm;

import org.junit.Test;

import static org.junit.Assert.*;


public class BookUnitTest {

    @Test
    public void bookToStringShouldReturnAppropriateString() throws Exception {
        Book tester = new Book(1, "Author", "Title", "2017-02-01", "2017-02-03", 666);

       /* tester.setTitle("Title");
        tester.setAuthor("Author");
        tester.setPageNumber(666);*/

        assertEquals("Message should be: Title, Author, 666 pages.", "Title, Author, 666 pages.", tester.toString());
    }
}
