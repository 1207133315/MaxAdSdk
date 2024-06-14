package com.example.applovindemo

import android.app.Application
import android.util.Log
import com.example.adsdk.Adads



class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        Adads.init(this,mutableListOf<String>("e636f013-8b9f-4d3c-857b-ddc38a47cd40")){
            Log.d("AppLovinManager", "onCreate: SDK初始化成功")
        }
    }
}