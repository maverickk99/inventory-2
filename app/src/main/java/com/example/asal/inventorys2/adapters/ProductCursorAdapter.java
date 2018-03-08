package com.example.asal.inventorys2.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.asal.inventorys2.R;
import com.example.asal.inventorys2.data.Contract;


public class ProductCursorAdapter extends CursorAdapter {

    private Context ctx;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        this.ctx = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item_main, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textViewPrice = (TextView) view.findViewById(R.id.price_text_view);
        TextView textViewProductName = (TextView) view.findViewById(R.id.product_name_text_view);

        final TextView textViewQuantity = (TextView) view.findViewById(R.id.quantity_text_view);

        int nameColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY);

        final String name = cursor.getString(nameColumnIndex);

        Integer price = cursor.getInt(priceColumnIndex);

        final Integer[] quantity = {cursor.getInt(quantityColumnIndex)};

        textViewPrice.setText(price + " $");
        textViewProductName.setText(name);
        textViewQuantity.setText(quantity[0] + "");

        Button reduceButton = (Button) view.findViewById(R.id.reduce_button);

        //This function use to increase or decrease the number of products
        // In meantime I'm checking min value as 0
        reduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 0) {
                    quantity[0] -= 1;
                    ContentValues data = new ContentValues();
                    data.put(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity[0]);
                    ctx.getContentResolver().update(Contract.ProductEntry.CONTENT_URI, data, Contract.ProductEntry.COLUMN_PRODUCT_NAME + "=?", new String[]{name});
                    textViewQuantity.setText(quantity[0] + "");

                }

            }
        });


    }
}
