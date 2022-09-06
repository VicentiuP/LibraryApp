package app.libraryapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ModifyBookController implements Initializable {

    @FXML
    private AnchorPane draggableWindow;

    @FXML
    private Label lb_page_title;
    @FXML
    private Label lb_message;

    @FXML
    private Button btn_browse;

    @FXML
    private TextField tf_author;
    @FXML
    private TextField tf_title;
    @FXML
    private TextField tf_category;
    @FXML
    private TextField tf_publisher;
    @FXML
    private TextField tf_pages;
    @FXML
    private TextField tf_age;
    @FXML
    private TextField tf_quantity;
    @FXML
    private TextField tf_cover;

    final FileChooser fileChooser = new FileChooser();

    private double x,y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lb_page_title.setText("Modify book");
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

        bookInformation();

        btn_browse.setOnAction(actionEvent ->{
            tf_cover.clear();
            browseBtnAction();
        });
    }

    public void bookInformation(){
        tf_author.setText(BookTempProfile.getInstance().getBook().getAuthor());
        tf_title.setText(BookTempProfile.getInstance().getBook().getTitle());
        tf_category.setText(BookTempProfile.getInstance().getBook().getCategory());
        tf_publisher.setText(BookTempProfile.getInstance().getBook().getPublisher());
        tf_pages.setText(String.valueOf(BookTempProfile.getInstance().getBook().getPages()));
        tf_age.setText(String.valueOf(BookTempProfile.getInstance().getBook().getAge()));
        tf_quantity.setText(String.valueOf(BookTempProfile.getInstance().getBook().getQuantity()));
        int bookId = BookTempProfile.getInstance().getBook().getBookId();
        tf_cover.setText(DataSource.getInstance().queryBookCoverNameById(bookId));
    }

    @FXML
    public void modifyBtnAction(ActionEvent event){

        Pattern digitPattern = Pattern.compile("\\d+");

        String bookIdStr = String.valueOf(BookTempProfile.getInstance().getBook().getBookId());
        String authorStr = tf_author.getText().trim();
        String titleStr = tf_title.getText().trim();
        String categoryStr = tf_category.getText().trim();
        String publisherStr = tf_publisher.getText().trim();
        String pagesStr = tf_pages.getText().trim();
        String ageStr = tf_age.getText().trim();
        String quantityStr = tf_quantity.getText().trim();
        String coverStr = tf_cover.getText().trim();

        if (authorStr.isEmpty() || titleStr.isEmpty() || categoryStr.isEmpty() || publisherStr.isEmpty() ||
                pagesStr.isEmpty() || ageStr.isEmpty() || quantityStr.isEmpty() || coverStr.isEmpty()) {
            lb_message.setText("Please fill in all the fields!");
        } else if (!digitPattern.matcher(pagesStr).matches()) {
            lb_message.setText("The pages field is incorrect!");
        } else if (!digitPattern.matcher(ageStr).matches()) {
            lb_message.setText("The age field is incorrect!");
        } else if (Integer.parseInt(ageStr) > 99){
            lb_message.setText("The age is too old!");
        }else if (!digitPattern.matcher(quantityStr).matches()) {
            lb_message.setText("The quantity field is incorrect!");
        } else {
            DataSource.getInstance().updateBookInformation(bookIdStr, authorStr, titleStr, categoryStr, publisherStr,
                    Integer.parseInt(pagesStr), Integer.parseInt(ageStr), Integer.parseInt(quantityStr), coverStr);

            LibraryPageController.getInstance().refreshBooksTable();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void browseBtnAction(){
        lb_message.setText("");

        fileChooser.setTitle("Choose an image file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*.png", "*jpg", "*.gif"));

        File file = fileChooser.showOpenDialog(null);

        if (file != null){
            tf_cover.appendText(file.getName());
        } else {
            lb_message.setText("Choose an image file!");
        }
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
