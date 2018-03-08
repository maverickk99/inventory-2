package com.example.asal.inventorys2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asal.inventorys2.data.Contract;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detail");

        final int getPosition = getIntent().getIntExtra("PositionDetail", 0);
        String[] projection = {Contract.ProductEntry.ID, Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Contract.ProductEntry.COLUMN_PRODUCT_NAME, Contract.ProductEntry.COLUMN_PRODUCT_PRICE, Contract.ProductEntry.COLUMN_SUPPLIER_NAME, Contract.ProductEntry.COLUMN_SUPPLIER_EMAIL, Contract.ProductEntry.COLUMN_SUPPLIER_PHONE};
        Cursor cursor = getContentResolver().query(Contract.ProductEntry.CONTENT_URI, projection, null, null, null);
        cursor.moveToPosition(getPosition);


        int nameColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int supplierNameColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_SUPPLIER_NAME);
        int supplierEmailColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_SUPPLIER_EMAIL);
        int supplierPhoneColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_SUPPLIER_PHONE);

        String supplierName = cursor.getString(supplierNameColumnIndex);

        final String supplierEmail = cursor.getString(supplierEmailColumnIndex);
        final String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
        final String name = cursor.getString(nameColumnIndex);

        Integer price = cursor.getInt(priceColumnIndex);

        final Integer[] quantity = {cursor.getInt(quantityColumnIndex)};

        TextView ViewSupplierName = (TextView) findViewById(R.id.supplier_name_detail_text_view);
        TextView ViewSupplierEmail = (TextView) findViewById(R.id.supplier_email_detail_text_view);
        TextView ViewSupplierPhone = (TextView) findViewById(R.id.supplier_phone_number_detail_text_view);
        TextView ViewPrice = (TextView) findViewById(R.id.price_detail_text_view);
        TextView ViewProductName = (TextView) findViewById(R.id.product_name_detail_text_view);

        final EditText ViewQuantity = (EditText) findViewById(R.id.quantity_edit_text);

        ViewPrice.setText(String.valueOf(price) + " $");
        ViewProductName.setText(name);
        ViewQuantity.setText(String.valueOf(quantity[0]));
        ViewSupplierEmail.setText(supplierEmail);
        ViewSupplierName.setText(supplierName);
        ViewSupplierPhone.setText(supplierPhone);

        Button increaseButtonDetail = findViewById(R.id.inrease_detail_button);
        Button decreaseButtonDetail = findViewById(R.id.decrease_detail_button);
        Button deleteButtonDetail = findViewById(R.id.delete_detail_button);
        Button callButtonDetail = findViewById(R.id.call_detail_button);
        Button editButtonDetail = findViewById(R.id.edit_detail_button);
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ViewQuantity.getText().toString().isEmpty() != true) {
                    ContentValues data = new ContentValues();
                    data.put(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(ViewQuantity.getText().toString()));
                    getContentResolver().update(Contract.ProductEntry.CONTENT_URI, data, Contract.ProductEntry.COLUMN_PRODUCT_NAME + "=?", new String[]{name});
                    String.valueOf(ViewQuantity.getText());
                }
            }
        };
        ViewQuantity.addTextChangedListener(textWatcher);


        editButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openEdit = new Intent(DetailActivity.this, EditActivity.class);
                openEdit.putExtra("Position", getPosition);
                startActivity(openEdit);
                finish();
            }
        });

        deleteButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this);
                builder1.setMessage("Are you sure this product will be deleted.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getContentResolver().delete(Contract.ProductEntry.CONTENT_URI, Contract.ProductEntry.COLUMN_PRODUCT_NAME + "=?", new String[]{name});
                                finish();

                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder1.create();
                alert.show();
            }
        });
        increaseButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity[0] += 1;
                ContentValues data = new ContentValues();
                data.put(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity[0]);
                getContentResolver().update(Contract.ProductEntry.CONTENT_URI, data, Contract.ProductEntry.COLUMN_PRODUCT_NAME + "=?", new String[]{name});
                ViewQuantity.setText(quantity[0] + "");
            }
        });
        decreaseButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 0) {
                    quantity[0] -= 1;
                    ContentValues data = new ContentValues();
                    data.put(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity[0]);
                    getContentResolver().update(Contract.ProductEntry.CONTENT_URI, data, Contract.ProductEntry.COLUMN_PRODUCT_NAME + "=?", new String[]{name});
                    ViewQuantity.setText(quantity[0] + "");
                }
            }
        });

        callButtonDetail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (supplierEmail.isEmpty() == false) {

                    String uri = "tel:" + supplierPhone.trim();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }

            }
        });
    }

}
