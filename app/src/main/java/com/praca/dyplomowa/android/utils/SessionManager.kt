package com.praca.dyplomowa.android.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.praca.dyplomowa.android.api.repository.UserRepository
import com.praca.dyplomowa.android.api.response.RefreshTokenResponse
import com.praca.dyplomowa.android.views.LoginActivityView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

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

    fun getCurrentUserUsername(context: Context): String? =
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

    fun clearSharedPrefs(context: Context) =
        getEncryptedSharedPrefs(context).edit().clear().apply()

    fun refreshToken(token: String, context: Context){
        val userRepository = UserRepository(context)
        userRepository.refreshToken(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .blockingSubscribe(getNewAccessTokenUsingRefreshObserverRX(context))
    }

    private fun getNewAccessTokenUsingRefreshObserverRX(context: Context): SingleObserver<Response<RefreshTokenResponse>> {
        return object : SingleObserver<Response<RefreshTokenResponse>> {

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onSubscribe(d: Disposable) {
                //Loading
            }

            override fun onSuccess(t: Response<RefreshTokenResponse>) {
                if(t.body()!!.status){
                    saveAccessToken(
                        context = context,
                        token = t.body()?.token
                    )
                }else{
                    clearSharedPrefs(context)
                    val intent = Intent(context, LoginActivityView::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    context.startActivity(intent)
                }

            }

        }
    }

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