package com.github.frayeralex.bibiphelp.activities

import com.github.frayeralex.bibiphelp.R
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.viewModels.HelpFormViewModel


class HelpFormActivity : AppCompatActivity() {

    private val viewModel by viewModels<HelpFormViewModel>()
    private var categoryId: Int? = null
    private var categoryLabel = ""

    private var isRequesting = false
    private var location : Location? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var button: Button
    private lateinit var messageInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryId = intent.getIntExtra(CategoriesActivity.CATEGORY_ID_KEY, 6)
        categoryLabel = intent.getStringExtra(CategoriesActivity.CATEGORY_LABEL)

        setContentView(R.layout.activity_help_form)

        setSupportActionBar(findViewById(R.id.toolbar_help))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = categoryLabel

        progressBar = findViewById(R.id.progressBar)

        button = findViewById(R.id.helpRequestButton)
        button.setOnClickListener{ handlePublishBtnClick() }

        messageInput = findViewById(R.id.message)

        viewModel.getLocationData().observe(this, Observer<Location> { location = it })

        viewModel.getRequestingState().observe(this, Observer<Boolean> {
            progressBar.isVisible = it
            isRequesting = it
        })

        viewModel.getCreatedSuccess().observe(this, Observer<Boolean> {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    fun handlePublishBtnClick() {
        if (isRequesting) return

        val message = messageInput.text.toString().trim()

        if (message == "") {
            return Toast.makeText(
                baseContext, R.string.help_input_label_empty,
                Toast.LENGTH_SHORT
            ).show()
        }

        if (location == null) {
            return Toast.makeText(
                baseContext, "Need location permissions",
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.createHelpRequest(message, categoryId!!, location!!)
    }
}