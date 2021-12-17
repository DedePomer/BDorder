package com.example.orderbd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LogReg extends AppCompatActivity implements View.OnClickListener
{


    EditText Login;
    EditText Pass;
    Button Reg, Auto;
    SQLiteDatabase database;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);


        Reg = findViewById(R.id.Reg);
        Reg.setOnClickListener(this);

        Auto = findViewById(R.id.Auto);
        Auto.setOnClickListener(this);

        Login = findViewById(R.id.Login);
        Pass = findViewById(R.id.Pass);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.Auto:
                Cursor logCursor = database.query(DBHelper.TABLE_USERS,null,null,null,null,null,null);
                if(logCursor.moveToFirst())
                {
                    int userIndex = logCursor.getColumnIndex(DBHelper.LOGIN);
                    int passIndex = logCursor.getColumnIndex(DBHelper.PASS);
                    int roleIndex = logCursor.getColumnIndex(DBHelper.ROLE);
                    do {
                        if(Login.getText().toString().equals(logCursor.getString(userIndex))&&(Pass.getText().toString().equals(logCursor.getString(passIndex))))
                        {
                            Toast toast;

                            if(logCursor.getInt(roleIndex)==1)
                            {
                                Intent inte = new Intent(this,MainActivity.class);
                                startActivity(inte);
                                toast = Toast.makeText(getApplicationContext(), "Здарова админ", Toast.LENGTH_LONG);
                            }
                            else
                            {
                                Intent inte = new Intent(this,Order.class);
                                inte.putExtra("thisUser",true);
                                startActivity(inte);
                                toast = Toast.makeText(getApplicationContext(), "Ну и зачем ты это запустил", Toast.LENGTH_LONG);
                            }
                            toast.show();
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getApplicationContext(), "Не правильный логин или пароль", Toast.LENGTH_LONG);
                        }
                    }while(logCursor.moveToNext());
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Не правильный логин или пароль", Toast.LENGTH_LONG);
                    toast.show();
                }
                logCursor.close();
                break;
                case R.id.Reg:
                    String log = Login.getText().toString();
                    String pas = Pass.getText().toString();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.LOGIN,log);
                    contentValues.put(DBHelper.PASS,pas);
                    contentValues.put(DBHelper.ROLE,2);
                    database.insert(DBHelper.TABLE_USERS,null,contentValues);
                    Toast toast = Toast.makeText(getApplicationContext(), "Запись добалена", Toast.LENGTH_LONG);
                    toast.show();
                    break;
        }

    }
}