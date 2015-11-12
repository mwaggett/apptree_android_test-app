package com.apptreesoftware.testapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by matthew on 10/12/15.
 */
public class ModelController {
    private static ModelController instance;

    private ArrayList<Person> people = new ArrayList<>();

    public static ModelController getInstance() {
        if ( instance == null ) {
            instance = new ModelController();
            instance.createFakeData();
        }
        return instance;
    }

    public Person personWithID(int personID) {
        for (Person person : people) {
            if ( person.id == personID ) {
                return person;
            }
        }
        return null;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    private void createFakeData() {
        Person person = new Person();
        person.id = 1;
        person.firstName = "Matthew";
        person.lastName = "Smith";
        person.address = "1234 Main St. Portland, Or 97209";
        person.email = "matthew.smith@apptreesoftware.com";
        person.phoneNumber = "123456-7894";
        people.add(person);

        person = new Person();
        person.id = 2;
        person.firstName = "Alexis";
        person.lastName = "Andreason";
        person.address = "4444 Hoyt St. Portland, Or 97209";
        person.email = "alexis.andreason@apptreesoftware.com";
        person.phoneNumber = "123-456.7894";
        people.add(person);

        person = new Person();
        person.id = 3;
        person.firstName = "Robert";
        person.lastName = "Guinn";
        person.address = "444 Naito Blvd. Portland, Or 97209";
        person.email = "robert.guinn@apptreesoftware.com";
        person.phoneNumber = "123.456.7849";
        people.add(person);
    }

    //Sort by last name
    //Look at Collections.sort
    public void sortByName() {
        Collections.sort(people, new PersonNameComparator());
    }



    //Sort by address
    public void sortByAddress() {
        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person personA, Person personB) {
                return personA.address.compareTo(personB.address);
            }
        });
    }

    //Format all phone numbers to use the format (324) 233 - 2333
    //Remove all non-numbers
    //Insert phone formatting
    public void formatPhoneNumbers() {
        for ( Person person : people ) {
            person.phoneNumber = formatPhoneNumber(person.phoneNumber);
        }
    }

    private String formatPhoneNumber(String phoneNumber) {

        return "";
    }
}

class PersonNameComparator implements Comparator<Person> {

    @Override
    public int compare(Person personA, Person personB) {
        return personA.lastName.compareTo(personB.lastName);
    }
}
