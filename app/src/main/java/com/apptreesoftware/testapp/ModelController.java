package com.apptreesoftware.testapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by matthew on 10/12/15.
 */
public class ModelController {

    private static final String TAG = ModelController.class.getSimpleName();
    private static Context mContext;

    private static ModelController instance;

    private ArrayList<Person> people = new ArrayList<>();

    public static ModelController getInstance(Context context, Callback callback) {
        mContext = context;
        if ( instance == null ) {
            instance = new ModelController();
            instance.fetchPeople(callback);
//            instance.createFakeData();
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

    private void fetchPeople(Callback callback) {
        String peopleUrl = "https://www.dropbox.com/s/0qe641r5ouao65h/people.json?dl=1";

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(peopleUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callback);
        } else {
            errorAlert();
        }
    }

//    private void fetchPeople(final MainActivity context) {
//        String peopleUrl = "https://www.dropbox.com/s/0qe641r5ouao65h/people.json?dl=1";
//
//        if (isNetworkAvailable()) {
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .url(peopleUrl)
//                    .build();
//            Call call = client.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    Log.v(TAG, e.getMessage());
//                    errorAlert();
//                }
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    try {
//                        String jsonData = response.body().string();
//                        if (response.isSuccessful()) {
//                            JSONObject object = new JSONObject(jsonData);
//                            JSONArray peopleJSON = object.getJSONArray("people");
//                            for (int i = 0; i < peopleJSON.length(); i++) {
//                                JSONObject personJSON = peopleJSON.getJSONObject(i);
//                                Person person = new Person();
//                                person.setId(i+1);
//                                person.setFirstName(personJSON.getString("firstName"));
//                                person.setLastName(personJSON.getString("lastName"));
//                                person.setEmail(personJSON.getString("email"));
//                                person.setAddress(personJSON.getString("address"));
//                                Bitmap photo = null;
//                                URL photoURL = new URL(personJSON.getString("photo"));
//                                if (photoURL != null) {
//                                    photo = BitmapFactory.decodeStream(photoURL.openConnection().getInputStream());
//                                }
//                                person.setPhoto(photo);
//                                people.add(person);
//                                Log.v(TAG, people.get(i).getFullName());
//                            }
//                        } else {
//                            errorAlert();
//                        }
//                        context.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                context.adapter.notifyDataSetChanged();
//                            }
//                        });
//                    } catch (IOException e) {
//                        Log.e(TAG, "Exception caught: ", e);
//                    } catch (JSONException e) {
//                        Log.e(TAG, "Exception caught: ", e);
//                    }
//                }
//            });
//        } else {
//            errorAlert();
//        }
//    }

    private void errorAlert() {
        Log.e(TAG, "ERROR!");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
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
        String strippedNumber = phoneNumber.replaceAll("[^0-9]", "");
        String areaCode = strippedNumber.substring(0,3);
        String middleThree = strippedNumber.substring(3,6);
        String lastFour = strippedNumber.substring(6);
        return "("+areaCode+") "+middleThree+" - "+lastFour;
    }
}

class PersonNameComparator implements Comparator<Person> {

    @Override
    public int compare(Person personA, Person personB) {
        return personA.lastName.compareTo(personB.lastName);
    }
}
