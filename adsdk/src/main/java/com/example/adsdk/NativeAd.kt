package com.example.adsdk

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView

class NativeAd(var context: Context, var adId: String, var adView: FrameLayout? = null) :
    MaxNativeAdListener() {
    private val TAG = "Adads"

    private var maxNativeAdView: MaxNativeAdView? = null
    private var adLoadCallback: AdLoadCallback? = null
    private var adDisplayCallback: AdDisplayCallback? = null
    var maxAd: MaxAd? = null
    var maxNativeAd: MaxNativeAdLoader = MaxNativeAdLoader(adId, context).apply {
        setNativeAdListener(this@NativeAd)
    }

    override fun onNativeAdLoaded(maxNativeAdView: MaxNativeAdView?, maxAd: MaxAd) {
        super.onNativeAdLoaded(maxNativeAdView, maxAd)
        Log.d(TAG, "onAdHidden: 广告加载成功")
        if (this.maxNativeAdView != null) {
            this.maxNativeAdView = null
        }
        if (this.maxAd != null) this.maxAd = null
        this.maxAd = maxAd
        this.maxNativeAdView = maxNativeAdView
        Adads.nativeAdAdList.add(this)
        adLoadCallback?.onAdsFinishLoading(true)
    }

    override fun onNativeAdLoadFailed(adId: String, maxError: MaxError) {
        super.onNativeAdLoadFailed(adId, maxError)
        Log.d(TAG, "onAdHidden: 广告加载失败:${maxError.message}")
        adLoadCallback?.onAdsFinishLoading(false)
        destroy()
    }

    override fun onNativeAdClicked(maxAd: MaxAd) {
        super.onNativeAdClicked(maxAd)
        Log.d(TAG, "onAdHidden: 广告点击")
        if (this.maxAd != null) this.maxAd = null
        this.maxAd = maxAd
        adDisplayCallback?.onAdClick()
    }

    override fun onNativeAdExpired(maxAd: MaxAd) {
        super.onNativeAdExpired(maxAd)
        if (this.maxAd != null) this.maxAd = null
        this.maxAd = maxAd
        Adads.nativeAdAdList.remove(this)
        destroy()
    }


    fun load(
        adLoadCallback: AdLoadCallback?
    ) {
        this.adLoadCallback = adLoadCallback
        // Load the first ad
        maxNativeAd?.loadAd()

    }

    fun show(
        adDisplayCallback: AdDisplayCallback?,placement:String=""
    ) {
        this.maxNativeAd?.setNativeAdListener(this)
        this.adDisplayCallback = adDisplayCallback
        try {
            maxNativeAd?.placement=placement
            adView?.visibility=View.VISIBLE
            adView?.removeAllViews()
            adView?.addView(maxNativeAdView)
            Adads.nativeAdAdList.remove(this)
            this.adDisplayCallback?.onAdDisplayed()
            destroy()
        } catch (e: Exception) {
            e.printStackTrace()
            this.adDisplayCallback?.onAdDisplayFailed()
            destroy()
        }
    }


    fun destroy() {
        adDisplayCallback = null
        adLoadCallback = null
        maxNativeAd?.destroy()
    }


}