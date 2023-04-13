package com.praca.dyplomowa.android.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

object FragmentNavigationUtils {

    fun loadFragmentFadeWithOneStringBundleValue(fragmentManager: FragmentManager, fragment: Fragment, argumentKey: String, argumentValue: String){
        val bundle = Bundle()
        bundle.putString(argumentKey,argumentValue)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun loadFragmentOpenWithOneStringBundleValue(fragmentManager: FragmentManager, fragment: Fragment, argumentKey: String, argumentValue: String){
        val bundle = Bundle()
        bundle.putString(argumentKey,argumentValue)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun replaceFragmentWithOneStringBundleValue(fragmentManager: FragmentManager, fragment: Fragment, argumentKey: String, argumentValue: String){
        val bundle = Bundle()
        bundle.putString(argumentKey,argumentValue)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }


}