package com.evilgeniusesapps.firebaseauth.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.evilgeniusesapps.firebaseauth.R;
import com.evilgeniusesapps.firebaseauth.activities.MainActivity;
import com.evilgeniusesapps.firebaseauth.interfaces.ChangeFragment;
import com.evilgeniusesapps.firebaseauth.models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private ChangeFragment changeFragment;

    // UI
    Toolbar toolbar;

    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSingUp;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    // Firebase
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    String authenticationID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.stringRegistration);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.setSupportActionBar(toolbar);

        editTextEmail = rootView.findViewById(R.id.editTextEmail);
        editTextPassword = rootView.findViewById(R.id.editTextPassword);
        textInputLayoutEmail = rootView.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = rootView.findViewById(R.id.textInputLayoutPassword);

        buttonSingUp = rootView.findViewById(R.id.buttonSingUp);
        buttonSingUp.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSingUp:
                checkFields();
                break;
        }
    }

    public void checkFields() {

        if ((editTextEmail.getText().length() == 0)) {
            textInputLayoutEmail.setError(getActivity().getResources().getText(R.string.stringErrorFillEmail));
            return;
        }
        textInputLayoutEmail.setError(null);
        if ((editTextPassword.getText().length() == 0)) {
            textInputLayoutPassword.setError(getActivity().getResources().getText(R.string.stringErrorFillPassword));
            return;
        }
        textInputLayoutPassword.setError(null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        createAccount(String.valueOf(editTextEmail.getText()), String.valueOf(editTextPassword.getText()));
    }

    public void createAccount(String email, String password) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getActivity().getText(R.string.stringUploading));
        pd.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                com.google.firebase.auth.FirebaseUser user = mAuth.getCurrentUser();
                authenticationID = user.getUid();

                SharedPreferences sharedPreferencesID = getActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesID.edit();
                editor.putString("UserID", authenticationID).apply();
                login();
            } else {
                Toast.makeText(getContext(), getActivity().getText(R.string.stringErrorIncorrectEmail), Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        });
    }

    private void writingToDatabase() {
        String email = String.valueOf(editTextEmail.getText());
        String password = String.valueOf(editTextPassword.getText());
        User user = new User(email, password);
        myRef.child("Users/" + authenticationID).setValue(user);
    }

    public void login() {
        writingToDatabase();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ChangeFragment) {
            changeFragment = (ChangeFragment) context;
        }
    }
}