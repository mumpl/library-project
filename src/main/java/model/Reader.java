package model;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    private String id;
    private String name;
    private List<Book> books;

    public Reader(String id, String name) {
        this.id = id;
        this.name = name;
        this.books = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Book> getBooks() { return books; }

    public void addBook(Book book) { books.add(book); }
    public void removeBook(Book book) { books.remove(book); }
}
