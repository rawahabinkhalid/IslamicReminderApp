package com.example.habitreminder.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habitreminder.R;
import com.example.habitreminder.login.LoginActivity;
import com.example.habitreminder.signup.UserDetailsSignupActivity;

public class LoginSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        Button login = findViewById(R.id.login_button);
        Button signup = findViewById(R.id.signup_button);
        TextView forgotPassword = findViewById(R.id.forget_password_link);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginSignupActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginSignupActivity.this, UserDetailsSignupActivity.class);
                startActivity(loginIntent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginSignupActivity.this, "Forgot password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
