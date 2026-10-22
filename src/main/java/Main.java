import model.Book;
import model.Reader;
import repository.InMemoryBookRepository;
import service.Library;

public class Main {
    public static void main(String[] args) {
        System.out.println("Библиотека\n");

        InMemoryBookRepository repository = new InMemoryBookRepository();
        Library library = new Library(repository);  // DI

        Book book1 = new Book("1", "Война и мир", "Толстой");
        Book book2 = new Book("2", "Преступление и наказание", "Достоевский");
        Book book3 = new Book("3", "Мастер и Маргарита", "Булгаков");

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        System.out.println("Добавлено 3 книги\n");

        Reader ivan = new Reader("R1", "Иван");
        Reader petr = new Reader("R2", "Петр");

        System.out.println("Доступные книги:");
        for (Book book : library.getAvailableBooks()) {
            System.out.println("  " + book);
        }
        System.out.println();

        System.out.println("Иван берет 'Война и мир'");
        library.borrowBook("1", ivan);
        System.out.println();

        System.out.println("Петр пытается взять 'Война и мир'");
        library.borrowBook("1", petr);
        System.out.println();

        System.out.println("Иван возвращает 'Война и мир'");
        library.returnBook("1", ivan);
        System.out.println();

        System.out.println("Доступные книги после возврата:");
        for (Book book : library.getAvailableBooks()) {
            System.out.println("  " + book);
        }
    }
}
