package com.example.bu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpInititalWithNumber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_initital_with_number);


        final EditText enter_phone = (EditText) findViewById(R.id.editText_enter_number);

        enter_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Button to_verify = (Button) findViewById(R.id.button_to_num_verification);

        to_verify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String number = String.valueOf(enter_phone.getText());
                number = number.replaceAll("[^0-9]","");

                if(number.length() == 10)
                {
                    //valid number
                    System.out.println("Valid number send the text now\n");


                    //send message


                    //open new activity
                    Intent intent = new Intent(getBaseContext(), SignUpVerificationActivity.class);
                    intent.putExtra("phone", number);
                    startActivity(intent);
                }
                else
                {
                    enter_phone.setError("Enter valid phone number");
                    System.out.println("Not valid number\n");
                }
            }
        });
    }
}
