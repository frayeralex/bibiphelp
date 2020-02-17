package com.github.frayeralex.bibiphelp.list_users
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.list_users.SingltonUser.getListEvent
import com.github.frayeralex.bibiphelp.models.EventModel
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.item_events.view.*
import kotlin.collections.ArrayList

class FragmentList : Fragment() {

    val mUserAdapter: UserAdapter = UserAdapter(getListEvent())
    lateinit var myRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_list, container, false)

        myRecyclerView = view.myRecyclerView


        myRecyclerView.layoutManager = LinearLayoutManager(context)
        myRecyclerView.setHasFixedSize(true)
        updateUI()

        return view
    }

    fun updateUI() {

        myRecyclerView.adapter = mUserAdapter
        Log.d ("fff4", "${SingltonUser.mlistEvents.toString()}")

        mUserAdapter.notifyDataSetChanged()

   }

    override fun onResume() {
        super.onResume()
        updateUI()
        //mUserAdapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class UserAdapter(val dataEvents: ArrayList<EventModel?>) : RecyclerView.Adapter<UserHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return UserHolder(layoutInflater.inflate(R.layout.item_events, parent, false))
            Log.d ("fff1", "${dataEvents[2].toString()}")

        }

        override fun getItemCount(): Int {
            return dataEvents.size
        }

        override fun onBindViewHolder(holder: UserHolder, position: Int) {

            val dataEvent = dataEvents[position]
            Log.d ("eee", "${position}2")
            holder.bind(dataEvent)
            //var viev: View = holder.itemView
        }

    }

    inner class UserHolder(val view: View) : RecyclerView.ViewHolder(view), OnClickListener
    {

        init {

            view.setOnClickListener(this)
            Log.d("fff2","222")
        }

        lateinit var mdataEvent: EventModel

        fun bind(dataEvent:EventModel?) {
            if (dataEvent != null) {
                mdataEvent = dataEvent
            }
            view.message.text = "${mdataEvent.message}"
            view.type.text = getString(getType(mdataEvent))
            view.frame_item_event.setBackgroundColor(resources.getColor(getTypeColor(mdataEvent)))
        }

        override fun onClick(v: View?) {
            val intent = ActivityUserPager.newIntent(context, mdataEvent.id)
            startActivity(intent)
            Toast.makeText(context,"Hi!!!",Toast.LENGTH_SHORT).show()
        }

        private fun getType(event: EventModel) = when (event.type) {
            "type_1" -> R.string.srun_out_of_fuel
            "type_2" -> R.string.swheel_replacement
            "type_3" -> R.string.slow_battery
            "type_4" -> R.string.sstuck_in_the_snow
            "type_5" -> R.string.stowing
            else -> R.string.sother
        }

        private fun getTypeColor (event: EventModel) = when (event.type) {
            "type_1" -> R.color.run_out_of_fuel
            "type_2" -> R.color.wheel_replacement
            "type_3" -> R.color.low_battery
            "type_4" -> R.color.stuck_in_the_snow
            "type_5" -> R.color.towing
            else -> R.color.other
        }



    }


}