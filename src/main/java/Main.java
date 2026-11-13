import model.Book;
import model.Reader;
import repository.InMemoryBookRepository;
import service.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Библиотека запущена\n");

        InMemoryBookRepository repository = new InMemoryBookRepository();
        Library library = new Library(repository);

        Book book1 = new Book("1", "Война и мир", "Толстой");
        Book book2 = new Book("2", "Преступление и наказание", "Достоевский");
        Book book3 = new Book("3", "Мастер и Маргарита", "Булгаков");

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        log.info("Добавлено 3 книги\n");

        Reader ivan = new Reader("R1", "Иван");
        Reader petr = new Reader("R2", "Петр");

        log.info("Доступные книги:");
        for (Book book : library.getAvailableBooks()) {
            log.info("  {}", book);
        }
        log.info("");

        log.info("Ваня берет 'Война и мир'");
        library.borrowBook("1", ivan);
        log.info("");

        log.info("Петр пытается взять 'Война и мир'");
        library.borrowBook("1", petr);
        log.info("");

        log.info("Ваня возвращает 'Война и мир'");
        library.returnBook("1", ivan);
        log.info("");

        log.info("Доступные книги после возврата:");
        for (Book book : library.getAvailableBooks()) {
            log.info("  {}", book);
        }

        log.info("Новая версия проекта!");


    }
}