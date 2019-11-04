package com.example.stylistadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stylistadmin.R;
import com.example.stylistadmin.model.SubCatInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MaleServiceActivity extends AppCompatActivity {
    EditText etServieName, etDuration, etServiceDescription, etPrice, etSubCategories, etCategories;
    TextView tvType;
    Button btnAddNow, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_service);
        tvType = findViewById(R.id.tvType);
        etServieName = findViewById(R.id.etServiceName);
        etServiceDescription = findViewById(R.id.etServiceDescription);
        etCategories = findViewById(R.id.etCategories);
        etPrice = findViewById(R.id.etPrice);
        etSubCategories = findViewById(R.id.etSubCategories);
        etDuration = findViewById(R.id.etDuration);
        btnAddNow = findViewById(R.id.btnAddNow);
        btnNext = findViewById(R.id.btnMoveNext);
        tvType.setText("Male");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("STYLIST").child(getIntent().getStringExtra("email")).child("SERVICES").child("MALE");
        btnAddNow.setOnClickListener(v -> {
            SubCatInfo subCatInfo = new SubCatInfo(etServiceDescription.getText().toString(), etServieName.getText().toString(), Integer.valueOf(etPrice.getText().toString()), Integer.valueOf(etDuration.getText().toString()));
            databaseReference.child(etCategories.getText().toString()).child(etSubCategories.getText().toString()).setValue(subCatInfo);
            etDuration.setText(null);
            etPrice.setText(null);
            etSubCategories.setText(null);
            etServiceDescription.setText(null);
            etServieName.setText(null);
        });
        btnNext.setOnClickListener(v -> {
            Intent intent=new Intent(MaleServiceActivity.this, PhotoUploadActivitys.class);
            intent.putExtra("name",getIntent().getStringExtra("email"));
            startActivity(intent);
        });
    }
}
