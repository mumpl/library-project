package service;

import model.Book;
import model.Reader;
import repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Library {
    private static final Logger log = LoggerFactory.getLogger(Library.class);
    private BookRepository repository;

    public Library(BookRepository repository) {
        this.repository = repository;
    }

    public void borrowBook(String bookId, Reader reader) {
        Book book = repository.findById(bookId);

        if (book == null) {
            log.error("Книга с ID {} не найдена", bookId);
            return;
        }

        // ИНДИВИДУАЛЬНОЕ ЗАДАНИЕ: проверка, что книга не выдана
        if (book.isBorrowed()) {
            log.warn("ОШИБКА: Книга '{}' уже выдана!", book.getTitle());
            return;
        }

        book.setBorrowed(true);
        reader.addBook(book);
        repository.update(book);
        log.info("Книга '{}' выдана {}", book.getTitle(), reader.getName());
    }

    public void returnBook(String bookId, Reader reader) {
        Book book = repository.findById(bookId);

        if (book == null) {
            log.error("Книга с ID {} не найдена", bookId);
            return;
        }

        if (!book.isBorrowed()) {
            log.warn("Книга '{}' и так в библиотеке", book.getTitle());
            return;
        }

        book.setBorrowed(false);
        reader.removeBook(book);
        repository.update(book);
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