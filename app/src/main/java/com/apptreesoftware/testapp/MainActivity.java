package com.apptreesoftware.testapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;

    ListView listView;
    PeopleAdapter adapter;
    ArrayList<Person> people;
    FloatingActionButton fab;
    RelativeLayout newPersonForm;
    EditText firstNameText;
    EditText lastNameText;
    EditText phoneText;
    EditText emailText;
    EditText addressText;
    ImageView imageInput;
    Button addPhotoButton;
    Button saveButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listView);
        people = ModelController.getInstance().getPeople();
        setupPeopleAdapter();

        newPersonForm = (RelativeLayout) findViewById(R.id.newPersonForm);
        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        emailText = (EditText) findViewById(R.id.emailText);
        addressText = (EditText) findViewById(R.id.addressText);
        imageInput = (ImageView) findViewById(R.id.imageInput);
        addPhotoButton = (Button) findViewById(R.id.addPhotoButton);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = people.size()+1;
                String firstName = firstNameText.getText().toString();
                String lastName = lastNameText.getText().toString();
                String phoneNumber = phoneText.getText().toString();
                String email = emailText.getText().toString();
                String address = addressText.getText().toString();
                imageInput.buildDrawingCache();
                Bitmap image = imageInput.getDrawingCache();

                Person newPerson = new Person();
                newPerson.setId(id);
                newPerson.setFirstName(firstName);
                newPerson.setLastName(lastName);
                newPerson.setEmail(email);
                newPerson.setPhoneNumber(phoneNumber);
                newPerson.setAddress(address);
                newPerson.setPhoto(image);
                people.add(newPerson);
                adapter.notifyDataSetChanged();
                toggleVisibility();
            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageInput.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            ModelController.getInstance().sortByAddress();
            setupPeopleAdapter();
        } else if (id == R.id.name_sort) {
            ModelController.getInstance().sortByName();
            setupPeopleAdapter();
        } else if (id == R.id.format_phone) {
            ModelController.getInstance().formatPhoneNumbers();
            setupPeopleAdapter();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupPeopleAdapter() {
        adapter = new PeopleAdapter(this,0, people);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Eventually add AlertDialog to confirm delete.
                people.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void toggleVisibility() {
        if (listView.getVisibility() == View.VISIBLE) {
            listView.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.INVISIBLE);
            newPersonForm.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            firstNameText.setText("");
            lastNameText.setText("");
            phoneText.setText("");
            emailText.setText("");
            addressText.setText("");
            newPersonForm.setVisibility(View.GONE);
        }
    }
}
