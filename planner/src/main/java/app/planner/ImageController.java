package app.planner;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ImageController {
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

    public void showRecords(ObservableList<Record> records){
        colType.setCellValueFactory(new PropertyValueFactory<Record, String>("type"));
        colTodo.setCellValueFactory(new PropertyValueFactory<Record, String>("todo"));
        colNote.setCellValueFactory(new PropertyValueFactory<Record, String>("note"));
        colDeadline.setCellValueFactory(new PropertyValueFactory<Record, String>("deadline"));

        recordsTable.setItems(records);
    }
}
