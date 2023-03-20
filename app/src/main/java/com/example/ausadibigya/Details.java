package com.example.ausadibigya;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import java.lang.Exception;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Details extends AppCompatActivity {

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("ausadi.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    @Override
        protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        String ausadistr = intent.getStringExtra("ausadi");
        TextView generic_name = findViewById(R.id.genericName);
        TextView ausadii = findViewById(R.id.medicine);
        TextView treatment = findViewById(R.id.Treatment);
        TextView sidEeffects = findViewById(R.id.sideEffects);
        TextView warning = findViewById(R.id.Warnings);
        //TextView direction = findViewById(R.id.DirectionOfUse);
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(this));
            JSONObject nam = obj.getJSONObject(ausadistr);
            ausadii.setText(ausadistr);
            generic_name.setText(nam.getString("GenericName"));
            treatment.setText(nam.getString("Treatment"));
            sidEeffects.setText(nam.getString("SideEffects"));
            warning.setText(nam.getString("Warnings"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
