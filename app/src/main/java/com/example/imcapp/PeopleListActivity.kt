package com.example.imcapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class PeopleListActivity : AppCompatActivity() {

    private lateinit var lvPersons: ListView
    private lateinit var btnBack: Button
    private lateinit var dbHelper: PersonDatabaseHelper
    private var personList: List<Person> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people_list)

        lvPersons = findViewById(R.id.lv_persons)
        btnBack = findViewById(R.id.btn_back_list)
        dbHelper = PersonDatabaseHelper(this)

        btnBack.setOnClickListener {
            finish()
        }

        loadPersons()
    }

    private fun loadPersons() {
        personList = dbHelper.getAllPersons()

        val names = personList.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        lvPersons.adapter = adapter

        lvPersons.setOnItemClickListener { _, _, position, _ ->
            val person = personList[position]
            val intent = Intent(this, IMCResultActivity::class.java)
            intent.putExtra("person_id", person.id)
            startActivity(intent)
        }
    }
}
