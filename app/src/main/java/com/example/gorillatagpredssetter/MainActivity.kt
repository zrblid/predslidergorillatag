package com.example.gorillatagpredssetter

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val prefsName = "preds_prefs"
    private val keyQuestIp = "quest_ip"
    private val keyWifiName = "wifi_name"
    private val keyPredMs = "pred_ms"
    private val minPredMs = 20
    private val maxPredMs = 60
    private val forcedMinHz = 70

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)

        val ipBox = findViewById<EditText>(R.id.ipBox)
        val wifiNameBox = findViewById<EditText>(R.id.wifiNameBox)
        val confirmWifiCheck = findViewById<CheckBox>(R.id.confirmWifiCheck)
        val predSlider = findViewById<SeekBar>(R.id.predSlider)
        val predLabel = findViewById<TextView>(R.id.predValue)
        val hzLabel = findViewById<TextView>(R.id.hzValue)
        val statusLabel = findViewById<TextView>(R.id.statusValue)
        val applyButton = findViewById<Button>(R.id.applyBtn)

        val savedIp = prefs.getString(keyQuestIp, "") ?: ""
        val savedWifiName = prefs.getString(keyWifiName, "") ?: ""
        val savedPredMs = prefs.getInt(keyPredMs, recommendedPredictionMs(forcedMinHz))
            .coerceIn(minPredMs, maxPredMs)

        ipBox.setText(savedIp)
        wifiNameBox.setText(savedWifiName)

        predSlider.max = maxPredMs - minPredMs
        predSlider.progress = savedPredMs - minPredMs

        hzLabel.text = "Refresh rate floor: ${forcedMinHz}Hz"
        predLabel.text = "Predictions: ${savedPredMs}ms"

        predSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = progress + minPredMs
                predLabel.text = "Predictions: ${value}ms"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        applyButton.setOnClickListener {
            val questIp = ipBox.text.toString().trim()
            val wifiName = wifiNameBox.text.toString().trim()
            val predMs = (predSlider.progress + minPredMs).coerceIn(minPredMs, maxPredMs)

            if (questIp.isBlank()) {
                Toast.makeText(this, "Enter the Quest IP first.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit()
                .putString(keyQuestIp, questIp)
                .putString(keyWifiName, wifiName)
                .putInt(keyPredMs, predMs)
                .apply()

            val doApply = {
                applySettings(questIp, wifiName, predMs, statusLabel)
            }

            if (confirmWifiCheck.isChecked && wifiName.isNotBlank()) {
                AlertDialog.Builder(this)
                    .setTitle("Wi-Fi confirmation")
                    .setMessage("Connect to ${wifiName} Wi-Fi?")
                    .setPositiveButton("Yes") { _, _ -> doApply() }
                    .setNegativeButton("No") { _, _ ->
                        statusLabel.text = "Status: cancelled by user"
                    }
                    .show()
            } else {
                doApply()
            }
        }
    }

    private fun applySettings(
        questIp: String,
        wifiName: String,
        predMs: Int,
        statusLabel: TextView
    ) {
        statusLabel.text = "Status: applying..."

        // Predictions are set relative to refresh rate floor (70Hz -> about 40ms default).
        val targetHz = forcedMinHz
        val adjustedPredMs = predMs.coerceAtLeast(recommendedPredictionMs(targetHz))

        val shellCommands = listOf(
            "setprop service.adb.tcp.port 5555",
            "settings put system min_refresh_rate ${targetHz}.0",
            "settings put system peak_refresh_rate ${targetHz}.0",
            "setprop debug.gorillatag.prediction_ms $adjustedPredMs",
            "echo adb_target=${questIp}:5555 wifi=${if (wifiName.isBlank()) "none" else wifiName}"
        )

        thread {
            val commandResults = shellCommands.map { cmd ->
                val result = runShellCommand(cmd)
                "[$cmd] exit=${result.exitCode} ${result.output.trim()}"
            }

            val hasFailure = commandResults.any { it.contains("exit=") && !it.contains("exit=0") }
            runOnUiThread {
                statusLabel.text = if (hasFailure) {
                    "Status: apply completed with warnings (check permissions/ADB state)."
                } else {
                    "Status: applied: ${targetHz}Hz + ${adjustedPredMs}ms"
                }
            }
        }
    }

    private fun recommendedPredictionMs(hz: Int): Int {
        return (2800.0 / hz.toDouble()).roundToInt().coerceIn(minPredMs, maxPredMs)
    }

    private fun runShellCommand(command: String): ShellResult {
        return try {
            val process = ProcessBuilder("sh", "-c", command)
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().use { it.readText() }
            val code = process.waitFor()
            ShellResult(code, output)
        } catch (e: Exception) {
            ShellResult(-1, e.message ?: "unknown error")
        }
    }

    data class ShellResult(val exitCode: Int, val output: String)
}
