package com.example.webservicetest;

public class SinhVien {
    private int id;
    private String name;
    private int date;
    private String address;

    public SinhVien(int id, String name, int date, String address) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDateNum() {
        return date;
    }

    public String getDateString(){
        return String.valueOf(date);
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
