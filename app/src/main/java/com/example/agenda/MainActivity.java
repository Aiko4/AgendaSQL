package com.example.agenda;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> nombresCont = new ArrayList<>();
    ArrayList<Contacto> contactos = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Contacto c1 = new Contacto("Marc", "Garcia", "Carrer Pi i Margall, 16", "612345678",  "marc.garcia@gmail.com", "2002-05-14");
        Contacto c2 = new Contacto("Laura", "Martinez", "Carrer de la Indústria, 25", "623456789", "laura.martinez@gmail.com", "2001-09-23");
        Contacto c3 = new Contacto("Jordi", "Sanchez", "Carrer de les Tres Creus, 5", "634567890", "jordi.sanchez@gmail.com", "1998-12-02");
        contactos.add(c1);
        contactos.add(c2);
        contactos.add(c3);

        ListView lvContacts = findViewById(R.id.list);
        Button btnAfegir = findViewById(R.id.btnAfegir);

        for (Contacto c: contactos) {
            nombresCont.add(c.getNom() + " " + c.getCognom());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresCont);
        lvContacts.setAdapter(adapter);

        lvContacts.setOnItemClickListener((parent, view, position, id) -> {
            Contacto c = contactos.get(position);
            Intent intent = new Intent(this, SecondActivityContact.class);

            intent.putExtra("posicio", position);
            intent.putExtra("nombre", c.getNom());
            intent.putExtra("apellido", c.getCognom());
            intent.putExtra("telefono", c.getTelefono());
            intent.putExtra("direccion", c.getDireccion());
            intent.putExtra("email", c.getEmail());

            startActivityForResult(intent, 1);
        });

        btnAfegir.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivityContact.class);
            intent.putExtra("posicio", -1);
            startActivityForResult(intent, 1);
        });
    }
}