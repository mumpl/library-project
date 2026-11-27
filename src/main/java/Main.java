
import model.Book;
import model.Reader;
import repository.InMemoryBookRepository;
import service.Library;
import monitoring.LibraryMetrics;
import monitoring.MetricsServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {

    private static final Logger log =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        log.info("Библиотека запущена");

        LibraryMetrics metrics = new LibraryMetrics();

        MetricsServer metricsServer = new MetricsServer(metrics, 8080);
        metricsServer.start();

        InMemoryBookRepository repository =
                new InMemoryBookRepository();

        Library library = new Library(repository, metrics);

        Book book1 = new Book("1", "Война и мир", "Толстой");
        Book book2 = new Book("2", "Преступление и наказание", "Достоевский");
        Book book3 = new Book("3", "Мастер и Маргарита", "Булгаков");

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        Reader ivan = new Reader("R1", "Иван");
        Reader petr = new Reader("R2", "Петр");

        // Первоначальная демонстрация
        log.info("Иван берет 'Война и мир'");
        library.borrowBook("1", ivan);

        log.info("Петр пытается взять уже выданную книгу");
        library.borrowBook("1", petr);

        log.info("Иван возвращает 'Война и мир'");
        library.returnBook("1", ivan);

        // Интерактивное управление библиотекой
        try (Scanner scanner = new Scanner(System.in)) {

            boolean running = true;

            while (running) {

                System.out.println("\n===== МЕНЮ БИБЛИОТЕКИ =====");
                System.out.println("1. Выдать книгу");
                System.out.println("2. Вернуть книгу");
                System.out.println("3. Попытаться выдать занятую книгу");
                System.out.println("0. Завершить работу");
                System.out.print("Выберите действие: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {

                    case "1":
                        System.out.print("Введите ID книги (1, 2, 3): ");
                        String borrowId = scanner.nextLine().trim();

                        System.out.print("Читатель (1 - Иван, 2 - Петр): ");
                        String borrowReader = scanner.nextLine().trim();

                        Reader borrower = borrowReader.equals("1")
                                ? ivan : petr;

                        if (borrowReader.equals("1")
                                || borrowReader.equals("2")) {
                            library.borrowBook(borrowId, borrower);
                        } else {
                            System.out.println("Неизвестный читатель");
                        }
                        break;

                    case "2":
                        System.out.print("Введите ID книги (1, 2, 3): ");
                        String returnId = scanner.nextLine().trim();

                        System.out.print("Читатель (1 - Иван, 2 - Петр): ");
                        String returnReader = scanner.nextLine().trim();

                        Reader returner = returnReader.equals("1")
                                ? ivan : petr;

                        if (returnReader.equals("1")
                                || returnReader.equals("2")) {
                            library.returnBook(returnId, returner);
                        } else {
                            System.out.println("Неизвестный читатель");
                        }
                        break;

                    case "3":
                        System.out.println(
                                "Сначала выберите книгу, которая уже выдана."
                        );
                        System.out.print("Введите ID занятой книги: ");
                        String unavailableId = scanner.nextLine().trim();

                        library.borrowBook(unavailableId, petr);
                        break;

                    case "0":
                        running = false;
                        break;

                    default:
                        System.out.println("Неверный пункт меню");
                }

                System.out.println(
                        "\nМетрики: http://localhost:8080/metrics"
                );
            }
        } finally {
            metricsServer.stop();
        }

        log.info("Библиотека остановлена");
    }
}
