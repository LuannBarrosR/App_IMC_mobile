package com.example.imcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var dbHelper: PersonDatabaseHelper
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText
    private lateinit var btnSave: Button
    private lateinit var btnGoList: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        dbHelper = PersonDatabaseHelper(this)

        etName = findViewById(R.id.et_name)
        etAge = findViewById(R.id.et_age)
        etWeight = findViewById(R.id.et_weight)
        etHeight = findViewById(R.id.et_height)
        btnSave = findViewById(R.id.btn_save)
        btnGoList = findViewById(R.id.btn_go_list)

        btnSave.setOnClickListener {
            savePerson()
        }

        btnGoList.setOnClickListener {
            startActivity(Intent(this, PeopleListActivity::class.java))
        }
    }

    private fun savePerson() {
        val name = etName.text.toString().trim()
        val ageStr = etAge.text.toString().trim()
        val weightStr = etWeight.text.toString().trim()
        val heightStr = etHeight.text.toString().trim()

        if (name.isEmpty() || ageStr.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val age = ageStr.toIntOrNull()
        val weight = weightStr.toFloatOrNull()
        val height = heightStr.toFloatOrNull()

        if (age == null || weight == null || height == null) {
            Toast.makeText(this, "Por favor, insira valores válidos.", Toast.LENGTH_SHORT).show()
            return
        }

        val person = Person(
            name = name,
            age = age,
            weight = weight,
            height = height
        )

        val id = dbHelper.addPerson(person).toInt()
        if (id > 0) {
            Toast.makeText(this, "Pessoa salva!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, IMCResultActivity::class.java)
            intent.putExtra("person_id", id)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Erro ao salvar pessoa.", Toast.LENGTH_SHORT).show()
        }
    }
}
