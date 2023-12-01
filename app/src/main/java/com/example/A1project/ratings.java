package com.example.A1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ratings extends AppCompatActivity {
    SQLiteDatabase db;
    public static final String BREATH_RATE = "BREATH_RATE";
    public static final String HEART_RATE = "HEART_RATE";
    public static final String NAUSEA = "NAUSEA";
    public static final String HEAD_ACHE = "HEAD_ACHE";
    public static final String DIARRHEA = "DIARRHEA";
    public static final String SOAR_THROAT = "SOAR_THROAT";
    public static final String FEVER = "FEVER";
    public static final String MUSCLE_ACHE = "MUSCLE_ACHE";
    public static final String LOSS_OF_SMELL_TASTE = "LOSS_OF_SMELL_TASTE";
    public static final String COUGH = "COUGH";
    public static final String SHORT_BREATH = "SHORT_BREATH";
    public static final String FEEL_TIRED = "FEEL_TIRED";

    private Map<String, Float> createMap() {
        Map<String,Float> symRatings = new HashMap<String,Float>();
        symRatings.put("Nausea", (float) 0.0);
        symRatings.put("Headache", (float) 0.0);
        symRatings.put("Diarrhea",(float) 0.0);
        symRatings.put("Soar Throat",(float) 0.0);
        symRatings.put("Fever",(float) 0.0);
        symRatings.put("Muscle Ache", (float) 0.0);
        symRatings.put("Loss of smell or taste", (float) 0.0);
        symRatings.put("Cough", (float) 0.0);
        symRatings.put("Shortness of Breath", (float) 0.0);
        symRatings.put("Feeling Tired", (float) 0.0);
        return symRatings;
    }
    Map<String, Float> symRatings = createMap();

    private List<String> symptomsList = new ArrayList<>(symRatings.keySet());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_symptoms);

        Intent intent = getIntent();

            db = openOrCreateDatabase("myDB.db", Context.MODE_PRIVATE, null);

                setContentView(R.layout.rate_symptoms);
                Spinner dropdown = findViewById(R.id.sym_list);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ratings.this,
                        android.R.layout.simple_spinner_item, symptomsList
                );
                dropdown.setAdapter(adapter);

                Spinner spnLocale = (Spinner)findViewById(R.id.sym_list);
                RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);

                rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        symRatings.put(spnLocale.getSelectedItem().toString(), rating);
                    }
                });
                Button saveSymptomsButton = (Button) findViewById(R.id.saveSymptoms);
                saveSymptomsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("test", "success");
                        ContentValues content = new ContentValues();
                        content.put(HEART_RATE, Float.valueOf(intent.getStringExtra("h")));
                        content.put(BREATH_RATE, Float.valueOf(intent.getStringExtra("r")));
                        content.put(NAUSEA, symRatings.get("Nausea"));
                        content.put(HEAD_ACHE, symRatings.get("Headache"));
                        content.put(DIARRHEA, symRatings.get("Diarrhea"));
                        content.put(SOAR_THROAT, symRatings.get("Soar Throat"));
                        content.put(FEVER, symRatings.get("Fever"));
                        content.put(MUSCLE_ACHE, symRatings.get("Muscle Ache"));
                        content.put(LOSS_OF_SMELL_TASTE, symRatings.get("Loss of smell or taste"));
                        content.put(COUGH, symRatings.get("Cough"));
                        content.put(SHORT_BREATH, symRatings.get("Shortness of Breath"));
                        content.put(FEEL_TIRED, symRatings.get("Feeling Tired"));
                        try {
                            db.update("ram",content,BREATH_RATE + "=?" ,new String[]{String.valueOf(Float.valueOf(intent.getStringExtra("r")))});
                        }
                        catch (SQLiteException e) {
                        }
                        finally {
                        }
                    }
                });
                spnLocale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) findViewById(R.id.symName)).setText(symptomsList.get(i));
                        rb.setRating(symRatings.get(symptomsList.get(i)));
                    }
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });
            }
}