package models;

import java.io.Serializable;

public class User extends DomainEntity implements Serializable {

    private String name;
    protected String surname;
    protected String photo;
    private Integer age;
    private String phone;
    private String nif;

    protected UserAccount userAccount;

    /*
    * Constructor clase User
    */
    public User() {
        super();
    }

    /*
    * Getters y Setters
    */


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", photo='" + photo + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", userAccount=" + userAccount +
                '}';
    }
}
