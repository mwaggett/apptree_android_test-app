package com.apptreesoftware.testapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class PersonInfoActivity extends AppCompatActivity {

    private static final String TAG = PersonInfoActivity.class.getSimpleName();

    int personId;
    ModelController instance;
    ArrayList<Person> people;
    Person person;
    TextView heading;
    EditText firstNameText;
    EditText lastNameText;
    EditText phoneText;
    EditText emailText;
    EditText addressText;
    ImageView imageInput;
    Button addPhotoButton;
    Button saveButton;
    Button cancelButton;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        personId = getIntent().getIntExtra("person_id", 0);

        heading = (TextView) findViewById(R.id.personFormHeading);
        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        emailText = (EditText) findViewById(R.id.emailText);
        addressText = (EditText) findViewById(R.id.addressText);
        imageInput = (ImageView) findViewById(R.id.imageInput);
        addPhotoButton = (Button) findViewById(R.id.addPhotoButton);

        instance = ModelController.getInstance(this, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                errorAlert();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                people = instance.getPeople();
                setupForm(personId);
            }
        });
        people = instance.getPeople();
        setupForm(personId);

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
                String firstName = firstNameText.getText().toString();
                String lastName = lastNameText.getText().toString();
                String phoneNumber = phoneText.getText().toString();
                String email = emailText.getText().toString();
                String address = addressText.getText().toString();
                imageInput.buildDrawingCache();
                Bitmap image = imageInput.getDrawingCache();

                person.setFirstName(firstName);
                person.setLastName(lastName);
                person.setEmail(email);
                person.setPhoneNumber(phoneNumber);
                person.setAddress(address);
                person.setPhoto(image);
                if (personId == 0) {
                    people.add(person);
                }
                Intent intent = new Intent(PersonInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupForm(int id) {
        if (id > 0) {
            person = instance.personWithID(personId);
            heading.setText("Edit");
            firstNameText.setText(person.firstName);
            lastNameText.setText(person.lastName);
            phoneText.setText(person.phoneNumber);
            emailText.setText(person.email);
            addressText.setText(person.address);
            if (person.photo != null) {
                imageInput.setImageBitmap(person.photo);
            }
        } else {
            person = new Person();
            person.setId(people.size() + 1);
        }
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
        getMenuInflater().inflate(R.menu.menu_person_info, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void errorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Oops!")
                .setMessage("There was an error. Please try again.")
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
