package app.libraryapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyUserAccountController implements Initializable {

    @FXML
    private AnchorPane draggableWindow;

    @FXML
    private Label lb_page_title;
    @FXML
    private Label lb_message;

    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_first_name;
    @FXML
    private TextField tf_last_name;
    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_address;
    @FXML
    private TextField tf_city;
    @FXML
    private TextField tf_country;
    @FXML
    private PasswordField pf_password;
    @FXML
    private PasswordField pf_confirm_password;

    private double x,y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lb_page_title.setText("Modify account");
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

        userInformation();
    }

    public void userInformation(){

        tf_username.setText(UserTempProfile.getInstance().getUser().getUsername());
        tf_first_name.setText(UserTempProfile.getInstance().getUser().getFirstName());
        tf_last_name.setText(UserTempProfile.getInstance().getUser().getLastName());
        tf_phone.setText(UserTempProfile.getInstance().getUser().getPhone());
        tf_email.setText(UserTempProfile.getInstance().getUser().getEmail());
        tf_address.setText(UserTempProfile.getInstance().getUser().getAddress());
        tf_city.setText(UserTempProfile.getInstance().getUser().getCity());
        tf_country.setText(UserTempProfile.getInstance().getUser().getCountry());
        pf_password.setText(UserTempProfile.getInstance().getUser().getPassword());

    }

    @FXML
    void modifyBtnAction(ActionEvent event){
        Tools.modifyUserAccountBtnAction(event, tf_username, tf_first_name, tf_last_name, tf_phone, tf_email, tf_address,
                tf_city, tf_country, pf_password, pf_confirm_password, lb_message);
    }

    @FXML
    void cancelBtnAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void hideMessage(){
        lb_message.setText("");
    }
}
