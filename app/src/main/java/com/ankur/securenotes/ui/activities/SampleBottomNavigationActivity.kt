package com.ankur.securenotes.ui.activities

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_sample_bottom_navigation.*
import com.ankur.securenotes.R

class SampleBottomNavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_bottom_navigation)

        setBottomNavigationItemSelectListener()
    }

    private fun setBottomNavigationItemSelectListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    Toast.makeText(this, "Home Selected", Toast.LENGTH_LONG).show()
                    true
                }

                R.id.navigation_dashboard -> {
                    Toast.makeText(this, "Dashboard Selected", Toast.LENGTH_LONG).show()
                    true
                }

                R.id.navigation_notifications -> {
                    Toast.makeText(this, "Notification Selected", Toast.LENGTH_LONG).show()
                    true
                }

                else ->  {
                    false
                }
            }
        }
    }
}