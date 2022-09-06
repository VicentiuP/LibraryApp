package app.libraryapp;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private Connection conn;

    public static final String DB_NAME = "libraryDatabase.db";
    public static final String DB_SOURCE = "jdbc:sqlite:D:\\Workspace_Java\\Exercises\\LibraryApp\\src\\main\\resources\\app\\database\\" + DB_NAME;

    public static final String COVERS_SOURCE = "D:/Workspace_Java/Exercises/LibraryApp/src/main/resources/app/img/covers/";

    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_USER_ID = "UserId";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_CITY = "City";
    public static final String COLUMN_COUNTRY = "Country";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_USER_TYPE = "UserType";

    public static final String TABLE_BOOKS = "Books";
    public static final String COLUMN_BOOK_ID = "BookId";
    public static final String COLUMN_AUTHOR = "Author";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_CATEGORY = "Category";
    public static final String COLUMN_PUBLISHER = "Publisher";
    public static final String COLUMN_PAGES = "Pages";
    public static final String COLUMN_AGE = "Age";
    public static final String COLUMN_COVER = "Cover";
    public static final String COLUMN_QUANTITY = "Quantity";

    public static final String TABLE_LENT_OR_RESERVED_BOOKS = "LentOrReservedBooks";
    public static final String COLUMN_STATUS = "Status";


    public static final String QUERY_USERNAME =
            "SELECT * FROM " + TABLE_USERS +
            " WHERE " + COLUMN_USERNAME + " = ?";

    public static final String QUERY_USER_ACCOUNT_BY_USERNAME =
            "SELECT * FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_USERNAME + " = ?";

    public static final String QUERY_USER_ACCOUNT_BY_ID =
            "SELECT * FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_USER_ID + " = ?";

    public static final String QUERY_USER_TYPE_BY_USERNAME =
            "SELECT " + COLUMN_USER_TYPE + " FROM " + TABLE_USERS +
            " WHERE " + COLUMN_USERNAME + " = ?";

    public static final String ADD_USER_ACCOUNT =
            "INSERT INTO " + TABLE_USERS + " (" +
            COLUMN_USERNAME + ", " +
            COLUMN_FIRST_NAME + ", " +
            COLUMN_LAST_NAME + ", " +
            COLUMN_PHONE + ", " +
            COLUMN_EMAIL + ", " +
            COLUMN_ADDRESS + ", " +
            COLUMN_CITY + ", " +
            COLUMN_COUNTRY + ", " +
            COLUMN_PASSWORD + ", " +
            COLUMN_USER_TYPE + ") " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_USER_ACCOUNT =
            "UPDATE " + TABLE_USERS +
            " SET " +
            COLUMN_USERNAME + " = ?, " +
            COLUMN_FIRST_NAME + " = ?, " +
            COLUMN_LAST_NAME + " = ?, " +
            COLUMN_PHONE + " = ?, " +
            COLUMN_EMAIL + " = ?, " +
            COLUMN_ADDRESS + " = ?, " +
            COLUMN_CITY + " = ?, " +
            COLUMN_COUNTRY + " = ?, " +
            COLUMN_PASSWORD + " = ? " +
            "WHERE " + COLUMN_USER_ID + " = ?";

    public static final String DELETE_USER_ACCOUNT =
            "DELETE FROM " + TABLE_USERS +
            " WHERE " + COLUMN_USER_ID  + " = ?";

    public static final String QUERY_EMPLOYEES_LIST =
            "SELECT * FROM " + TABLE_USERS +
            " WHERE " + COLUMN_USER_TYPE + " IN ('admin', 'employee')";

    public static final String QUERY_CUSTOMERS_LIST =
            "SELECT * FROM " + TABLE_USERS +
            " WHERE " + COLUMN_USER_TYPE + " = 'customer'";

    public static final String QUERY_BOOKS_LIST =
            "SELECT * FROM " + TABLE_BOOKS;

    public static final String QUERY_BOOKS_BY_CUSTOMER =
            "SELECT " + TABLE_BOOKS + "." + COLUMN_BOOK_ID + ", " +
                        TABLE_BOOKS + "." + COLUMN_COVER + ", " +
                        TABLE_BOOKS + "." + COLUMN_AUTHOR + ", " +
                        TABLE_BOOKS + "." + COLUMN_TITLE + ", " +
                        TABLE_LENT_OR_RESERVED_BOOKS + "." + COLUMN_STATUS +
            " FROM " + TABLE_BOOKS +
            " INNER JOIN " + TABLE_LENT_OR_RESERVED_BOOKS +
            " ON " + TABLE_BOOKS + "." + COLUMN_BOOK_ID + " = " + TABLE_LENT_OR_RESERVED_BOOKS + "."  + COLUMN_BOOK_ID +
            " WHERE " + TABLE_LENT_OR_RESERVED_BOOKS + "." + COLUMN_USER_ID + " = ?";

    public static final String ADD_LENT_OR_RESERVED_BOOK =
            "INSERT INTO " + TABLE_LENT_OR_RESERVED_BOOKS + " (" +
                    COLUMN_BOOK_ID + ", " +
                    COLUMN_USER_ID + ", " +
                    COLUMN_FIRST_NAME + ", " +
                    COLUMN_LAST_NAME + ", " +
                    COLUMN_STATUS + ", " +
                    COLUMN_QUANTITY + ") " +
            "VALUES (?, ?, ?, ?, ?, 1)";

    public static final String UPDATE_BOOK_QUANTITY_AFTER_LENDING =
            "UPDATE " + TABLE_BOOKS +
            " SET " + COLUMN_QUANTITY + " = " + COLUMN_QUANTITY + " - 1" +
            " WHERE " + COLUMN_BOOK_ID + " = ?";

    public static final String UPDATE_BOOK_QUANTITY_AFTER_RETURNING =
            "UPDATE " + TABLE_BOOKS +
            " SET " + COLUMN_QUANTITY + " = " + COLUMN_QUANTITY + " + 1" +
            " WHERE " + COLUMN_BOOK_ID + " = ?";

    public static final String UPDATE_BOOK_STATUS_BY_BOOK_ID =
            "UPDATE " + TABLE_LENT_OR_RESERVED_BOOKS +
            " SET " + COLUMN_STATUS + " = 'LENT'" +
            " WHERE " + COLUMN_BOOK_ID + " = ?";

    public static final String CHECK_BOOK_STATUS_BY_BOOK_ID_AND_CUSTOMER_ID =
            "SELECT " + COLUMN_STATUS + " FROM " + TABLE_LENT_OR_RESERVED_BOOKS +
            " WHERE " + COLUMN_BOOK_ID + " = ? AND " + COLUMN_USER_ID + " = ?";

    public static final String DELETE_LENT_OR_RESERVED_BOOK =
            "DELETE FROM " + TABLE_LENT_OR_RESERVED_BOOKS +
            " WHERE " + COLUMN_BOOK_ID + " = ? AND " + COLUMN_USER_ID + " = ?";

    public static final String QUERY_BOOK_INFO_BY_ID =
            "SELECT * FROM " + TABLE_BOOKS +
            " WHERE " + COLUMN_BOOK_ID + " = ?";

    public static final String ADD_NEW_BOOK =
            "INSERT INTO " + TABLE_BOOKS + " (" +
                    COLUMN_AUTHOR + ", " +
                    COLUMN_TITLE + ", " +
                    COLUMN_CATEGORY + ", " +
                    COLUMN_PUBLISHER + ", " +
                    COLUMN_PAGES + ", " +
                    COLUMN_AGE + ", " +
                    COLUMN_QUANTITY + ", " +
                    COLUMN_COVER + ") " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String QUERY_BOOK =
            "SELECT * FROM " + TABLE_BOOKS +
            " WHERE " + COLUMN_AUTHOR + " = ? AND  " +
                        COLUMN_TITLE + " = ? AND " +
                        COLUMN_PUBLISHER + " = ?";

    public static final String UPDATE_BOOK_INFORMATION =
            "UPDATE " + TABLE_BOOKS +
                    " SET " +
                    COLUMN_AUTHOR + " = ?, " +
                    COLUMN_TITLE + " = ?, " +
                    COLUMN_CATEGORY + " = ?, " +
                    COLUMN_PUBLISHER + " = ?, " +
                    COLUMN_PAGES + " = ?, " +
                    COLUMN_AGE + " = ?, " +
                    COLUMN_QUANTITY + " = ?, " +
                    COLUMN_COVER + " = ? " +
                    "WHERE " + COLUMN_BOOK_ID + " = ?";

    public static final String QUERY_BOOK_COVER_NAME_BY_ID =
            "SELECT " + COLUMN_COVER + " FROM " + TABLE_BOOKS +
            " WHERE " + COLUMN_BOOK_ID + " = ?";

    public static final String DELETE_BOOK =
            "DELETE FROM " + TABLE_BOOKS +
            " WHERE " + COLUMN_BOOK_ID  + " = ?";

    public static final String QUERY_CUSTOMERS_BY_LENT_BOOK_ID =
            "SELECT * FROM " + TABLE_LENT_OR_RESERVED_BOOKS +
            " WHERE " + COLUMN_BOOK_ID + " = ?";

    public static final String QUERY_AVAILABLE_BOOK_QUANTITY_BY_BOOK_ID =
            "SELECT " + COLUMN_QUANTITY  + " FROM " + TABLE_BOOKS +
            " WHERE " + COLUMN_BOOK_ID + " = ?";

    public static final String QUERY_RESERVED_BOOK_QUANTITY_BY_BOOK_ID =
            "SELECT " + COLUMN_QUANTITY  + " FROM " + TABLE_LENT_OR_RESERVED_BOOKS +
            " WHERE " + COLUMN_BOOK_ID + " = ? AND " +
                        COLUMN_STATUS + " = 'RESERVED'";

    public static final String QUERY_LENT_BOOK_QUANTITY_BY_BOOK_ID =
            "SELECT " + COLUMN_QUANTITY  + " FROM " + TABLE_LENT_OR_RESERVED_BOOKS +
            " WHERE " + COLUMN_BOOK_ID + " = ? AND " +
                        COLUMN_STATUS + " = 'LENT'";

    public static final String UPDATE_BOOK_STATUS_BY_USER_ID =
            "UPDATE " + TABLE_LENT_OR_RESERVED_BOOKS +
            " SET " + COLUMN_STATUS + " = 'LENT'" +
            " WHERE " + COLUMN_USER_ID + " = ?";



    private PreparedStatement checkIfUserExists;
    private PreparedStatement checkUserTypeByUsername;
    private PreparedStatement queryUserAccountById;
    private PreparedStatement queryUserAccountByUsername;
    private PreparedStatement addUserAccount;
    private PreparedStatement updateUserAccount;
    private PreparedStatement deleteUserAccount;
    private PreparedStatement queryEmployeesList;
    private PreparedStatement queryCustomersList;
    private PreparedStatement queryBooksList;
    private PreparedStatement queryBooksByCustomer;
    private PreparedStatement addLentOrReservedBook;
    private PreparedStatement updateBookQuantityAfterLending;
    private PreparedStatement updateBookQuantityAfterReturning;
    private PreparedStatement updateBookStatusByBookId;
    private PreparedStatement checkBookStatusByBookIdAndCustomerId;
    private PreparedStatement deleteLentOrReservedBook;
    private PreparedStatement queryBookInfoById;
    private PreparedStatement addNewBook;
    private PreparedStatement checkIfBookExists;
    private PreparedStatement updateBookInformation;
    private PreparedStatement queryBookCoverNameById;
    private PreparedStatement deleteBook;
    private PreparedStatement queryCustomersByLentBookId;
    private PreparedStatement queryAvailableBookQuantityByBookId;
    private PreparedStatement queryReservedBookQuantityByBookId;
    private PreparedStatement queryLentBookQuantityByBookId;
    private PreparedStatement updateBookStatusByUserId;


    final static DataSource instance = new DataSource();

    DataSource(){}

    public static DataSource getInstance(){
        return instance;
    }


    public boolean open(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_SOURCE);

            checkIfUserExists = conn.prepareStatement(QUERY_USERNAME);
            checkUserTypeByUsername = conn.prepareStatement(QUERY_USER_TYPE_BY_USERNAME);
            queryUserAccountById = conn.prepareStatement(QUERY_USER_ACCOUNT_BY_ID);
            queryUserAccountByUsername = conn.prepareStatement(QUERY_USER_ACCOUNT_BY_USERNAME);
            addUserAccount = conn.prepareStatement(ADD_USER_ACCOUNT);
            updateUserAccount = conn.prepareStatement(UPDATE_USER_ACCOUNT);
            deleteUserAccount = conn.prepareStatement(DELETE_USER_ACCOUNT);
            queryEmployeesList = conn.prepareStatement(QUERY_EMPLOYEES_LIST);
            queryCustomersList = conn.prepareStatement(QUERY_CUSTOMERS_LIST);
            queryBooksList = conn.prepareStatement(QUERY_BOOKS_LIST);
            queryBooksByCustomer = conn.prepareStatement(QUERY_BOOKS_BY_CUSTOMER);
            addLentOrReservedBook = conn.prepareStatement(ADD_LENT_OR_RESERVED_BOOK);
            updateBookQuantityAfterLending = conn.prepareStatement(UPDATE_BOOK_QUANTITY_AFTER_LENDING);
            updateBookQuantityAfterReturning = conn.prepareStatement(UPDATE_BOOK_QUANTITY_AFTER_RETURNING);
            updateBookStatusByBookId = conn.prepareStatement(UPDATE_BOOK_STATUS_BY_BOOK_ID);
            checkBookStatusByBookIdAndCustomerId = conn.prepareStatement(CHECK_BOOK_STATUS_BY_BOOK_ID_AND_CUSTOMER_ID);
            deleteLentOrReservedBook = conn.prepareStatement(DELETE_LENT_OR_RESERVED_BOOK);
            queryBookInfoById = conn.prepareStatement(QUERY_BOOK_INFO_BY_ID);
            addNewBook = conn.prepareStatement(ADD_NEW_BOOK);
            checkIfBookExists = conn.prepareStatement(QUERY_BOOK);
            updateBookInformation = conn.prepareStatement(UPDATE_BOOK_INFORMATION);
            queryBookCoverNameById = conn.prepareStatement(QUERY_BOOK_COVER_NAME_BY_ID);
            deleteBook = conn.prepareStatement(DELETE_BOOK);
            queryCustomersByLentBookId = conn.prepareStatement(QUERY_CUSTOMERS_BY_LENT_BOOK_ID);
            queryAvailableBookQuantityByBookId = conn.prepareStatement(QUERY_AVAILABLE_BOOK_QUANTITY_BY_BOOK_ID);
            queryReservedBookQuantityByBookId = conn.prepareStatement(QUERY_RESERVED_BOOK_QUANTITY_BY_BOOK_ID);
            queryLentBookQuantityByBookId = conn.prepareStatement(QUERY_LENT_BOOK_QUANTITY_BY_BOOK_ID);
            updateBookStatusByUserId = conn.prepareStatement(UPDATE_BOOK_STATUS_BY_USER_ID);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try{
            if (checkIfUserExists != null){
                checkIfUserExists.close();
            }

            if (checkUserTypeByUsername != null){
                checkUserTypeByUsername.close();
            }

            if (queryUserAccountById != null){
                queryUserAccountById.close();
            }

            if (queryUserAccountByUsername != null){
                queryUserAccountByUsername.close();
            }

            if (addUserAccount != null){
                addUserAccount.close();
            }

            if (updateUserAccount != null){
                updateUserAccount.close();
            }

            if (deleteUserAccount != null){
                deleteUserAccount.close();
            }

            if (queryEmployeesList != null){
                queryEmployeesList.close();
            }

            if (queryCustomersList != null){
                queryCustomersList.close();
            }

            if (queryBooksList != null){
                queryBooksList.close();
            }

            if (queryBooksByCustomer != null){
                queryBooksByCustomer.close();
            }

            if (addLentOrReservedBook != null){
                addLentOrReservedBook.close();
            }

            if (updateBookQuantityAfterLending != null){
                updateBookQuantityAfterLending.close();
            }

            if (updateBookQuantityAfterReturning != null){
                updateBookQuantityAfterReturning.close();
            }

            if (updateBookStatusByBookId != null){
                updateBookStatusByBookId.close();
            }

            if (checkBookStatusByBookIdAndCustomerId != null){
                checkBookStatusByBookIdAndCustomerId.close();
            }

            if (deleteLentOrReservedBook != null){
                deleteLentOrReservedBook.close();
            }

            if (queryBookInfoById != null){
                queryBookInfoById.close();
            }

            if (addNewBook != null){
                addNewBook.close();
            }

            if (checkIfBookExists != null){
                checkIfBookExists.close();
            }

            if (updateBookInformation != null){
                updateBookInformation.close();
            }

            if (queryBookCoverNameById != null){
                queryBookCoverNameById.close();
            }

            if (deleteBook != null){
                deleteBook.close();
            }

            if (queryCustomersByLentBookId != null){
                queryCustomersByLentBookId.close();
            }

            if (queryAvailableBookQuantityByBookId != null){
                queryAvailableBookQuantityByBookId.close();
            }

            if (queryReservedBookQuantityByBookId != null){
                queryReservedBookQuantityByBookId.close();
            }

            if (queryLentBookQuantityByBookId != null){
                queryLentBookQuantityByBookId.close();
            }

            if (updateBookStatusByUserId != null){
                updateBookStatusByUserId.close();
            }

            if (conn != null){
                conn.close();
            }
        } catch (SQLException e){
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public boolean checkIfUserExists(String username, String password){
        ResultSet resultSet;

        try {
            checkIfUserExists.setString(1, username);
            resultSet = checkIfUserExists.executeQuery();

            if (resultSet.next()){
                String retrievedPassword = resultSet.getString(COLUMN_PASSWORD);
                if (retrievedPassword.equals(password)){
                    return true;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkUserName(String username){
        ResultSet resultSet;
        try {
            checkIfUserExists.setString(1, username);
            resultSet = checkIfUserExists.executeQuery();

            if (resultSet.isBeforeFirst()){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public void addUserAccount(String username, String firstName, String lastName, String phone, String email,
                               String address, String city, String country, String password, String userType){

        try {
            addUserAccount.setString(1, username);
            addUserAccount.setString(2, firstName);
            addUserAccount.setString(3, lastName);

            String newPhoneFormat = phone.replaceFirst("^\\(?(\\d{3})\\)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$", "($1) $2-$3");
            addUserAccount.setString(4, newPhoneFormat);

            addUserAccount.setString(5, email);
            addUserAccount.setString(6, address);
            addUserAccount.setString(7, city);
            addUserAccount.setString(8, country);
            addUserAccount.setString(9, password);
            addUserAccount.setString(10, userType);
            addUserAccount.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserAccount(String userId, String username, String firstName, String lastName, String phone,
                                  String email, String address, String city, String country, String password){

        try {
            updateUserAccount.setString(1, username);
            updateUserAccount.setString(2, firstName);
            updateUserAccount.setString(3, lastName);

            String newPhoneFormat = phone.replaceFirst("^\\(?(\\d{3})\\)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$", "($1) $2-$3");
            updateUserAccount.setString(4, newPhoneFormat);

            updateUserAccount.setString(5, email);
            updateUserAccount.setString(6, address);
            updateUserAccount.setString(7, city);
            updateUserAccount.setString(8, country);
            updateUserAccount.setString(9, password);
            updateUserAccount.setString(10, userId);
            updateUserAccount.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserAccount(String userId){
        try {
            deleteUserAccount.setString(1, userId);
            deleteUserAccount.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User queryUserAccountById(String userId){

        User user = new User();

        try {
            queryUserAccountById.setString(1, userId);
            ResultSet resultSet = queryUserAccountById.executeQuery();

            while (resultSet.next()){
                user.setUserId(resultSet.getInt(COLUMN_USER_ID));
                user.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
                user.setLastName(resultSet.getString(COLUMN_LAST_NAME));
                user.setUsername(resultSet.getString(COLUMN_USERNAME));
                user.setPhone(resultSet.getString(COLUMN_PHONE));
                user.setEmail(resultSet.getString(COLUMN_EMAIL));
                user.setAddress(resultSet.getString(COLUMN_ADDRESS));
                user.setCity(resultSet.getString(COLUMN_CITY));
                user.setCountry(resultSet.getString(COLUMN_COUNTRY));
                user.setPassword(resultSet.getString(COLUMN_PASSWORD));
                user.setUserType(resultSet.getString(COLUMN_USER_TYPE));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    public User queryUserAccountByUsername(String username){

        User loggedUser = new User();

        try {
            queryUserAccountByUsername.setString(1, username);
            ResultSet resultSet = queryUserAccountByUsername.executeQuery();

            while (resultSet.next()){
                loggedUser.setUserId(resultSet.getInt(COLUMN_USER_ID));
                loggedUser.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
                loggedUser.setLastName(resultSet.getString(COLUMN_LAST_NAME));
                loggedUser.setUsername(resultSet.getString(COLUMN_USERNAME));
                loggedUser.setPhone(resultSet.getString(COLUMN_PHONE));
                loggedUser.setEmail(resultSet.getString(COLUMN_EMAIL));
                loggedUser.setAddress(resultSet.getString(COLUMN_ADDRESS));
                loggedUser.setCity(resultSet.getString(COLUMN_CITY));
                loggedUser.setCountry(resultSet.getString(COLUMN_COUNTRY));
                loggedUser.setPassword(resultSet.getString(COLUMN_PASSWORD));
                loggedUser.setUserType(resultSet.getString(COLUMN_USER_TYPE));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return loggedUser;
    }

    public List<User> queryEmployeesList(){
        try (ResultSet resultSet = queryEmployeesList.executeQuery()){

            List<User> users = new ArrayList<>();

            while (resultSet.next()) {

                User user = new User();

                user.setUserId(resultSet.getInt(COLUMN_USER_ID));
                user.setUsername(resultSet.getString(COLUMN_USERNAME));
                user.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
                user.setLastName(resultSet.getString(COLUMN_LAST_NAME));
                user.setPhone(resultSet.getString(COLUMN_PHONE));
                user.setEmail(resultSet.getString(COLUMN_EMAIL));
                user.setAddress(resultSet.getString(COLUMN_ADDRESS));
                user.setCity(resultSet.getString(COLUMN_CITY));
                user.setCountry(resultSet.getString(COLUMN_COUNTRY));
                user.setPassword(resultSet.getString(COLUMN_PASSWORD));

                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<User> queryCustomersList(){
        try (ResultSet resultSet = queryCustomersList.executeQuery()){

            List<User> users = new ArrayList<>();

            while (resultSet.next()) {

                User user = new User();

                user.setUserId(resultSet.getInt(COLUMN_USER_ID));
                user.setUsername(resultSet.getString(COLUMN_USERNAME));
                user.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
                user.setLastName(resultSet.getString(COLUMN_LAST_NAME));
                user.setPhone(resultSet.getString(COLUMN_PHONE));
                user.setEmail(resultSet.getString(COLUMN_EMAIL));
                user.setAddress(resultSet.getString(COLUMN_ADDRESS));
                user.setCity(resultSet.getString(COLUMN_CITY));
                user.setCountry(resultSet.getString(COLUMN_COUNTRY));
                user.setPassword(resultSet.getString(COLUMN_PASSWORD));

                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Book> queryBooksList(){
        try (ResultSet resultSet = queryBooksList.executeQuery()){

            List<Book> books = new ArrayList<>();

            while (resultSet.next()) {

                Book book = new Book();

                FileInputStream input = new FileInputStream(COVERS_SOURCE + resultSet.getString(COLUMN_COVER));
                Image image = new Image(input);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);

                book.setBookId(resultSet.getInt(COLUMN_BOOK_ID));
                book.setCover(imageView);
                book.setAuthor(resultSet.getString(COLUMN_AUTHOR));
                book.setTitle(resultSet.getString(COLUMN_TITLE));
                book.setCategory(resultSet.getString(COLUMN_CATEGORY));
                book.setPublisher(resultSet.getString(COLUMN_PUBLISHER));
                book.setPages(resultSet.getInt(COLUMN_PAGES));
                book.setAge(resultSet.getInt(COLUMN_AGE));
                book.setQuantity(resultSet.getInt(COLUMN_QUANTITY));

                books.add(book);
            }
            return books;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> queryLentBooksByCustomerId(int customerId){
        ResultSet resultSet;

        List<Book> lentBooks = new ArrayList<>();

        try {
            queryBooksByCustomer.setInt(1, customerId);
            resultSet = queryBooksByCustomer.executeQuery();

            while (resultSet.next()){
                Book book = new Book();

                FileInputStream input = new FileInputStream(COVERS_SOURCE + resultSet.getString(COLUMN_COVER));
                Image image = new Image(input);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);

                book.setBookId(resultSet.getInt(COLUMN_BOOK_ID));
                book.setCover(imageView);
                book.setAuthor(resultSet.getString(COLUMN_AUTHOR));
                book.setTitle(resultSet.getString(COLUMN_TITLE));
                book.setStatus(resultSet.getString(COLUMN_STATUS));

                lentBooks.add(book);
            }

        } catch (SQLException e){
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return lentBooks;
    }

    public void addLentOrReservedBook(int selectedBookId, int customerId, String firstName, String lastName, String typeAction){
        try {
            addLentOrReservedBook.setInt(1, selectedBookId);
            addLentOrReservedBook.setInt(2, customerId);
            addLentOrReservedBook.setString(3, firstName);
            addLentOrReservedBook.setString(4, lastName);
            addLentOrReservedBook.setString(5, typeAction);
            addLentOrReservedBook.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            updateBookQuantityAfterLending.setInt(1, selectedBookId);
            updateBookQuantityAfterLending.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (typeAction.equals("LENT")){
            LendBooksController.getInstance().refreshBooksTable();
        }

        if (typeAction.equals("RESERVED")){
            BooksReservationController.getInstance().refreshBooksTable();
        }

        CustomerDatasheetController.getInstance().refreshBooksTable();
    }

    public String checkBookStatusByBookIdAndCustomerId(int selectedBookId, int customerId){
        String bookStatus = "";

        try {
            checkBookStatusByBookIdAndCustomerId.setInt(1, selectedBookId);
            checkBookStatusByBookIdAndCustomerId.setInt(2, customerId);
            ResultSet resultSet = checkBookStatusByBookIdAndCustomerId.executeQuery();

            if (resultSet.isBeforeFirst()){
                bookStatus = resultSet.getString(COLUMN_STATUS);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bookStatus;
    }

    public void updateBookStatusByBookId(int bookId){
        try {
            updateBookStatusByBookId.setInt(1, bookId);
            updateBookStatusByBookId.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        CustomerDatasheetController.getInstance().refreshBooksTable();
    }

    public void deleteLentOrReservedBook(int bookId, int customerId){
        try {
            deleteLentOrReservedBook.setInt(1, bookId);
            deleteLentOrReservedBook.setInt(2, customerId);
            deleteLentOrReservedBook.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            updateBookQuantityAfterReturning.setInt(1, bookId);
            updateBookQuantityAfterReturning.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Book queryBookInfoById(int bookId){

        Book book = new Book();

        try {
            queryBookInfoById.setInt(1, bookId);
            ResultSet resultSet = queryBookInfoById.executeQuery();

            FileInputStream input = new FileInputStream(COVERS_SOURCE + resultSet.getString(COLUMN_COVER));
            Image image = new Image(input);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);

            while (resultSet.next()){
                book.setBookId(resultSet.getInt(COLUMN_BOOK_ID));
                book.setCover(imageView);
                book.setAuthor(resultSet.getString(COLUMN_AUTHOR));
                book.setTitle(resultSet.getString(COLUMN_TITLE));
                book.setCategory(resultSet.getString(COLUMN_CATEGORY));
                book.setPublisher(resultSet.getString(COLUMN_PUBLISHER));
                book.setPages(resultSet.getInt(COLUMN_PAGES));
                book.setAge(resultSet.getInt(COLUMN_AGE));
                book.setQuantity(resultSet.getInt(COLUMN_QUANTITY));
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return book;
    }

    public void addNewBook(String author, String title, String category, String publisher, int pages,
                           int age, int quantity, String cover){

        try {
            addNewBook.setString(1, author);
            addNewBook.setString(2, title);
            addNewBook.setString(3, category);
            addNewBook.setString(4, publisher);
            addNewBook.setInt(5, pages);
            addNewBook.setInt(6, age);
            addNewBook.setInt(7, quantity);
            addNewBook.setString(8, cover);
            addNewBook.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfBookExists(String author, String title, String publisher){
        ResultSet resultSet;

        try {
            checkIfBookExists.setString(1, author);
            checkIfBookExists.setString(2, title);
            checkIfBookExists.setString(3, publisher);
            resultSet = checkIfBookExists.executeQuery();

            if (resultSet.next()){
                String retrievedAuthor = resultSet.getString(COLUMN_AUTHOR);
                String retrievedTitle = resultSet.getString(COLUMN_TITLE);
                String retrievedPublisher = resultSet.getString(COLUMN_PUBLISHER);
                if (retrievedAuthor.equals(author)&& retrievedTitle.equals(title) && retrievedPublisher.equals(publisher)){
                    return true;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public void updateBookInformation(String bookId, String author, String title, String category, String publisher,
                                  int pages, int age, int quantity, String cover){

        try {
            updateBookInformation.setString(1, author);
            updateBookInformation.setString(2, title);
            updateBookInformation.setString(3, category);
            updateBookInformation.setString(4, publisher);
            updateBookInformation.setInt(5, pages);
            updateBookInformation.setInt(6, age);
            updateBookInformation.setInt(7, quantity);
            updateBookInformation.setString(8, cover);
            updateBookInformation.setString(9, bookId);
            updateBookInformation.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String queryBookCoverNameById(int bookId){
        ResultSet resultSet;
        String coverName = "";

        try {
            queryBookCoverNameById.setInt(1, bookId);
            resultSet = queryBookCoverNameById.executeQuery();
            coverName = resultSet.getString(COLUMN_COVER);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return coverName;
    }

    public void deleteBook(String bookId){
        try {
            deleteBook.setString(1, bookId);
            deleteBook.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> queryCustomersByLentBookId(int bookId){
        ResultSet resultSet;

        List<User> customers = new ArrayList<>();

        try {
            queryCustomersByLentBookId.setInt(1, bookId);
            resultSet = queryCustomersByLentBookId.executeQuery();

            while (resultSet.next()){
                User customer = new User();

                customer.setUserId(resultSet.getInt(COLUMN_USER_ID));
                customer.setFirstName(resultSet.getString(COLUMN_FIRST_NAME));
                customer.setLastName(resultSet.getString(COLUMN_LAST_NAME));
                customer.setStatus(resultSet.getString(COLUMN_STATUS));
                customer.setQuantity(resultSet.getInt(COLUMN_QUANTITY));

                customers.add(customer);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return customers;
    }

    public int queryAvailableBookQuantityByBookId(int bookId){
        ResultSet resultSet;

        try {
            queryAvailableBookQuantityByBookId.setInt(1, bookId);
            resultSet = queryAvailableBookQuantityByBookId.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt(COLUMN_QUANTITY);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return 0;
    }

    public int queryReservedBookQuantityByBookId(int bookId){
        ResultSet resultSet;
        int reservedQuantity = 0;

        try {
            queryReservedBookQuantityByBookId.setInt(1, bookId);
            resultSet = queryReservedBookQuantityByBookId.executeQuery();
            while (resultSet.next()){
                reservedQuantity += resultSet.getInt(COLUMN_QUANTITY);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return reservedQuantity;
    }

    public int queryLentBookQuantityByBookId(int bookId){
        ResultSet resultSet;
        int lentQuantity = 0;

        try {
            queryLentBookQuantityByBookId.setInt(1, bookId);
            resultSet = queryLentBookQuantityByBookId.executeQuery();
            while (resultSet.next()){
                lentQuantity += resultSet.getInt(COLUMN_QUANTITY);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return lentQuantity;
    }

    public void updateBookStatusByUserId(int userId){
        try {
            updateBookStatusByUserId.setInt(1, userId);
            updateBookStatusByUserId.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        BookDatasheetController.getInstance().refreshBooksTable();
    }

}
