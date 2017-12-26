package com.example.elisabeth.depressionsapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final RadioGroup langGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton langDe = (RadioButton) findViewById(R.id.radioButtonDe);
        RadioButton langEn = (RadioButton) findViewById(R.id.radioButtonEn);

        //setLocale("en");

        /*langGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId){

                Log.d("selected language", "id" + checkedId);

                if (checkedId == R.id.radioButtonDe) {
                    Log.d("Checked: ", "DE");
                    setLocale("de");
                    langGroup.check(R.id.radioButtonDe);
                } else if (checkedId == R.id.radioButtonEn) {
                    Log.d("Checked: ", "EN");
                    setLocale("en");
                    langGroup.check(R.id.radioButtonEn);
                }
            }
        });*/

        Locale lang = getLocale();
        Log.d("LOCALE: ", lang.toString());

        langGroup.clearCheck();
        if (lang.equals("de_DE")) {
            langGroup.check(R.id.radioButtonDe);
        } else if (lang.equals("en_EN")) {
            langGroup.check(R.id.radioButtonEn);
        }
    }


    public Locale getLocale() {
        return getResources().getConfiguration().locale;

    }
    /**
     * Changes the app's Locale
     */
    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, SettingsActivity.class);
        startActivity(refresh);
    }
}
