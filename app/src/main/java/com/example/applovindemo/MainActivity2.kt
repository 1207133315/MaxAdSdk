package com.example.applovindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.ai.protecthealth.R
import com.example.adsdk.AdDisplayCallback
import com.example.adsdk.AdType
import com.example.adsdk.Adads

class MainActivity2 : AppCompatActivity() {
    var TAG="MainActivity2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        findViewById<Button>(R.id.showAdButton).setOnClickListener {

            Adads.showAds(this,AdType.REWARD, adDisplayCallback = object :AdDisplayCallback{
                override fun onAdClick() {
                    Log.d(TAG, "onAdClick: 广告点击")
                }

                override fun onAdClose() {
                    Log.d(TAG, "onAdClose: 广告关闭")
                }

                override fun onAdDisplayFailed() {
                    Log.d(TAG, "onAdDisplayFailed: 广告展示失败")
                }

                override fun onAdDisplayed() {
                    Log.d(TAG, "onAdDisplayed: 广告展示")
                }

                override fun onUserRewarded() {
                    Log.d(TAG, "onAdDisplayFailed: 获取到奖励")
                }
            })
        }

    }
}