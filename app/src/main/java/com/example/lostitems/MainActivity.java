package com.example.lostitems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, registerBtn;
    EditText usernameET, passwordET;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMain();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
    }

    private void initMain()
    {
        loginBtn = (Button) findViewById(R.id.loginButton);
        registerBtn = (Button) findViewById(R.id.registerButton);
        usernameET = (EditText) findViewById(R.id.userNameET);
        passwordET = (EditText) findViewById(R.id.passwordET);

        //FireBase
        mAuth = FirebaseAuth.getInstance();
    }

    private void loginUser() {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            usernameET.setError("Username can not be empty!");
            usernameET.requestFocus();
        }
        else if(TextUtils.isEmpty(password))
        {
            passwordET.setError("Password can not be empty!");
            passwordET.requestFocus();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this,"Welcome!" ,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,ChoosingRoleActivity.class));
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"There is an error! " + task.getException() ,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}