package com.github.frayeralex.bibiphelp.list_users

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.github.frayeralex.bibiphelp.R
import kotlinx.android.synthetic.main.fragnent_user.view.*
import java.util.*

class FragmentUser : Fragment() {

    companion object {
        val ARG_USER_ID = "user_id_com.gmail2548sov.autohelp"

        fun newInstanse(userId: String?): Fragment {
            val arg = Bundle()
            arg.putSerializable(ARG_USER_ID, userId)
            val fragment = FragmentUser()
            fragment.arguments = arg
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragnent_user, container, false)
        val id = arguments?.getString(ARG_USER_ID)
        val dataEvent = SingltonUser.getId(id)
        view.title_datalied.text = dataEvent?.message ?: ""


        return view
    }




}