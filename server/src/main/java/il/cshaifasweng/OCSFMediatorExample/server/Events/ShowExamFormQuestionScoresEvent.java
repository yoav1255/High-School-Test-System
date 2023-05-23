package il.cshaifasweng.OCSFMediatorExample.server.Events;

import il.cshaifasweng.OCSFMediatorExample.entities.QuestionScore;

import java.util.List;

public class ShowExamFormQuestionScoresEvent {
    List<QuestionScore> questionScores;

    public ShowExamFormQuestionScoresEvent(List<QuestionScore> questionScores) {
        this.questionScores = questionScores;
    }

    public List<QuestionScore> getQuestionScores() {
        return questionScores;
    }
}
