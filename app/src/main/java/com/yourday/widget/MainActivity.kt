package com.yourday.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yourday.widget.databinding.ActivityMainBinding
import com.yourday.widget.widget.DaySecondsWidgetProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.refreshButton.setOnClickListener {
            DaySecondsWidgetProvider.refreshAll(this)
        }
    }
}
