package com.example.cityconnect;

import com.google.type.Date;
import com.google.type.LatLng;

public class Reports {
    public String title;
    public String description;
    public String name;
    public String surname;
    public String id;
    public String email;
    public String status;
    public String address;
    public String postalcode;
    public String latLng;
    public String date;
    public String city;

    public Reports(String title, String description, String name, String surname,
                   String id, String email, String status, String address, String postalcode,
                   String latLng, String date, String city) {
        this.title = title;
        this.description = description;
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.email = email;
        this.status = status;
        this.address = address;
        this.postalcode = postalcode;
        this.latLng = latLng;
        this.date = date;
        this.city = city;
    }
}
