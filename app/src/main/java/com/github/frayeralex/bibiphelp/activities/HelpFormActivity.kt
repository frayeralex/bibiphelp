package com.github.frayeralex.bibiphelp.activities

import com.github.frayeralex.bibiphelp.R
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.constatns.EventTypes
import com.github.frayeralex.bibiphelp.constatns.InputValidation
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.utils.PermissionManager
import com.github.frayeralex.bibiphelp.viewModels.HelpFormViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_help_form.*


class HelpFormActivity : AppCompatActivity() {

    private val viewModel by viewModels<HelpFormViewModel>()
    private var categoryId: Int? = null
    private var categoryLabel = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isRequesting = false
    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryId = intent.getIntExtra(CategoriesActivity.CATEGORY_ID_KEY, EventTypes.OTHER)
        categoryLabel = intent.getStringExtra(CategoriesActivity.CATEGORY_LABEL)!!

        setContentView(R.layout.activity_help_form)

        setSupportActionBar(toolbarHelp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = categoryLabel

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        textCount.text = "0 / ${InputValidation.helpMsgMaxLength}"

        helpRequestButton.setOnClickListener { handlePublishBtnClick() }

        messageInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(cs: Editable) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setMessage(cs.toString())
            }
        })

        viewModel.getLocationData().observe(this, Observer<Location> { location = it })
        viewModel.getValidationError().observe(this, Observer {
            if (it != null) {
                validationText.text = resources.getText(it)
                validationText.isVisible = true
            } else {
                validationText.isVisible = false
            }

        })

        viewModel.getMessage().observe(this, Observer<String> {
            textCount.text = "${it.length} / ${InputValidation.helpMsgMaxLength}"
            updateTextInputStyles(it.length <= InputValidation.helpMsgMaxLength)
        })

        viewModel.getRequestingState().observe(this, Observer {
            progressBar.isVisible = it
            isRequesting = it
        })

        viewModel.getCreatedEventId().observe(this, Observer {
            if (it != null) {
                val intent = Intent(this, WaitHelpActivity::class.java)
                intent.putExtra(IntentExtra.eventId, it)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun updateTextInputStyles(isValid: Boolean) {
        if (isValid) {
            messageInput.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
            messageInput.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            textCount.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        } else {
            messageInput.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent))
            messageInput.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            textCount.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        }
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

    private fun handlePublishBtnClick() {
        if (isRequesting) return

        val message = messageInput.text.toString().trim()

        if (message.isEmpty()) {
            return Toast.makeText(
                baseContext, R.string.help_input_label_empty,
                Toast.LENGTH_LONG
            ).show()
        }

        if (message.length > InputValidation.helpMsgMaxLength) {
            return Toast.makeText(
                baseContext, R.string.help_input_large_text,
                Toast.LENGTH_LONG
            ).show()
        }

        if (location == null) {
            PermissionManager.checkLocationPermission(this, ACCESS_FINE_LOCATION)

            return
        }

        viewModel.createHelpRequest(message, categoryId!!, location!!)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location = it }
                }
                return
            }
            else -> {
            }
        }
    }

    companion object {
        const val ACCESS_FINE_LOCATION = 1
    }
}