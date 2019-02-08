package webSource;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity // This tells Hibernate to make a table out of this class

public class User {


    private String password;

//    private String token;

    @Id
    private String email;

    private String token;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;

    }
}

