package test;
import model.Book;
import model.Reader;
import repository.InMemoryBookRepository;
import service.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    private Library library;
    private Book book;
    private Reader ivan;
    private Reader petr;

    @BeforeEach
    void setUp() {
        InMemoryBookRepository repository = new InMemoryBookRepository();
        library = new Library(repository);   //DI

        book = new Book("1", "Война и мир", "Толстой");
        library.addBook(book);

        ivan = new Reader("R1", "Иван");
        petr = new Reader("R2", "Петр");
    }

    //успешная выдача
    @Test
    void testSuccessfulBorrow() {
        library.borrowBook("1", ivan);

        assertTrue(book.isBorrowed());
        assertTrue(ivan.getBooks().contains(book));
        assertEquals(1, ivan.getBooks().size());
    }

    //нельзя взять, которая уже у него
    @Test
    void testCannotBorrowSameBookTwiceBySameReader() {
        library.borrowBook("1", ivan);
        library.borrowBook("1", ivan);

        assertEquals(1, ivan.getBooks().size());
        assertTrue(book.isBorrowed());
    }

    //индивид задание
    @Test
    void testIndividualTask_BookAlreadyBorrowedByAnother() {
        library.borrowBook("1", ivan);
        library.borrowBook("1", petr);

        assertTrue(ivan.getBooks().contains(book));
        assertFalse(petr.getBooks().contains(book));
        assertEquals(0, petr.getBooks().size());
        assertTrue(book.isBorrowed());
    }

    // Тест: нельзя вернуть книгу, которую не брали
    @Test
    void testCannotReturnNotBorrowedBook() {
        library.returnBook("1", ivan);

        // Книга остаётся доступной, её нет ни у кого
        assertFalse(book.isBorrowed());
        assertFalse(ivan.getBooks().contains(book));
        assertEquals(0, ivan.getBooks().size());
    }
}
