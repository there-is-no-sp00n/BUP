package com.example.bu;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity
{
    public static final String FIRST_NAME = "first name";
    public static final String LAST_NAME = "last name";
    public static final String PHONE_NUMBER = "phone number";
    public static final String ZIP_CODE = "zipcode";
    public static final String PASSWORD = "password";

    private FirebaseUser user;


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
        final EditText password = (EditText) findViewById(R.id.editText_password);

        final String phone_num = extras.getString("phone");

        String out_to_gui = phone_num;
        out_to_gui = out_to_gui.substring(0,2) + " " + "(" + out_to_gui.substring(2,5) + ")" + " " + out_to_gui.substring(5,8) + "-" + out_to_gui.substring(8);
        phone_num_s.setText(out_to_gui);




        button_sign_up_data.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String f_name = String.valueOf(first_name.getText());
                String l_name = String.valueOf(last_name.getText());

               // phone_num = phone_num.replaceAll("[^0-9]","");
                String zip = String.valueOf(zipcode.getText());
                String passpass = String.valueOf(password.getText());

                int ready_for_verification_code = 0;



                if(f_name.isEmpty() || l_name.isEmpty() || zip.isEmpty() || passpass.isEmpty())
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

                    if(passpass.isEmpty())
                    {
                        password.setError("PLEASE ENTER PASSWORD!" + "");
                    }
                }

                else
                {
                    f_name = String.valueOf(first_name.getText());
                    l_name = String.valueOf(last_name.getText());
                  //  phone_num = String.valueOf(phone_num_s_up.getText());
                    zip = String.valueOf(zipcode.getText());

                    if(f_name.length() > 1 && f_name != null && f_name.matches("^[a-zA-Z]*$"))
                    {
                        //meaning first name is good
                        ready_for_verification_code++;
                    }

                    else
                    {
                        first_name.setError("ENTER VALID FIRST NAME");
                        ready_for_verification_code = 0;
                    }

                    if(l_name.length() > 1 && l_name != null && l_name.matches("^[a-zA-Z]*$"))
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

                    if(passpass.length() < 6)
                    {
                        password.setError("PASSWORD MUST BE GREATER THAN 6 CHARACTERS");
                        ready_for_verification_code = 0;
                    }

                    else
                    {
                        ready_for_verification_code++;
                    }
                }

                System.out.println("Ready indicator: " + ready_for_verification_code);

                if(ready_for_verification_code == 4)
                {
                    //meaning all the info in the sign up field is valid and ready for phone number verification
                    //get the data ready to be stored

                    Map<String, Object> data_to_save = new HashMap<String, Object>();
                    data_to_save.put(FIRST_NAME, f_name);
                    data_to_save.put(LAST_NAME, l_name);
                    data_to_save.put(PHONE_NUMBER, phone_num);
                    data_to_save.put(ZIP_CODE, zip);
                    data_to_save.put(PASSWORD, passpass);

                    //System.out.println("AAAAAAAA");

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    String user_uid = user.getUid();

                    to_upload = FirebaseFirestore.getInstance().document("users/" + user_uid);//UUID.randomUUID());



                    to_upload.set(data_to_save).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            System.out.println("User upload successful!\n");
                            Intent intent = new Intent(SignUpActivity.this, HomeScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

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

    private static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    private String mix_it_up(String pw)
    {
        String pass = "M" + pw + "z3";
        System.out.println("Secret is: " + pass + "\n" );



        return pass;
    }
}
