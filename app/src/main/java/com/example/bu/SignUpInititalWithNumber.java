package com.example.bu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignUpInititalWithNumber extends AppCompatActivity
{

   // private FirebaseDatabase database = FirebaseDatabase.getInstance();
    //come back here later to add the number already registered part
    private DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("users");
    private boolean isExist;

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

              isExist = false;

                if(number.length() == 10)
                {
                    //valid number
                    System.out.println("Valid number send the text now\n");

                   // final String temp_num = "+1" + number;

                   // System.out.println("number is " + temp_num + " \n");

                    //if(user_ref.orderByChild("phone number").equalTo(temp_num).)
                    //{

                    //}

                   /* user_ref.orderByChild("phone number").equalTo(temp_num).addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                           // for(DataSnapshot data: dataSnapshot.getChildren())
                            //{
                                System.out.println("In looop!\n");
                                if (dataSnapshot.exists())//data.child(temp_num).exists())
                                {
                                    isExist = true;
                                    System.out.println("Phone number already exists\n");
                                }
                                else
                                {
                                    isExist = false;
                                    System.out.println("New phone. Create new user\n");
                                }
                            //}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                            System.out.println("DATABASE ERROR\n");
                        }
                    });

                    */

                }
                else
                {
                    enter_phone.setError("Enter valid phone number");
                    System.out.println("Not valid number\n");
                }

                if(isExist == true)
                {
                    enter_phone.setError("Phone number already registered, please SIGN IN");
                }

                else
                {
                    //send message


                    //open new activity
                    Intent intent = new Intent(getBaseContext(), SignUpVerificationActivity.class);
                    intent.putExtra("phone", number);
                    startActivity(intent);
                }
            }
        });
    }
}
