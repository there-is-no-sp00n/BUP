package com.example.bu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignInVerificationActivity extends AppCompatActivity {

    private FirebaseAuth phAuth;
    private String verificationID;
    private String phone_num;
    private EditText text_input;

    private FirebaseUser current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_verification);

        text_input = (EditText) findViewById(R.id.editText_enter_verify_code);

        phAuth = FirebaseAuth.getInstance();


        final Bundle extras = getIntent().getExtras();

        phone_num = extras.getString("phone");



        sendVerificationCode(phone_num);

        //verifyCode(code);

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
                else if(code.length() < 6 || code.length() > 6)
                {
                    text_input.setError("Enter valid code");
                    text_input.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code)
    {
        System.out.println("In verifyCode\n");
        try
        {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
            signInWithCred(credential);
        }
        catch (Exception e)
        {
            text_input.setError("INVALID CODE");
        }

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
                            Intent intent = new Intent(getBaseContext(), HomeScreenActivity.class);
                            //intent.putExtra("phone", phone_num );
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        System.out.println("In sendVerificationCode\n");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

        System.out.println("In sendVerificationCode2\n");


        //  current_user = phAuth.getCurrentUser();

       // System.out.println("User to sign is: " + current_user.getUid() + "\n");
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            System.out.println("IN onCodeSent\n");
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
                System.out.println("VERIFY DONE\n");
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e)
        {
            Toast.makeText(SignInVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
