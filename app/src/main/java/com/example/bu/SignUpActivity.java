package com.example.bu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        final Button button_sign_up_data = (Button) findViewById(R.id.button_sign_up_w_data);

        final EditText first_name = (EditText) findViewById(R.id.editText_first_name);
        final EditText last_name = (EditText) findViewById(R.id.editText_last_name);
        final EditText phone_num_s_up =(EditText) findViewById(R.id.editText_phone_number);
        final EditText zipcode = (EditText) findViewById(R.id.editText_zip_code);


        phone_num_s_up.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        button_sign_up_data.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String f_name = String.valueOf(first_name.getText());
                String l_name = String.valueOf(last_name.getText());
                String phone_num = String.valueOf(phone_num_s_up.getText());
                phone_num = phone_num.replaceAll("[^0-9]","");
                String zip = String.valueOf(zipcode.getText());

                int ready_for_verification_code = 0;


                TextView error_first_name = (TextView) findViewById(R.id.first_name_error);
                TextView error_last_name = (TextView) findViewById(R.id.last_name_error);
                TextView error_phone_number = (TextView) findViewById(R.id.phone_number_error);
                TextView error_zip = (TextView) findViewById(R.id.zip_code_error);



                if(f_name.isEmpty() || l_name.isEmpty() || phone_num.isEmpty() || zip.isEmpty())
                {
                    if(f_name.isEmpty())
                    {
                        error_first_name.setText("FIRST NAME CANNOT BE EMPTY!" + "");
                    }
                    else
                    {
                        error_first_name.setText("");
                    }

                    if(l_name.isEmpty())
                    {
                        error_last_name.setText("LAST NAME CANNOT BE EMPTY!" + "");
                    }
                    else
                    {
                        error_last_name.setText("");
                    }

                    if(phone_num.isEmpty())
                    {
                        error_phone_number.setText("PHONE NUMBER CANNOT BE EMPTY!" +  "");
                    }
                    else
                    {
                        error_phone_number.setText("");
                    }

                    if(zip.isEmpty())
                    {
                        error_zip.setText("ZIP CANNOT BE EMPTY!" + "");
                    }
                    else
                    {
                        error_zip.setText("");
                    }
                }

                else
                {
                    error_first_name.setText("");
                    error_last_name.setText("");
                    error_phone_number.setText("");
                    error_zip.setText("");


                    f_name = String.valueOf(first_name.getText());
                    l_name = String.valueOf(last_name.getText());
                    phone_num = String.valueOf(phone_num_s_up.getText());
                    zip = String.valueOf(zipcode.getText());

                    if(f_name.length() > 1)
                    {
                        //meaning first name is good
                        ready_for_verification_code++;
                        error_first_name.setText("");
                    }

                    else
                    {
                        error_first_name.setText("ENTER VALID FIRST NAME");
                        ready_for_verification_code = 0;
                    }

                    if(l_name.length() > 1)
                    {
                        //last name is good
                        ready_for_verification_code++;
                        error_last_name.setText("");
                    }
                    else
                    {
                        error_last_name.setText("ENTER VALID LAST NAME");
                        ready_for_verification_code = 0;
                    }

                    phone_num = phone_num.replaceAll("[^0-9]","");
                    if(phone_num.length() == 10)
                    {
                        //phone number good
                        ready_for_verification_code++;
                        error_phone_number.setText("");
                    }
                    else
                    {
                        error_phone_number.setText("ENTER VALID PHONE NUMBER");
                        ready_for_verification_code = 0;
                    }

                    if(zip.length() == 5)
                    {
                        //zipcode good
                        ready_for_verification_code++;
                        error_zip.setText("");
                        //perform zip look up from .csv
                    }
                    else
                    {
                        error_zip.setText("ENTER VALID ZIPCODE");
                        ready_for_verification_code = 0;
                    }
                }

                System.out.println("Ready indicator: " + ready_for_verification_code);

                if(ready_for_verification_code == 4)
                {
                    //meaning all the info in the sign up field is valid and ready for phone number verification
                    //get the data ready to be stored


                    //activate the code enter field activity
                    startActivity(new Intent(SignUpActivity.this, SignUpVerificationActivity.class));

                }


            }
        });
    }
}
