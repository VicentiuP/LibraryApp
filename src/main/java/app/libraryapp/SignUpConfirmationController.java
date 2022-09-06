package app.libraryapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpConfirmationController implements Initializable{

    @FXML
    private HBox draggableWindow;

    @FXML
    private Label lb_first_txt;
    @FXML
    private Label lb_second_txt;

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

        String username = UserTempProfile.getInstance().getUser().getUsername();
        showWelcomeMessage(username);
    }

    public void showWelcomeMessage(String username) {
        lb_first_txt.setText("Congratulations " + username + "!");
        lb_second_txt.setText("Account successfully created!");
    }

    @FXML
    public void okBtnAction(ActionEvent event){
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
