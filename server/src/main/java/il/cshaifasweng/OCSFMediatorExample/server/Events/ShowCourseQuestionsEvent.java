package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Question;

import java.util.List;

public class ShowCourseQuestionsEvent {
    List<Question> questions;

    public ShowCourseQuestionsEvent(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
