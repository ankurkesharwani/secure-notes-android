package com.ankur.securenotes.ui.fragments.password_editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ankur.securenotes.R

class PasswordEditorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_editor, container, false)
    }

    companion object {

        @JvmField
        val TAG = this::class.java.name
    }
}