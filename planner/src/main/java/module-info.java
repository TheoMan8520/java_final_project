module app.planner {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens app.planner to javafx.fxml;
    exports app.planner;
}