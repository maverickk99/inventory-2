package com.example.asal.inventorys2;

/**
 * Created by Asal on 7.3.2018.
 */

import android.content.ContentValues;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.util.Patterns;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.WindowManager;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.example.asal.inventorys2.data.Contract;

public class AddActivity extends AppCompatActivity {
    EditText editTextProductName;
    EditText editTextPrice;
    EditText editTextQuantity;
    EditText editTextSupplierName;
    EditText editTextSupplierEmail;
    EditText editTextSupplierPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // you can call or do what you want with your EditText here
        setContentView(R.layout.dialog_add_product);
        editTextProductName = (EditText) findViewById(R.id.add_product_name_edit_text);
        editTextPrice = (EditText) findViewById(R.id.add_price_edit_text);
        editTextQuantity = (EditText) findViewById(R.id.add_quantity_edit_text);
        editTextSupplierName = (EditText) findViewById(R.id.add_supplier_name_edit_text);
        editTextSupplierEmail = (EditText) findViewById(R.id.add_supplier_email_edit_text);
        editTextSupplierPhone = (EditText) findViewById(R.id.add_supplier_phone_number_edit_text);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_icon_item) {
            insertData();
        }
        return true;
    }

    private void insertData() {
        if (editTextPrice.getText().toString().trim().isEmpty() || editTextProductName.getText().toString().trim().isEmpty() || editTextQuantity.getText().toString().trim().isEmpty() || editTextSupplierName.getText().toString().trim().isEmpty() || editTextSupplierPhone.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please all information entry.", Toast.LENGTH_SHORT).show();

        } else if (isValidEmail(editTextSupplierEmail.getText().toString().trim()) == false) {
            Toast.makeText(this, "You need to right your mail address correctly.", Toast.LENGTH_SHORT).show();

        } else {

            ContentValues data = new ContentValues();
            data.put(Contract.ProductEntry.COLUMN_PRODUCT_PRICE, Integer.parseInt(editTextPrice.getText().toString()));
            data.put(Contract.ProductEntry.COLUMN_PRODUCT_NAME, editTextProductName.getText().toString());
            data.put(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(editTextQuantity.getText().toString()));
            data.put(Contract.ProductEntry.COLUMN_SUPPLIER_NAME, editTextSupplierName.getText().toString());
            data.put(Contract.ProductEntry.COLUMN_SUPPLIER_EMAIL, editTextSupplierEmail.getText().toString());
            data.put(Contract.ProductEntry.COLUMN_SUPPLIER_PHONE, editTextSupplierPhone.getText().toString());
            getContentResolver().insert(Contract.ProductEntry.CONTENT_URI, data);
            finish();
        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        // check email is valid with regex patterns
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
