package com.example.agenda;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivityContact extends AppCompatActivity {

    private long idSeleccionat = -1;
    private ContactsDB helper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adaptador;
    private EditText nom, cognom, direccion, telefono, email, fechaNac;
    private TextView estat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_contact);

        ListView llista = findViewById(R.id.llista);

        nom = findViewById(R.id.nombre);
        cognom = findViewById(R.id.apellido);
        telefono = findViewById(R.id.telefono);
        direccion = findViewById(R.id.direccion);
        email = findViewById(R.id.email);
        fechaNac = findViewById(R.id.fechaNac);

        estat = findViewById(R.id.estat);

        helper = new ContactsDB(this);
        db = helper.getWritableDatabase();

        adaptador = new SimpleCursorAdapter(this,
                R.layout.activity_second_contact,
                consultar(),
                new String[]{"nom", "cognom", "direccion", "telefono", "email", "fechaNacimiento"},
                new int[]{R.id.nombre, R.id.apellido, R.id.direccion, R.id.telefono, R.id.email, R.id.fechaNac},
                0);

        llista.setAdapter(adaptador);

        llista.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            idSeleccionat = id;
            nom.setText(cursor.getString(cursor.getColumnIndexOrThrow("nom")));
            cognom.setText(cursor.getString(cursor.getColumnIndexOrThrow("cognom")));
            direccion.setText(cursor.getString(cursor.getColumnIndexOrThrow("direccion")));
            telefono.setText(cursor.getString(cursor.getColumnIndexOrThrow("telefono")));
            email.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            fechaNac.setText(cursor.getString(cursor.getColumnIndexOrThrow("fechaNacimiento")));

            estat.setText("Editando #" + id);
        });

        findViewById(R.id.añadir).setOnClickListener(v -> inserir());
        findViewById(R.id.modificar).setOnClickListener(v -> modificar());
        findViewById(R.id.borrar).setOnClickListener(v -> eliminar());
        findViewById(R.id.limpiar).setOnClickListener(v -> netejar());
    }
    private ContentValues llegirCamps() {
        String n = nom.getText().toString().trim();
        String c = cognom.getText().toString().trim();
        String d = direccion.getText().toString().trim();
        String t = telefono.getText().toString().trim();
        String e = email.getText().toString().trim();
        String f = fechaNac.getText().toString().trim();
        if (n.isEmpty() || c.isEmpty() || d.isEmpty() || t.isEmpty() || e.isEmpty() || f.isEmpty() ) {
            missatge("Completa todos los campos");
            return null;
        }
        ContentValues valors = new ContentValues();
        valors.put("nom", n);
        valors.put("cognom", c);
        valors.put("direccion", d);
        valors.put("telefono", t);
        valors.put("email", e);
        valors.put("fechaNacimiento", f);
        return valors;
    }

    private Cursor consultar() {
        return db.rawQuery("SELECT _id, nom, cognom, direccion, telefono, email, fechaNacimiento FROM contactos ORDER BY _id", null);
    }

    private void inserir() {
        ContentValues valors = llegirCamps();
        if (valors == null) return;
        long id = db.insert("contactos", null, valors);
        if (id == -1) {
            missatge("No s'ha pogut afegir el contacte");
            return;
        }
        actualitzarLlista();
    }

    private void modificar() {
        if (idSeleccionat == -1) {
            missatge("Selecciona un contacte");
            return;
        }
        ContentValues valors = llegirCamps();
        if (valors == null) return;
        int files = db.update("contactos", valors, "_id = ?",
                new String[]{String.valueOf(idSeleccionat)});
        missatge("Files modificades: " + files);
        actualitzarLlista();
    }

    private void eliminar() {
        if (idSeleccionat == -1) {
            missatge("Selecciona un contacto");
            return;
        }
        long id = idSeleccionat;

        new AlertDialog.Builder(this)
                .setTitle("Eliminar contacto")
                .setMessage("Quieres eliminar el registro #" + id + "?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    db.delete("contactos", "_id = ?",
                            new String[]{String.valueOf(id)});
                    actualitzarLlista();
                }).show();
    }

    private void actualitzarLlista() {
        adaptador.changeCursor(consultar());
        netejar();
    }

    private void netejar() {
        idSeleccionat = -1;
        nom.setText("");
        cognom.setText("");
        direccion.setText("");
        telefono.setText("");
        email.setText("");
        fechaNac.setText("");
        estat.setText("Nuevo contacto");
    }

    private void missatge(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("id", idSeleccionat);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (adaptador != null) adaptador.changeCursor(null);
        if (helper != null) helper.close();
        super.onDestroy();
    }
}
