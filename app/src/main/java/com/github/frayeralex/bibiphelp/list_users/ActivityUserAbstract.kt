package com.github.frayeralex.bibiphelp.list_users


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.activities.MainActivity

abstract class ActivityUserAbstract : AppCompatActivity() {

    abstract fun createFragment(): Fragment

    val mFragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)



        val fragment: Fragment? = mFragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            mFragmentManager.beginTransaction().add(R.id.fragment_container, createFragment()).commit()

        }


    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_map_view -> {
            Log.d ("ddd","555")
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

        Log.d ("fff8", "${SingltonUser.mlistEvents.toString()}")
    }

}