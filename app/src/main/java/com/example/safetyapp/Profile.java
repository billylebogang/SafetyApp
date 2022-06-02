package com.example.safetyapp;

public class Profile {

    String name,surname, number, id,kinName,kinNumber, address,gender, age;

    public Profile(){
        this.name = "name";
        this.surname = "surname";
        this.number = "number";
        this.age="age";
        this.id = "id";
        this.kinName = "kinName";
        this.kinNumber = "kinNumber";
        this.address = "address";
        this.gender = "gender";
    }

    public Profile(String name,String surname, String age, String number, String id, String kinName, String kinNumber, String address, String gender) {
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.age=age;
        this.id = id;
        this.kinName = kinName;
        this.kinNumber = kinNumber;
        this.address = address;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.name = age;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKinName() {
        return kinName;
    }

    public void setKinName(String kinName) {
        this.kinName = kinName;
    }

    public String getKinNumber() {
        return kinNumber;
    }

    public void setKinNumber(String kinNumber) {
        this.kinNumber = kinNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
