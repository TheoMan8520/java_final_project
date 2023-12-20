package app.planner;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GuideController {
    @FXML
    public Label title;
    @FXML
    public Label guide;

    @FXML
    private Integer order = 1;

    public void next() {
        if(order < 8) {
            order += 1;
            showGuide();
        }
        else {
            order = 1;
            showGuide();
        }
    }
    public void back() {
        if(order > 1) {
            order -= 1;
            showGuide();
        }
        else {
            order = 8;
            showGuide();
        }
    }

    public void showGuide() {
        switch (order) {
            case 1:
                guide.setText("1. Ensure that you have postgresql.");
                break;
            case 2:
                guide.setText("2. Create database that you want to use or use the exist one.");
                break;
            case 3:
                guide.setText("3. Go to file option and setup the database with this app.");
                break;
            case 4:
                guide.setText("4. Fill database name, username and password.");
                break;
            case 5:
                guide.setText("5. Create the table with this app.");
                break;
            case 6:
                guide.setText("6. Check the table");
                break;
            case 7:
                guide.setText("7. Get back to the main page via file option.");
                break;
            case 8:
                guide.setText("8. Reload and use the app as you wish.");
                break;
            default:
                guide.setText("Guide");
                break;
        }
    }
}
