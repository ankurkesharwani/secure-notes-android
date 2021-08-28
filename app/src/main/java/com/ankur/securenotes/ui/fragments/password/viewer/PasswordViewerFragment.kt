package com.ankur.securenotes.ui.fragments.password.viewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ankur.securenotes.databinding.FragmentPasswordViewerBinding

class PasswordViewerFragment : Fragment() {

    private lateinit var binding: FragmentPasswordViewerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordViewerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {

        @JvmField
        val TAG: String = this::class.java.name
    }
}