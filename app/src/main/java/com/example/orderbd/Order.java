package com.example.orderbd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Order extends AppCompatActivity implements View.OnClickListener{

    Button Bmain, Bnemain, Border;
    TextView TVsumm;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    int summ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        TVsumm = findViewById(R.id.Summ);
        Bmain = findViewById(R.id.Main);
        Bnemain = findViewById(R.id.NeMain);
        Border = findViewById(R.id.Border);

        Bmain.setOnClickListener(this);
        Bnemain.setOnClickListener(this);
        Border.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.Main:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.NeMain:
                Toast toast = Toast.makeText(getApplicationContext(), "Страница таже", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.Border:
                Toast toast1;
                if(summ!=0)
                {
                    TVsumm.setText("");
                    toast1 = Toast.makeText(getApplicationContext(), "Сумма заказа: " + summ , Toast.LENGTH_LONG);
                    toast1.show();
                    summ = 0;
                }
                else
                {
                    toast1 = Toast.makeText(getApplicationContext(), "Сумма не может быть равна 0", Toast.LENGTH_LONG);
                    toast1.show();
                }
                break;
            default:
                    Button b = (Button) view;
                    Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                    int idb = view.getId();
                    cursor.moveToPosition(idb-1);
                    int id = cursor.getColumnIndex(DBHelper.KEY_PRICE);
                    summ += Integer.valueOf(cursor.getString(id));
                    TVsumm.setText(String.valueOf(summ));
                break;
        }
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

                Button btnbasket = new Button(this);
                btnbasket.setOnClickListener(this);
                params.weight = 1.0f;
                btnbasket.setLayoutParams(params);
                btnbasket.setText("В КОРЗИНУ");
                btnbasket.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(btnbasket);

                dbOutput.addView(dbOutputRow);
            }
            while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }
}