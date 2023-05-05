package il.cshaifasweng.OCSFMediatorExample.client;

import javax.persistence.*;

@MappedSuperclass
@Table(name = "Person")

public class Person {
    @Id
    private String id;
    private String first_name;
    private String last_name;
    private String gender;

    // TODO: add email and password

}
