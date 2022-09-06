package app.libraryapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    public static void newScene(ActionEvent event, String fxmlFile){
        try {
            Parent root = FXMLLoader.load(Tools.class.getResource(fxmlFile));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void newPanel(String fxmlFile) throws IOException {

        Parent root = FXMLLoader.load(Tools.class.getResource(fxmlFile));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void addUserAccountBtnAction(
            ActionEvent event, TextField tf_username, TextField tf_first_name, TextField tf_last_name,
            TextField tf_phone, TextField tf_email, TextField tf_address, TextField tf_city, TextField tf_country,
            PasswordField pf_password, PasswordField pf_confirm_password, String userTypeStr, Label lb_message) {

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
                    emailStr, addressStr, cityStr, countryStr, passwordStr, userTypeStr);

            if (userTypeStr.equals("employee")){
                EmployeesPageController.getInstance().refreshEmployeesTable();
            } else {
                CustomersPageController.getInstance().refreshCustomersTable();
            }


            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public static void modifyUserAccountBtnAction(
            ActionEvent event, TextField tf_username, TextField tf_first_name, TextField tf_last_name,
            TextField tf_phone, TextField tf_email, TextField tf_address, TextField tf_city, TextField tf_country,
            PasswordField pf_password, PasswordField pf_confirm_password, Label lb_message) {

        Pattern phonePattern = Pattern.compile("^\\(?(\\d{3})\\)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$");
        Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z\\d_-]+(\\.[A-Za-z\\d_-]+)*@"
                + "[^-][A-Za-z\\d-]+(\\.[A-Za-z\\d-]+)*(\\.[A-Za-z]{2,})$");

        String userIdStr = String.valueOf(UserTempProfile.getInstance().getUser().getUserId());
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
        } else if (!phoneMatcher.matches()){
            lb_message.setText("The phone number must have 10 digits (continuously written) or US format!");
        } else if (!emailMatcher.matches()){
            lb_message.setText("The email is incorrect!");
        } else if (passwordStr.length() < 4) {
            lb_message.setText("The password must have at least 4 characters!");
        } else if (!passwordStr.equals(confirmPasswordStr)) {
            lb_message.setText("Please confirm the password correctly!");
        } else {
            DataSource.getInstance().updateUserAccount(userIdStr, usernameStr, firstNameStr, lastNameStr, phoneStr,
                    emailStr, addressStr, cityStr, countryStr, passwordStr);

            if (AdminMenuController.getInstance().accessedPage.equals("employee")){
                EmployeesPageController.getInstance().refreshEmployeesTable();
            } else {
                CustomersPageController.getInstance().refreshCustomersTable();
            }

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public static void minimizeStage(ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public static void exitApp() throws IOException {
        FXMLLoader loader = new FXMLLoader(Tools.class.getResource("exit.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

}
