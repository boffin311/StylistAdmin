package com.example.stylistadmin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stylistadmin.model.DiscountOb;
import com.example.stylistadmin.model.Location;
import com.example.stylistadmin.R;
import com.example.stylistadmin.model.RatingOb;
import com.example.stylistadmin.model.StylistDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText etName, etAge, etDescription, etUser, etRating, etEmail, etSex, etExperience, etTagLine, etCity, etDiscount, etZaloninDiscount;
    Button btnRegister;
    EditText etProfile;
    ImageView imageCurrentLocation;
    EditText tvLocation;
    TextView tvLatitude, tvLongitude;
    private static final int REQUEST_LOCATION = 9879;
    Double lat = 0.0;
    Double lng = 0.0;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etAge = findViewById(R.id.tvage);
        etDescription = findViewById(R.id.tvDescription);
        etEmail = findViewById(R.id.etNodeName);
        etProfile = findViewById(R.id.etProfile);
        etCity = findViewById(R.id.etcity);
        tvLongitude = findViewById(R.id.etLongitude);
        tvLocation = findViewById(R.id.tvLocation);
        tvLatitude = findViewById(R.id.etLattitude);
        etDiscount = findViewById(R.id.etDiscount);
        etZaloninDiscount = findViewById(R.id.etZaloninDiscount);
        etExperience = findViewById(R.id.tvExperience);
        etName = findViewById(R.id.tvName);
        imageCurrentLocation=findViewById(R.id.imageCurrentLocation);
        etRating = findViewById(R.id.etRating);
        etSex = findViewById(R.id.tvSex);
        etTagLine = findViewById(R.id.etTagLine);
        etUser = findViewById(R.id.etNoOfUser);
        btnRegister = findViewById(R.id.btnNext);
        imageCurrentLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivityForResult(intent,REQUEST_LOCATION);
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("STYLIST");
        btnRegister.setOnClickListener(v -> {
            databaseReference = databaseReference.child(creatId(etEmail.getText().toString()));
            StylistDetails stylistDetails = new StylistDetails(Integer.valueOf(etExperience.getText().toString()), etName.getText().toString(), etTagLine.getText().toString(), etDescription.getText().toString(), etSex.getText().toString(), Integer.valueOf(etAge.getText().toString()), etEmail.getText().toString(), 0, 0, "27/07/2019", etProfile.getText().toString());
            databaseReference.child("STYLISTDETAILS").setValue(stylistDetails);
            RatingOb ratingOb = new RatingOb(Integer.valueOf(etRating.getText().toString()), Integer.valueOf(etUser.getText().toString()));
            databaseReference.child("RATING").setValue(ratingOb);
            Location location = new Location(tvLocation.getText().toString(), etCity.getText().toString(), Double.valueOf(tvLatitude.getText().toString()), Double.valueOf(tvLongitude.getText().toString()));
            databaseReference.child("LOCATION").setValue(location);
            DiscountOb discountOb = new DiscountOb(Integer.valueOf(etDiscount.getText().toString()), Integer.valueOf(etZaloninDiscount.getText().toString()));
            databaseReference.child("DISCOUNT").setValue(discountOb);
            databaseReference.child("stylistId").setValue(getZaloninId());
            databaseReference.child("bookingno").setValue(0);
            databaseReference.child("WALLET").child("points").setValue(0);
            etRating.setText(null);
            etAge.setText(null);
            etDescription.setText(null);

            etCity.setText(null);
            tvLongitude.setText(null);
            tvLatitude.setText(null);
            etDiscount.setText(null);
            etZaloninDiscount.setText(null);
            etExperience.setText(null);
            etName.setText(null);
            etRating.setText(null);
            etProfile.setText(null);
            etTagLine.setText(null);
            etSex.setText(null);
            etUser.setText(null);
            tvLocation.setText(null);
            Intent intent = new Intent(MainActivity.this, FemaleServiceActivity.class);
            intent.putExtra("email", creatId(etEmail.getText().toString()));
            etEmail.setText(null);
            startActivity(intent);
        });
    }

    String getZaloninId() {
        Random random = new Random();
        return "ZAS" + (random.nextInt(90000) + 10000);
    }

    String creatId(String email) {
        String createdId = "";
        email = email.toUpperCase();
        for (int i = 0; i < email.length(); ++i) {
            if (email.charAt(i) == '@')
                break;
            createdId += email.charAt(i);
        }
        return createdId;
    }


    @Override
    protected void onActivityResult(int requestcode, int resultCode, Intent data) {
        super.onActivityResult(requestcode, resultCode, data);
        if (requestcode == REQUEST_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                tvLocation.setText(data.getStringExtra("long_address"));
                lat = data.getDoubleExtra("lat", 28.6915);
                lng = data.getDoubleExtra("lng", 77.2024);
                tvLatitude.setText(String.valueOf(lat));
                tvLongitude.setText(String.valueOf(lng));
            }
        }
    }
}
