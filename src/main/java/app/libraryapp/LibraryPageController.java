package app.libraryapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryPageController implements Initializable {

    @FXML
    private HBox draggableWindow;

    @FXML
    private Label lb_page_title;
    @FXML
    private Label lb_username;
    @FXML
    private Label lb_message;

    @FXML
    private TextField searchBar;

    @FXML
    private Button btn_refresh;

    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, Integer> column_id;
    @FXML
    private TableColumn<Book, String> column_cover;
    @FXML
    private TableColumn<Book, String> column_author;
    @FXML
    private TableColumn<Book, String> column_title;
    @FXML
    private TableColumn<Book, String> column_category;
    @FXML
    private TableColumn<Book, Integer> column_publisher;
    @FXML
    private TableColumn<Book, Integer> column_pages;
    @FXML
    private TableColumn<Book, Integer> column_age;
    @FXML
    private TableColumn<Book, Integer> column_quantity;

    @FXML
    private Label gp_lb_id;
    @FXML
    private Label gp_lb_author;
    @FXML
    private Label gp_lb_title;
    @FXML
    private Label gp_lb_category;
    @FXML
    private Label gp_lb_publisher;
    @FXML
    private Label gp_lb_pages;
    @FXML
    private Label gp_lb_age;

    @FXML
    private ImageView img_cover;

    private double x,y = 0;

    public boolean selectedBook = false;

    ObservableList<Book> books = FXCollections.observableArrayList(DataSource.getInstance().queryBooksList());

    private static LibraryPageController instance;
    public LibraryPageController(){
        instance = this;
    }
    public static LibraryPageController getInstance(){
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lb_username.setText(UserLoggedProfile.getInstance().getUser().getUsername());
        lb_page_title.setText("Library Page");
        lb_message.getText();

        draggableWindow.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        draggableWindow.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) draggableWindow.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });

        showBooksTable();
        booksTable.setOnMouseClicked(mouseEvent -> {
            lb_message.setText("");
            if (!booksTable.getSelectionModel().isEmpty()){
                selectedBook = true;
                BookTempProfile.getInstance().setBook(selectedBookInformation());
                showSelectedBookInfoInGridPane();
                img_cover.setImage(BookTempProfile.getInstance().getBook().getCover().getImage());
            }
        });

        searchBar.setOnMouseClicked(mouseEvent -> {
            refreshBooksTable();
            searchBarAction();
        });

        btn_refresh.setOnAction(mouseEvent -> refreshBooksTable());

    }

    @FXML
    public void showBooksTable(){

        column_id.setCellValueFactory(new PropertyValueFactory<>("BookId"));
        column_cover.setCellValueFactory(new PropertyValueFactory<>("Cover"));
        column_author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        column_title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        column_category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        column_publisher.setCellValueFactory(new PropertyValueFactory<>("Publisher"));
        column_pages.setCellValueFactory(new PropertyValueFactory<>("Pages"));
        column_age.setCellValueFactory(new PropertyValueFactory<>("Age"));
        column_quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

        booksTable.setItems(books);
    }

    @FXML
    public void showSelectedBookInfoInGridPane(){
        gp_lb_id.setText(String.valueOf(BookTempProfile.getInstance().getBook().getBookId()));
        gp_lb_author.setText(BookTempProfile.getInstance().getBook().getAuthor());
        gp_lb_title.setText(BookTempProfile.getInstance().getBook().getTitle());
        gp_lb_category.setText(BookTempProfile.getInstance().getBook().getCategory());
        gp_lb_publisher.setText(BookTempProfile.getInstance().getBook().getPublisher());
        gp_lb_pages.setText(String.valueOf(BookTempProfile.getInstance().getBook().getPages()));
        gp_lb_age.setText(String.valueOf(BookTempProfile.getInstance().getBook().getAge()));
    }

    public Book selectedBookInformation(){
        Book selectedBook = new Book();

        if (!booksTable.getSelectionModel().isEmpty()){
            int selectedBookId = booksTable.getSelectionModel().getSelectedItem().getBookId();
            selectedBook = DataSource.getInstance().queryBookInfoById(selectedBookId);
        }

        return selectedBook;
    }

    @FXML
    public void searchBarAction(){

        FilteredList<Book> filteredData = new FilteredList<>(books, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(Book -> {
            if (newValue.isEmpty() || newValue.isBlank()){
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            if (String.valueOf(Book.getBookId()).contains(searchKeyword)){
                return true;
            } else if (Book.getAuthor().toLowerCase().contains(searchKeyword)){
                return true;
            } else if (Book.getTitle().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (Book.getCategory().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (Book.getPublisher().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (String.valueOf(Book.getPages()).contains(searchKeyword)) {
                return true;
            } else return (String.valueOf(Book.getAge()).contains(searchKeyword));
        }));

        SortedList<Book> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(booksTable.comparatorProperty());
        booksTable.setItems(sortedList);
    }

    @FXML
    public void addBtnAction() throws IOException{
        Tools.newPanel("add_book.fxml");
    }

    @FXML
    public void modifyBtnAction() throws IOException {
        if (selectedBook){
            BookTempProfile.getInstance().setBook(selectedBookInformation());
            Tools.newPanel("modify_book.fxml");
            lb_message.setText("");
        } else {
            lb_message.setText("Please select a book!");
        }
    }

    @FXML
    public void deleteBtnAction() throws IOException{
        if (selectedBook){
            BookTempProfile.getInstance().setBook(selectedBookInformation());
            Tools.newPanel("delete_book.fxml");
            lb_message.setText("");
        } else {
            lb_message.setText("Please select a book!");
        }
    }

    @FXML
    public void datasheetBtnAction() throws IOException{
        if (selectedBook){
            BookTempProfile.getInstance().setBook(selectedBookInformation());
            Tools.newPanel("book_datasheet.fxml");
            lb_message.setText("");
        } else {
            lb_message.setText("Please select a book!");
        }
    }

    @FXML
    public void refreshBooksTable(){
        lb_message.setText("");

        books = FXCollections.observableArrayList(DataSource.getInstance().queryBooksList());
        showBooksTable();

        selectedBook = false;
        BookTempProfile.getInstance().setBook(new Book());
        showSelectedBookInfoInGridPane();
        gp_lb_id.setText("");
        gp_lb_pages.setText("");
        gp_lb_age.setText("");
        img_cover.imageProperty().set(null);

        searchBar.clear();
    }

    @FXML
    void backBtnAction(ActionEvent event) {
        Tools.newScene(event, "admin_menu.fxml");
    }

    @FXML
    public void logOutBtnAction(ActionEvent event) {
        Tools.newScene(event, "login.fxml");
    }

    @FXML
    public void minimizeBtnAction(ActionEvent event){
        Tools.minimizeStage(event);
    }

    @FXML
    public void exitBtnAction() throws IOException {
        Tools.exitApp();
    }

}
