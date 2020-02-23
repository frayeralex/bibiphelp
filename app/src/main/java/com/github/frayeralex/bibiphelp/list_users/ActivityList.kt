package com.github.frayeralex.bibiphelp.list_users

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.activities.MainActivity


class ActivityList: ActivityUserAbstract() {


    override fun createFragment(): Fragment {
        Log.d ("fff7", "${SingltonUser.mlistEvents.toString() }")

        return FragmentList()
    }

    companion object {
        fun backMainActivity(context: Context?):Intent {
            val intent = Intent(context, MainActivity::class.java)
            return intent
        }
    }


}