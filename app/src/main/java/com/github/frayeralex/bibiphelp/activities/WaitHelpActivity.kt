package com.github.frayeralex.bibiphelp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.App
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.viewModels.WaitHelpViewModel
import kotlinx.android.synthetic.main.activity_wait_help.*


class WaitHelpActivity : AppCompatActivity() {

    private val app by lazy { application as App }
    private val viewModel by viewModels<WaitHelpViewModel>()
    private lateinit var eventId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_wait_help)

        eventId = intent.getStringExtra(IntentExtra.eventId)!!

        app.getCacheManager().activeHelpRequest = eventId

        closeEventBtn.setOnClickListener{ handleCloseBtnClick() }

        viewModel.getEvent(eventId).observe(this, Observer {
            if (it != null) {
                updateUI(it)
            }
        })
    }

    override fun onBackPressed() {}

    private fun handleCloseBtnClick() {
        val intent = Intent(this, CloseEventActivity::class.java)
        intent.putExtra(IntentExtra.eventId, eventId)
        startActivity(intent)
    }

    private fun updateUI(event: EventModel) {
        helpersCount.text = event.helpers.size.toString()

        if (event.helpers.isEmpty()) {
            waitMainTitle.text = resources.getText(R.string.wait_help_main_label)
            waitSecondaryTitle.text = resources.getText(R.string.wait_help_secondary_label)
            waitMainImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.help_request_placeholder))
        } else {
            waitMainTitle.text = resources.getText(R.string.wait_help_main_label_success)
            waitSecondaryTitle.text = resources.getText(R.string.wait_help_secondary_label_success)
            waitMainImg.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.help_coming))
        }
    }
}