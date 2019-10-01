package com.example.nevadagraci.keepreceipt;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nevadagraci.keepreceipt.db.ReceiptDb;
import com.example.nevadagraci.keepreceipt.model.Receipt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* Input Receipt class is responsible for taking user input for a receipt and inserting it into the database*/
// Author of class: Nevada Graci

public class InputReceipt extends Activity {

    // Define XML variables
    static EditText edtDate;
    EditText edtTitle, edtTotal, edtPaymentType;
    Button btnSave, btnTakePhoto;
    CheckBox chkFood, chkEntertainment, chkPersonal;
    TextView txtCheckboxes;

    // define variables
    String category;
    Double total;
    static boolean dateChanged;
    static final int TAKE_PICTURE = 1;
    private Uri outputFileUri;
    static String uri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_receipt);

        // initialize XML elements
        edtDate = findViewById(R.id.edt_date);
        edtTitle = findViewById(R.id.edt_title);
        edtTotal = findViewById(R.id.edt_total);
        edtPaymentType = findViewById(R.id.edt_payment_type);
        btnSave = findViewById(R.id.btn_save);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        chkFood = findViewById(R.id.chk_food);
        chkEntertainment = findViewById(R.id.chk_entertainment);
        chkPersonal = findViewById(R.id.chk_personal);
        txtCheckboxes = findViewById(R.id.txt_checkboxes);

        // set the initial dateChanged variable to false - when the app starts, date is not clicked
        dateChanged = false;

        // set onClickListener for edtDate to call the DatePickerFragment
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "datePicker");
            }
        });

        // set an onCheckChangedListener the checkboxes to deal with error checking
        chkFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chkPersonal.setEnabled(false);
                    chkEntertainment.setEnabled(false);
                    txtCheckboxes.setError(null);
                }
                else {
                    chkPersonal.setEnabled(true);
                    chkEntertainment.setEnabled(true);
                }
            }
        });

        chkPersonal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chkFood.setEnabled(false);
                    chkEntertainment.setEnabled(false);
                    txtCheckboxes.setError(null);
                }
                else {
                    chkFood.setEnabled(true);
                    chkEntertainment.setEnabled(true);
                }
            }
        });

        chkEntertainment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chkFood.setEnabled(false);
                    chkPersonal.setEnabled(false);
                    txtCheckboxes.setError(null);
                }
                else {
                    chkFood.setEnabled(true);
                    chkPersonal.setEnabled(true);
                }
            }
        });

        // set an onClickListener for btnTakePhoto to deal with picture intent
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the user entered receipt title and error check to ensure that the title is not empty
                String title = edtTitle.getText().toString();
                if (title.isEmpty()) {
                    edtTitle.setError("Please enter receipt title");
                }
                else {
                    // create a new picture intent
                    Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // call the getFileUri method to set a uri for the picture
                    outputFileUri = getFileUri(title);

                    // check to ensure that the camera handler is available - start the activity if it is, display a toast if it is not available
                    if (isIntentHandlerAvailable(pictureIntent)) {
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(pictureIntent, TAKE_PICTURE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Camera handler not available", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        // set an onClickListener for btnSave to deal with user input and saving the user inputted data to the database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get values of the edit texts
                String title = edtTitle.getText().toString();
                String inputTotal = edtTotal.getText().toString();
                String paymentType = edtPaymentType.getText().toString();
                String date = edtDate.getText().toString();

                // error checking for invalid input
                if (title.isEmpty() || !dateChanged || inputTotal.isEmpty() || paymentType.isEmpty()) {
                    if (title.isEmpty()) {
                        edtTitle.setError("Please enter title of receipt");
                    }
                    if (!dateChanged) {
                        edtDate.setError("Please enter date");
                    }
                    if (inputTotal.isEmpty()) {
                        edtTotal.setError("Please enter total");
                    }
                    if (paymentType.isEmpty()) {
                        edtPaymentType.setError("Please enter payment type");
                    }
                    if (!chkFood.isChecked() && !chkEntertainment.isChecked() && !chkPersonal.isChecked()) {
                        txtCheckboxes.requestFocus();
                        txtCheckboxes.setError("Please select a category"); // fix this
                    }
                }
                else {
                    // check to ensure that the inputted total is a number
                    try {
                        total = Double.parseDouble(inputTotal);
                    } catch (NumberFormatException e) {
                        edtTotal.setError("Please enter valid total");
                    }

                    // check to ensure that that the total is not negative
                    if (total < 0) {
                        edtTotal.setError("Please enter a valid total");
                    } else {
                        // set the category variable corresponding checkbox values if it is checked
                        if (chkFood.isChecked()) {
                            category = "Food";
                        }
                        if (chkEntertainment.isChecked()) {
                            category = "Entertainment";
                        }
                        if (chkPersonal.isChecked()) {
                            category = "Personal";
                        }

                        // instance of receipt class
                        Receipt receipt = new Receipt(title, date, total, paymentType, category, uri);

                        // create an instance of the ReceiptDb class
                        ReceiptDb receiptDb = new ReceiptDb(getApplicationContext());

                        // save receipt
                        receiptDb.saveReceipt(receipt);

                        // display a message if the receipt has been successfuly added
                        Toast.makeText(getApplicationContext(), "Receipt successfully added!", Toast.LENGTH_LONG).show();

                        // reset all fields
                        clear();
                    }

                }
            }

        });

    }

    // overriden onActivityResult method ensures that the pictureIntent was successful by displaying a toast and setting the uri variable to the outoutFileUri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "Picture added successfully!", Toast.LENGTH_LONG).show();
            uri = outputFileUri.toString();
        }
    }

    // getFileUri method intakes the title of the receipt and saves the photo
    private Uri getFileUri(String title) {
        // creates a directory for ReceiptPictures
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ReceiptPictures");

        // checks to ensure that the folder does not exist
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Log.e("MainActivity", "Can't create folder " + folder.toString());
                return null;
            }
        }

        // checks to ensure that you can write to the folder
        if (!folder.canWrite()) {
            Log.e("MainActivity", "Can't write to " + folder.toString());
            return null;
        }

        // sets the name of the photo according to the title of the receipt and the current date and time
        String fileName = title + new SimpleDateFormat("yyMMdd_hhmmss", Locale.CANADA).format(new Date())+ ".jpg";

        // saves the photo to the corresponding folder
        File file = new File(folder, fileName);

        // returns the uri of the file
        return Uri.fromFile(file);
    }

    // isIntentHandlerAvailable method ensures that the device can handle a picture intent
    private boolean isIntentHandlerAvailable(Intent intent) {

        PackageManager pm = getPackageManager();
        return intent.resolveActivity(pm) != null;
    }

    // clear method clears all the user inputted values
    public void clear() {
        outputFileUri = null;
        edtTitle.setText("");
        edtDate.setText("");
        edtTotal.setText("");
        edtPaymentType.setText("");
        chkFood.setChecked(false);
        chkEntertainment.setChecked(false);
        chkPersonal.setChecked(false);
        uri = "";
    }

    // static class which implements the DatePickerFragment
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        // overriden onCreateDialog creates the DatePickerFragement with the year, month, and day
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        // overriden onDateSet method sets editText to DatePicker
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            dateChanged = true;
            edtDate.setError(null);
        }
    }
}
