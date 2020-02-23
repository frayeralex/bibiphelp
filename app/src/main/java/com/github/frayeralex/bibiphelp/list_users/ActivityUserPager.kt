package com.github.frayeralex.bibiphelp.list_users

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.activities.MainActivity
import com.github.frayeralex.bibiphelp.list_users.SingltonUser.mlistEvents
import com.github.frayeralex.bibiphelp.models.EventModel
import kotlinx.android.synthetic.main.activiti_user_page.*
import java.util.*

class ActivityUserPager : AppCompatActivity() {

    companion object {

        val EXTRA_ID: String = "id_com.gmail2548sov.autohelp"

        fun newIntent(context: Context?, userId: String?): Intent {
            val intent = Intent(context, ActivityUserPager::class.java)
            intent.putExtra(EXTRA_ID, userId)
            return intent
        }
    }

    val listEvents: List<EventModel?> = mlistEvents

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiti_user_page)

        val userId = intent.getStringExtra(EXTRA_ID)
        val pagerUser = pager_user
        val fragmentManager = supportFragmentManager
        pagerUser.adapter = (object : FragmentStatePagerAdapter(fragmentManager) {


            override fun getItem(position: Int): Fragment {
                val dataEvent = listEvents.get(position)
                return FragmentUser.newInstanse(dataEvent?.id)
            }


            override fun getCount(): Int {
                return listEvents.size
            }

        })

        for (i in 0..listEvents.size) {

            if (listEvents[i]?.id == userId) {
                pager_user.setCurrentItem(i)
                break
            }


        }


    }


}