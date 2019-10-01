package com.example.nevadagraci.keepreceipt.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nevadagraci.keepreceipt.model.Receipt;

import java.util.ArrayList;

/* The ReceiptDb class is responsible for dealing with database functionality of the program such as inputting user defined values into the database and performing queries */
// Author: Nevada Graci, getData method by Jessica Rosales
public class ReceiptDb extends Activity {

    // data fields to hold database objects
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    // constants for database name and version
    private static final String DB_NAME = "receipts_db.db";
    private static final int DB_VERSION = 1;

    // constants for table and fields
    private static final String RECEIPTS_TABLE = "Receipts";

    // ** add date
    private static final String ID = "_id";
    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String TOTAL = "total";
    private static final String PAYMENTTYPE = "paymentType";
    private static final String CATEGORY = "category";
    private static final String PICTURE = "camera";

    // create string format
    private final static String FORMAT = "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " + "%s TEXT, %s TEXT, %s DECIMAL(10,2), %s TEXT, %s TEXT, %s TEXT)";


    // create query string for table
    private static final String CREATE_RECEIPTS_TABLE = String.format(FORMAT, RECEIPTS_TABLE, ID,TITLE, DATE, TOTAL, PAYMENTTYPE, CATEGORY, PICTURE);


    // create constructor
    public ReceiptDb(Context context) {
        openHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // helper class that creates database
    private static class DBHelper extends SQLiteOpenHelper {

        private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // initialize database
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_RECEIPTS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + RECEIPTS_TABLE);
            onCreate(db);
        }
    }

    // method that saves receipts to database
    public void saveReceipt(Receipt receipt) {
        // get database to write to
        database = openHelper.getWritableDatabase();

        // create ContentValues to hold field/name value pairs
        ContentValues cv = new ContentValues();
        cv.put(TITLE, receipt.getTitle());
        cv.put(DATE, receipt.getDate());
        cv.put(TOTAL, receipt.getTotal());
        cv.put(PAYMENTTYPE, receipt.getPaymentType());
        cv.put(CATEGORY, receipt.getCategory());
        cv.put(PICTURE, receipt.isPicture());

        // call the insert in the database
        long id = database.insert(RECEIPTS_TABLE, null, cv);

        // set the id of the receipt
        receipt.setDbId(id);

        // close database
        database.close();
    }

    // getData method intakes a category, performs a query based on the category entered, and returns an ArrayList of found entries of that category
    public ArrayList<Receipt> getData(String spnCategory){
        // gets the readable database
        database = openHelper.getReadableDatabase();

        // initalizes selection and selectionArgs vales
        String selection = null;
        String[] selectionArgs = null;

        // sets the selection and selectionArgs according to the intaken category
        if (spnCategory != null && spnCategory.length() > 0) {
            selection = CATEGORY + " = ?";
            selectionArgs = new String[] {spnCategory};
        }

        // sets a cursor to perform a query
        Cursor cursor = database.query(RECEIPTS_TABLE, null,selection,selectionArgs,null,null,TITLE);

        // create an ArrayList of type Receipt
        ArrayList<Receipt> receipts = new ArrayList<>();

        // while loop goes through database entries, sets corresponding variables to the cursor value, and adds an instance of receipt to the ArrayList
        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndex(ID));
            String title = cursor.getString(cursor.getColumnIndex(TITLE));
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            float total = Float.parseFloat(cursor.getString(cursor.getColumnIndex(TOTAL)));
            String paymenttype = cursor.getString(cursor.getColumnIndex(PAYMENTTYPE));
            String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
            String picture = cursor.getString(cursor.getColumnIndex(PICTURE));

            receipts.add(new Receipt(title,date,total,paymenttype,category,picture));
        }

        // closes cursor and database
        cursor.close();
        database.close();

        // returns list of receipts
        return receipts;
    }

}
