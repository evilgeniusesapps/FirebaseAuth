package com.evilgeniusesapps.firebaseauth.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.evilgeniusesapps.firebaseauth.R;
import com.evilgeniusesapps.firebaseauth.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    TextView textViewEmail;
    Button buttonLogout;

    DatabaseReference myRef;

    StorageReference storageReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.stringMain);
        setSupportActionBar(toolbar);

        textViewEmail = findViewById(R.id.textViewEmail);


        buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);

        loadingUserData();
    }

    private void loadingUserData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                textViewEmail.setText("" + value.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), R.string.stringErrorIncorrectEmail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, AuthenticationActivity.class));
        finish();
    }
}