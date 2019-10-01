package com.example.nevadagraci.keepreceipt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nevadagraci.keepreceipt.db.ReceiptDb;
import com.example.nevadagraci.keepreceipt.model.Receipt;

import java.util.ArrayList;
import java.util.Collections;

/* ViewReceipt class is responsible for displaying receipt values to the user as well as allowing users to view their receipts*/
/* Author: Jessica Rosales */

public class ViewReceipt extends Activity {

    // define XML variables
    Button btnView1, btnView2, btnView3, btnView4;
    TextView txtReceipt1, txtReceipt2, txtReceipt3, txtReceipt4, txtReceiptTotal1, txtReceiptTotal2, txtReceiptTotal3, txtReceiptTotal4, txtTotal;

    // define variables
    String outputUri1, outputUri2, outputUri3, outputUri4 = "";
    String title1, title2, title3, title4 = "";
    String date1, date2, date3, date4 = "";
    String payment1, payment2, payment3, payment4 = "";
    String total1, total2, total3, total4 = "";
    String spnCategoryValue = "Personal";

    public View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt);

        // initialize XML values
        btnView1 = findViewById(R.id.btn_view_1);
        btnView2 = findViewById(R.id.btn_view_2);
        btnView3 = findViewById(R.id.btn_view_3);
        btnView4 = findViewById(R.id.btn_view_4);
        txtReceipt1 = findViewById(R.id.txt_receipt_1);
        txtReceipt2 = findViewById(R.id.txt_receipt_2);
        txtReceipt3 = findViewById(R.id.txt_receipt_3);
        txtReceipt4 = findViewById(R.id.txt_receipt_4);
        txtReceiptTotal1 = findViewById(R.id.txt_total_receipt_1);
        txtReceiptTotal2 = findViewById(R.id.txt_total_receipt_2);
        txtReceiptTotal3 = findViewById(R.id.txt_total_receipt_3);
        txtReceiptTotal4 = findViewById(R.id.txt_total_receipt_4);
        txtTotal = findViewById(R.id.txt_overall_total);
        view = findViewById(R.id.view_layout);

        // get the intent from the menu and apply background
        Intent intent = getIntent();
        boolean darkMode = intent.getBooleanExtra("dark_mode", true);
        int darkModeColour = intent.getIntExtra("darkModeColour", 0);

        if (!darkMode) {
            view.setBackgroundColor(darkModeColour);
        }

        // define and implement category spinner
        Spinner spnCategory = findViewById(R.id.spn_category);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.sort_by_cat,
                android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter1);

        // onItemSelectedListener sets the displayed receipt values according to the item of the sort spinner selected
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // sets and displays list of receipts that are in the personal category
                if (selectedItem.equals("Personal")) {
                    ArrayList<Receipt> receipts = createListOfReceipts("Personal");
                    displayReceipts(receipts);
                    spnCategoryValue = "Personal";
                }
                // sets and displays list of receipts that are in the food category
                else if (selectedItem.equals("Food")) {
                    ArrayList<Receipt> receipts = createListOfReceipts("Food");
                    // sorts list by price if selected
                    displayReceipts(receipts);
                    spnCategoryValue = "Food";
                }
                // sets and displays list of receipts that are in the entertainment category
                else if (selectedItem.equals("Entertainment")) {
                    ArrayList<Receipt> receipts = createListOfReceipts("Entertainment");
                    displayReceipts(receipts);
                    spnCategoryValue = "Entertainment";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // implement onClickListeners for buttons which starts the receipt details activity and passes corresponding values
        btnView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ReceiptDetails.class);
                    intent.putExtra("outputUri", outputUri1);
                    intent.putExtra("title", title1);
                    intent.putExtra("date", date1);
                    intent.putExtra("payment", payment1);
                    intent.putExtra("total", total1);
                    startActivity(intent);
            }
        });

        btnView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ReceiptDetails.class);
                    intent.putExtra("outputUri", outputUri2);
                    intent.putExtra("title", title2);
                    intent.putExtra("date", date2);
                    intent.putExtra("payment", payment2);
                    intent.putExtra("total", total2);
                    startActivity(intent);
            }
        });

        btnView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ReceiptDetails.class);
                    intent.putExtra("outputUri", outputUri3);
                    intent.putExtra("title", title3);
                    intent.putExtra("date", date3);
                    intent.putExtra("payment", payment3);
                    intent.putExtra("total", total3);
                    startActivity(intent);
            }
        });

        btnView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ReceiptDetails.class);
                    intent.putExtra("outputUri", outputUri4);
                    intent.putExtra("title", title4);
                    intent.putExtra("date", date4);
                    intent.putExtra("payment", payment4);
                    intent.putExtra("total", total4);
                    startActivity(intent);
            }
        });

    }

    // method createListOfReceipts adds receipt entries from database to a list and returns the list
    public ArrayList<Receipt> createListOfReceipts(String category) {
        ReceiptDb receiptDb = new ReceiptDb(getApplicationContext());

        ArrayList<Receipt> receipts = receiptDb.getData(category);

        return receipts;
    }


    // method displayReceipts sets the textviews according to their corresponding values
    public void displayReceipts(ArrayList<Receipt> receipts) {

        txtReceipt1.setText("");
        txtReceipt2.setText("");
        txtReceipt3.setText("");
        txtReceipt4.setText("");

        txtReceiptTotal1.setText("");
        txtReceiptTotal2.setText("");
        txtReceiptTotal3.setText("");
        txtReceiptTotal4.setText("");

        txtTotal.setText("0.00");

        double total_of_totals = 0;

            if (receipts.size() >= 1) {
                txtReceipt1.setText(receipts.get(0).getTitle());

                txtReceiptTotal1.setText(String.format("%.2f", receipts.get(0).getTotal()));

                outputUri1 = receipts.get(0).isPicture();

                title1 = receipts.get(0).getTitle();

                date1 = receipts.get(0).getDate();

                payment1 = receipts.get(0).getPaymentType();

                total1 = String.format("%.2f", receipts.get(0).getTotal());

                total_of_totals += receipts.get(0).getTotal();
                if (receipts.size() >= 2) {
                    txtReceipt2.setText(receipts.get(1).getTitle());

                    outputUri2 = receipts.get(1).isPicture();

                    title2 = receipts.get(1).getTitle();

                    date2 = receipts.get(1).getDate();

                    payment2 = receipts.get(1).getPaymentType();

                    total2 = String.format("%.2f", receipts.get(1).getTotal());

                    txtReceiptTotal2.setText(String.format("%.2f", receipts.get(1).getTotal()));

                    total_of_totals += receipts.get(1).getTotal();
                    if (receipts.size() >= 3) {
                        txtReceipt3.setText(receipts.get(2).getTitle());

                        outputUri3 = receipts.get(2).isPicture();

                        title3 = receipts.get(2).getTitle();

                        date3 = receipts.get(2).getDate();

                        payment3 = receipts.get(2).getPaymentType();

                        total3 = String.format("%.2f", receipts.get(2).getTotal());

                        txtReceiptTotal3.setText(String.format("%.2f", receipts.get(2).getTotal()));

                        total_of_totals += receipts.get(2).getTotal();
                        if (receipts.size() >= 4) {
                            txtReceipt4.setText(receipts.get(3).getTitle());

                            outputUri4 = receipts.get(3).isPicture();

                            title4 = receipts.get(3).getTitle();

                            date4 = receipts.get(3).getDate();

                            payment4 = receipts.get(3).getPaymentType();

                            total4 = String.format("%.2f", receipts.get(3).getTotal());

                            txtReceiptTotal4.setText(String.format("%.2f", receipts.get(3).getTotal()));

                            total_of_totals += receipts.get(3).getTotal();


                        }
                    }
                }
            }
        txtTotal.setText(String.format("%.2f", total_of_totals));
    }

}
