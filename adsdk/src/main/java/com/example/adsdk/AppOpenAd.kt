package com.example.adsdk

import android.content.Context
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd

class AppOpenAd(var context: Context, var adId: String) : MaxAdListener{
    private val TAG = "Adads"
    private var adLoadCallback: AdLoadCallback?=null
    private var adDisplayCallback: AdDisplayCallback?=null
    private var createTime = System.currentTimeMillis()
    var maxAd:MaxAd?=null
    var maxAppOpenAd: MaxAppOpenAd = MaxAppOpenAd(adId, context).apply {
        setListener(this@AppOpenAd)
    }


    override fun onAdLoaded(maxAd: MaxAd) {
        Log.d(TAG, "onAdHidden: 广告加载成功")
        this.maxAd=maxAd
        createTime = System.currentTimeMillis()
        adLoadCallback?.onAdsFinishLoading(true)

        Adads.appOpenAdList.add(this)
    }

    override fun onAdLoadFailed(adId: String, maxError: MaxError) {
        Log.d(TAG, "onAdHidden: 广告加载失败:${maxError.message}")
        adLoadCallback?.onAdsFinishLoading(false)
        destroy()
    }

    override fun onAdDisplayed(maxAd: MaxAd) {
        Log.d(TAG, "onAdHidden: 广告展示")
        this.maxAd=maxAd
        Adads.appOpenAdList.remove(this)
        adDisplayCallback?.onAdDisplayed()
    }

    override fun onAdHidden(maxAd: MaxAd) {
        Log.d(TAG, "onAdHidden: 广告关闭")
        this.maxAd=maxAd
        adDisplayCallback?.onAdClose()
        destroy()

    }

    override fun onAdClicked(maxAd: MaxAd) {
        Log.d(TAG, "onAdHidden: 广告点击")
        this.maxAd=maxAd
        adDisplayCallback?.onAdClick()
    }



    override fun onAdDisplayFailed(maxAd: MaxAd, maxError: MaxError) {
        Log.d(TAG, "onAdHidden: 广告展示失败:${maxError.message}")
        this.maxAd=maxAd
        adDisplayCallback?.onAdDisplayFailed()
        destroy()
    }


    fun load(
        adLoadCallback: AdLoadCallback?
    ) {
        this.adLoadCallback=adLoadCallback
        // Load the first ad
        maxAppOpenAd?.loadAd()
    }




    fun show(
        adDisplayCallback: AdDisplayCallback?,
        placement:String
    ) {
        this.maxAppOpenAd?.setListener(this)
        this.adDisplayCallback=adDisplayCallback
        if (maxAppOpenAd!!.isReady) {
            Log.d(TAG, "show: 展示广告")
            maxAppOpenAd?.showAd(placement)
        }else{
            adDisplayCallback?.onAdDisplayFailed()
        }
    }

    fun isExpired(): Boolean {
        return System.currentTimeMillis() - createTime > 1000 * 60 * 60 * 4
    }

    fun destroy() {
        adLoadCallback=null
        adDisplayCallback=null
        maxAppOpenAd?.destroy()
    }
}