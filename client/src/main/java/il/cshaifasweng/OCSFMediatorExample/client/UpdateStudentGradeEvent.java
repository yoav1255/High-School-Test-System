package il.cshaifasweng.OCSFMediatorExample.client;

public class UpdateStudentGradeEvent {
    private int newGrade;

    public UpdateStudentGradeEvent(int newGrade) {
        this.newGrade = newGrade;
    }

    public int getNewGrade() {
        return newGrade;
    }

    public void setNewGrade(int newGrade) {
        this.newGrade = newGrade;
    }
}
