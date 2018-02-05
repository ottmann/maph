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

/**
 * Created by elisabeth on 27.12.17.
 */

public class QuestionsActivity extends AppCompatActivity {

    private static final String LOG_TAG = QuestionsActivity.class.getSimpleName();

    SmileRating smileRating1, smileRating2, smileRating3, smileRating4, smileRating5;
    int level1, level2, level3, level4, level5;

    Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

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
    }
}

