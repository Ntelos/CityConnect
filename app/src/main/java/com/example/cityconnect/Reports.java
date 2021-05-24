package com.example.cityconnect;

import com.google.type.Date;
import com.google.type.LatLng;

public class Reports {
    private String title;
    private String description;
    private String name;
    private String surname;
    private String id;
    private String email;
    private String status;
    private String address;
    private String postalcode;
    private String latLng;
    private String date;
    private String city;

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
