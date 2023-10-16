package com.example.firebase_messanger.activities.lock_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firebase_messanger.R
import com.example.firebase_messanger.activities.MainActivity

class SetPinActivity : AppCompatActivity() {
    private lateinit var pinEditText: EditText
    private lateinit var setupButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_pin)
        pinEditText = findViewById(R.id.pinSetupEditText)
        setupButton = findViewById(R.id.setupButton)

        sharedPreferences = getSharedPreferences("pin_pref", Context.MODE_PRIVATE)

        setupButton.setOnClickListener {
            val enteredPin = pinEditText.text.toString()
            if (enteredPin.length != 4) {
                Toast.makeText(this@SetPinActivity, "PIN must be 4 digits", Toast.LENGTH_SHORT).show()
            } else {
                sharedPreferences.edit().putString("pin", enteredPin).apply()
                val intent = Intent(this@SetPinActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}