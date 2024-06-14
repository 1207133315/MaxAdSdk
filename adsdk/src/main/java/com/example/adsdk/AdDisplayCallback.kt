package com.example.adsdk

import androidx.annotation.Keep

@Keep
interface AdDisplayCallback {
    fun onAdDisplayed()
    fun onAdDisplayFailed()
    fun onAdClick()
    fun onAdClose()
    fun onUserRewarded()
}