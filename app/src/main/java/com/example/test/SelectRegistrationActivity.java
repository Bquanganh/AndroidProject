package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SelectRegistrationActivity extends AppCompatActivity {

    private Button donorButton, recipientButton;
    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registration);




        donorButton = findViewById(R.id.donorButton);
        donorButton.setOnClickListener(view -> {
            Intent intent = new Intent (SelectRegistrationActivity.this, Donor.class);
            startActivity(intent);
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent (SelectRegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        recipientButton = findViewById(R.id.recipientButton);
        recipientButton.setOnClickListener(view -> {
            Intent intent = new Intent (SelectRegistrationActivity.this, RecipientRegistrationActivity.class);
            startActivity(intent);
            finish();
        });


    }
}