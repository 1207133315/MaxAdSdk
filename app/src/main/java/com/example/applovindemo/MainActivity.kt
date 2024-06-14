package com.example.applovindemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.ai.protecthealth.R
import com.example.adsdk.AdLoadCallback
import com.example.adsdk.AdType
import com.example.adsdk.Adads

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            findViewById<TextView>(R.id.showAd).setOnClickListener {
                startActivity(Intent(this,MainActivity2::class.java))
            }
      //  Handler().postDelayed({

        //},2000)


    }

    override fun onResume() {
        super.onResume()
        Adads.loadAds(this,AdType.REWARD,object :AdLoadCallback{
            override fun onAdsFinishLoading(isLoadSuccess: Boolean) {
                Log.d("Adads", "onAdsFinishLoading: 加载结束:${isLoadSuccess}")
            }
        })
    }
}