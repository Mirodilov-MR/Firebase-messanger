package com.example.firebase_messanger.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_messanger.adapters.pageradapter.SignInUpAdapter
import com.example.firebase_messanger.databinding.ActivityRegisterBinding
import com.example.firebase_messanger.fragments.SignInFragment
import com.example.firebase_messanger.fragments.SignUpFragment


class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private var adapter: SignInUpAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        adapter = SignInUpAdapter(supportFragmentManager)
        adapter!!.addPagerFragment(SignUpFragment(), "Sign Up")
        adapter!!.addPagerFragment(SignInFragment(), "Login In")
        registerBinding.viewPager.adapter = adapter
        registerBinding.tabLayout.setupWithViewPager(registerBinding.viewPager)


    }
}