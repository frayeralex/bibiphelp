package com.github.frayeralex.bibiphelp.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.frayeralex.bibiphelp.R
import kotlinx.android.synthetic.main.activity_cancel_help.*

class CancelHelpActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_help)
        setSupportActionBar(toolbarCancelHelp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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