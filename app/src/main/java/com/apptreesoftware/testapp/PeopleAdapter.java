package com.apptreesoftware.testapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by matthew on 10/12/15.
 */
public class PeopleAdapter extends ArrayAdapter<Person> {
    public PeopleAdapter(Context context, int resource, List<Person> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        final Person person = getItem(position);
        if ( convertView == null ) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.person_cell,parent, false);
        } else {
            view = convertView;
        }
        TextView nameField = (TextView) view.findViewById(R.id.nameLabel);
        TextView phoneField = (TextView) view.findViewById(R.id.phoneLabel);
        TextView emailField = (TextView) view.findViewById(R.id.emailLabel);
        TextView addressField = (TextView) view.findViewById(R.id.addressLabel);
        Button editButton = (Button) view.findViewById(R.id.editButton);

        nameField.setText(person.firstName + " " + person.lastName);
        phoneField.setText(person.phoneNumber);
        emailField.setText(person.email);
        addressField.setText(person.address);

        final CardView personCell = (CardView) view.findViewById(R.id.personCell);
        final RelativeLayout newPersonForm = (RelativeLayout) view.findViewById(R.id.newPersonForm);
        final TextView heading = (TextView) view.findViewById(R.id.addPersonLabel);
        final EditText firstNameText = (EditText) view.findViewById(R.id.firstNameText);
        final EditText lastNameText = (EditText) view.findViewById(R.id.lastNameText);
        final EditText phoneText = (EditText) view.findViewById(R.id.phoneText);
        final EditText emailText = (EditText) view.findViewById(R.id.emailText);
        final EditText addressText = (EditText) view.findViewById(R.id.addressText);
        final Button saveButton = (Button) view.findViewById(R.id.saveButton);
        final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personCell.setVisibility(View.GONE);
                heading.setText("Edit");
                newPersonForm.setVisibility(View.VISIBLE);
                firstNameText.setText(person.firstName);
                lastNameText.setText(person.lastName);
                phoneText.setText(person.phoneNumber);
                emailText.setText(person.email);
                addressText.setText(person.address);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String firstName = firstNameText.getText().toString();
                        String lastName = lastNameText.getText().toString();
                        String phoneNumber = phoneText.getText().toString();
                        String email = emailText.getText().toString();
                        String address = addressText.getText().toString();

                        person.setFirstName(firstName);
                        person.setLastName(lastName);
                        person.setPhoneNumber(phoneNumber);
                        person.setEmail(email);
                        person.setAddress(address);
                        notifyDataSetChanged();

                        newPersonForm.setVisibility(View.GONE);
                        personCell.setVisibility(View.VISIBLE);
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPersonForm.setVisibility(View.GONE);
                        personCell.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        return view;
    }
}
