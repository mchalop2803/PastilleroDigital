package models;

import org.checkerframework.common.aliasing.qual.Unique;

import java.io.Serializable;

public class UserAccount extends DomainEntity implements Serializable {
    @Unique
    private String email;
    private String password;

    /**
     * Constructor de la clase UserAccount
     */
    public UserAccount(){
        super();
    }

    //Getters y Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
