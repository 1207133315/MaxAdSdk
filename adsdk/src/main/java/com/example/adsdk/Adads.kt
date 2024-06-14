package com.example.adsdk

import android.app.Activity
import android.app.ProgressDialog.show
import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.Keep
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.applovin.sdk.AppLovinSdkInitializationConfiguration

object Adads {
    private const val SDK_KEY =
        "0sSgZAJBQEuLqgITq2i2RXvh7cf_iapg7dVnV8quzX3HRnSRvT3kszzUTgxPQwgfgCnahQPZOud_dUvTuG9fPt"
    internal val interstitialAdList = mutableListOf<InterstitialAd>()
    internal val appOpenAdList = mutableListOf<AppOpenAd>()
    internal val rewardedAdList = mutableListOf<RewardedAd>()
    internal val nativeAdAdList = mutableListOf<NativeAd>()
    private const val maxCacheLength = 3
    private const val AD_ID_INTERSTITIAL = "cd3e7bc595f41570"
    private const val AD_ID_REWARD_VIDEO = "9837843ddba2122a"
    private const val AD_ID_APP_OPEN = "fbf04949a5e933dd"
    private const val AD_ID_NATIVE = "e1812c570d710897"
    fun init(context: Context,testDeviceList: MutableList<String>?=null, complete: ((sdkConfig: AppLovinSdkConfiguration) -> Unit)? = null) {
        val lovinSdk = AppLovinSdk.getInstance(context.applicationContext)
        val initConfig =
            AppLovinSdkInitializationConfiguration.builder(SDK_KEY, context.applicationContext)
                .setMediationProvider(AppLovinMediationProvider.MAX)
                .setTestDeviceAdvertisingIds(testDeviceList)
                .build()
        lovinSdk.initialize(initConfig) { sdkConfig ->
            // Start loading ads
            complete?.invoke(sdkConfig)
        }

    }

    fun loadAds(context: Context, adType: AdType, adLoadCallback: AdLoadCallback?) {
        when (adType) {
            AdType.INTERSTITIAL -> {
                loadInterstitialAd(context, adLoadCallback)
            }

            AdType.REWARD -> {
                loadRewardAd(context, adLoadCallback)
            }

            AdType.NATIVE -> {
                loadNativeAd(context, adLoadCallback)
            }

            AdType.APP_OPEN -> {
                loadAppOpenAd(context, adLoadCallback)
            }

            else -> {
                loadInterstitialAd(context, adLoadCallback)
            }
        }
    }

    fun showAds(
        context: Context,
        adType: AdType,
        adView: AdView? = null,
        placement: String="",
        adDisplayCallback: AdDisplayCallback?
    ) {
        when (adType) {
            AdType.INTERSTITIAL -> {
                showInterstitialAd(context, placement,adDisplayCallback)
            }

            AdType.REWARD -> {
                showRewardAd(context,placement, adDisplayCallback)
            }

            AdType.NATIVE -> {
                showNativeAd(context, adView,placement, adDisplayCallback)
            }

            AdType.APP_OPEN -> {
                showAppOpenAd(context, placement,adDisplayCallback)
            }

            else -> {
                showInterstitialAd(context, placement,adDisplayCallback)
            }
        }
    }

    fun loadInterstitialAd(context: Context, adLoadCallback: AdLoadCallback?) {
        (context as? Activity)?.apply {
            interstitialAdList.removeIf { it.isExpired() }
            if (interstitialAdList.size >= maxCacheLength && interstitialAdList.find { !it.isExpired() } != null) {
                Log.d("InterstitialAd", "条件满足,不加载 ")
                adLoadCallback?.onAdsFinishLoading(true)
                return
            } else {
                Log.d("InterstitialAd", "条件不满足,加载 ")
                val interstitialAd = InterstitialAd(this, AD_ID_INTERSTITIAL)
                interstitialAd.load(adLoadCallback)
            }
        }
    }

    fun showInterstitialAd(
        context: Context,
        placement: String,
        adDisplayCallback: AdDisplayCallback?,

    ) {
        val interstitialAd = interstitialAdList.filter { !it.isExpired() && it.maxAd != null }
            .maxByOrNull { it.maxAd!!.revenue }
        interstitialAd?.apply {
            show(adDisplayCallback, placement)
        }
    }

    fun loadAppOpenAd(context: Context, adLoadCallback: AdLoadCallback?) {
        appOpenAdList.removeIf { it.isExpired() }
        if (appOpenAdList.size >= maxCacheLength && appOpenAdList.find { !it.isExpired() } != null) {
            adLoadCallback?.onAdsFinishLoading(true)
            return
        } else {
            val appOpenAd = AppOpenAd(context, AD_ID_APP_OPEN)
            appOpenAd?.load(adLoadCallback)
        }

    }

    fun showAppOpenAd(
        context: Context,
        placement: String,
        adDisplayCallback: AdDisplayCallback?
    ) {
        val appOpenAd = appOpenAdList.filter { !it.isExpired() && it.maxAd != null }
            .maxByOrNull { it.maxAd!!.revenue }
        appOpenAd?.apply {
            show(adDisplayCallback, placement)
        }
    }


    fun loadRewardAd(context: Context, adLoadCallback: AdLoadCallback?) {
        (context as? Activity)?.apply {
            rewardedAdList.removeIf { it.isExpired() }
            if (rewardedAdList.size >= maxCacheLength && rewardedAdList.find { !it.isExpired() } != null) {
                adLoadCallback?.onAdsFinishLoading(true)
                return
            } else {
                val rewardedAd = RewardedAd(this, AD_ID_REWARD_VIDEO)
                rewardedAd?.load(adLoadCallback)
            }
        }
    }

    fun showRewardAd(
        context: Context,
        placement: String,
        adDisplayCallback: AdDisplayCallback?
    ) {
        val rewardedAd = rewardedAdList.filter { !it.isExpired() && it.maxAd != null }
            .maxByOrNull { it.maxAd!!.revenue }
        rewardedAd?.apply {
            show(adDisplayCallback, placement)
        }
    }

    fun loadNativeAd(context: Context, adLoadCallback: AdLoadCallback?) {
        nativeAdAdList.removeIf { it.maxAd?.nativeAd?.isExpired == true }
        if (appOpenAdList.size >= maxCacheLength && appOpenAdList.find { !it.isExpired() } != null) {
            adLoadCallback?.onAdsFinishLoading(true)
            return
        } else {
            val rewardedAd = NativeAd(context, AD_ID_NATIVE)
            rewardedAd?.load(adLoadCallback)
        }

    }

    fun showNativeAd(
        context: Context,
        adView: AdView?,
        placement: String,
        adDisplayCallback: AdDisplayCallback?
    ) {
        val rewardedAd =
            nativeAdAdList.filter { it.maxAd != null && it.maxAd?.nativeAd?.isExpired == false }
                .maxByOrNull { it.maxAd!!.revenue }
        rewardedAd?.apply {
            this.adView = adView
            show(adDisplayCallback, placement)
        }
    }

    fun readyToServe(adType: AdType): Boolean {
        if (adType == AdType.NATIVE) {
            return nativeAdAdList.size > 0 && nativeAdAdList.find { it.maxAd?.nativeAd?.isExpired == false } != null
        } else if (adType == AdType.REWARD) {
            return rewardedAdList.size > 0 && rewardedAdList.find { !it.isExpired() } != null
        } else if (adType == AdType.INTERSTITIAL) {
            return interstitialAdList.size > 0 && interstitialAdList.find { !it.isExpired() } != null
        } else if (adType == AdType.APP_OPEN) {
            return appOpenAdList.size > 0 && appOpenAdList.find { !it.isExpired() } != null
        } else {
            return interstitialAdList.size > 0 && interstitialAdList.find { !it.isExpired() } != null
        }

    }
}