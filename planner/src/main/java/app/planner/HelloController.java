package app.planner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class HelloController {

    @FXML
    public Record currentRecord;
    @FXML
    public TableView<Record> recordsTable;
    @FXML
    private TableColumn<Record, String> colType;
    @FXML
    private TableColumn<Record, String> colTodo;
    @FXML
    private TableColumn<Record, String> colNote;
    @FXML
    private TableColumn<Record, String> colDeadline;

    @FXML
    public TextField newType;
    @FXML
    public TextField newTodo;
    @FXML
    public TextField newNote;
    @FXML
    public TextField newDeadline;

    @FXML
    private String user;
    @FXML
    private String password;
    @FXML
    private String database;


    public void setDb(String db, String user, String pw) {
        this.database = db;
        this.user = user;
        this.password = pw;
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

    public void reload() {
        if (user == null || password == null || database == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Please connect to your database");
            errorAlert.showAndWait();
        }
        else {
            Connection conn = getConnection();
            if(conn != null) {
                try {
                    showRecords();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                            "Your database is ready to use.");
                    successAlert.setTitle("Operation succeed");
                    successAlert.setHeaderText("Proceed successfully");
                    successAlert.showAndWait();
                }
                catch (Exception e) {
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
    public void getSetUpPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("setup.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) recordsTable.getScene().getWindow();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Set Up Page");
            newStage.setResizable(false);
            newStage.show();

            currentStage.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
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

    public void showRecords(){
        ObservableList<Record> records = getRecords();

        colType.setCellValueFactory(new PropertyValueFactory<Record, String>("type"));
        colTodo.setCellValueFactory(new PropertyValueFactory<Record, String>("todo"));
        colNote.setCellValueFactory(new PropertyValueFactory<Record, String>("note"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<Record, String>("deadline"));

        recordsTable.setItems(records);
    }

    public void saveRecord(){
        if (!(newType.getText().isEmpty() || newTodo.getText().isEmpty() || newNote.getText().isEmpty() || newDeadline.getText().isEmpty())) {
            String query = "INSERT INTO record (type, todo, note, deadline) VALUES ('" + newType.getText() + "','" + newTodo.getText() + "','" + newNote.getText() + "','"
                    + newDeadline.getText() + "')";
            executeQuery(query);
            resetTextField();
            showRecords();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                    "The record has been saved.");
            successAlert.setTitle("Operation succeed");
            successAlert.setHeaderText("Proceed successfully");
            successAlert.showAndWait();
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Please fill all.");
            errorAlert.showAndWait();
        }
    }

    public void updateRecord(){
        if (currentRecord != null) {
            if (!(newType.getText().isEmpty() || newTodo.getText().isEmpty() || newNote.getText().isEmpty() || newDeadline.getText().isEmpty())) {
                String query = "UPDATE record SET type  = '" + newType.getText() + "', todo = '" + newTodo.getText() + "', note = '" +
                        newNote.getText() + "', deadline = '" + newDeadline.getText() + "' WHERE id = " + currentRecord.getId();
                executeQuery(query);
                resetTextField();
                showRecords();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                        "The record has been updated.");
                successAlert.setTitle("Operation succeed");
                successAlert.setHeaderText("Proceed successfully");
                successAlert.showAndWait();
            }
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                        "Please fill all.");
                errorAlert.showAndWait();
            }
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Please select record to update.");
            errorAlert.showAndWait();
        }
    }

    public void deleteRecord(){
        if (currentRecord != null) {
            if (!(newType.getText().isEmpty() || newTodo.getText().isEmpty() || newNote.getText().isEmpty() || newDeadline.getText().isEmpty())) {
                String query = "DELETE FROM record WHERE id =" + currentRecord.getId();
                executeQuery(query);
                resetTextField();
                showRecords();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION,
                        "The record has been deleted.");
                successAlert.setTitle("Operation succeed");
                successAlert.setHeaderText("Proceed successfully");
                successAlert.showAndWait();
            }
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                        "Please fill all.");
                errorAlert.showAndWait();
            }
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                    "Please select record to update.");
            errorAlert.showAndWait();
        }
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
    private void selectRecord(MouseEvent event) {
        currentRecord = recordsTable.getSelectionModel().getSelectedItem();

        newType.setText(currentRecord.getType());
        newTodo.setText(currentRecord.getTodo());
        newNote.setText(currentRecord.getNote());
        newDeadline.setText(currentRecord.getDeadline());
    }

    @FXML
    private void resetTextField(){
        newType.setText("");
        newTodo.setText("");
        newNote.setText("");
        newDeadline.setText("");
    }

    @FXML
    private void saveImage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("image.fxml"));
            Parent root = loader.load();
            ImageController controller = loader.getController();
            controller.showRecords(getRecords());

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("This is your planner!");
            newStage.setResizable(false);
            newStage.show();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
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