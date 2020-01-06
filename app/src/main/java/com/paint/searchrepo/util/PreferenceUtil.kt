package com.paint.searchrepo.util

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.paint.searchrepo.App

object PreferenceUtil {
    private val prefs: SharedPreferences? =
        App.instance.applicationContext.getSharedPreferences(Const.APP_NAME, MODE_PRIVATE)

    fun getPreferenceString(key: String): String? = prefs?.getString(key, "")

    fun getPreferenceInt(key: String): Int = prefs?.getInt(key, 0) ?: 0

    fun setPreferenceString(key: String, value: String) {
        prefs?.edit()?.putString(key, value)?.apply()
    }

    fun setPreferenceInt(key: String, value: Int) {
        prefs?.edit()?.putInt(key, value)?.apply()
    }

    fun removePreferenceString(key: String) {
        prefs?.edit()?.remove(key)?.apply()
    }
}