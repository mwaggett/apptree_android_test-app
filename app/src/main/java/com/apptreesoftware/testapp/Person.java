package com.apptreesoftware.testapp;

import android.graphics.Bitmap;

import java.util.Comparator;

/**
 * Created by matthew on 10/12/15.
 */
public class Person {
    int id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String address;
    Bitmap photo;

    public Person() {}

    public Person(int id, String firstName, String lastName, String email, String phoneNumber, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

}
