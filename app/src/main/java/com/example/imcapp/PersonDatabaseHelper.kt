package com.example.imcapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Person(
    var id: Int = 0,
    val name: String,
    val age: Int,
    val weight: Float,
    val height: Float
)

class PersonDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "imc_database.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_PERSONS = "persons"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_HEIGHT = "height"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PERSONS_TABLE = ("CREATE TABLE $TABLE_PERSONS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT NOT NULL, "
                + "$COLUMN_AGE INTEGER NOT NULL, "
                + "$COLUMN_WEIGHT REAL NOT NULL, "
                + "$COLUMN_HEIGHT REAL NOT NULL)")
        db.execSQL(CREATE_PERSONS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PERSONS")
        onCreate(db)
    }

    fun addPerson(person: Person): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, person.name)
            put(COLUMN_AGE, person.age)
            put(COLUMN_WEIGHT, person.weight)
            put(COLUMN_HEIGHT, person.height)
        }
        val id = db.insert(TABLE_PERSONS, null, values)
        db.close()
        return id
    }

    fun getPerson(id: Int): Person? {
        val db = readableDatabase
        var person: Person? = null
        val cursor: Cursor = db.query(
            TABLE_PERSONS,
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_AGE, COLUMN_WEIGHT, COLUMN_HEIGHT),
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        if (cursor.moveToFirst()) {
            person = Person(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                weight = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                height = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT))
            )
        }
        cursor.close()
        db.close()
        return person
    }

    fun getAllPersons(): List<Person> {
        val personList = mutableListOf<Person>()
        val selectQuery = "SELECT * FROM $TABLE_PERSONS"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val person = Person(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                    weight = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                    height = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT))
                )
                personList.add(person)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return personList
    }

    fun deletePerson(id: Int): Int {
        val db = writableDatabase
        val deletedRows = db.delete(TABLE_PERSONS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return deletedRows
    }
}

