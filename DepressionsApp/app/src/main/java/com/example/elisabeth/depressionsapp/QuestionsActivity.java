package com.example.elisabeth.depressionsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuestionsActivity extends AppCompatActivity {

    private static final String LOG_TAG = QuestionsActivity.class.getSimpleName();

    private List<String> answersQuestions = new ArrayList<String>();
    //List<String> dropdownAnswerList1, dropdownAnswerList2, dropdownAnswerList3, dropdownAnswerList4, dropdownAnswerList5;
    //private int spinnerItem, spinnerItem2, spinnerItem3, spinnerItem4, spinnerItem5;

    SmileRating smileRating1, smileRating2, smileRating3, smileRating4, smileRating5;
    int level1, level2, level3, level4, level5;

    Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        answersQuestions.add("Stimme voll zu");
        answersQuestions.add("Stimme zu");
        answersQuestions.add("Stimme eher zu");
        answersQuestions.add("Stimme eher nicht zu");
        answersQuestions.add("Stimme nicht zu");
        answersQuestions.add("Stimme gar nicht zu");

        //TODO: use to save data to DB
        /*findViewById(R.id.buttonSubmitForm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if an spinner options have been selected
                if ((!(spinnerItem == 0)) && (!(spinnerItem2 == 0)) && (!(spinnerItem3 == 0)) && (!(spinnerItem4 == 0)) && (!(spinnerItem5 == 0))) {
                    //Get data and save to DB
                    //if (dropdownAnswerList1 != null && dropdownAnswerList2 != null) {
                    //
                    //}

                    //add mood to the general database
                    //DatabaseManager.addUserSighting(newMood);

                    //return to the main screen
                    startActivity(new Intent(QuestionsActivity.this, MainActivity.class));
                } else {
                    //show toast prompting user to input an animal and a description
                    Toast.makeText(getApplication().getBaseContext(), getString(R.string.moodActivity_please_select_from_spinners),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        //Get all the smiley rating bars from the layout
        smileRating1 = (SmileRating) findViewById(R.id.smile_rating_1);
        smileRating2 = (SmileRating) findViewById(R.id.smile_rating_2);
        smileRating3 = (SmileRating) findViewById(R.id.smile_rating_3);
        smileRating4 = (SmileRating) findViewById(R.id.smile_rating_4);
        smileRating5 = (SmileRating) findViewById(R.id.smile_rating_5);



        findViewById(R.id.buttonSubmitForm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get smiley rating input values (1-5, 0 if nothing selected)
                level1 = smileRating1.getRating();
                level2 = smileRating2.getRating();
                level3 = smileRating3.getRating();
                level4 = smileRating4.getRating();
                level5 = smileRating5.getRating();

                if (level1 != 0 && level2 != 0 && level3 != 0 && level4 != 0 && level5 != 0) {
                    startActivity(new Intent(QuestionsActivity.this, MainActivity.class));
                    Toast.makeText(getApplication().getBaseContext(), getString(R.string.moodActivity_toastSuccess),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication().getBaseContext(), getString(R.string.moodActivity_toastNoInput),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //TODO: use for internationalization
        locale = getResources().getConfiguration().locale;

        /*//TODO: in funktion auslagern
        //Dropdown spinner, containing all the answers
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        dropdownAnswerList1 = new ArrayList<String>();
        dropdownAnswerList1.add(getString(R.string.moodActivity_please_select));
        for (String s : answersQuestions) {
            //if (locale.equals(Locale.ENGLISH)) {
            //dropdownAnswerList1.add(s);
            //} else {
            dropdownAnswerList1.add(s);
            //}
        }

        // Creates an adapter for the spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                dropdownAnswerList1);

        // Sets the dropdown layout style and attaches the data adapter to the spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        //Dropdown spinner, containing all the answers
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);
        dropdownAnswerList2 = new ArrayList<String>();
        dropdownAnswerList2.add(getString(R.string.moodActivity_please_select));
        for (String s : answersQuestions) {
            dropdownAnswerList2.add(s);
        }

        // Creates an adapter for the spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                dropdownAnswerList2);

        // Sets the dropdown layout style and attaches the data adapter to the spinner
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);

        //Dropdown spinner, containing all the answers
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner.setOnItemSelectedListener(this);
        dropdownAnswerList3 = new ArrayList<String>();
        dropdownAnswerList3.add(getString(R.string.moodActivity_please_select));
        for (String s : answersQuestions) {
            //if (locale.equals(Locale.ENGLISH)) {
            //dropdownAnswerList1.add(s);
            //} else {
            dropdownAnswerList3.add(s);
            //}
        }

        // Creates an adapter for the spinner
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                dropdownAnswerList3);

        // Sets the dropdown layout style and attaches the data adapter to the spinner
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(dataAdapter3);

        //Dropdown spinner, containing all the answers
        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner.setOnItemSelectedListener(this);
        dropdownAnswerList4 = new ArrayList<String>();
        dropdownAnswerList4.add(getString(R.string.moodActivity_please_select));
        for (String s : answersQuestions) {
            //if (locale.equals(Locale.ENGLISH)) {
            //dropdownAnswerList1.add(s);
            //} else {
            dropdownAnswerList4.add(s);
            //}
        }

        // Creates an adapter for the spinner
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                dropdownAnswerList4);

        // Sets the dropdown layout style and attaches the data adapter to the spinner
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(dataAdapter4);

        //Dropdown spinner, containing all the answers
        Spinner spinner5 = (Spinner) findViewById(R.id.spinner5);
        spinner.setOnItemSelectedListener(this);
        dropdownAnswerList5 = new ArrayList<String>();
        dropdownAnswerList5.add(getString(R.string.moodActivity_please_select));
        for (String s : answersQuestions) {
            //if (locale.equals(Locale.ENGLISH)) {
            //dropdownAnswerList1.add(s);
            //} else {
            dropdownAnswerList5.add(s);
            //}
        }

        // Creates an adapter for the spinner
        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                dropdownAnswerList5);

        // Sets the dropdown layout style and attaches the data adapter to the spinner
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(dataAdapter5);*/

        }

        /*@Override
        public void onItemSelected (AdapterView < ? > adapterView, View view,int i, long l){

            adapterView.getItemAtPosition(i);
            switch (adapterView.getId()) {
                case R.id.spinner:
                    Log.d(LOG_TAG, "Spinner Selected: " + i);
                    spinnerItem = i;
                    break;

                case R.id.spinner2:
                    Log.d(LOG_TAG, "Spinner 2 Selected: " + i);
                    spinnerItem2 = i;
                    break;
            }
        }

        @Override
        public void onNothingSelected (AdapterView < ? > adapterView){
            //Needed to implement interface, but doesn't need functionality in our case
        }

        public void fillSpinner(ArrayList dropdownAnswerList) {
            //Dropdown spinner, containing all the answers
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            spinner.setOnItemSelectedListener(this);
            dropdownAnswerList = new ArrayList<String>();
            dropdownAnswerList.add(getString(R.string.moodActivity_please_select));
            for (String s : answersQuestions) {
                //if (locale.equals(Locale.ENGLISH)) {
                //dropdownAnswerList1.add(s);
                //} else {
                dropdownAnswerList.add(s);
                //}
            }
        }*/
}

