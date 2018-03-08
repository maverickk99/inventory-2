
/**
 * Created by Asal on 6.3.2018.
 */

package com.example.asal.inventorys2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class ProductProvider extends ContentProvider {

    private static final int PRODUCTS  = 1;
    private static final int PRODUCT_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_PRODUCT, PRODUCTS);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_PRODUCT + "/#", PRODUCT_ID);
    }

    private DbHelper myDbHelper;

    @Override
    public boolean onCreate() {

        // create dbHelper here
        myDbHelper = new DbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//GET READABLE DATABASE
        SQLiteDatabase database = myDbHelper.getReadableDatabase();
//THİS CURSOR WILL HOLD THE RESULT OF THE PRODUCT QUERY
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {

            case PRODUCTS:
// For the PRODUCT code, query the product table directly with the given projection, selection, selection arguments, and sort order.
                cursor = database.query(Contract.ProductEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                // For the PRODUCT_ID code, extract out the ID from the URI.
                selection = Contract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Contract.ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            default:
                throw new IllegalArgumentException("Can not query unknown " + uri);


        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion isn't supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        SQLiteDatabase database = myDbHelper.getWritableDatabase();
        long id = database.insert(Contract.ProductEntry.TABLE_NAME, null, values);
        Integer quantity =values.getAsInteger(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        // I'm doing null controls here and throwing error messages
        if(quantity<0)
        {
            throw new IllegalArgumentException("Quantity can not be less than zero");
        }
        String supplierName=values.getAsString(Contract.ProductEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Supplier name can not be null");
        }
        String supplierEmail=values.getAsString(Contract.ProductEntry.COLUMN_SUPPLIER_EMAIL);
        if (supplierEmail == null) {
            throw new IllegalArgumentException("Supplier e-mail can not be null");
        }
        String supplierPhone=values.getAsString(Contract.ProductEntry.COLUMN_SUPPLIER_PHONE);
        if (supplierPhone == null) {
            throw new IllegalArgumentException("Supplier phone number can not be null");
        }
        String productName = values.getAsString(Contract.ProductEntry.COLUMN_PRODUCT_NAME);
        if (productName == null) {
            throw new IllegalArgumentException("Product name can not be null");
        }
        Integer price = values.getAsInteger(Contract.ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price < 0) {
            throw new IllegalArgumentException("Price requires valid number");
        }
        if (id == -1) {
            Log.e("Row", "Row can not be equals to -1" + uri);
            Toast.makeText(getContext(), "error row !=-1", Toast.LENGTH_SHORT).show();
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updatePath(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = Contract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePath(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update isn't supported for " + uri);
        }
    }

    private int updatePath(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(Contract.ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(Contract.ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        if (values.containsKey(Contract.ProductEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger(Contract.ProductEntry.COLUMN_PRODUCT_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Path requires valid gender");
            }

        }
        if (values.containsKey(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Path requires valid weight");
            }
        }
        //supplieremail
        if (values.containsKey(Contract.ProductEntry.COLUMN_SUPPLIER_EMAIL)) {
            String supplierEmail = values.getAsString(Contract.ProductEntry.COLUMN_SUPPLIER_EMAIL);
            if (supplierEmail == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        //suppliernumber
        if (values.containsKey(Contract.ProductEntry.COLUMN_SUPPLIER_PHONE)) {
            String supplierNumber = values.getAsString(Contract.ProductEntry.COLUMN_SUPPLIER_PHONE);
            if (supplierNumber == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        //suppliername
        if (values.containsKey(Contract.ProductEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(Contract.ProductEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = myDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(Contract.ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = myDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return database.delete(Contract.ProductEntry.TABLE_NAME, selection, selectionArgs);
            case PRODUCT_ID:
                selection = Contract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(Contract.ProductEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Delete action isn't supported for " + uri);
        }
    }
    @Override
    public String getType(Uri uri) {
        //find URI type here with matcher
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return Contract.ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return Contract.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}

