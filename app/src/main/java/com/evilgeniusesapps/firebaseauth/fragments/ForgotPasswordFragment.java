package com.evilgeniusesapps.firebaseauth.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.evilgeniusesapps.firebaseauth.R;
import com.evilgeniusesapps.firebaseauth.interfaces.ChangeFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener{

    private ChangeFragment changeFragment;

    // UI
    Toolbar toolbar;

    TextInputLayout textInputLayoutEmail;
    EditText editTextEmail;
    Button buttonReset;

    TextView textViewCheckEmail;
    Button buttonOk;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.stringPasswordRecovery);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.setSupportActionBar(toolbar);

        textInputLayoutEmail = rootView.findViewById(R.id.textInputLayoutEmail);
        editTextEmail = rootView.findViewById(R.id.editTextEmail);
        buttonReset = rootView.findViewById(R.id.buttonReset);

        textViewCheckEmail = rootView.findViewById(R.id.textViewCheckEmail);
        buttonOk = rootView.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(this);

        buttonReset.setOnClickListener(this);
        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonReset:
                checkFields();
                break;

            case R.id.buttonOk:
                changeFragment.setFragment(new LoginFragment());
                break;
        }
    }

    public void checkFields() {
        if ((editTextEmail.getText().length() == 0)) {
            textInputLayoutEmail.setError(getActivity().getResources().getText(R.string.stringErrorFillEmail));
            return;
        }
        resetPassword();
    }

    private void resetPassword(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getActivity().getText(R.string.stringUploading));
        pd.show();

        FirebaseAuth.getInstance().sendPasswordResetEmail(editTextEmail.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                textInputLayoutEmail.setVisibility(View.GONE);
                buttonReset.setVisibility(View.GONE);
                textViewCheckEmail.setVisibility(View.VISIBLE);
                buttonOk.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), getActivity().getText(R.string.stringEmailNotFound), Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ChangeFragment) {
            changeFragment = (ChangeFragment) context;
        }
    }
}