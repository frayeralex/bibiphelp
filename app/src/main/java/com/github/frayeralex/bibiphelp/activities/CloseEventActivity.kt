package com.github.frayeralex.bibiphelp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.constatns.EventStatuses
import com.github.frayeralex.bibiphelp.viewModels.CloseEventViewModel
import kotlinx.android.synthetic.main.activity_close_event.*

class CloseEventActivity : AppCompatActivity() {

    private val optionsIdList = listOf(EventStatuses.SUCCESS, EventStatuses.SELF, EventStatuses.NONACTUAL)
    private val viewModel by viewModels<CloseEventViewModel>()
    private lateinit var eventId : String
    private var isRequesting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_close_event)

        setSupportActionBar(toolbarCloseEvent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        eventId = intent.getStringExtra(WaitHelpActivity.EVENT_ID)!!

        showOptions()

        viewModel.getIsClosed().observe(this, Observer {
            if (it) {
                handleSuccessCloseEvent()
            }
        })

        viewModel.getRequestingState().observe(this, Observer {
            isRequesting = it
            progressBar.isVisible = isRequesting
        })
    }

    private fun handleSuccessCloseEvent() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
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

    private fun showOptions() {
        optionList.removeAllViews()

        optionsIdList.forEach {
            val listItem = createItem(it)

            listItem.addView(createImage(it))
            listItem.addView(createLabel(it))

            listItem.setOnClickListener { _ -> handleItemClick(it) }

            optionList.addView(listItem)
        }
    }

    private fun handleItemClick(index: Int) {
        if (!isRequesting) {
            viewModel.closeEvent(eventId, index)
        }
    }

    private fun createLabel(index: Int) = TextView(this).apply {
        text = when(index) {
            EventStatuses.SUCCESS -> resources.getText(R.string.close_option_success)
            EventStatuses.SELF -> resources.getText(R.string.close_option_self)
            EventStatuses.NONACTUAL -> resources.getText(R.string.close_option_non_actual)
            else -> resources.getText(R.string.close_option_non_actual)
        }
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun createItem(index: Int): LinearLayout {
        val listItem = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            id = index
        }


        listItem.setPadding(20, 30, 20, 30)

        listItem.setBackgroundColor(Color.WHITE)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(16, 8, 16, 8)

        listItem.layoutParams = params

        listItem.gravity = Gravity.CENTER_VERTICAL

        return listItem
    }

    private fun createImage(index: Int): ImageView {
        val img = ImageView(this)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 0, 25, 0)

        img.layoutParams = params

        img.setImageResource(when(index) {
            EventStatuses.SUCCESS -> R.drawable.close_opt_1
            EventStatuses.SELF -> R.drawable.close_opt_2
            EventStatuses.NONACTUAL -> R.drawable.close_opt_3
            else -> R.drawable.close_opt_3
        })

        return img
    }
}