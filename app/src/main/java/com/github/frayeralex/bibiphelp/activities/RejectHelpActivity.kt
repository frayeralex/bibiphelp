package com.github.frayeralex.bibiphelp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.viewModels.DetailsEventViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_reject_help.*

class RejectHelpActivity : AppCompatActivity() {
    companion object {
        const val SAVIOR = 1
    }

    private val viewModel by viewModels<DetailsEventViewModel>()
    private var user: FirebaseUser? = null
    lateinit var eventId: String
    var requestStatus: String = RequestStatuses.UNCALLED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reject_help)
        eventId = intent.getStringExtra(IntentExtra.eventId)!!
        setSupportActionBar(toolbarCancelHelp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.getUser().observe(this, Observer { user = it })
        viewModel.getRequestStatus().observe(this, Observer { handleRequestStatus(it) })
        noRejectBtn.setOnClickListener { noRejectHelp() }
        yesRejectBtn.setOnClickListener { yesRejectHelp() }
    }

    private fun handleRequestStatus(status: String) {
        requestStatus = status
        when (status) {
            RequestStatuses.SUCCESS -> {
                val intent = Intent(this, ConfirmedHelpActivity::class.java)
                intent.putExtra(IntentExtra.eventId, eventId)
                intent.putExtra(IntentExtra.challenger, SAVIOR)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            RequestStatuses.FAILURE -> {
                Toast.makeText(
                    baseContext, R.string.error_common,
                    Toast.LENGTH_LONG
                ).show()
            }
            RequestStatuses.PENDING -> {
                // todo add progress bar
            }
        }
    }

    private fun noRejectHelp() {
        if (requestStatus == RequestStatuses.PENDING || user == null) return
        viewModel.sendHelpRequest(eventId, user?.uid.toString())
    }

    private fun yesRejectHelp() {
        super.onBackPressed()
        true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            super.onBackPressed()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {}
}