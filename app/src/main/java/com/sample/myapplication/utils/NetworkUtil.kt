package com.sample.myapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager

object NetworkUtil {

    val NOT_CONNECTED = 0
    val WIFI = 1
    val MOBILE = 2

    fun getConnectionStatus(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.getActiveNetworkInfo()
        if (activeNetwork != null) {
            if (activeNetwork!!.getType() === ConnectivityManager.TYPE_WIFI)
                return WIFI
            if (activeNetwork!!.getType() === ConnectivityManager.TYPE_MOBILE)
                return MOBILE
        }
        return NOT_CONNECTED
    }

    fun getConnectionStatusString(context: Context): String {
        val connectionStatus = NetworkUtil.getConnectionStatus(context)
        if (connectionStatus == NetworkUtil.WIFI)
            return "Connected to Wifi"
        return if (connectionStatus == NetworkUtil.MOBILE) "Connected to Mobile Data" else "No internet connection"
    }

    fun getWifiName(context: Context): String? {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var wifiName = wifiManager.getConnectionInfo().getSSID()
        if (wifiName != null) {
            if (!wifiName!!.contains("unknown ssid") && wifiName!!.length > 2) {
                if (wifiName!!.startsWith("\"") && wifiName!!.endsWith("\""))
                    wifiName = wifiName!!.subSequence(1, wifiName!!.length- 1).toString()
                return wifiName
            } else {
                return ""
            }
        } else {
            return ""
        }
    }

    fun getMobileNetworkName(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkName = tm.getNetworkOperatorName()
        return if (networkName != null) {
            networkName
        } else ""
    }

    fun getGateway(context: Context): String {
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return NetworkUtil.intToIp(wm.getDhcpInfo().gateway)
    }

    fun intToIp(i: Int): String {
        return (i shr 24 and 0xFF).toString() + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 8 and 0xFF) + "." +
                (i and 0xFF)
    }
}