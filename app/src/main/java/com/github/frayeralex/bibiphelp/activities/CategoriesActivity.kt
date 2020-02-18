package com.github.frayeralex.bibiphelp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.frayeralex.bibiphelp.R

class CategoriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        setSupportActionBar(findViewById(R.id.toolbar_1))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}