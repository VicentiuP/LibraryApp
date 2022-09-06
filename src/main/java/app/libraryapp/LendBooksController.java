package app.libraryapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LendBooksController implements Initializable {

    @FXML
    private AnchorPane draggableWindow;

    @FXML
    private Label lb_page_title;
    @FXML
    private Label lb_message;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, String> column_cover;
    @FXML
    private TableColumn<Book, String> column_author;
    @FXML
    private TableColumn<Book, String> column_title;
    @FXML
    private TableColumn<Book, String> column_category;
    @FXML
    private TableColumn<Book, String> column_publisher;
    @FXML
    private TableColumn<Book, Integer> column_pages;
    @FXML
    private TableColumn<Book, Integer> column_age;
    @FXML
    private TableColumn<Book, Integer> column_quantity;

    private double x,y = 0;

    public boolean selectedBook = false;

    ObservableList<Book> books = FXCollections.observableArrayList(DataSource.getInstance().queryBooksList());

    private static LendBooksController instance;
    public LendBooksController(){
        instance = this;
    }
    public static LendBooksController getInstance(){
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lb_page_title.setText("Lend book");
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

        searchBar.setOnMouseClicked(mouseEvent -> {
            selectedBook = false;
            searchBar.clear();
            searchBarAction();
            lb_message.setText("");
        });

        booksTable.setOnMouseClicked(mouseEvent -> {
            lb_message.setText("");
            if (!booksTable.getSelectionModel().isEmpty()){
                selectedBook = true;
            }
        });

    }

    public void showBooksTable(){
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
    public void searchBarAction(){

        FilteredList<Book> filteredData = new FilteredList<>(books, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(Book -> {
            if (newValue.isEmpty() || newValue.isBlank()){
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            if (Book.getAuthor().toLowerCase().contains(searchKeyword)){
                return true;
            } else if (Book.getTitle().toLowerCase().contains(searchKeyword)){
                return true;
            } else if (Book.getCategory().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (Book.getPublisher().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (String.valueOf(Book.getPages()).contains(searchKeyword)) {
                return true;
            } else if (String.valueOf(Book.getAge()).contains(searchKeyword)) {
                return true;
            } else return  (String.valueOf(Book.getQuantity()).toLowerCase().contains(searchKeyword));
        }));

        SortedList<Book> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(booksTable.comparatorProperty());
        booksTable.setItems(sortedList);
    }

    @FXML
    public void lendBtnAction(){
        if (selectedBook){
            int stock = booksTable.getSelectionModel().getSelectedItem().getQuantity();
            if (stock != 0){
                int selectedBookId = booksTable.getSelectionModel().getSelectedItem().getBookId();
                int customerId = UserTempProfile.getInstance().getUser().getUserId();
                String firstName = UserTempProfile.getInstance().getUser().getFirstName();
                String lastName = UserTempProfile.getInstance().getUser().getLastName();

                String alreadyLentOrReserved =
                        DataSource.getInstance().checkBookStatusByBookIdAndCustomerId(selectedBookId, customerId);

                if (alreadyLentOrReserved.isEmpty()){
                    DataSource.getInstance().addLentOrReservedBook(selectedBookId, customerId, firstName, lastName, "LENT");
                    lb_message.setText("Book lent!");
                } else {
                    lb_message.setText("This book is already " + alreadyLentOrReserved.toLowerCase() + "!");
                }
            } else {
                lb_message.setText("Out of stock!");
            }
        } else {
            lb_message.setText("Please select a book!");
        }
    }

    @FXML
    public void closeBtnAction(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void refreshBooksTable(){
        books = FXCollections.observableArrayList(DataSource.getInstance().queryBooksList());
        selectedBook = false;
        showBooksTable();
        searchBar.clear();
    }
}
