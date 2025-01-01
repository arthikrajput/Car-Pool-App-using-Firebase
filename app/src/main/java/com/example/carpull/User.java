package com.example.carpull;

public class User {

    private String name;
    private String mobile;
    private String birthdate;
    private String gender;
    private String designation;
    private String email; // Make sure you have email here if needed

    // No-argument constructor (required for Firebase)
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    // Constructor with parameters
    public User(String email,String name, String mobile, String birthdate, String gender, String designation) {
        this.email = email;
        this.name = name;
        this.mobile = mobile;
        this.birthdate = birthdate;
        this.gender = gender;
        this.designation = designation;
    }

    // Getters and Setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
