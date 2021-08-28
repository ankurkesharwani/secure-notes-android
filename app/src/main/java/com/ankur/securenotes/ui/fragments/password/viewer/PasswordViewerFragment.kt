package com.ankur.securenotes.ui.fragments.password.viewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ankur.securenotes.R

class PasswordViewerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_viewer, container, false)
    }

    companion object {

        @JvmField
        val TAG = this::class.java.name

        const val PARAM_MODE_FLAG = "PARAM_MODE_FLAG"
        const val PARAM_PASSWORD_ID = "PARAM_PASSWORD_ID"
    }
}