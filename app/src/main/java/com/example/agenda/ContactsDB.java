package com.example.agenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDB extends SQLiteOpenHelper {
    public ContactsDB(Context context)  {
        super(context, "videojocs.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Contactos (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT NOT NULL, " +
                "cognom TEXT NOT NULL, " +
                "direccion TEXT NOT NULL, " +
                "telefono TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "fechaNacimiento TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException("Migració pendent");
    }
}
