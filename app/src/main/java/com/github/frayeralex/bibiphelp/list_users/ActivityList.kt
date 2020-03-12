package com.github.frayeralex.bibiphelp.list_users

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.github.frayeralex.bibiphelp.activities.MainActivity
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.viewModels.ListEventViewModel


class ActivityList : ActivityUserAbstract() {



    override fun createFragment(): Fragment {
        return FragmentList()
    }

    companion object {
        fun backMainActivity(context: Context?): Intent {
            val intent = Intent(context, MainActivity::class.java)
            return intent
        }

    }


}