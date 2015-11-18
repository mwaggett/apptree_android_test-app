package com.apptreesoftware.testapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        ImageView imageField = (ImageView) view.findViewById(R.id.personImage);
        Button editButton = (Button) view.findViewById(R.id.editButton);

        nameField.setText(person.firstName + " " + person.lastName);
        phoneField.setText(person.phoneNumber);
        emailField.setText(person.email);
        addressField.setText(person.address);
        if (person.photo != null) {
            imageField.setImageBitmap(person.photo);
        } else {
            imageField.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.sym_def_app_icon));
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PersonInfoActivity.class);
                intent.putExtra("person_id", person.id);
                getContext().startActivity(intent);
            }
        });

        return view;
    }
}
