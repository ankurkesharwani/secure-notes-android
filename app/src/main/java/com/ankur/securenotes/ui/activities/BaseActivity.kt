package com.ankur.securenotes.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R

open class BaseActivity: AppCompatActivity() {

    private var shownFragmentTag: String? = null

    protected fun isFragmentPresent(tag: String): Boolean {
        return findFragment(tag) != null
    }

    protected fun findFragment(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }

    protected fun showFragment(tag: String, bundleArgs: Bundle?): Fragment? {
        if (isFragmentPresent(tag)) {
            return null
        }

        // Add the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = newFragment(tag) ?: return null

        // Set arguments
        if (bundleArgs != null) {
            if (bundleArgs.keySet().count() > 0) {
                fragment.arguments = bundleArgs
            }
        }

        // Show
        if (shownFragmentTag != null) {
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, tag)
        } else {
            fragmentTransaction.add(R.id.fragmentContainer, fragment, tag)
        }
        fragmentTransaction.commit()
        shownFragmentTag = tag
        return fragment
    }

    protected fun removeFragment(tag: String): Fragment? {
        val fragment = findFragment(tag) ?: return null

        // Remove the fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
        shownFragmentTag = tag

        return fragment
    }

    protected open fun newFragment(tag: String): Fragment? {
        return null
    }
}