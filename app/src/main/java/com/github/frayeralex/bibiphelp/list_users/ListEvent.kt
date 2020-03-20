package com.github.frayeralex.bibiphelp.list_users

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.activities.EventDetails
import com.github.frayeralex.bibiphelp.activities.MainActivity
import com.github.frayeralex.bibiphelp.constatns.EventTypes
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.utils.DistanceCalculator
import com.github.frayeralex.bibiphelp.viewModels.ListEventViewModel
import kotlinx.android.synthetic.main.item_event.view.*
import kotlinx.android.synthetic.main.list_event.*

class ListEvent : AppCompatActivity() {

//    private val viewModel by lazy {
//        ViewModelProviders.of(this).get(ListEventViewModel::class.java)
//    }

    private val viewModel by viewModels<ListEventViewModel>()

    val mEventAdapter = EventAdapter()


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_list_activity, menu)
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_event)


        setSupportActionBar(list_toolbar)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = mEventAdapter

        viewModel.getEvents()
            .observe(this, Observer<MutableList<EventModel>> { mEventAdapter.refreshEvents(it) })

        viewModel.getLocationData()
            .observe(this, Observer<Location> { mEventAdapter.refreshDistance(it) })

        myRecyclerView.setHasFixedSize(true)
    }


    override fun
            onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_map_view -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    inner class EventAdapter : RecyclerView.Adapter<EventHolder>() {

        var events: MutableList<EventModel> = ArrayList()
        lateinit var myLocation: Location
        var distance: Double = 0.0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return EventHolder(layoutInflater.inflate(R.layout.item_event, parent, false))

        }

        override fun getItemCount(): Int {
            return events.size
        }


        override fun onBindViewHolder(holder: EventHolder, position: Int) {
            val dataEvent = events[position]
            holder.bind(dataEvent, myLocation)
            //var viev: View = holder.itemView
        }

        fun refreshEvents(events: MutableList<EventModel>) {
            this.events = events
            notifyDataSetChanged()
        }

        fun refreshDistance(mylocation: Location) {
            myLocation = mylocation
            notifyDataSetChanged()
        }

    }

    inner class EventHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        var mDistance: Double = 0.0

        lateinit var mdataEvent: EventModel
        private var selectedEventId: String? = null


        fun bind(dataEvent: EventModel, myLocation: Location) {
            if (dataEvent != null) {
                mdataEvent = dataEvent
            }
            view.messageInput.text = "${mdataEvent.message}"
            view.type.text = getString(getType(mdataEvent))

            // *DISTANCE*********************************************
            mDistance = DistanceCalculator.distance(
                dataEvent.lat ?: 0.0,
                dataEvent.long ?: 0.0,
                myLocation.latitude,
                myLocation.longitude
            )
            view.distance.text = resources.getString(
                R.string.distance_km!!,
                DistanceCalculator.formatDistance(mDistance)
            )


            val myBorder = view.frame_item_event.getBackground() as GradientDrawable
            myBorder.setColor(resources.getColor(getTypeColor(mdataEvent)))

            // view.setBackgroundColor(resources.getColor(getTypeColor(mdataEvent)))
        }

        override fun onClick(v: View?) {
            val intent = Intent(this@ListEvent, EventDetails::class.java)
            intent.putExtra(IntentExtra.eventId, mdataEvent.id)
            intent.putExtra(IntentExtra.eventDistance, mDistance)
            startActivity(intent)
            Toast.makeText(this@ListEvent, "Hi!!!", Toast.LENGTH_SHORT).show()
        }

        private fun getType(event: EventModel) = when (event.type) {
            EventTypes.OIL -> R.string.srun_out_of_fuel
            EventTypes.WHEEL -> R.string.sheel_replacement
            EventTypes.ENERGY -> R.string.slow_battery
            EventTypes.SNOW -> R.string.sstuck_in_the_snow
            EventTypes.TOWING -> R.string.stowing
            else -> R.string.sother
        }

        private fun getTypeColor(event: EventModel) = when (event.type) {
            EventTypes.OIL -> R.color.run_out_of_fuel
            EventTypes.WHEEL -> R.color.wheel_replacement
            EventTypes.ENERGY -> R.color.low_battery
            EventTypes.SNOW -> R.color.stuck_in_the_snow
            EventTypes.TOWING -> R.color.towing
            else -> R.color.other
        }


    }

}