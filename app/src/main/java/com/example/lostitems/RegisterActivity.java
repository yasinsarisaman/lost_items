package com.example.lostitems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameReg, passwordReg, confPasswordReg, emailReg;
    Button registerBtn;
    FirebaseAuth mAuth;
    Toolbar registerToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initRegister();

        registerBtn.setOnClickListener(view ->
        {
            CreateUser();
        });
    }

    private void CreateUser() {
        String username = usernameReg.getText().toString();
        String password = passwordReg.getText().toString();
        String email = emailReg.getText().toString();
        String confPassword = confPasswordReg.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            usernameReg.setError("Username can not be empty!");
            usernameReg.requestFocus();
        }
        else if(TextUtils.isEmpty(password))
        {
            passwordReg.setError("Password can not be empty!");
            passwordReg.requestFocus();
        }
        else if(TextUtils.isEmpty(confPassword))
        {
            confPasswordReg.setError("Password confirmation can not be empty!");
            confPasswordReg.requestFocus();
        }
        else if(TextUtils.isEmpty(email))
        {
            emailReg.setError("Email can not be empty!");
            emailReg.requestFocus();
        }
        else if(!password.equals(confPassword))
        {
            Toast.makeText(RegisterActivity.this,"Password and Password Confirmation cannot be different", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this,"User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,"There was an error while registering " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void initRegister()
    {
        registerToolbar = (Toolbar)findViewById(R.id.RegisterToolbar);
        if (registerToolbar != null) {
            setSupportActionBar(registerToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        usernameReg = findViewById(R.id.usernameETRegister);
        passwordReg = findViewById(R.id.passwordETRegister);
        confPasswordReg = findViewById(R.id.passwordConfirmationETRegister);
        emailReg = findViewById(R.id.emailETRegister);
        registerBtn = findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();
    }
}