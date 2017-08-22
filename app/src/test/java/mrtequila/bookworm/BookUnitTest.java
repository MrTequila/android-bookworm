package mrtequila.bookworm;

import org.junit.Test;

import static org.junit.Assert.*;


public class BookUnitTest {

    @Test
    public void bookToStringShouldReturnAppropriateString() {
        Book tester = new Book();

        tester.setTitle("Title");
        tester.setAuthor("Author");
        tester.setPageNumber(666);

        assertEquals("Message should be: Title, Author, 666 pages.", "Title, Author, 666 pages.", tester.toString());
    }
}
