package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ShowOneStudentController {

    @FXML
    private TableView<?> GradesTable;

    @FXML
    private TableColumn<?, ?> TableCourse;

    @FXML
    private TableColumn<?, ?> TableGrade;

    @FXML
    private TableColumn<?, ?> TableSubject;

    @FXML
    private TableColumn<?, ?> TableTeacher;

    @FXML
    private TableColumn<?, ?> TableTestID;

}
