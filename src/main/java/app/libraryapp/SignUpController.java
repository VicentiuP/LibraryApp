package app.libraryapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController implements Initializable {

    @FXML
    private HBox draggableWindow;

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

        lb_page_title.setText("Create account");

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
    public void signUpBtnAction(ActionEvent event){
        Pattern phonePattern = Pattern.compile("^\\(?(\\d{3})\\)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$");
        Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z\\d_-]+(\\.[A-Za-z\\d_-]+)*@"
                + "[^-][A-Za-z\\d-]+(\\.[A-Za-z\\d-]+)*(\\.[A-Za-z]{2,})$");

        String usernameStr = tf_username.getText().trim();
        String firstNameStr = tf_first_name.getText().trim();
        String lastNameStr = tf_last_name.getText().trim();
        String phoneStr = tf_phone.getText().trim();
            Matcher phoneMatcher = phonePattern.matcher(phoneStr);
        String emailStr = tf_email.getText().trim();
            Matcher emailMatcher = emailPattern.matcher(emailStr);
        String addressStr = tf_address.getText().trim();
        String cityStr = tf_city.getText().trim();
        String countryStr = tf_country.getText().trim();
        String passwordStr = pf_password.getText().trim();
        String confirmPasswordStr = pf_confirm_password.getText().trim();

        if (usernameStr.isEmpty() || firstNameStr.isEmpty() || lastNameStr.isEmpty() || phoneStr.isEmpty() ||
                emailStr.isEmpty() || addressStr.isEmpty() || cityStr.isEmpty() || countryStr.isEmpty() ||
                passwordStr.isEmpty() || confirmPasswordStr.isEmpty()) {
            lb_message.setText("Please fill in all the fields!");
        } else if (DataSource.getInstance().checkUserName(usernameStr)) {
            lb_message.setText("User already exists!");
        } else if (!phoneMatcher.matches()){
            lb_message.setText("The phone number must have 10 digits (continuously written) or US format!");
        } else if (!emailMatcher.matches()){
            lb_message.setText("The email is incorrect!");
        } else if (passwordStr.length() < 4) {
            lb_message.setText("The password must have at least 4 characters!");
        } else if (!passwordStr.equals(confirmPasswordStr)) {
            lb_message.setText("Please confirm the password correctly!");
        } else {
            DataSource.getInstance().addUserAccount(usernameStr, firstNameStr, lastNameStr, phoneStr,
                    emailStr, addressStr, cityStr, countryStr, passwordStr, "customer");

            UserTempProfile.getInstance().setUser(new User(usernameStr, firstNameStr, lastNameStr, phoneStr,
                    emailStr, addressStr, cityStr, countryStr, passwordStr, "customer"));

            Tools.newScene(event, "signup_confirmation.fxml");
        }
    }

    @FXML
    public void cancelBtnAction(ActionEvent event){
        Tools.newScene(event, "login.fxml");
    }

    @FXML
    public void minimizeBtnAction(ActionEvent event){
        Tools.minimizeStage(event);
    }

    @FXML
    public void exitBtnAction() throws IOException{
        Tools.exitApp();
    }

    @FXML
    public void hideMessage(){
        lb_message.setText("");
    }
}
