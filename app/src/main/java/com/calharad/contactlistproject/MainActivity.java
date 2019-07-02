package com.calharad.contactlistproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Glowna aktywnosc
 * */
public class MainActivity extends Activity implements AdapterView.OnItemLongClickListener {

    public final static String[] ACTION_LIST = {"Send SMS", "Call", "Send email"};

    private Button btnClearSelected;
    private Button btnSave;
    private EditText etName;
    private EditText etPhone;
    private EditText etEmail;
    private ListView lvContacts;


    private DatabaseAdapter dbAdapter;
    private Cursor contactCursor;
    private List<ContactsList> contactsList;
    private ContactListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //widok
        btnClearSelected = findViewById(R.id.btnClearSelected);
        btnSave = findViewById(R.id.btnSave);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        lvContacts = findViewById(R.id.lvContacts);


        initListView();                         //tworzenie listy
        buttonsOnClickListeners();              //obsluga klikniecia przyciskow

    }

    /**
     * Inicjalizacja bazy danych i adaptera listy oraz pobranie kontaktow
     * */
    private void initListView() {
        dbAdapter = new DatabaseAdapter(getApplicationContext());
        contactsList = new ArrayList<>();

        getAll();                                                   // pobranie danych do obiektu Cursor

        listAdapter = new ContactListAdapter(this, contactsList);	// pobieranie obiektu Cursor naszych danych.
        lvContacts.setAdapter(listAdapter);                         //utworzenie nowego obiektu ArrayAdapter,
        initListViewOnItemClick();                                  //obsluga klikniecia listy
    }


    /**
     * Pobranie wszystkich rekordow (kontaktow)
     * */
    private void getAll() {
        contactCursor = dbAdapter.getAllContacts();

        if(contactCursor != null && contactCursor.moveToFirst()) {
            do {
                long id = contactCursor.getLong(DatabaseAdapter.ID_COLUMN);
                String name = contactCursor.getString(DatabaseAdapter.NAME_COLUMN);
                String phone = contactCursor.getString(DatabaseAdapter.PHONE_COLUMN);
                String email = contactCursor.getString(DatabaseAdapter.EMAIL_COLUMN);
                boolean selected = contactCursor.getInt(DatabaseAdapter.SELECTED_COLUMN) > 0;
                contactsList.add(new ContactsList(id, name, phone, email, selected));
            } while(contactCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        if(dbAdapter != null)
            dbAdapter.close();  //zamkniecie bazy
        super.onDestroy();
    }

    /**
     * Obsluga klikniecia w element listy - aktualizacja pola selected
     * */
    private void initListViewOnItemClick() {
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ContactsList contact = contactsList.get(position);
                if(contact.isSelected())
                    dbAdapter.updateContact(contact.getId(), contact.getName(), contact.getPhone(), contact.getEmail(), false);
                else
                    dbAdapter.updateContact(contact.getId(), contact.getName(), contact.getPhone(), contact.getEmail(), true);

                updateListViewData();
            }
        });
        lvContacts.setOnItemLongClickListener(this);
    }


    /**
     * Obsluga przyciskow "Add Contact" i "Delete selected"
     * */
    private void buttonsOnClickListeners() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSave:
                        saveNewContact();   //dodanie kontaktu
                        break;
                    case R.id.btnClearSelected:
                        deleteContact();    //usuniecie kontaktu
                        break;

                    default:
                        break;
                }
            }
        };

        btnClearSelected.setOnClickListener(onClickListener);
        btnSave.setOnClickListener(onClickListener);
    }


    /**
     * Pobranie danych z pol EditText i zapisanie w bazie danych
     * */
    private void saveNewContact(){
        String contactName = etName.getText().toString();
        String contactPhone = etPhone.getText().toString();
        String contactEmail = etEmail.getText().toString();
        if(contactName.equals("")){
            etName.setError("Name couldn't be empty string.");
        }
        else if(contactPhone.equals("")){
            etPhone.setError("Phone couldn't be empty string.");
        }
        else if(contactEmail.equals("")){
            etEmail.setError("Email couldn't be empty string.");
        }
        else {
            dbAdapter.insertContact(contactName, contactPhone, contactEmail);
            etName.setText("");
            etPhone.setText("");
            etEmail.setText("");

        }
        updateListViewData();
    }


    /**
     * Usuniecie koktaktu jesli jest zaznaczony
     * */
    private void deleteContact(){
        if(contactCursor != null && contactCursor.moveToFirst()) {
            do {
                if(contactCursor.getInt(DatabaseAdapter.SELECTED_COLUMN) == 1) {
                    long id = contactCursor.getLong(DatabaseAdapter.ID_COLUMN);
                    dbAdapter.deleteContact(id);
                }
            } while (contactCursor.moveToNext());
        }
        updateListViewData();
    }

    /**
     * Aktualizacja listy
     * */
    private void updateListViewData() {
        contactsList.clear();
        getAll();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Action:");
        builder.setItems(ACTION_LIST, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContactsList el = contactsList.get(position);
                switch(ACTION_LIST[which]) {
                    case "Send SMS":
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.fromParts("sms", el.getPhone(), null)));
                        break;
                    case "Call":
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("tel:" + el.getPhone())));
                        break;
                    case "Send email":
                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + el.getEmail())));
                        break;
                }
            }
        });
        builder.show();
        return false;
    }
}