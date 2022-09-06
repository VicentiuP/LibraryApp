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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomersPageController implements Initializable {


    @FXML
    private HBox draggableWindow;

    @FXML
    private Label lb_page_title;
    @FXML
    private Label lb_username;
    @FXML
    private Label lb_message;

    @FXML
    private Button btn_refresh;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView<User> customersTable;
    @FXML
    private TableColumn<User, Integer> column_id;
    @FXML
    private TableColumn<User, String> column_username;
    @FXML
    private TableColumn<User, String> column_first_name;
    @FXML
    private TableColumn<User, String> column_last_name;
    @FXML
    private TableColumn<User, Integer> column_phone;
    @FXML
    private TableColumn<User, String> column_email;
    @FXML
    private TableColumn<User, String> column_address;
    @FXML
    private TableColumn<User, String> column_city;
    @FXML
    private TableColumn<User, String> column_country;
    @FXML
    private TableColumn<User, String> column_password;

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

    public boolean selectedCustomer = false;

    ObservableList<User> customers = FXCollections.observableArrayList(DataSource.getInstance().queryCustomersList());

    private static CustomersPageController instance;
    public CustomersPageController(){
        instance = this;
    }
    public static CustomersPageController getInstance(){
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lb_username.setText(UserLoggedProfile.getInstance().getUser().getUsername());
        lb_page_title.setText("Customers Page");
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

        showCustomersTable();
        customersTable.setOnMouseClicked(mouseEvent -> {
            lb_message.setText("");
            if (!customersTable.getSelectionModel().isEmpty()){
                selectedCustomer = true;
                UserTempProfile.getInstance().setUser(selectedCustomerInformation());
                showSelectedCustomerInfoInGridPane();
            }
        });

        searchBar.setOnMouseClicked(mouseEvent -> {
            refreshCustomersTable();
            searchBarAction();
        });

        btn_refresh.setOnAction(mouseEvent -> refreshCustomersTable());
    }

    @FXML
    public void showCustomersTable(){

        column_id.setCellValueFactory(new PropertyValueFactory<>("UserId"));
        column_username.setCellValueFactory(new PropertyValueFactory<>("Username"));
        column_first_name.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        column_last_name.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        column_phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        column_email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        column_address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        column_city.setCellValueFactory(new PropertyValueFactory<>("City"));
        column_country.setCellValueFactory(new PropertyValueFactory<>("Country"));
        column_password.setCellValueFactory(new PropertyValueFactory<>("Password"));

        customersTable.setItems(customers);
    }

    @FXML
    public void showSelectedCustomerInfoInGridPane(){

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

    public User selectedCustomerInformation(){

        User selectedCustomer = new User();

        if (!customersTable.getSelectionModel().isEmpty()){
            String selectedCustomerId = String.valueOf(customersTable.getSelectionModel().getSelectedItem().getUserId());
            selectedCustomer = DataSource.getInstance().queryUserAccountById(selectedCustomerId);
        }

        return selectedCustomer;

    }

    @FXML
    public void searchBarAction(){

        FilteredList<User> filteredData = new FilteredList<>(customers, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(User -> {
            if (newValue.isEmpty() || newValue.isBlank()){
                return true;
            }

            String searchKeyword = newValue.toLowerCase();

            if (String.valueOf(User.getUserId()).contains(searchKeyword)){
                return true;
            } else if (User.getUsername().toLowerCase().contains(searchKeyword)){
                return true;
            } else if (User.getFirstName().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (User.getLastName().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (User.getPhone().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (User.getEmail().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (User.getAddress().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (User.getCity().toLowerCase().contains(searchKeyword)) {
                return true;
            } else return User.getCountry().toLowerCase().contains(searchKeyword);
        }));

        SortedList<User> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(customersTable.comparatorProperty());
        customersTable.setItems(sortedList);
    }

    @FXML
    public void addBtnAction() throws IOException{
        refreshCustomersTable();
        Tools.newPanel("add_user_account.fxml");
    }

    @FXML
    public void modifyBtnAction() throws IOException {
        if (selectedCustomer){
            UserTempProfile.getInstance().setUser(selectedCustomerInformation());
            Tools.newPanel("modify_user_account.fxml");
            lb_message.setText("");
        } else {
            lb_message.setText("Please select a customer!");
        }
    }

    @FXML
    public void deleteBtnAction() throws IOException{
        if (selectedCustomer){
            UserTempProfile.getInstance().setUser(selectedCustomerInformation());
            Tools.newPanel("delete_user_account.fxml");
            lb_message.setText("");
        } else {
            lb_message.setText("Please select a customer!");
        }
    }

    @FXML
    public void datasheetBtnAction() throws IOException{
        if (selectedCustomer){
            UserTempProfile.getInstance().setUser(selectedCustomerInformation());
            Tools.newPanel("customer_datasheet.fxml");
            lb_message.setText("");
        } else {
            lb_message.setText("Please select a customer!");
        }
    }

    @FXML
    public void refreshCustomersTable(){
        lb_message.setText("");

        customers = FXCollections.observableArrayList(DataSource.getInstance().queryCustomersList());
        showCustomersTable();

        selectedCustomer = false;
        UserTempProfile.getInstance().setUser(new User());
        showSelectedCustomerInfoInGridPane();
        gp_lb_id.setText("");

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
