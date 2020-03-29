package com.github.frayeralex.bibiphelp.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.App
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.viewModels.DetailsEventViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_reject_help.*

class RejectHelpActivity : AppCompatActivity() {

    private val app by lazy { application as App }
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
        viewModel.getHelperRejectRequestStatus().observe(this, Observer { handleRequestStatus(it) })
        noRejectBtn.setOnClickListener { noRejectHelp() }
        yesRejectBtn.setOnClickListener { yesRejectHelp() }
    }

    private fun handleRequestStatus(status: String) {
        requestStatus = status
        when (status) {
            RequestStatuses.SUCCESS -> {
                app.getCacheManager().resetMeHelpForEvent()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            RequestStatuses.FAILURE -> {
                Toast.makeText(
                    baseContext, R.string.error_common,
                    Toast.LENGTH_LONG
                ).show()
                progressBar.isVisible = false
            }
            RequestStatuses.PENDING -> {
                progressBar.isVisible = true
            }
        }
    }

    private fun yesRejectHelp() {
        if (requestStatus == RequestStatuses.PENDING || user == null) return
        viewModel.rejectHelperRequest(eventId, user?.uid.toString())
        setResult(Activity.RESULT_OK)
    }

    private fun noRejectHelp() {
        super.onBackPressed()
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
}