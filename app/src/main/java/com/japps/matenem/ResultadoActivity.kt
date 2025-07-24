package com.japps.matenem

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.japps.matenem.databinding.ActivityResultadoBinding
import com.japps.matenem.db.DataBase
import com.japps.matenem.entities.Desempenho
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResultadoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultadoBinding
    private val INTERSTITIAL_ID = "ca-app-pub-2077187211919243/5907318866"
    private var mInterstitialAd: InterstitialAd? = null
    private final val TAG = "ResultadoActivity"
    private lateinit var dataBase: DataBase

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CommitPrefEdits", "SetTextI18n", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadoBinding.inflate(layoutInflater)
        dataBase = DataBase(applicationContext)
        setContentView(binding.root)

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, INTERSTITIAL_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        Log.d(TAG, "Ad dismissed fullscreen content.")
                        mInterstitialAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        // Called when ad fails to show.
                        Log.e(TAG, "Ad failed to show fullscreen content.")
                        mInterstitialAd = null
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Ad showed fullscreen content.")
                    }
                }
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(this@ResultadoActivity)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
            }
        })

        val acertos = intent.getIntExtra("acertos", 0)
        val erros = intent.getIntExtra("erros", 0)
        val totalQuestoes = intent.getIntExtra("total_questoes", 0)
        val desempenho: Float = (acertos * 100.00f) / totalQuestoes

        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putFloat("last_note", desempenho)
        editor.apply()

        binding.acertosValue.text = acertos.toString()
        binding.errosValue.text = erros.toString()
        binding.totalQuestoesValue.text = totalQuestoes.toString()
        binding.desempenhoValue.text = String.format("%.2f%%", desempenho)
        //volta para o menu de simulado
        binding.backMenuButton.setOnClickListener {
            val data: LocalDateTime = LocalDateTime.now()
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            val formatedDate: String = formatter.format(data)
            val newDesempenho: Desempenho = Desempenho(null, formatedDate, totalQuestoes, desempenho)
            dataBase.insertDesempenho(newDesempenho)
            val backMenuIntent: Intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(backMenuIntent)
        }
        //volta para as questoes
        binding.resultadoBackButton.setOnClickListener {
            finish()
        }
    }
}