package com.github.frayeralex.bibiphelp.list_users

import android.util.Log
import androidx.fragment.app.Fragment



class ActivityList: ActivityUserAbstract() {



    override fun createFragment(): Fragment {
        Log.d ("fff7", "${SingltonUser.mlistEvents.toString() }")
        return FragmentList()
    }
}