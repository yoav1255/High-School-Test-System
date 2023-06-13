package il.cshaifasweng.OCSFMediatorExample.server.Events;

import java.util.List;

public class GetUniqueExamCode {
    private String examFormCode;

    public GetUniqueExamCode(String examFormCode) {
        this.examFormCode = examFormCode;
    }

    public String getUniqueExamCode() {
        return examFormCode;
    }
}
