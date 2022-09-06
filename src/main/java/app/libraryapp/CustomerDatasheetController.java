package app.libraryapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerDatasheetController implements Initializable {

    @FXML
    private AnchorPane draggableWindow;

    @FXML
    private Label lb_page_title;
    @FXML
    private Label lb_message;

    @FXML
    private Button btn_lend_new_book;
    @FXML
    private Button btn_reserve_new_book;
    @FXML
    private Button btn_accept_reservation;
    @FXML
    private Button btn_cancel_reservation;
    @FXML
    private Button btn_book_returned;
    @FXML
    private Button btn_exit;

    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<User, String> column_cover;
    @FXML
    private TableColumn<User, String> column_author;
    @FXML
    private TableColumn<User, String> column_title;
    @FXML
    private TableColumn<User, String> column_status;

    @FXML
    private Label gp_lb_id;
    @FXML
    private Label gp_lb_username;
    @FXML
    private Label gp_lb_first_name;
    @FXML
    private Label gp_lb_last_name;
    @FXML
    private Label gp_lb_phone;
    @FXML
    private Label gp_lb_email;
    @FXML
    private Label gp_lb_address;
    @FXML
    private Label gp_lb_city;
    @FXML
    private Label gp_lb_country;
    @FXML
    private Label gp_lb_password;

    private double x,y = 0;

    public boolean selectedBook = false;

    int customerId = UserTempProfile.getInstance().getUser().getUserId();
    ObservableList<Book> books = FXCollections.observableArrayList(DataSource.getInstance().queryLentBooksByCustomerId(customerId));

    private static CustomerDatasheetController instance;
    public CustomerDatasheetController(){
        instance = this;
    }

    public static CustomerDatasheetController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String selectedUser = UserTempProfile.getInstance().getUser().getUsername();
        lb_page_title.setText(selectedUser + " Datasheet");

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

        booksTable.setOnMouseClicked(mouseEvent -> {
            lb_message.setText("");
            if (!booksTable.getSelectionModel().isEmpty()){
                selectedBook = true;
            }
        });

        if (UserLoggedProfile.getInstance().getUser().getUserType().equals("customer")){
            btn_lend_new_book.setVisible(false);
            btn_accept_reservation.setVisible(false);
            btn_book_returned.setVisible(false);
            btn_reserve_new_book.setTranslateX(-85);
            btn_reserve_new_book.setTranslateY(20);
            btn_cancel_reservation.setTranslateX(-85);
            btn_cancel_reservation.setTranslateY(20);
            btn_exit.setText("Log Out");
            btn_exit.setOnAction(this::logOutBtnAction);
        } else {
            btn_exit.setText("Close");
            btn_exit.setOnAction(this::closeBtnAction);
        }

        showSelectedUserInfoInGridPane();

        showBooksTable();

        customiseBookStatus(column_status);

    }

    public void showBooksTable(){
        column_cover.setCellValueFactory(new PropertyValueFactory<>("Cover"));
        column_author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        column_title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        column_status.setCellValueFactory(new PropertyValueFactory<>("Status"));

        booksTable.setItems(books);
    }

    @FXML
    public void showSelectedUserInfoInGridPane(){

        gp_lb_id.setText(String.valueOf(UserTempProfile.getInstance().getUser().getUserId()));
        gp_lb_username.setText(UserTempProfile.getInstance().getUser().getUsername());
        gp_lb_first_name.setText(UserTempProfile.getInstance().getUser().getFirstName());
        gp_lb_last_name.setText(UserTempProfile.getInstance().getUser().getLastName());
        gp_lb_phone.setText(UserTempProfile.getInstance().getUser().getPhone());
        gp_lb_email.setText(UserTempProfile.getInstance().getUser().getEmail());
        gp_lb_address.setText(UserTempProfile.getInstance().getUser().getAddress());
        gp_lb_city.setText(UserTempProfile.getInstance().getUser().getCity());
        gp_lb_country.setText(UserTempProfile.getInstance().getUser().getCountry());
        gp_lb_password.setText(UserTempProfile.getInstance().getUser().getPassword());

    }

    @FXML
    public void lendNewBookBtnAction() throws IOException {
        lb_message.setText("");
        Tools.newPanel("lend_books.fxml");
    }

    @FXML
    public void reserveNewBookBtnAction() throws IOException {
        lb_message.setText("");
        Tools.newPanel("books_reservation.fxml");
    }

    @FXML
    public void acceptReservationBtnAction(){
        if (selectedBook){
            int selectedBookId = booksTable.getSelectionModel().getSelectedItem().getBookId();
            String bookStatus = booksTable.getSelectionModel().getSelectedItem().getStatus();

            if (bookStatus.equals("RESERVED")){
                DataSource.getInstance().updateBookStatusByBookId(selectedBookId);
                lb_message.setText("Book lent!");
                selectedBook = false;
                refreshBooksTable();
            } else {
                lb_message.setText("This book is already lent!");
            }
        } else {
            lb_message.setText("Please select a book!");
        }
    }

    @FXML
    public void cancelReservationBtnAction(){
        if (selectedBook){
            int selectedBookId = booksTable.getSelectionModel().getSelectedItem().getBookId();
            int customerId = UserTempProfile.getInstance().getUser().getUserId();
            String bookStatus = booksTable.getSelectionModel().getSelectedItem().getStatus();

            if (bookStatus.equals("RESERVED")){
                DataSource.getInstance().deleteLentOrReservedBook(selectedBookId, customerId);
                lb_message.setText("Reservation cancelled");
                selectedBook = false;
                refreshBooksTable();
            } else {
                lb_message.setText("This book is lent!");
            }
        } else {
            lb_message.setText("Please select a book!");
        }
    }

    @FXML
    public void bookReturnedBtnAction(){
        if (selectedBook){
            int selectedBookId = booksTable.getSelectionModel().getSelectedItem().getBookId();
            int customerId = UserTempProfile.getInstance().getUser().getUserId();
            String bookStatus = booksTable.getSelectionModel().getSelectedItem().getStatus();

            if (bookStatus.equals("LENT")){
                DataSource.getInstance().deleteLentOrReservedBook(selectedBookId, customerId);
                lb_message.setText("Book returned!");
                selectedBook = false;
                refreshBooksTable();
            } else {
                lb_message.setText("This book is reserved only!");
            }
        } else {
            lb_message.setText("Please select a book!");
        }
    }

    public void closeBtnAction(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void logOutBtnAction(ActionEvent event) {
        Tools.newScene(event, "login.fxml");
    }

    private void customiseBookStatus(TableColumn<User, String> columnName) {
        columnName.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                setText(empty ? "" : getItem());
                setGraphic(null);

                if (!isEmpty()) {
                    if(item.equals("RESERVED"))
                        setTextFill(Color.rgb(72,145,120));
                    else
                        setTextFill(Color.rgb(174,83,83));
                }
            }
        });
    }

    public void refreshBooksTable(){
        books = FXCollections.observableArrayList(DataSource.getInstance().queryLentBooksByCustomerId(customerId));
        showBooksTable();
        lb_message.setText("");
    }
}



