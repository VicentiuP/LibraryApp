package app.libraryapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @FXML
    private HBox draggableWindow;

    @FXML
    private Label lb_page_title;
    @FXML
    private Label lb_username;
    @FXML
    private Label lb_message;

    @FXML
    private Button btn_employees;
    @FXML
    private Button btn_customers;
    @FXML
    private Button btn_library;

    public String accessedPage;

    private double x,y = 0;

    private static AdminMenuController instance;
    public AdminMenuController(){
        instance = this;
    }
    public static AdminMenuController getInstance(){
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lb_username.setText(UserLoggedProfile.getInstance().getUser().getUsername());
        lb_page_title.setText("Administration Menu");
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

        if (UserLoggedProfile.getInstance().getUser().getUserType().equals("employee")){
            btn_employees.setVisible(false);
            btn_customers.setTranslateX(-81);
            btn_library.setTranslateX(-81);
        }
    }

    @FXML
    public void employeesBtnAction(ActionEvent event){
        Tools.newScene(event, "employees_page.fxml");
        accessedPage = "employee";
    }

    @FXML
    public void customersBtnAction(ActionEvent event){
        Tools.newScene(event, "customers_page.fxml");
        accessedPage = "customer";
    }

    @FXML
    public void libraryBtnAction(ActionEvent event){
        Tools.newScene(event, "library_page.fxml");
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
