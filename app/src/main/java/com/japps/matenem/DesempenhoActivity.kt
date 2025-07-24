package com.japps.matenem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.japps.matenem.databinding.ActivityDesempenhoBinding
import com.japps.matenem.db.DataBase
import com.japps.matenem.entities.Desempenho

class DesempenhoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDesempenhoBinding
    private lateinit var dataBase: DataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDesempenhoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = DataBase(applicationContext)
        val lista: List<Desempenho> = dataBase.listDesempenho()

        binding.desempenhoRecycler.adapter = DesempenhoAdapter(lista, this)
        binding.desempenhoRecycler.layoutManager = LinearLayoutManager(this)

        binding.desempenhoBackButton.setOnClickListener {
            finish()
        }
    }
}