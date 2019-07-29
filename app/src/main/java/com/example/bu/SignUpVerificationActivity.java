package com.example.bu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpVerificationActivity extends AppCompatActivity {


    private FirebaseAuth phAuth;
    private String verificationID;
    private String phone_num;
    private EditText text_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_verification);

        text_input = (EditText) findViewById(R.id.editText_enter_verify_code) ;
        Bundle extras = getIntent().getExtras();
        phone_num = extras.getString("phone");

        phone_num = "+1" + phone_num;
        phAuth = FirebaseAuth.getInstance();

        sendVerificationCode(phone_num);

        findViewById(R.id.button_verify_code).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String code = text_input.getText().toString().trim();

                if(code.isEmpty())
                {
                    text_input.setError("Enter code");
                    text_input.requestFocus();
                    return;
                }
                else if(code.length() < 6)
                {
                    text_input.setError("Enter valid code");
                    text_input.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });


      //          );

        System.out.println("I am in verification " + phone_num);
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInWithCred(credential);
    }

    private void signInWithCred(PhoneAuthCredential credential)
    {
        phAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                            intent.putExtra("phone", phone_num );
                            startActivity(intent);
                        }
                        else
                        {

                        }
                    }
                });
    }

    private void sendVerificationCode(String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
        {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e)
        {
            Toast.makeText(SignUpVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}


