package com.calharad.contactlistproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Klasa zarządzająca baza danych
 * */
public class DatabaseAdapter extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "LOG_DATABASE";

    private static final int DB_VERSION = 1;                            //wersja bazy danych
    private static final String DB_NAME = "database";                   //nazwa bazy danych
    private static final String TABLE_CONTACTS = "contacts";            //nazwa tabeli

    //utworzenie p�l odpowiadaj�cym kolumnom tabeli w bazie danych ->

    public static final String KEY_ID = "id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;

    public static final String KEY_NAME = "name";
    public static final String NAME_OPTIONS = "TEXT NOT NULL";
    public static final int NAME_COLUMN = 1;

    public static final String KEY_PHONE = "phone";
    public static final String PHONE_OPTIONS = "TEXT NOT NULL";
    public static final int PHONE_COLUMN = 2;

    public static final String KEY_EMAIL = "email";
    public static final String EMAIL_OPTIONS = "TEXT NOT NULL";
    public static final int EMAIL_COLUMN = 3;

    public static final String KEY_SELECTED = "selected";
    public static final String SELECTED_OPTIONS = "INTEGER DEFAULT 0";
    public static final int SELECTED_COLUMN = 4;

    /**
     * Konstruktor
     * */
    public DatabaseAdapter(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Tworzenie tabeli
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //tworzenie tabeli
        String DB_CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "( "
                + KEY_ID + " " + ID_OPTIONS + ", "
                + KEY_NAME + " " + NAME_OPTIONS + ", "
                + KEY_PHONE + " " + PHONE_OPTIONS + ", "
                + KEY_EMAIL + " " + EMAIL_OPTIONS + ", "
                + KEY_SELECTED + " " + SELECTED_OPTIONS
                + ");";

        db.execSQL(DB_CREATE_CONTACTS_TABLE);

        Log.d(DEBUG_TAG, "Database creating... Table " + TABLE_CONTACTS + " created" );
    }

    /**
     * Aktualizacja bazy danych
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_CONTACTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_CONTACTS;
        db.execSQL(DROP_CONTACTS_TABLE);                                               //usuwanie starej tabeli

        onCreate(db);                                                                  //utworzenie nowej tabeli

        Log.d("DEBUG_TAG", "Database updating...");
    }

    /**
     * Dodanie kontaktu do bazy danych
     * */
    public long insertContact(String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();                                 //otworzenie polaczenia z baza danych

        ContentValues newContactValues = new ContentValues();
        newContactValues.put(KEY_NAME, name);
        newContactValues.put(KEY_PHONE, phone);
        newContactValues.put(KEY_EMAIL, email);

        return db.insert(TABLE_CONTACTS, null, newContactValues);
    }

    /**
     * Aktualizacja kontaktu - selected - true / false
     * */
    public boolean updateContact(long id, String name, String phone, String email, boolean selected) {
        SQLiteDatabase db = this.getWritableDatabase();

        String where = KEY_ID + "=" + id;                                               //wybor wiersza na podstawie ID
        int selectedContact = selected ? 1 : 0;

        ContentValues updateContactValues = new ContentValues();
        updateContactValues.put(KEY_NAME, name);
        updateContactValues.put(KEY_PHONE, phone);
        updateContactValues.put(KEY_EMAIL, email);
        updateContactValues.put(KEY_SELECTED, selectedContact);

        return db.update(TABLE_CONTACTS, updateContactValues, where, null) > 0;
    }

    /**
     * Usuwanie kontaktu z bazy danych
     * */
    public boolean deleteContact(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String where = KEY_ID + "=" + id;
        return db.delete(TABLE_CONTACTS, where, null) > 0;
    }

    /**
     * Pobranie kontaktu z bazy danych
     * */
    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_SELECTED };
        return db.query(TABLE_CONTACTS, columns, null, null,null, null,null, null);
    }

}