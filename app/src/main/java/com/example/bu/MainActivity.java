package com.example.bu;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<AuthUI .IdpConfig> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        Button button_sign_in = (Button) findViewById(R.id.button_sign_in);
        EditText phone_number = (EditText) findViewById(R.id.field_phone_number);

        Button button_sign_up = (Button) findViewById(R.id.button_sign_up);

        button_sign_up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, SignUpInititalWithNumber.class));
            }
        });

        //the number being entered is formatted to cell phone style
        phone_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        button_sign_in.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText phone_number = (EditText) findViewById(R.id.field_phone_number);
                TextView view_number = (TextView) findViewById(R.id.text_view_number);

                //this is where the number is stripped off the () and -
                String phone_num = String.valueOf(phone_number.getText());
                phone_num = phone_num.replaceAll("[^0-9]","");

                if(TextUtils.isDigitsOnly(phone_num) && phone_num.length() == 10)
                {
                    phone_num = "+1" + phone_num;
                    view_number.setText(phone_num + "");
                    //need to p[a
                    Intent intent = new Intent(getBaseContext(), SignInVerificationActivity.class);
                    intent.putExtra("phone", phone_num );

                    startActivity(intent);

                }
                else
                {
                    view_number.setText("ENTER VALID NUMBER!" + "");
                }

            }
        });
    }
}
