package com.example.bu;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity
{
    public static final String FIRST_NAME = "first name";
    public static final String LAST_NAME = "last name";
    public static final String PHONE_NUMBER = "phone number";
    public static final String ZIP_CODE = "zipcode";


    private DocumentReference to_upload;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Bundle extras = getIntent().getExtras();

        final Button button_sign_up_data = (Button) findViewById(R.id.button_sign_up_w_data);

        final EditText first_name = (EditText) findViewById(R.id.editText_first_name);
        final EditText last_name = (EditText) findViewById(R.id.editText_last_name);
        //final EditText phone_num_s_up =(EditText) findViewById(R.id.editText_phone_number);
        final EditText zipcode = (EditText) findViewById(R.id.editText_zip_code);
        final TextView phone_num_s = (TextView) findViewById(R.id.editText_phone_number);

        final String phone_num = extras.getString("phone");

        String temp = phone_num;
        temp.replaceFirst("(\\d{1})(\\d{3})(\\d{3})(\\d+)", "($2)-($3)-($4");
        phone_num_s.setText(temp);



        //phone_num_s_up.addTextChangedListener(new PhoneNumberFormattingTextWatcher());



        button_sign_up_data.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String f_name = String.valueOf(first_name.getText());
                String l_name = String.valueOf(last_name.getText());

               // phone_num = phone_num.replaceAll("[^0-9]","");
                String zip = String.valueOf(zipcode.getText());

                int ready_for_verification_code = 0;



                if(f_name.isEmpty() || l_name.isEmpty() || zip.isEmpty())
                {
                    if(f_name.isEmpty())
                    {
                        first_name.setError("FIRST NAME CANNOT BE EMPTY!" + "");
                    }

                    if(l_name.isEmpty())
                    {
                        last_name.setError("LAST NAME CANNOT BE EMPTY!" + "");
                    }

                    if(zip.isEmpty())
                    {
                        zipcode.setError("ZIP CANNOT BE EMPTY!" + "");
                    }
                }

                else
                {
                    f_name = String.valueOf(first_name.getText());
                    l_name = String.valueOf(last_name.getText());
                  //  phone_num = String.valueOf(phone_num_s_up.getText());
                    zip = String.valueOf(zipcode.getText());

                    if(f_name.length() > 1)
                    {
                        //meaning first name is good
                        ready_for_verification_code++;
                    }

                    else
                    {
                        first_name.setError("ENTER VALID FIRST NAME");
                        ready_for_verification_code = 0;
                    }

                    if(l_name.length() > 1)
                    {
                        //last name is good
                        ready_for_verification_code++;
                    }
                    else
                    {
                        last_name.setError("ENTER VALID LAST NAME");
                        ready_for_verification_code = 0;
                    }


                    if(zip.length() == 5)
                    {
                        //zipcode good
                        ready_for_verification_code++;

                        //perform zip look up from .csv
                    }
                    else
                    {
                        zipcode.setError("ENTER VALID ZIPCODE");
                        ready_for_verification_code = 0;
                    }
                }

                System.out.println("Ready indicator: " + ready_for_verification_code);

                if(ready_for_verification_code == 3)
                {
                    //meaning all the info in the sign up field is valid and ready for phone number verification
                    //get the data ready to be stored

                    Map<String, Object> data_to_save = new HashMap<String, Object>();
                    data_to_save.put(FIRST_NAME, f_name);
                    data_to_save.put(LAST_NAME, l_name);
                    data_to_save.put(PHONE_NUMBER, phone_num);
                    data_to_save.put(ZIP_CODE, zip);

                    System.out.println("AAAAAAAA");
                    to_upload = FirebaseFirestore.getInstance().document("users/" + UUID.randomUUID());



                    to_upload.set(data_to_save).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            System.out.println("User upload successful!\n");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            System.out.println("User upload failed!\n");
                        }
                    });




                    //activate the code enter field activity
                    //startActivity(new Intent(SignUpActivity.this, SignUpVerificationActivity.class));

                }


            }
        });
    }
}
