package com.github.frayeralex.bibiphelp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.frayeralex.bibiphelp.App
import com.github.frayeralex.bibiphelp.cache.BaseSharedPreferencesManager
import com.github.frayeralex.bibiphelp.constatns.IntentExtra

class SplashActivity : AppCompatActivity() {

    private val app by lazy { application as App }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val helpRequestEventId = app.getCacheManager().activeHelpRequest
        val meActiveHelperForEvent = app.getCacheManager().meActiveHelperForEvent

        when {
            helpRequestEventId != BaseSharedPreferencesManager.DEFAULT_STRING -> {
                val intent = Intent(this, WaitHelpActivity::class.java)
                intent.putExtra(IntentExtra.eventId, helpRequestEventId)
                startActivity(intent)
            }
            meActiveHelperForEvent != BaseSharedPreferencesManager.DEFAULT_STRING -> {
                val intent = Intent(this, ConfirmedHelpActivity::class.java)
                intent.putExtra(IntentExtra.eventId, meActiveHelperForEvent)
                startActivity(intent)
            }
            app.getCacheManager().isOnBoarded -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            else -> {
                startActivity(Intent(this, OnBoardingActivity::class.java))
            }
        }

        finish()
    }
}