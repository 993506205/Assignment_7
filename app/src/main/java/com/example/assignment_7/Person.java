package com.example.assignment_7;

public class Person {
    private String first_name;
    private String second_name;
    private String phone;
    private String education;
    private String hobbies;

    public Person(String first_name, String second_name, String phone, String education, String hobbies) {
        this.first_name = first_name;
        this.second_name = second_name;
        this.phone = phone;
        this.education = education;
        this.hobbies = hobbies;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
}
