
package monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

public class LibraryMetrics {

    private final PrometheusMeterRegistry registry;

    private final Counter operationsTotal;
    private final Counter operationsSuccess;
    private final Counter operationsError;
    private final Counter booksIssued;
    private final Counter booksReturned;
    private final Counter unavailableBookAttempts;

    public LibraryMetrics() {
        registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        operationsTotal = Counter.builder("application_operations_total")
                .description("Общее количество операций библиотеки")
                .register(registry);

        operationsSuccess = Counter.builder("application_operations_success_total")
                .description("Количество успешных операций")
                .register(registry);

        operationsError = Counter.builder("application_operations_error_total")
                .description("Количество ошибочных операций")
                .register(registry);

        booksIssued = Counter.builder("library_books_issued_total")
                .description("Количество выданных книг")
                .register(registry);

        booksReturned = Counter.builder("library_books_returned_total")
                .description("Количество возвращенных книг")
                .register(registry);

        unavailableBookAttempts = Counter.builder("library_unavailable_book_attempts_total")
                .description("Попытки получить недоступную книгу")
                .register(registry);
    }

    public void recordBorrowSuccess() {
        operationsTotal.increment();
        operationsSuccess.increment();
        booksIssued.increment();
    }

    public void recordBorrowError(boolean unavailable) {
        operationsTotal.increment();
        operationsError.increment();

        if (unavailable) {
            unavailableBookAttempts.increment();
        }
    }

    public void recordReturnSuccess() {
        operationsTotal.increment();
        operationsSuccess.increment();
        booksReturned.increment();
    }

    public void recordReturnError() {
        operationsTotal.increment();
        operationsError.increment();
    }

    public PrometheusMeterRegistry getRegistry() {
        return registry;
    }
}

