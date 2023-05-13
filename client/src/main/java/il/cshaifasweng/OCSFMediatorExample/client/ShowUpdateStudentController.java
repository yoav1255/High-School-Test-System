package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.server.StudentTest;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ShowUpdateStudentController {
    private StudentTest studentTest;
    @FXML
    TextField oldGrade;
    @FXML
    TextField newGrade;

    public ShowUpdateStudentController(StudentTest studentTest){
        EventBus.getDefault().register(this);
        this.studentTest=studentTest;
    }

    public StudentTest getStudentTest() {
        return studentTest;
    }

    public void setStudentTest(StudentTest studentTest) {
        this.studentTest = studentTest;
    }

    @Subscribe
    @FXML
    public void onShowUpdateStudentEvent(ShowUpdateStudentEvent event){
        try{
            System.out.println("in show Update Student controller");
            setStudentTest(event.getStudentTest());
            oldGrade.setText(Integer.toString(studentTest.getGrade()));
            //TODO finish the function
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Subscribe
    @FXML
    public void handleUpdateButton(){
        try {
            System.out.println("in Update Student controller(update)");
            int newG = Integer.parseInt(newGrade.getText());
            App.switchScreen("showOneStudent");
            EventBus.getDefault().post(new UpdateStudentGradeEvent(newG));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
