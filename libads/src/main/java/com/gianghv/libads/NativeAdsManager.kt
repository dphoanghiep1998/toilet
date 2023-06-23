package com.gianghv.libads

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.gianghv.libads.utils.Constants
import com.gianghv.libads.utils.Utils
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd

class NativeAdsManager constructor(
    private val context: Context,
    private val idNativeAds01: String,
    private val idNativeAds02: String,
    private val idNativeAds03: String
) {
    private var nativeAd: NativeAd? = null
    private var mAdLoader: AdLoader? = null

    private var nativeAdsManagerHome :NativeAdsManager ?= null
    var isAdLoaded = false

    val mNativeAd get() = nativeAd

    var handler: Handler? = null
    var runable: Runnable? = null

    fun loadAds(
        onLoadSuccess: ((NativeAd) -> Unit)? = null,
        onLoadFail: ((failed: Boolean) -> Unit)? = null
    ) {
        if (!Utils.isOnline(context)) {
            onLoadFail?.invoke(true)
            return
        }

        handler = Handler(Looper.getMainLooper())
        runable = Runnable {
            if (handler != null) {
                onLoadFail?.invoke(true)
            }
        }
        handler?.postDelayed(runable!!, Constants.TIME_OUT)

        requestAds(idNativeAds01, onLoadSuccess, onLoadFail = {
            requestAds(idNativeAds02, onLoadSuccess, onLoadFail = {
                requestAds(idNativeAds03, onLoadSuccess, onLoadFail = {
                    runable?.let { handler?.removeCallbacks(it) }
                    onLoadFail?.invoke(true)
                })
            })
        })
    }

    fun requestAds(
        idNativeAds: String,
        onLoadSuccess: ((NativeAd) -> Unit)? = null,
        onLoadFail: (() -> Unit)? = null
    ) {
        mAdLoader = AdLoader.Builder(context, idNativeAds).forNativeAd {
            nativeAd?.destroy()
            nativeAd = it
            nativeAd?.setOnPaidEventListener {
                Utils.postRevenueAdjust(it, "Native")
            }
            onLoadSuccess?.invoke(it)
        }.withAdListener(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                isAdLoaded = true
                runable?.let { handler?.removeCallbacks(it) }
                handler = null
            }

            override fun onAdClosed() {
                super.onAdClosed()
                val request = AdRequest.Builder().build()
                mAdLoader?.loadAd(request)
                isAdLoaded = false
                Log.d("TAG", "onAdClosed:3123213 ")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                onLoadFail?.invoke()
            }
        }).build()

        val request = AdRequest.Builder().build()
        mAdLoader?.loadAd(request)
    }
}