package app.libraryapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private AnchorPane draggableWindow;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField pf_password;

    @FXML
    private Label lb_message;

    private double x,y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        draggableWindow.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        draggableWindow.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) draggableWindow.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });

    }

    @FXML
    public void logInBtnAction(ActionEvent event){
        if (logInStatus()){
            UserLoggedProfile.getInstance().setUser(DataSource.getInstance().queryUserAccountByUsername(tf_username.getText()));

            String userType = UserLoggedProfile.getInstance().getUser().getUserType();
            if (userType.equals("admin") || userType.equals("employee")){
                Tools.newScene(event, "admin_menu.fxml");
            } else {
                UserTempProfile.getInstance().setUser(UserLoggedProfile.getInstance().getUser());
                Tools.newScene(event,"customer_datasheet.fxml");
            }
        }
    }

    public boolean logInStatus(){
        boolean status = false;

        String username = tf_username.getText();
        String password = pf_password.getText();

        if (username.isEmpty()) {
            lb_message.setText("Please enter your username!");
        } else if (password.isEmpty()) {
            lb_message.setText("Please enter your password!");
        } else {
            if(!DataSource.getInstance().checkIfUserExists(username, password)){
                lb_message.setText("Wrong username or password!");
            } else {
                status = true;
            }
        }

        return status;
    }

    @FXML
    public void forgotPasswordBtnAction(){
        lb_message.setText("Unavailable!");
    }

    @FXML
    public void signUpBtnAction(ActionEvent event){
        Tools.newScene(event, "signup.fxml");
    }

    @FXML
    public void minimizeBtnAction(ActionEvent event){
        Tools.minimizeStage(event);
    }

    @FXML
    public void exitBtnAction() throws IOException {
        Tools.exitApp();
    }

    @FXML
    public void hideMessage(){
        lb_message.setText("");
    }

}