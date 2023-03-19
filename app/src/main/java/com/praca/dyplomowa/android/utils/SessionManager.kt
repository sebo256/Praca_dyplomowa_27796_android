package com.praca.dyplomowa.android.utils

import android.content.Context
import com.praca.dyplomowa.android.R

object SessionManager {

    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"
    const val CURRENT_USER_USERNAME = "current_user_username"

    fun saveAccessToken(context: Context, token: String?) =
        saveStringSharedPrefs(context, ACCESS_TOKEN, token)

    fun saveRefreshToken(context: Context, token: String?) =
        saveStringSharedPrefs(context, REFRESH_TOKEN, token)

    fun saveCurrentUserUsername(context: Context, id: String?) =
        saveStringSharedPrefs(context, CURRENT_USER_USERNAME, id)

    fun getAccessToken(context: Context): String? =
        getStringSharedPrefs(context, ACCESS_TOKEN)

    fun getRefreshToken(context: Context): String? =
        getStringSharedPrefs(context, REFRESH_TOKEN)

    fun getCurrentUserId(context: Context): String? =
        getStringSharedPrefs(context, CURRENT_USER_USERNAME)



    fun saveStringSharedPrefs(context: Context, key: String, value: String?){
        val sharedPrefsEditor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        sharedPrefsEditor.putString(key, value).apply()
    }

    fun getStringSharedPrefs(context: Context, key: String): String?{
        val sharedPrefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return sharedPrefs.getString(key, null)
    }

    fun clearSharedPrefs(context: Context){
        val sharedPrefsEditor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        sharedPrefsEditor.clear().apply()
    }

}