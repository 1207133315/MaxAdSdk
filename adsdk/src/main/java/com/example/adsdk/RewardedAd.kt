package com.example.adsdk

import android.app.Activity
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd

class RewardedAd(var context: Activity, var adId: String) : MaxRewardedAdListener {
    private val TAG = "Adads"
    private var adLoadCallback: AdLoadCallback? = null
    private var adDisplayCallback: AdDisplayCallback? = null
    var maxAd:MaxAd?=null
    private var createTime = System.currentTimeMillis()
    var maxRewardedAd: MaxRewardedAd = MaxRewardedAd.getInstance(adId, context).apply {
        setListener(this@RewardedAd)
    }


    override fun onAdLoaded(maxAd: MaxAd) {
        Log.d(TAG, "onAdHidden: 广告加载成功")
        this.maxAd=maxAd
        createTime = System.currentTimeMillis()
        adLoadCallback?.onAdsFinishLoading(true)
        Adads.rewardedAdList.add(this)
    }

    override fun onAdLoadFailed(adId: String, maxError: MaxError) {
        Log.d(TAG, "onAdHidden: 广告加载失败:${maxError.message}")
        adLoadCallback?.onAdsFinishLoading(false)
    }

    override fun onAdDisplayed(maxAd: MaxAd) {
        Log.d(TAG, "onAdHidden: 广告展示")
        this.maxAd=maxAd
        Adads.rewardedAdList.remove(this)
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

    override fun onUserRewarded(maxAd: MaxAd, maxReward: MaxReward) {
        this.maxAd=maxAd
        adDisplayCallback?.onUserRewarded()
    }


    fun load(
        adLoadCallback: AdLoadCallback?
    ) {
        this.adLoadCallback = adLoadCallback
        // Load the first ad
        maxRewardedAd.loadAd()
    }


    fun show(
        adDisplayCallback: AdDisplayCallback?
        ,placement:String=""
    ) {
        this.maxRewardedAd.setListener(this)
        this.adDisplayCallback = adDisplayCallback
        if (maxRewardedAd.isReady) {
            maxRewardedAd.showAd(placement)
        }
    }

    fun isExpired(): Boolean {
        return System.currentTimeMillis() - createTime > 1000 * 60 * 60 * 4
    }
    fun destroy() {
        adLoadCallback = null
        adDisplayCallback = null
    }
}