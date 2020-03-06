package com.github.frayeralex.bibiphelp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.frayeralex.bibiphelp.App

class SplashActivity : AppCompatActivity() {

    private val app by lazy { application as App }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (app.getCache().isOnBoarded) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, OnBoardingActivity::class.java))
        }

        finish()
    }
}