package com.example.asal.inventorys2;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.asal.inventorys2.adapters.ProductCursorAdapter;
import com.example.asal.inventorys2.data.Contract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ProductCursorAdapter myAdapter;
    ListView listView;
    public static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    public void delete(String name) {
        getContentResolver().delete(Contract.ProductEntry.CONTENT_URI, Contract.ProductEntry.COLUMN_PRODUCT_NAME + "=?", new String[]{name});
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        listView = (ListView) findViewById(R.id.main_list_view);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openAddPage = new Intent(MainActivity.this, AddActivity.class);
                startActivity(openAddPage);
            }
        });
        getLoaderManager().initLoader(0, null, this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openDetail = new Intent(MainActivity.this, DetailActivity.class);
                openDetail.putExtra("PositionDetail", position);
                startActivity(openDetail);
            }
        });
    }

    protected void onResume() {

        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {Contract.ProductEntry.ID, Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Contract.ProductEntry.COLUMN_PRODUCT_NAME, Contract.ProductEntry.COLUMN_PRODUCT_PRICE};
        return new CursorLoader(this, Contract.ProductEntry.CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        TextView textViewEmpty = (TextView) findViewById(R.id.empty_text_view);
        if (data.getCount() == 0) {
            listView.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
            myAdapter = new ProductCursorAdapter(this, data);
            listView.setAdapter(myAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);

    }
}
