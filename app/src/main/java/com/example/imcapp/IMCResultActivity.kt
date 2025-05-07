package com.example.imcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class IMCResultActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvIMCValue: TextView
    private lateinit var tvIMCStatus: TextView
    private lateinit var tvIMCAdvice: TextView
    private lateinit var ivIMCImage: ImageView
    private lateinit var btnBack: Button
    private lateinit var btnDelete: Button

    private lateinit var dbHelper: PersonDatabaseHelper
    private var currentPersonId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc_result)

        tvName = findViewById(R.id.tv_person_name)
        tvIMCValue = findViewById(R.id.tv_imc_value)
        tvIMCStatus = findViewById(R.id.tv_imc_status)
        tvIMCAdvice = findViewById(R.id.tv_imc_advice)
        ivIMCImage = findViewById(R.id.iv_imc_image)
        btnBack = findViewById(R.id.btn_back)
        btnDelete = findViewById(R.id.btn_delete)

        dbHelper = PersonDatabaseHelper(this)

        currentPersonId = intent.getIntExtra("person_id", -1)
        if (currentPersonId == -1) {
            Toast.makeText(this, "Pessoa não encontrada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnDelete.setOnClickListener {
            deletePerson()
        }

        val person = dbHelper.getPerson(currentPersonId)
        if (person != null) {
            displayIMC(person)
        } else {
            Toast.makeText(this, "Pessoa não encontrada", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun displayIMC(person: Person) {
        tvName.text = person.name
        val imc = person.weight / (person.height * person.height)
        val imcRounded = String.format("%.2f", imc)
        tvIMCValue.text = "IMC: $imcRounded"

        val (statusText, adviceText, imageRes) = when {
            imc < 18.5 -> Triple(
                "Abaixo do peso",
                "Procure um médico. Algumas pessoas têm um baixo peso por características do seu organismo e tudo bem. Outras podem estar enfrentando problemas, como a desnutrição. É preciso saber qual é o caso.",
                R.drawable.img_abaixopeso
            )
            imc in 18.6..24.9 -> Triple(
                "Peso Normal",
                "Que bom que você está com o peso normal! E o melhor jeito de continuar assim é mantendo um estilo de vida ativo e uma alimentação equilibrada.",
                R.drawable.img_peso_normal
            )
            imc in 25.0..29.9 -> Triple(
                "Sobrepeso",
                "Ele é, na verdade, uma pré-obesidade e muitas pessoas nessa faixa já apresentam doenças associadas, como diabetes e hipertensão. Importante rever hábitos e buscar ajuda antes de, por uma série de fatores, entrar na faixa da obesidade pra valer.",
                R.drawable.img_sobre_peso
            )
            imc in 30.0..34.9 -> Triple(
                "Obesidade I",
                "Sinal de alerta! Chegou na hora de se cuidar, mesmo que seus exames sejam normais. Vamos dar início a mudanças hoje! Cuide de sua alimentação. Você precisa iniciar um acompanhamento com nutricionista e/ou endocrinologista.",
                R.drawable.img_obesidade_grau_1
            )
            imc in 35.0..39.9 -> Triple(
                "Obesidade II",
                "Mesmo que seus exames aparentem estar normais, é hora de se cuidar, iniciando mudanças no estilo de vida com o acompanhamento próximo de profissionais de saúde.",
                R.drawable.img_obesidade_grau_2
            )
            else -> Triple(
                "Obesidade III",
                "Aqui o sinal é vermelho, com forte probabilidade de já existirem doenças muito graves associadas. O tratamento deve ser ainda mais urgente.",
                R.drawable.img_obesidade_grau_3
            )
        }

        tvIMCStatus.text = statusText
        tvIMCAdvice.text = adviceText
        ivIMCImage.setImageResource(imageRes)
    }

    private fun deletePerson() {
        val deletedCount = dbHelper.deletePerson(currentPersonId)
        if (deletedCount > 0) {
            Toast.makeText(this, "Pessoa excluída com sucesso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PeopleListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Erro ao excluir pessoa", Toast.LENGTH_SHORT).show()
        }
    }
}