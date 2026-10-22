package model;

import org.apache.commons.lang3.StringUtils;   // ← импорт из внешней библиотеки

public class Book {
    private String id;
    private String title;
    private String author;
    private boolean borrowed;

    public Book(String id, String title, String author) {
                        //пусто или нет
        if (StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("Название книги не может быть пустым");
        }
        if (StringUtils.isBlank(author)) {
            throw new IllegalArgumentException("Автор не может быть пустым");
        }

        this.id = id;
        this.title = title;
        this.author = author;
        this.borrowed = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isBorrowed() { return borrowed; }
    public void setBorrowed(boolean borrowed) { this.borrowed = borrowed; }

    @Override
    public String toString() {
        //StringUtils.capitalize делает первую букву заглавной
        return StringUtils.capitalize(title) + " (" + author + ") - " +
                (borrowed ? "выдана" : "доступна");
    }
}