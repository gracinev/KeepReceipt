package com.example.nevadagraci.keepreceipt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.TotalCaptureResult;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiptDetails extends Activity {
    ImageView imageView;
    TextView txtTitle, txtDate, txtPayment, txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);

        imageView = findViewById(R.id.img_view);
        txtTitle = findViewById(R.id.txt_title);
        txtDate = findViewById(R.id.txt_date);
        txtPayment = findViewById(R.id.txt_payment);
        txtTotal = findViewById(R.id.txt_total);

        Intent intent = getIntent();
        String outputUri = intent.getStringExtra("outputUri");
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String payment = intent.getStringExtra("payment");
        String total = intent.getStringExtra("total");

        txtTitle.setText(getResources().getString(R.string.input_title, title));
        txtDate.setText(getResources().getString(R.string.input_date, date));
        txtPayment.setText(getResources().getString(R.string.input_payment_type, payment));
        txtTotal.setText(getResources().getString(R.string.input_total, total));

        if (!outputUri.isEmpty()) {
            Uri uri = Uri.parse(outputUri);

            Bitmap bitmapFull = BitmapFactory.decodeFile(uri.getPath());

            imageView.setImageBitmap(bitmapFull);

        }
    }
}
