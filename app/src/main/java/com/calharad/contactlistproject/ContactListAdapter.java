package com.calharad.contactlistproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactListAdapter extends ArrayAdapter<ContactsList> {
    private Activity context;
    private List<ContactsList> contactsList;


    /**
     * Konstruktor, jako jeden z argumentow przekazuje utworzony wyglad contact_list, zamiast obiektu kontekstu przekazywany jest obiekt aktywnosci
     * */
    public ContactListAdapter(Activity context, List<ContactsList> contactsList) {
        super(context, R.layout.contact_list, contactsList);
        this.context = context;
        this.contactsList = contactsList;
    }

    /**
     * Klasa statyczna przechowywujaca poszczegolne element UI
     * */
    static class ViewHolder {
        public TextView tvContactName;
        public TextView tvContactPhone;
        public TextView tvContactEmail;
        public ImageView status;
    }


    /**
     * Metoda zwracajaca gotowy element listy, zwraca obiekt View ktory znajduje sie na konkretnej pozycji listy
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        //wykorzystanie obiektu convertView( odzyskany obiekt widoku) do optymalizacji
        //adaptera, aby nie tworzyc nowego widoku dla kolejnych elementow listy, widok
        //tworzymy tylko gdy obiekt converView == null
        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.contact_list, null, true);

            //pobieranie elementow UI do ViewHoldera, jest to wzorzec, ktory ogranicza ilosc
            // wywolan metody findViewById do minimum(metoda findViewById uzywana jest tylko w
            // momencie tworzenia nowego widoku), poprawia wydajnosc aplikacji
            viewHolder = new ViewHolder();
            viewHolder.tvContactName = rowView.findViewById(R.id.tvContactName);
            viewHolder.tvContactPhone = rowView.findViewById(R.id.tvContactPhone);
            viewHolder.tvContactEmail = rowView.findViewById(R.id.tvContactEmail);
            viewHolder.status = rowView.findViewById(R.id.status);

            rowView.setTag(viewHolder);         //przekazanie obiektu do widoku
        } else {
            viewHolder = (ViewHolder) rowView.getTag();         //odzyskiwanie obiektu widoku, przez pobranie "zalacznika" w postaci ViewHoldera
        }

        //operacje na elementach UI na ktore wskazuje ViewHolder przypisanie danych do pol tekstowych
        ContactsList contact = contactsList.get(position);
        viewHolder.tvContactName.setText(contact.getName());
        viewHolder.tvContactPhone.setText(contact.getPhone());
        viewHolder.tvContactEmail.setText(contact.getEmail());

        //zmiana widoku w zaleznosci od zaznaczenia elementu
        if(contact.isSelected()) {
            viewHolder.status.setVisibility(View.VISIBLE);
        } else {
            viewHolder.status.setVisibility(View.GONE);
        }

        return rowView;
    }
}