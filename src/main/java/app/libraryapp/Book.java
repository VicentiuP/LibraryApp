package app.libraryapp;

import javafx.scene.image.ImageView;

public class Book {

    private int bookId;
    private String author;
    private String title;
    private String category;
    private String publisher;
    private int pages;
    private int age;
    private ImageView cover;
    private int quantity;
    private String status;

    public Book(){}

//    public Book(int bookId, String author, String title, String category, String publisher, int pages,
//                int age, ImageView cover, int quantity, String status) {
//        this.bookId = bookId;
//        this.author = author;
//        this.title = title;
//        this.category = category;
//        this.publisher = publisher;
//        this.pages = pages;
//        this.age = age;
//        this.cover = cover;
//        this.quantity = quantity;
//        this.status = status;
//    }

    public Book(ImageView cover, String author, String title, String category, String publisher, int pages,
                int age, int quantity) {
        this.cover = cover;
        this.author = author;
        this.title = title;
        this.category = category;
        this.publisher = publisher;
        this.pages = pages;
        this.age = age;
        this.quantity = quantity;
    }

    public Book(ImageView cover, String author, String title, String status) {
        this.cover = cover;
        this.author = author;
        this.title = title;
        this.status = status;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public ImageView getCover() {
        return cover;
    }

    public void setCover(ImageView cover) {
        this.cover = cover;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
