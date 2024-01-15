package com.example.quiz.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.Home;
import com.example.quiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        EditText emailTxt = findViewById(R.id.email_input);
        EditText passwordTxt = findViewById(R.id.password_input);
        Button loginBtn = findViewById(R.id.login_button);
        TextView registrationLink = findViewById(R.id.registration_link);

        // Login handler
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                // Reset errors
                emailTxt.setError(null);
                passwordTxt.setError(null);

                // Validate fields
                if (TextUtils.isEmpty(email)) {
                    emailTxt.setError("E-mail adresa je obavezna");
                    emailTxt.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailTxt.setError("Unesite ispravnu e-mail adresu");
                    emailTxt.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordTxt.setError("Lozinka adresa je obavezna");
                    passwordTxt.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    passwordTxt.setError("Lozinka mora sadržavati minimalno 6 znakova");
                    passwordTxt.requestFocus();
                    return;
                }

                // Perform login
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Uspješno ste se prijavili", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Došlo je do pogreške prilikom prijave", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // Registration link handler
        registrationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });
    }
}
