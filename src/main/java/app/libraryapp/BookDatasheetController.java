package app.libraryapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BookDatasheetController implements Initializable {

    @FXML
    private AnchorPane draggableWindow;

    @FXML
    private Label lb_page_title;
    @FXML
    private Label lb_message;

    @FXML
    private TableView<User> customersTable;
    @FXML
    private TableColumn<User, Integer> column_customer_id;
    @FXML
    private TableColumn<User, String> column_first_name;
    @FXML
    private TableColumn<User, String> column_last_name;
    @FXML
    private TableColumn<User, String> column_status;
    @FXML
    private TableColumn<User, Integer> column_quantity;


    @FXML
    private Label gp_lb_book_id;
    @FXML
    private Label gp_lb_book_author;
    @FXML
    private Label gp_lb_book_title;
    @FXML
    private Label gp_lb_book_category;
    @FXML
    private Label gp_lb_book_publisher;
    @FXML
    private Label gp_lb_book_pages;
    @FXML
    private Label gp_lb_book_age;
    @FXML
    private Label gp_lb_book_total_qty;
    @FXML
    private Label gp_lb_book_available_qty;
    @FXML
    private Label gp_lb_book_reserved_qty;
    @FXML
    private Label gp_lb_book_lent_qty;

    @FXML
    private ImageView img_cover;

    private double x,y = 0;

    public boolean selectedCustomer = false;

    int bookId = BookTempProfile.getInstance().getBook().getBookId();
    ObservableList<User> customers = FXCollections.observableArrayList(DataSource.getInstance().queryCustomersByLentBookId(bookId));

    private static BookDatasheetController instance;
    public BookDatasheetController(){
        instance = this;
    }

    public static BookDatasheetController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lb_page_title.setText("'" + BookTempProfile.getInstance().getBook().getTitle() + "' Datasheet");
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

        customersTable.setOnMouseClicked(mouseEvent -> {
            lb_message.setText("");
            if (!customersTable.getSelectionModel().isEmpty()){
                selectedCustomer = true;
            }
        });

        showSelectedBookInfoInGridPane();

        img_cover.setImage(BookTempProfile.getInstance().getBook().getCover().getImage());

        showCustomersTable();

        customiseBookStatus(column_status);

    }

    public void showCustomersTable(){

        column_customer_id.setCellValueFactory(new PropertyValueFactory<>("UserId"));
        column_first_name.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        column_last_name.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        column_status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        column_quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

        customersTable.setItems(customers);
    }

    @FXML
    public void showSelectedBookInfoInGridPane(){

        int bookId = BookTempProfile.getInstance().getBook().getBookId();
        int availableQuantity = DataSource.getInstance().queryAvailableBookQuantityByBookId(bookId);
        int reservedQuantity = DataSource.getInstance().queryReservedBookQuantityByBookId(bookId);
        int lentQuantity = DataSource.getInstance().queryLentBookQuantityByBookId(bookId);
        int totalQuantity = availableQuantity + lentQuantity + reservedQuantity;

        gp_lb_book_id.setText(String.valueOf(bookId));
        gp_lb_book_author.setText(BookTempProfile.getInstance().getBook().getAuthor());
        gp_lb_book_title.setText(BookTempProfile.getInstance().getBook().getTitle());
        gp_lb_book_category.setText(BookTempProfile.getInstance().getBook().getCategory());
        gp_lb_book_publisher.setText(BookTempProfile.getInstance().getBook().getPublisher());
        gp_lb_book_pages.setText(String.valueOf(BookTempProfile.getInstance().getBook().getPages()));
        gp_lb_book_age.setText(String.valueOf(BookTempProfile.getInstance().getBook().getAge()));
        gp_lb_book_total_qty.setText(String.valueOf(totalQuantity));
        gp_lb_book_available_qty.setText(String.valueOf(availableQuantity));
        gp_lb_book_reserved_qty.setText(String.valueOf(reservedQuantity));
        gp_lb_book_lent_qty.setText(String.valueOf(lentQuantity));

    }

    @FXML
    public void acceptReservationBtnAction(){
        if (selectedCustomer){
            int selectedCustomerId = customersTable.getSelectionModel().getSelectedItem().getUserId();
            String bookStatus = customersTable.getSelectionModel().getSelectedItem().getStatus();

            if (bookStatus.equals("RESERVED")){
                DataSource.getInstance().updateBookStatusByUserId(selectedCustomerId);
                lb_message.setText("Book lent!");
                selectedCustomer = false;
                refreshCustomersTable();
            } else {
                lb_message.setText("This book is already lent!");
            }
        } else {
            lb_message.setText("Please select a customer!");
        }
    }

    @FXML
    public void cancelReservationBtnAction(){
        if (selectedCustomer){
            int selectedCustomerId = customersTable.getSelectionModel().getSelectedItem().getUserId();
            int bookId = BookTempProfile.getInstance().getBook().getBookId();
            String bookStatus = customersTable.getSelectionModel().getSelectedItem().getStatus();

            if (bookStatus.equals("RESERVED")){
                DataSource.getInstance().deleteLentOrReservedBook(bookId, selectedCustomerId);
                lb_message.setText("Reservation cancelled");
                selectedCustomer = false;
                refreshCustomersTable();
            } else {
                lb_message.setText("This book is lent!");
            }
        } else {
            lb_message.setText("Please select a customer!");
        }
    }

    @FXML
    public void bookReturnedBtnAction(){
        if (selectedCustomer){
            int selectedCustomerId = customersTable.getSelectionModel().getSelectedItem().getUserId();
            int bookId = BookTempProfile.getInstance().getBook().getBookId();
            String bookStatus = customersTable.getSelectionModel().getSelectedItem().getStatus();

            if (bookStatus.equals("LENT")){
                DataSource.getInstance().deleteLentOrReservedBook(bookId, selectedCustomerId);
                lb_message.setText("Book returned!");
                selectedCustomer = false;
                refreshCustomersTable();
            } else {
                lb_message.setText("This book is reserved only!");
            }
        } else {
            lb_message.setText("Please select a book!");
        }
    }

    @FXML
    public void closeBtnAction(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();

        LibraryPageController.getInstance().refreshBooksTable();
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

    public void refreshCustomersTable(){
        customers = FXCollections.observableArrayList(DataSource.getInstance().queryCustomersByLentBookId(bookId));
        showCustomersTable();
        showSelectedBookInfoInGridPane();
    }
}



