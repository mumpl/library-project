
package service;

import model.Book;
import model.Reader;
import repository.BookRepository;
import monitoring.LibraryMetrics;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Library {

    private static final Logger log =
            LoggerFactory.getLogger(Library.class);

    private final BookRepository repository;
    private final LibraryMetrics metrics;

    public Library(BookRepository repository) {
        this(repository, new LibraryMetrics());
    }

    public Library(BookRepository repository, LibraryMetrics metrics) {
        this.repository = repository;
        this.metrics = metrics;
    }

    public void borrowBook(String bookId, Reader reader) {
        Book book = repository.findById(bookId);

        if (book == null) {
            log.error("Книга с ID {} не найдена", bookId);
            metrics.recordBorrowError(false);
            return;
        }

        if (book.isBorrowed()) {
            log.warn("ОШИБКА: Книга '{}' уже выдана!", book.getTitle());
            metrics.recordBorrowError(true);
            return;
        }

        book.setBorrowed(true);
        reader.addBook(book);
        repository.update(book);

        metrics.recordBorrowSuccess();

        log.info("Книга '{}' выдана {}", book.getTitle(), reader.getName());
    }

    public void returnBook(String bookId, Reader reader) {
        Book book = repository.findById(bookId);

        if (book == null) {
            log.error("Книга с ID {} не найдена", bookId);
            metrics.recordReturnError();
            return;
        }

        if (!book.isBorrowed()) {
            log.warn("Книга '{}' и так в библиотеке", book.getTitle());
            metrics.recordReturnError();
            return;
        }

        book.setBorrowed(false);
        reader.removeBook(book);
        repository.update(book);

        metrics.recordReturnSuccess();

        log.info("Книга '{}' возвращена", book.getTitle());
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();

        for (Book book : repository.findAll()) {
            if (!book.isBorrowed()) {
                available.add(book);
            }
        }

        return available;
    }

    public void addBook(Book book) {
        repository.save(book);
    }
}
