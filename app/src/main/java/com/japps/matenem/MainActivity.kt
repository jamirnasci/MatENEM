package com.japps.matenem

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowMetrics
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.japps.matenem.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val BANNER_ID = "ca-app-pub-2077187211919243/7859551012"
    private lateinit var adView: AdView
    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@MainActivity) {}
        }

        val adView = AdView(this)
        adView.adUnitId = BANNER_ID
        val adWidth: Int = (binding.adViewContainer.width / resources.displayMetrics.density).toInt()
        adView.setAdSize(adSize)
        this.adView = adView

        binding.adViewContainer.removeAllViews()
        binding.adViewContainer.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        if(sharedPreferences.contains("last_note")){
            val lastNote: Float = sharedPreferences.getFloat("last_note", 0f)
            binding.lastNoteTextView.text = "Último desempenho: " + String.format("%.2f%%", lastNote)
        }else{
            binding.lastNoteCardView.visibility = View.INVISIBLE
        }

        binding.questionsNumberSeekBar.setOnSeekBarChangeListener(
            object : OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    binding.questionsNumber.setText(binding.questionsNumberSeekBar.progress.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            }
        )
        binding.simuladoButton.setOnClickListener {
            val simuladoIntent: Intent = Intent(applicationContext, SimuladoActivity::class.java)
            val questionsN: Int = Integer.parseInt(binding.questionsNumber.text.toString())
            if(questionsN <= 0){
                Toast.makeText(applicationContext, "Escolha a quantidade de questões", Toast.LENGTH_SHORT).show()
            }else{
                simuladoIntent.putExtra("questions_n", questionsN)
                startActivity(simuladoIntent)
            }
        }
        binding.meuDesempenhoButton.setOnClickListener {
            val desempenhoIntent: Intent = Intent(applicationContext, DesempenhoActivity::class.java)
            startActivity(desempenhoIntent)
        }
    }
    private val adSize: AdSize
        get() {
            val displayMetrics = resources.displayMetrics
            val adWidthPixels =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
                    windowMetrics.bounds.width()
                } else {
                    displayMetrics.widthPixels
                }
            val density = displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }
}