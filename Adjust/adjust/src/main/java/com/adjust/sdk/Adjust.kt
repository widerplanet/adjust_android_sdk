//
//  Adjust.java
//  Adjust
//
//  Created by Christian Wellenbrock on 2012-10-11.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//  See the file MIT-LICENSE for copying permission.
//

package com.adjust.sdk

import android.content.Context
import android.net.Uri

/**
 * The main interface to Adjust.
 * Use the methods of this class to tell Adjust about the usage of your app.
 * See the README for details.
 */
object Adjust {

    private var defaultInstance: AdjustInstance? = null

    var isEnabled: Boolean
        get() {
            val adjustInstance = Adjust.getDefaultInstance()
            return adjustInstance.isEnabled
        }
        set(enabled) {
            val adjustInstance = Adjust.getDefaultInstance()
            adjustInstance.isEnabled = enabled
        }

    val adid: String?
        get() {
            val adjustInstance = Adjust.getDefaultInstance()
            return adjustInstance.adid
        }

    val attribution: AdjustAttribution?
        get() {
            val adjustInstance = Adjust.getDefaultInstance()
            return adjustInstance.attribution
        }

    @Synchronized
    fun getDefaultInstance(): AdjustInstance {
        if (defaultInstance == null) {
            defaultInstance = AdjustInstance()
        }
        return defaultInstance as AdjustInstance
    }

    fun onCreate(adjustConfig: AdjustConfig) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.onCreate(adjustConfig)
    }

    fun trackEvent(event: AdjustEvent) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.trackEvent(event)
    }

    fun onResume() {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.onResume()
    }

    fun onPause() {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.onPause()
    }

    fun appWillOpenUrl(url: Uri) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.appWillOpenUrl(url)
    }

    fun setReferrer(referrer: String, context: Context) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.sendReferrer(referrer, context)
    }

    fun setOfflineMode(enabled: Boolean) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.setOfflineMode(enabled)
    }

    fun sendFirstPackages() {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.sendFirstPackages()
    }

    fun addSessionCallbackParameter(key: String, value: String) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.addSessionCallbackParameter(key, value)
    }

    fun addSessionPartnerParameter(key: String, value: String) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.addSessionPartnerParameter(key, value)
    }

    fun removeSessionCallbackParameter(key: String) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.removeSessionCallbackParameter(key)
    }

    fun removeSessionPartnerParameter(key: String) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.removeSessionPartnerParameter(key)
    }

    fun resetSessionCallbackParameters() {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.resetSessionCallbackParameters()
    }

    fun resetSessionPartnerParameters() {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.resetSessionPartnerParameters()
    }

    fun setPushToken(token: String) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.setPushToken(token)
    }

    fun setPushToken(token: String, context: Context) {
        val adjustInstance = Adjust.getDefaultInstance()
        adjustInstance.setPushToken(token, context)
    }

    fun getGoogleAdId(context: Context, onDeviceIdRead: OnDeviceIdsRead) {
        Util.getGoogleAdId(context, onDeviceIdRead)
    }

    fun getAmazonAdId(context: Context): String? {
        return Util.getFireAdvertisingId(context.contentResolver)
    }
}
