package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;

@Entity
@Table(name="principal")
public class Principal extends User {

    public Principal(String id, String first_name, String last_name, String gender, String email, String password) {
        super(id, first_name, last_name, gender, email, password);
    }
    public Principal(){}
}
