package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView name,type;
    private EditText email, idNumber, bloodGroup;
    private CircleImageView profileImage;
    private Button btn_cancel,btn_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        type = findViewById(R.id.type);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        idNumber= findViewById(R.id.idNumber);
        bloodGroup= findViewById(R.id.bloodGroup);
        profileImage = findViewById(R.id.profileImage);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    type.setText(snapshot.child("type").getValue().toString());
                    name.setText(snapshot.child("name").getValue().toString());
                    idNumber.setText(snapshot.child("idNumber").getValue().toString());
                    bloodGroup.setText(snapshot.child("bloodGroup").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());

                    if(snapshot.hasChild("profilepictureurl")){
                        Glide.with(getApplicationContext()).load(snapshot.child("profilepictureurl").getValue().toString()).into(profileImage);
                    }else{
                        profileImage.setImageResource(R.drawable.profile);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (idNumber.getEditableText().toString()!=null)
                            {
                                reference.child("idNumber").setValue(idNumber.getEditableText().toString());
                                Toast.makeText(EditProfileActivity.this,"Data has been updated",Toast.LENGTH_LONG).show();

                            }else
                            {
                                Toast.makeText(EditProfileActivity.this,"You must enter the value of Phone Number",Toast.LENGTH_LONG).show();

                            }

                            if (email.getEditableText().toString()!=null)
                            {
                                reference.child("email").setValue(email.getEditableText().toString());
                                Toast.makeText(EditProfileActivity.this,"Data has been updated",Toast.LENGTH_LONG).show();
                            }else
                            {
                                Toast.makeText(EditProfileActivity.this,"You must enter the value of Email",Toast.LENGTH_LONG).show();

                            }


                            if (bloodGroup.getEditableText().toString()!=null)
                            {
                                reference.child("bloodGroup").setValue(bloodGroup.getEditableText().toString());
                                Toast.makeText(EditProfileActivity.this,"Data has been updated",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(EditProfileActivity.this, "You must enter the value of BloodGroup", Toast.LENGTH_LONG).show();
                            }
                            Intent intent= new Intent(EditProfileActivity.this,ProfileActivity.class);
                            startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
