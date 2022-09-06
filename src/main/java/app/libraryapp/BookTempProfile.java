package app.libraryapp;

public final class BookTempProfile {
    private Book book;

    private final static BookTempProfile instance = new BookTempProfile();

    private BookTempProfile() {}

    public static BookTempProfile getInstance() {
        return instance;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return this.book;
    }
}
