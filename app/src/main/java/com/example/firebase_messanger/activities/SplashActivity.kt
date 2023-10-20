package com.example.firebase_messanger.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_messanger.activities.lock_app.PinCodeActivity
import com.example.firebase_messanger.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var splashBinding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        const val SPLASH_TIME_OUT: Long = 400
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        val currentUser = auth.currentUser

        val intent = if (currentUser != null) {
            Intent(this, PinCodeActivity::class.java)
        } else {
            Intent(this, RegisterActivity::class.java)
        }

        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}
