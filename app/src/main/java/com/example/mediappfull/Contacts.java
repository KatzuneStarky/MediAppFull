package com.example.mediappfull;

public class Contacts {

    String number;
    String name;

    public Contacts(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public Contacts() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
