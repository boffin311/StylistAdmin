package com.example.stylistadmin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stylistadmin.adapter.AdapterImagesList;
import com.example.stylistadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class PhotoUploadActivitys extends AppCompatActivity {
    public static final int PICK_REQUEST=3452;
    private Uri filePath;
    int count=0;
    ArrayList<String> arrayList;
    TextView tvPercentage;
    ImageView imageChoosePhoto;
    RecyclerView rvImages;
    AdapterImagesList adapterImages;
    Button btnNextPG;
    Button btnChoosePhoto,btnUpload;
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("STYLIST");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload_activitys);
        imageChoosePhoto=findViewById(R.id.imageChoosePhoto);
        arrayList=new ArrayList<>();
        btnUpload=findViewById(R.id.btnUpload);
        btnChoosePhoto=findViewById(R.id.btnChoosePhoto);
        tvPercentage=findViewById(R.id.tvPercentage);
        rvImages=findViewById(R.id.rvImages);
        btnNextPG=findViewById(R.id.btnNextPG);
        rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterImages=new AdapterImagesList(arrayList,this);
        rvImages.setAdapter(adapterImages);
        btnChoosePhoto.setOnClickListener(v->{
            choosePhoto();
        });
        btnUpload.setOnClickListener(v -> uploadPhoto());
        btnNextPG.setOnClickListener(v->{
            Intent intent=new Intent(PhotoUploadActivitys.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
    private void  choosePhoto() {
        Intent intent =new  Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageChoosePhoto.setImageBitmap(bitmap);

                    count = 1;
                } catch (Exception e) {

                }

            }

        }
    }

    private void uploadPhoto() {
        if (count == 1) {
            final StorageReference childReference = storage.child("STYLISTIMAGES").child(getIntent().getStringExtra("name").toUpperCase())
                    .child(String.valueOf(System.currentTimeMillis()));
            childReference.putFile(filePath).
                    addOnSuccessListener(taskSnapshot -> {
                        childReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            arrayList.add(uri.toString());
                            databaseReference.child(getIntent().getStringExtra("name")).child("STYLISTDETAILS").child("IMAGES")
                                    .setValue(arrayList);

                            adapterImages.notifyDataSetChanged();

                        });
                        tvPercentage.setText("Uploaded");
                        count=0;
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            tvPercentage.setText((taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()*100.0)+"%");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

}
