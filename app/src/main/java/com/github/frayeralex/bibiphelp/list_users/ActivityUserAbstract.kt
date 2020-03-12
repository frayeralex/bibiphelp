package com.github.frayeralex.bibiphelp.list_users


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.activities.MainActivity
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.viewModels.ListEventViewModel
import com.google.firebase.auth.FirebaseUser

abstract class ActivityUserAbstract : AppCompatActivity() {


    abstract fun createFragment(): Fragment






    val mFragmentManager = supportFragmentManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        val fragment: Fragment? = mFragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            mFragmentManager.beginTransaction().add(R.id.fragment_container, createFragment())
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_map_view -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

    }


}