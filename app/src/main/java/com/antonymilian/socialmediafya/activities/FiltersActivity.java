package com.antonymilian.socialmediafya.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.antonymilian.socialmediafya.R;

public class FiltersActivity extends AppCompatActivity {

    String mExtraCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        mExtraCategory = getIntent().getStringExtra("category");
        Toast.makeText(this, "La categoria que seleccionó es " + mExtraCategory, Toast.LENGTH_SHORT).show();
    }
}