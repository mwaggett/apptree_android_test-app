package com.apptreesoftware.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        View view;
        Person person = getItem(position);
        if ( convertView == null ) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.person_cell,parent, false);
        } else {
            view = convertView;
        }
        TextView nameField = (TextView) view.findViewById(R.id.nameLabel);
        TextView phoneField = (TextView) view.findViewById(R.id.phoneLabel);
        TextView emailField = (TextView) view.findViewById(R.id.emailLabel);
        TextView addressField = (TextView) view.findViewById(R.id.addressLabel);

        nameField.setText(person.firstName + " " + person.lastName);
        phoneField.setText(person.phoneNumber);
        emailField.setText(person.email);
        addressField.setText(person.address);

        return view;
    }
}
