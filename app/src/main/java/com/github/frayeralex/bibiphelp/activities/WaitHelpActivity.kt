package com.github.frayeralex.bibiphelp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.frayeralex.bibiphelp.R

class WaitHelpActivity : AppCompatActivity() {

    private var counter = 0
    private lateinit var eventId : String
    private lateinit var helpersCount : TextView
    private lateinit var rejectBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_wait_help)

        eventId = intent.getStringExtra(HelpFormActivity.EVENT_ID)!!

        helpersCount = findViewById(R.id.helpersCount)

        rejectBtn = findViewById(R.id.rejectBtn)

        rejectBtn.setOnClickListener{ handleRejectBtnClick() }

        //todo Implement logic to show count of helpers
    }

    private fun handleRejectBtnClick() {
        // todo implement go to Rejection Reason Confirmation Activity
        counter += 1
        helpersCount.text = counter.toString()
    }
}