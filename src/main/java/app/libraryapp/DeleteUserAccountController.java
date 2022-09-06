package app.libraryapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteUserAccountController implements Initializable {

    @FXML
    private AnchorPane draggableWindow;

    @FXML
    private Label lb_message;

    private double x,y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lb_message.setText("Are you sure you want to delete?");

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
    public void yesBtnAction(ActionEvent event) {
        String userTempProfileId = String.valueOf(UserTempProfile.getInstance().getUser().getUserId());
        DataSource.getInstance().deleteUserAccount(userTempProfileId);

        if (AdminMenuController.getInstance().accessedPage.equals("employee")){
            EmployeesPageController.getInstance().refreshEmployeesTable();
        } else {
            CustomersPageController.getInstance().refreshCustomersTable();
        }

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void noBtnAction(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
