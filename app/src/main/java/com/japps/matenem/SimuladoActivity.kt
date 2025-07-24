package com.japps.matenem

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.japps.matenem.databinding.ActivitySimuladoBinding
import com.japps.matenem.entities.Question
import com.japps.matenem.questions.LoadQuestions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class SimuladoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySimuladoBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimuladoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var acertos = 0
        var erros = 0
        var totalQuestoes = intent.getIntExtra("questions_n", 5)

        CoroutineScope(Dispatchers.IO).launch {
            val loadQuestions: LoadQuestions = LoadQuestions()
            val questions: List<Question> = loadQuestions.load(totalQuestoes)
            totalQuestoes = questions.size
            questions.forEach { question ->
                val questionView: View = layoutInflater.inflate(R.layout.question_layout, null)
                val comando: TextView = questionView.findViewById<TextView>(R.id.comandoTextView)
                val radioGroup: RadioGroup =
                    questionView.findViewById<RadioGroup>(R.id.questionsRadioGroup)
                val a: RadioButton = questionView.findViewById<RadioButton>(R.id.aRadio)
                val b: RadioButton = questionView.findViewById<RadioButton>(R.id.bRadio)
                val c: RadioButton = questionView.findViewById<RadioButton>(R.id.cRadio)
                val d: RadioButton = questionView.findViewById<RadioButton>(R.id.dRadio)
                val e: RadioButton = questionView.findViewById<RadioButton>(R.id.eRadio)
                val gabaritoValue: TextView = questionView.findViewById<TextView>(R.id.gabaritoValue)
                val corrigirButton: Button = questionView.findViewById<Button>(R.id.corrigirButton)

                comando.text = question.comando
                a.text = question.a
                b.text = question.b
                c.text = question.c
                d.text = question.d
                e.text = question.e

                corrigirButton.setOnClickListener {
                    val checkedId: Int = radioGroup.checkedRadioButtonId
                    val checkedRadio: RadioButton = questionView.findViewById<RadioButton>(checkedId)
                    val radioTextContent = checkedRadio.text
                    if (radioTextContent[0].toString() == question.gabarito) {
                        Toast.makeText(applicationContext, "Parabéns, acertou", Toast.LENGTH_SHORT)
                            .show()
                        acertos++
                    } else {
                        Toast.makeText(applicationContext, "Você errou", Toast.LENGTH_SHORT).show()
                        erros++
                    }
                    corrigirButton.isEnabled = false
                    corrigirButton.isClickable = false
                    corrigirButton.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.disabled)
                    gabaritoValue.text = question.gabarito.uppercase(Locale.getDefault())
                }
                CoroutineScope(Dispatchers.Main).launch{
                    binding.questionScrollView.addView(questionView)
                }
            }
        }

        binding.finalizarButton.setOnClickListener {
            val resultadoIntent: Intent = Intent(applicationContext, ResultadoActivity::class.java)
            resultadoIntent.putExtra("acertos", acertos)
            resultadoIntent.putExtra("erros", erros)
            resultadoIntent.putExtra("total_questoes", totalQuestoes)
            startActivity(resultadoIntent)
        }
        binding.simuladoBackButton.setOnClickListener {
            finish()
        }
    }
}