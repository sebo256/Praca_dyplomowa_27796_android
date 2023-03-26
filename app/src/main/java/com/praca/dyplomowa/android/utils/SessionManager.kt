package com.praca.dyplomowa.android.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.praca.dyplomowa.android.R

object SessionManager {

    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"
    const val CURRENT_USER_USERNAME = "current_user_username"
    const val IS_ADMIN = "is_admin"


    fun saveAccessToken(context: Context, token: String?) =
        saveStringSharedPrefs(context, ACCESS_TOKEN, token)

    fun saveRefreshToken(context: Context, token: String?) =
        saveStringSharedPrefs(context, REFRESH_TOKEN, token)

    fun saveCurrentUserUsername(context: Context, id: String?) =
        saveStringSharedPrefs(context, CURRENT_USER_USERNAME, id)

    fun saveIsAdmin(context: Context, isAdmin: Boolean) =
        saveBooleanSharedPrefs(context, IS_ADMIN, isAdmin)

    fun getAccessToken(context: Context): String? =
        getStringSharedPrefs(context, ACCESS_TOKEN)

    fun getRefreshToken(context: Context): String? =
        getStringSharedPrefs(context, REFRESH_TOKEN)

    fun getCurrentUserId(context: Context): String? =
        getStringSharedPrefs(context, CURRENT_USER_USERNAME)

    fun getIsAdmin(context: Context): Boolean =
        getBooleanSharedPrefs(context, IS_ADMIN)

    fun saveStringSharedPrefs(context: Context, key: String, value: String?) =
        getEncryptedSharedPrefs(context).edit().putString(key,value).apply()

    fun saveBooleanSharedPrefs(context: Context, key: String, value: Boolean) =
        getEncryptedSharedPrefs(context).edit().putBoolean(key,value).apply()

    fun getStringSharedPrefs(context: Context, key: String): String? =
        getEncryptedSharedPrefs(context).getString(key,null)

    fun getBooleanSharedPrefs(context: Context, key: String): Boolean =
        getEncryptedSharedPrefs(context).getBoolean(key,false)

    fun clearSharedPrefsTest(context: Context) =
        getEncryptedSharedPrefs(context).edit().clear().apply()



//    fun saveStringSharedPrefs(context: Context, key: String, value: String?){
//        val sharedPrefsEditor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
//        sharedPrefsEditor.putString(key, value).apply()
//    }
//
//    fun getStringSharedPrefs(context: Context, key: String): String?{
//        val sharedPrefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
//        return sharedPrefs.getString(key, null)
//    }
//
//    fun clearSharedPrefs(context: Context){
//        val sharedPrefsEditor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
//        sharedPrefsEditor.clear().apply()
//    }

    fun getEncryptedSharedPrefs(context: Context): SharedPreferences{
        val masterKey: MasterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            "SharedPrefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return sharedPreferences
    }
}