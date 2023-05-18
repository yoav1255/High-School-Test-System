package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class CustomMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
    private Object data;

    public CustomMessage(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
