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

class PinCodeActivity : AppCompatActivity() {
    private lateinit var pinEditText: EditText
    private lateinit var unlockButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_code)
        pinEditText = findViewById(R.id.pinEditText)
        unlockButton = findViewById(R.id.unlockButton)
        sharedPreferences = getSharedPreferences("pin_pref", Context.MODE_PRIVATE)

        val savedPin = sharedPreferences.getString("pin", null)
        if (savedPin == null) {
            val intent = Intent(this@PinCodeActivity, SetPinActivity::class.java)
            startActivity(intent)
            finish()
        }

        unlockButton.setOnClickListener {
            val enteredPin = pinEditText.text.toString()
            if (enteredPin == savedPin) {
                val intent = Intent(this@PinCodeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@PinCodeActivity, "Incorrect PIN", Toast.LENGTH_SHORT).show()
            }
        }
    }
}