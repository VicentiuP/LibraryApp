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

public class EmployeesPageController implements Initializable {

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
    private TableView<User> employeesTable;
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

    private boolean selectedEmployee = false;

    ObservableList<User> employees = FXCollections.observableArrayList(DataSource.getInstance().queryEmployeesList());

    private static EmployeesPageController instance;
    public EmployeesPageController(){
        instance = this;
    }
    public static EmployeesPageController getInstance(){
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lb_username.setText(UserLoggedProfile.getInstance().getUser().getUsername());
        lb_page_title.setText("Employees Page");
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

        showEmployeesTable();
        employeesTable.setOnMouseClicked(mouseEvent -> {
            lb_message.setText("");
            if (!employeesTable.getSelectionModel().isEmpty()){
                selectedEmployee = true;
                UserTempProfile.getInstance().setUser(selectedEmployeeInformation());
                showSelectedEmployeeInfoInGridPane();
            }
        });

        searchBar.setOnMouseClicked(mouseEvent -> {
            refreshEmployeesTable();
            searchBarAction();
        });

        btn_refresh.setOnAction(mouseEvent -> refreshEmployeesTable());
    }

    @FXML
    public void showEmployeesTable(){

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

        employeesTable.setItems(employees);
    }

    @FXML
    public void showSelectedEmployeeInfoInGridPane(){

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

    public User selectedEmployeeInformation(){

        User selectedEmployee = new User();

        if (!employeesTable.getSelectionModel().isEmpty()){
            String selectedEmployeeId = String.valueOf(employeesTable.getSelectionModel().getSelectedItem().getUserId());
            selectedEmployee = DataSource.getInstance().queryUserAccountById(selectedEmployeeId);
        }

        return selectedEmployee;

    }

    @FXML
    public void searchBarAction(){

        FilteredList<User> filteredData = new FilteredList<>(employees, b -> true);

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
        sortedList.comparatorProperty().bind(employeesTable.comparatorProperty());
        employeesTable.setItems(sortedList);
    }

    @FXML
    public void addBtnAction() throws IOException{
        refreshEmployeesTable();
        Tools.newPanel("add_user_account.fxml");
    }

    @FXML
    public void modifyBtnAction() throws IOException {
        if (selectedEmployee){
            UserTempProfile.getInstance().setUser(selectedEmployeeInformation());
            Tools.newPanel("modify_user_account.fxml");
            lb_message.setText("");
        } else {
            lb_message.setText("Please select an employee!");
        }
    }

    @FXML
    public void deleteBtnAction() throws IOException{
        if (selectedEmployee){
            String userTempProfile = String.valueOf(UserTempProfile.getInstance().getUser().getUserId());
            String userLoggedProfile = String.valueOf(UserLoggedProfile.getInstance().getUser().getUserId());

            if (userTempProfile.equals(userLoggedProfile)){
                lb_message.setText("You cannot delete your own account!");
            } else {
                UserTempProfile.getInstance().setUser(selectedEmployeeInformation());
                Tools.newPanel("delete_user_account.fxml");
                lb_message.setText("");
            }
        } else {
            lb_message.setText("Please select an employee!");
        }
    }

    @FXML
    public void refreshEmployeesTable(){
        lb_message.setText("");

        employees = FXCollections.observableArrayList(DataSource.getInstance().queryEmployeesList());
        showEmployeesTable();

        selectedEmployee = false;
        UserTempProfile.getInstance().setUser(new User());
        showSelectedEmployeeInfoInGridPane();
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
