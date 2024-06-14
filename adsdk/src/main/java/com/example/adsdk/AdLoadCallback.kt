package com.example.adsdk

import androidx.annotation.Keep

@Keep
interface AdLoadCallback {
    fun onAdsFinishLoading(isLoadSuccess:Boolean)
}