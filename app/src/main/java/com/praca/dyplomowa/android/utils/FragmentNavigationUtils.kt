package com.praca.dyplomowa.android.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

object FragmentNavigationUtils {

    fun addFragmentFade(fragmentManager: FragmentManager, fragment: Fragment){
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun addFragmentOpen(fragmentManager: FragmentManager, fragment: Fragment){
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun replaceFragmentFade(fragmentManager: FragmentManager, fragment: Fragment){
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun replaceFragmentOpen(fragmentManager: FragmentManager, fragment: Fragment){
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun addFragmentFadeWithSourceFragment(fragmentManager: FragmentManager, fragment: Fragment, argumentSourceFragmentName: String){
        val bundle = Bundle()
        bundle.putString("argumentSourceFragmentName", argumentSourceFragmentName)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun addFragmentOpenWithSourceFragment(fragmentManager: FragmentManager, fragment: Fragment, argumentSourceFragmentName: String){
        val bundle = Bundle()
        bundle.putString("argumentSourceFragmentName", argumentSourceFragmentName)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
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

    fun addFragmentFadeWithOneStringBundleValue(fragmentManager: FragmentManager, fragment: Fragment, argumentKey: String, argumentValue: String){
        val bundle = Bundle()
        bundle.putString(argumentKey,argumentValue)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun addFragmentOpenWithOneStringBundleValue(fragmentManager: FragmentManager, fragment: Fragment, argumentKey: String, argumentValue: String){
        val bundle = Bundle()
        bundle.putString(argumentKey,argumentValue)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun addFragmentFadeWithThreeStringBundleValue(fragmentManager: FragmentManager, fragment: Fragment, firstArgumentKey: String, firstArgumentValue: String, secondArgumentKey: String, secondArgumentValue: String, thirdArgumentKey: String, thirdArgumentValue: String){
        val bundle = Bundle()
        bundle.putString(firstArgumentKey,firstArgumentValue)
        bundle.putString(secondArgumentKey,secondArgumentValue)
        bundle.putString(thirdArgumentKey,thirdArgumentValue)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }


    fun addFragmentFadeWithOneStringBundleValueAndSourceFragment(fragmentManager: FragmentManager, fragment: Fragment, argumentKey: String, argumentValue: String, argumentSourceFragmentName: String){
        val bundle = Bundle()
        bundle.putString(argumentKey,argumentValue)
        bundle.putString("argumentSourceFragmentName", argumentSourceFragmentName)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun addFragmentOpenWithOneStringBundleValueAndSourceFragment(fragmentManager: FragmentManager, fragment: Fragment ,argumentKey: String, argumentValue: String, argumentSourceFragmentName: String){
        val bundle = Bundle()
        bundle.putString(argumentKey,argumentValue)
        bundle.putString("argumentSourceFragmentName", argumentSourceFragmentName)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(android.R.id.content, fragment, fragment::class.simpleName)
            .addToBackStack(null)
            .commit()
    }


}