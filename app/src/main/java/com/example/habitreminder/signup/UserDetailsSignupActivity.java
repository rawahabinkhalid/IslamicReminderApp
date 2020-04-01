package com.example.habitreminder.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.habitreminder.R;

public class UserDetailsSignupActivity extends AppCompatActivity {

    private EditText emailText, passwordText, confirmText, nameText;
    private boolean nameCheck, emailCheck, passwordCheck, confirmPassCheck;
    private String name, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_signup);
        TextView goBack = findViewById(R.id.header_signup_back);
        Button gotoMapActivity = findViewById(R.id.next);
        nameText = findViewById(R.id.name_input);
        emailText = findViewById(R.id.email_input);
        passwordText = findViewById(R.id.password_input);
        confirmText = findViewById(R.id.confirm_password_input);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        gotoMapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameCheck && emailCheck && passwordCheck && confirmPassCheck){
                    Intent mapIntent = new Intent(UserDetailsSignupActivity.this, UserMapSignupActivity.class);
                    mapIntent.putExtra("name", name);
                    mapIntent.putExtra("email", email);
                    mapIntent.putExtra("password", password);
                    startActivity(mapIntent);
                }
            }
        });

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() >= 3 ){
                    name = charSequence.toString();
                    nameText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                    nameCheck = true;
                } else {
                    nameCheck = false;
                    nameText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email = charSequence.toString().trim();
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                    emailCheck = true;
                } else{
                    emailCheck = false;
                    emailText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

//        emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                email = emailText.getText().toString().trim();
//                if (!hasFocus) {}
//            }
//        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = charSequence.toString().trim();
                if(password.length() >= 8 && password.matches(".*\\d.*")){
                    passwordText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                    passwordCheck = true;
                } else{
                    passwordCheck = false;
                    passwordText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        confirmText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPassword = confirmText.getText().toString().trim();
                if(confirmPassword.matches(password)){
                    confirmText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
                    confirmPassCheck = true;
                } else{
                    confirmPassCheck = false;
                    confirmText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
}
