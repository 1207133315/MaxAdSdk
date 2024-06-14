package com.example.adsdk

import android.app.Activity
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd

internal class InterstitialAd(var activityContext: Activity, var adId: String) : MaxAdListener {
    private val TAG = "Adads"
    private var adLoadCallback: AdLoadCallback? = null
    private var adDisplayCallback: AdDisplayCallback? = null
    private var createTime = System.currentTimeMillis()
    var maxAd:MaxAd?=null
    var maxInterstitialAd: MaxInterstitialAd? =
        MaxInterstitialAd(adId, activityContext).apply {
            setListener(this@InterstitialAd)
        }


    override fun onAdLoaded(maxAd: MaxAd) {
        Log.d(TAG, "onAdHidden: 广告加载成功")
        this.maxAd=maxAd
        createTime = System.currentTimeMillis()
        adLoadCallback?.onAdsFinishLoading(true)
        Adads.interstitialAdList.add(this)
    }

    override fun onAdLoadFailed(adId: String, maxError: MaxError) {
        Log.d(TAG, "onAdHidden: 广告加载失败:${maxError.message}")
        adLoadCallback?.onAdsFinishLoading(false)

    }

    override fun onAdDisplayed(maxAd: MaxAd) {
        Log.d(TAG, "onAdHidden: 广告展示")
        this.maxAd=maxAd
        Adads.interstitialAdList.remove(this)
        adDisplayCallback?.onAdDisplayed()
    }

    override fun onAdHidden(maxAd: MaxAd) {
        this.maxAd=maxAd
        Log.d(TAG, "onAdHidden: 广告关闭")
        adDisplayCallback?.onAdClose()
        destroy()

    }

    override fun onAdClicked(maxAd: MaxAd) {
        this.maxAd=maxAd
        Log.d(TAG, "onAdHidden: 广告点击")
        adDisplayCallback?.onAdClick()
    }


    override fun onAdDisplayFailed(maxAd: MaxAd, maxError: MaxError) {
        this.maxAd=maxAd
        Log.d(TAG, "onAdHidden: 广告展示失败:${maxError.message}")
        adDisplayCallback?.onAdDisplayFailed()
        destroy()
    }


    fun load(
        adLoadCallback: AdLoadCallback?
    ) {
        this.adLoadCallback = adLoadCallback
        // Load the first ad
        maxInterstitialAd?.loadAd()
    }


    fun show(
        adDisplayCallback: AdDisplayCallback?,
        placement:String
    ) {
        this.maxInterstitialAd?.setListener(this)
        this.adDisplayCallback = adDisplayCallback
        if (maxInterstitialAd!!.isReady) {
            Log.d(TAG, "show: 展示广告")
            maxInterstitialAd?.showAd(placement)
        } else {
            adDisplayCallback?.onAdDisplayFailed()
        }
    }

    fun isExpired(): Boolean {
        return System.currentTimeMillis() - createTime > 1000 * 60 * 60 * 4
    }


    fun destroy() {
        adLoadCallback = null
        adDisplayCallback = null
        createTime=0
        maxInterstitialAd?.destroy()
    }
}