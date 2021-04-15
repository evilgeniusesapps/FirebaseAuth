package com.evilgeniusesapps.firebaseauth.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evilgeniusesapps.firebaseauth.R;
import com.evilgeniusesapps.firebaseauth.activities.MainActivity;
import com.evilgeniusesapps.firebaseauth.interfaces.ChangeFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private ChangeFragment changeFragment;

    // UI
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewForgotPassword;
    Button buttonLogin;
    Button buttonSingUp;

    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    // Firebase
    private FirebaseAuth mAuth;
    String AuthenticationID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        editTextEmail = rootView.findViewById(R.id.editTextEmail);
        editTextPassword = rootView.findViewById(R.id.editTextPassword);
        textViewForgotPassword = rootView.findViewById(R.id.textViewForgotPassword);

        textInputLayoutEmail = rootView.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = rootView.findViewById(R.id.textInputLayoutPassword);

        buttonLogin = rootView.findViewById(R.id.buttonLogin);
        buttonSingUp = rootView.findViewById(R.id.buttonSingUp);

        textViewForgotPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        buttonSingUp.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                checkFields();
                break;

            case R.id.textViewForgotPassword:
                changeFragment.setFragment(new ForgotPasswordFragment());
                break;

            case R.id.buttonSingUp:
                changeFragment.setFragment(new RegistrationFragment());
                break;
        }
    }

    public void checkFields() {

        if ((editTextEmail.getText().length() == 0)) {
            textInputLayoutEmail.setError(getActivity().getResources().getText(R.string.stringErrorFillEmail));
            return;
        }

        if ((editTextPassword.getText().length() == 0)) {
            textInputLayoutPassword.setError(getActivity().getResources().getText(R.string.stringErrorFillPassword));
            return;
        }

        signIn(String.valueOf(editTextEmail.getText()), String.valueOf(editTextPassword.getText()));
    }

    private void signIn(String email, String password) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getActivity().getText(R.string.stringUploading));
        pd.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                login();
            } else {
                Toast.makeText(getContext(), getActivity().getText(R.string.stringErrorIncorrectEmailOrPassword), Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        });
    }

    public void login() {
        FirebaseUser user = mAuth.getCurrentUser();
        AuthenticationID = user.getUid();
        SharedPreferences sharedPreferencesID = getActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesID.edit();
        editor.putString("UserID", AuthenticationID).apply();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
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