
package com.example.gorillatagpredssetter

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ipBox = findViewById<EditText>(R.id.ipBox)
        val slider = findViewById<SeekBar>(R.id.predSlider)
        val predLabel = findViewById<TextView>(R.id.predValue)
        val applyButton = findViewById<Button>(R.id.applyBtn)

        slider.max = 60
        slider.progress = 40

        slider.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                predLabel.text = "Predictions: ${value}ms"
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        applyButton.setOnClickListener {
            // placeholder command builder
        }
    }
}
