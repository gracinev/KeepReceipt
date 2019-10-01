package com.example.nevadagraci.keepreceipt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/* Main Activity displays the home page of the app and brings the user to the Input Receipt or View Receipt activity through an intent*/
// Author: Nevada Graci and Jessica Rosales

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // onClickListener which brings the user to the Input Receipt activity
        findViewById(R.id.btn_input_receipt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InputReceipt.class);
                startActivity(intent);
            }
        });

        // onClickListener which brings the user to the Input Receipt Activity
        findViewById(R.id.btn_view_receipt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewReceipt.class);
                startActivity(intent);
            }
        });
    }
}
