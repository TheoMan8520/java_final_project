package app.planner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class SetUpController {

    @FXML
    private String user;
    @FXML
    private String password;
    @FXML
    private String database;

    @FXML
    private TextField inputUser;
    @FXML
    private TextField inputPassword;
    @FXML
    private TextField inputDatabase;
    @FXML
    private boolean finish = false;


    public void getMainPage() {
        try {
            if(finish) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();
                HelloController controller = loader.getController();
                controller.setDb(database, user, password);

                Stage currentStage = (Stage) inputUser.getScene().getWindow();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.setTitle("Planner");
                newStage.setResizable(false);
                newStage.show();

                currentStage.close();
            }
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                        "Please finish set up database.");
                errorAlert.showAndWait();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void createTable() {
        try {
            String query = """
                    CREATE TABLE record (
                       id SERIAL PRIMARY KEY,
                       type VARCHAR(50),
                       todo VARCHAR(100),
                       note VARCHAR(50),
                       deadline VARCHAR(50)
                    );
                    """;
            executeQuery(query);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                    "Create table successfully.");
            successAlert.setTitle("Operation succeed");
            successAlert.setHeaderText("Proceed successfully");
            successAlert.showAndWait();
        }
        catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "An error occurred while trying to create table.");
            errorAlert.showAndWait();
        }
    }
    public void checkAll() {
        if (user == null || password == null || database == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Please connect to your database");
            errorAlert.showAndWait();
        }
        else {
            Connection conn = getConnection();
            if (conn != null) {
                ObservableList<Record> records = getRecords();
                if (records != null) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                            "Your database is ready to use.");
                    successAlert.setTitle("Operation succeed");
                    successAlert.setHeaderText("Proceed successfully");
                    successAlert.showAndWait();
                    finish = true;
                }
                else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                            "Record table doesn't exist. Please create the record table.");
                    errorAlert.showAndWait();
                }
            }
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                        "Your database does not exist or either your username or password is wrong.");
                errorAlert.showAndWait();
            }
        }
    }

    public void saveDbInfo() {
        if (!(inputUser.getText().isEmpty() || inputDatabase.getText().isEmpty() || inputPassword.getText().isEmpty())) {
            user = inputUser.getText();
            password = inputPassword.getText();
            database = inputDatabase.getText();

            Connection conn = getConnection();
            if (conn != null) {inputUser.setText("");
                inputPassword.setText("");
                inputDatabase.setText("");
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                        """
                                  Your database has been set successfully.
                                  Please check for record table.
                                 """);
                successAlert.setTitle("Operation succeed");
                successAlert.setHeaderText("Proceed successfully");
                successAlert.showAndWait();
            }
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                        "Your database does not exist or either your username or password is wrong.");
                errorAlert.showAndWait();
            }
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Please fill all.");
            errorAlert.showAndWait();
        }
    }

    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:postgresql:"+database, user, password);
            return conn;
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    public ObservableList<Record> getRecords(){
        ObservableList<Record> records = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM record";
        Statement st;
        ResultSet rs;

        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Record record;
            while(rs.next()){
                record = new Record(rs.getInt("id"), rs.getString("type"), rs.getString("todo"), rs.getString("note"), rs.getString("deadline"));
                records.add(record);
            }
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
        return records;
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }

    @FXML
    private void getGuidePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("guide.fxml"));
            Parent root = loader.load();
            GuideController controller = loader.getController();
            controller.showGuide();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Guide");
            newStage.setResizable(false);
            newStage.show();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}