package com.example.bu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_verification);

        System.out.println("I am in verification\n");
    }
}