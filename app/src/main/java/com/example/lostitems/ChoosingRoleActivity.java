package com.example.lostitems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChoosingRoleActivity extends AppCompatActivity {

    Toolbar rolesToolbar;
    CardView senderCV, receiverCV, settingsCV, informationCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_role);

        init();

        senderCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoosingRoleActivity.this, SenderActivity.class);
                startActivity(intent);
            }
        });

        receiverCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoosingRoleActivity.this, ReceiverMapActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init()
    {
        rolesToolbar = (Toolbar) findViewById(R.id.RoleActivityToolbar);
        if(rolesToolbar != null)
        {
            setSupportActionBar(rolesToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        senderCV = (CardView) findViewById(R.id.senderCardView);
        receiverCV = (CardView) findViewById(R.id.receiverCardView);
        settingsCV = (CardView) findViewById(R.id.settingsCardView);
        informationCV = (CardView) findViewById(R.id.informationCardView);
    }
}