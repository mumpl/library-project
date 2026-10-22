package service;
import model.Book;
import model.Reader;
import repository.BookRepository;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private BookRepository repository;

    public Library(BookRepository repository) {  // Dependency Injection
        this.repository = repository;
    }

    public void borrowBook(String bookId, Reader reader) {
        Book book = repository.findById(bookId);

        if (book == null) {
            System.out.println("Книга не найдена");
            return;
        }

        if (book.isBorrowed()) {
            System.out.println("ОШИБКА: Книга '" + book.getTitle() + "' уже выдана!");
            return;
        }

        book.setBorrowed(true);
        reader.addBook(book);
        repository.update(book);
        System.out.println("Книга '" + book.getTitle() + "' выдана " + reader.getName());
    }

    public void returnBook(String bookId, Reader reader) {
        Book book = repository.findById(bookId);

        if (book == null) {
            System.out.println("Книга не найдена");
            return;
        }

        if (!book.isBorrowed()) {
            System.out.println("Книга и так в библиотеке");
            return;
        }

        book.setBorrowed(false);
        reader.removeBook(book);
        repository.update(book);
        System.out.println("Книга '" + book.getTitle() + "' возвращена");
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
