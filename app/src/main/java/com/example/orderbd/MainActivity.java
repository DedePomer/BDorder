package com.example.orderbd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnOrder;
    Button Bmain, Bnemain;
    EditText etName, etPrice;
    TextView tvSumm;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Bmain = findViewById(R.id.Main);
        Bnemain = findViewById(R.id.NeMain);
        Bmain.setOnClickListener(this);
        Bnemain.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.Add);
        btnAdd.setOnClickListener(this);

        tvSumm = (TextView) findViewById(R.id.Summ);
        etName = (EditText) findViewById(R.id.Name);
        etPrice = (EditText) findViewById(R.id.Price);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();
    }


    public void UpdateTable()
    {
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int genderIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);

            TableLayout dbOutput = findViewById(R.id.Table);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);

                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputPrice = new TextView(this);
                params.weight = 3.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(genderIndex));
                dbOutputRow.addView(outputPrice);

                Button btndelete = new Button(this);
                btndelete.setOnClickListener(this);
                params.weight = 1.0f;
                btndelete.setLayoutParams(params);
                btndelete.setText("УДАЛИТЬ");
                btndelete.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(btndelete);



                dbOutput.addView(dbOutputRow);
            }
            while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Add:
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_PRICE, price);

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                UpdateTable();
                etName.setText("");
                etPrice.setText("");
                break;

            case R.id.Main:
                Toast toast = Toast.makeText(getApplicationContext(), "Страница таже", Toast.LENGTH_LONG);
                toast.show();
                break;

            case R.id.NeMain:
                Intent intent = new Intent(this, Order.class);
                intent.putExtra("thisUser",false);
                startActivity(intent);
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();
                database.delete(dbHelper.TABLE_CONTACTS, dbHelper.KEY_ID+" = ?", new String[] {String.valueOf(v.getId())} );

                Cursor cursorUpdater = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAME);
                    int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);

                    int realID = 1;
                    do{
                        if (cursorUpdater.getInt(idIndex) > realID){
                            contentValues.put(dbHelper.KEY_ID, realID);
                            contentValues.put(dbHelper.KEY_NAME, cursorUpdater.getString(nameIndex));
                            contentValues.put(dbHelper.KEY_PRICE, cursorUpdater.getString(priceIndex));
                            database.replace(dbHelper.TABLE_CONTACTS, null, contentValues);
                        }
                        realID++;
                    }
                    while(cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast()) {
                        if (cursorUpdater.moveToLast() && v.getId()!=realID) {
                            database.delete(dbHelper.TABLE_CONTACTS, dbHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                        }
                    }
                    UpdateTable();
                }
                else
                    Log.d("mLog","0 rows");
                break;
        }

    }
}