package com.example.bu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

public class HomeScreenActivity extends AppCompatActivity {

  //  private EditText edit_date;// = (EditText) findViewById(R.id.editText_date);
    private TextView user_name;// = (TextView) findViewById(R.id.textView_name);
    private TextView user_zip;// = (TextView) findViewById(R.id.textView_zip);
    private FirebaseUser user;// = FirebaseAuth.getInstance().getCurrentUser();

    private Button sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

      //  user_name = (TextView) findViewById(R.id.textView_name);
      //  user_zip = (TextView) findViewById(R.id.textView_zip);
        user = FirebaseAuth.getInstance().getCurrentUser();
        sign_out = (Button) findViewById(R.id.button_sign_out);

        sign_out.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(HomeScreenActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                System.out.println("USER: " + user.getUid() +" signed out!\n");
                startActivity(i);
            }
        });


        //user_name.setEnabled(false);
        //user_zip.setEnabled(false);

       // String name_from_server = user.getDisplayName();
       // String zip_from_server = user.getUid();

      //  Date current_date = Calendar.getInstance().getTime();
        //edit_date.setText(Stringcurrent_date);

        //user_name.setText(name_from_server);
        //user_zip.setText(zip_from_server);





    }
}
