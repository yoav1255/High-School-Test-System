package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.Question_Score;

import java.util.List;

public class ShowExamFormQuestionScoresEvent {
    List<Question_Score> questionScores;

    public ShowExamFormQuestionScoresEvent(List<Question_Score> questionScores) {
        this.questionScores = questionScores;
    }

    public List<Question_Score> getQuestionScores() {
        return questionScores;
    }
}
