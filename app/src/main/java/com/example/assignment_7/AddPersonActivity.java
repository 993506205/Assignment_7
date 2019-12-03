package com.example.assignment_7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;

public class AddPersonActivity extends AppCompatActivity{
    public static final String EXTRA_FNAME = "com.example.assignment_7.EXTRA_FNAME";
    public static final String EXTRA_LNAME = "com.example.assignment_7.EXTRA_LNAME";
    public static final String EXTRA_PHONE = "com.example.assignment_7.EXTRA_PHONE";
    public static final String EXTRA_EDLVL = "com.example.assignment_7.EXTRA_EDLVL";
    public static final String EXTRA_HOBBIES = "com.example.assignment_7.EXTRA_HOBBIES";
    public static final String EXTRA_LOCATION = "com.exmple.assignment_7.EXTRA_LOCATION";
    private String ed_lvl;
    private String hobbies;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        Button btn_saveI = (Button) findViewById(R.id.btn_saveI);
        Button btn_saveE = (Button) findViewById(R.id.btn_saveE);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);

        Spinner ed_spinner = (Spinner) findViewById(R.id.spinner_education);
        Spinner hb_spinner = (Spinner) findViewById(R.id.spinner_hobbies);

        ArrayAdapter<CharSequence> ed_adapter = ArrayAdapter.createFromResource(this, R.array.education_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> hb_adapter = ArrayAdapter.createFromResource(this, R.array.hobbies_array, android.R.layout.simple_spinner_item);

        ed_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hb_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ed_spinner.setAdapter(ed_adapter);
        hb_spinner.setAdapter(hb_adapter);

        ed_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ed_lvl = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        hb_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hobbies = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_saveI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity(true);
            }
        });

        btn_saveE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity(false);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMainActivity();
            }
        });
    }

    public void openMainActivity(boolean isInternal){
        EditText ed_fname = (EditText) findViewById(R.id.ed_fname);
        EditText ed_lname = (EditText) findViewById(R.id.ed_lname);
        EditText ed_phone = (EditText) findViewById(R.id.ed_phone);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_FNAME, ed_fname.getText().toString());
        intent.putExtra(EXTRA_LNAME, ed_lname.getText().toString());
        intent.putExtra(EXTRA_PHONE, ed_phone.getText().toString());
        intent.putExtra(EXTRA_EDLVL, ed_lvl);
        intent.putExtra(EXTRA_HOBBIES, hobbies);
        intent.putExtra(EXTRA_LOCATION, isInternal);
        startActivity(intent);
    }

    public void backMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
