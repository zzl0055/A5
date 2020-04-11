package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    SQLiteData myDb;
    TextView balance;
    Button btnAdd;
    Button btnSub;
    EditText editDate;
    EditText editPrice;
    EditText editItem;
    TableLayout history;
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new SQLiteData(this);

        balance = (TextView) findViewById(R.id.balance);
        editDate = (EditText) findViewById(R.id.editDate);
        editPrice = (EditText) findViewById(R.id.editPrice);
        editItem = (EditText) findViewById(R.id.editItem);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        history = (TableLayout) findViewById(R.id.tableHistory);
        AddTransaction();
        GetHistory();
    }

    public void AddTransaction(){
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = Double.parseDouble(editPrice.getText().toString());
                        Model model = new Model();
                        model.mDate =  editDate.getText().toString();
                        model.mItem = editItem.getText().toString();
                        model.mPrice = price;
                        boolean result = myDb.createTransaction(model);
                        if (result)
                            Toast.makeText(MainActivity.this, "Transaction Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Transaction Not Created", Toast.LENGTH_LONG).show();
                        GetHistory();
                        ClearText();
                    }
                }
        );

        btnSub.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = -1 * Double.parseDouble(editPrice.getText().toString());
                        Model model = new Model();
                        model.mDate =  editDate.getText().toString();
                        model.mItem = editItem.getText().toString();
                        model.mPrice = price;
                        boolean result = myDb.createTransaction(model);
                        if (result)
                            Toast.makeText(MainActivity.this, "Transaction Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Transaction Not Created", Toast.LENGTH_LONG).show();
                        GetHistory();
                        ClearText();
                    }
                }
        );
    }

    public void GetHistory(){
        ClearTable();
        Cursor result = myDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        Double balance = 0.0;
        while(result.moveToNext()){
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams columnLayout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            columnLayout.weight = 1;

            TextView date = new TextView(this);
            date.setLayoutParams(columnLayout);
            date.setText(result.getString(2));
            tr.addView(date);

            TextView priceView = new TextView(this);
            priceView.setLayoutParams(columnLayout);
            priceView.setText(result.getString(3));
            tr.addView(priceView);

            TextView item = new TextView(this);
            item.setLayoutParams(columnLayout);
            item.setText(result.getString(1));
            tr.addView(item);

            history.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            // get price for balance
            double price = Double.parseDouble(result.getString(3));
            balance += price;
        }
        MainActivity.this.balance.setText("Current Balance: $" + df.format(balance));
    }

    public void ClearText(){
        MainActivity.this.editDate.setText("");
        MainActivity.this.editPrice.setText("");
        MainActivity.this.editItem.setText("");
    }

    public void ClearTable(){
        int count = history.getChildCount();
        for (int i = 1; i < count; i++) {
            history.removeViewAt(1);
        }
    }

}
