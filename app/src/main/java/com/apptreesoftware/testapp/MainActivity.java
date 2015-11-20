package com.apptreesoftware.testapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ModelController instance;
    ListView listView;
    PeopleAdapter adapter;
    ArrayList<Person> people;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);
        instance = ModelController.getInstance(this, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                errorAlert();
                Log.e(TAG, e.getMessage());
            }
            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        JSONObject object = new JSONObject(jsonData);
                        JSONArray peopleJSON = object.getJSONArray("people");
                        for (int i = 0; i < peopleJSON.length(); i++) {
                            JSONObject personJSON = peopleJSON.getJSONObject(i);
                            Person person = new Person();
                            person.setId(i+1);
                            person.setFirstName(personJSON.getString("firstName"));
                            person.setLastName(personJSON.getString("lastName"));
                            person.setEmail(personJSON.getString("email"));
                            person.setAddress(personJSON.getString("address"));
                            Bitmap photo = null;
                            URL photoURL = new URL(personJSON.getString("photo"));
                            if (photoURL != null) {
                                photo = BitmapFactory.decodeStream(photoURL.openConnection().getInputStream());
                            }
                            person.setPhoto(photo);
                            people.add(person);
                        }
                    } else {
                        errorAlert();
                        Log.e(TAG, "Response unsuccessful.");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    errorAlert();
                    Log.e(TAG, e.getMessage());
                } catch (JSONException e) {
                    errorAlert();
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        setupPeopleAdapter();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.address_sort) {
            instance.sortByAddress();
            setupPeopleAdapter();
        } else if (id == R.id.name_sort) {
            instance.sortByName();
            setupPeopleAdapter();
        } else if (id == R.id.format_phone) {
            instance.formatPhoneNumbers();
            setupPeopleAdapter();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setupPeopleAdapter() {
        people = instance.getPeople();
        adapter = new PeopleAdapter(this,0, people);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure you want to delete this contact?")
                        .setMessage("This cannot be undone.")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                people.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                builder.create();
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    private void errorAlert() {
        ErrorDialogFragment dialog = new ErrorDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
