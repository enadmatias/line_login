package com.example.linesample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LandingPage extends AppCompatActivity {
    private TextView tv_name, tv_id, tv_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        tv_name = findViewById(R.id.textView);
        tv_id = findViewById(R.id.textView2);
        tv_token = findViewById(R.id.textView3);

        Intent intent = getIntent();
        String name = intent.getStringExtra("display_name");
        String id = intent.getStringExtra("user_id");
        String token = intent.getStringExtra("token");

        tv_name.setText(name);
        tv_id.setText(id);
        tv_token.setText(token);
    }
}
