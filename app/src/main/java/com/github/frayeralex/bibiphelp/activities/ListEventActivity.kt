package com.github.frayeralex.bibiphelp.activities

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
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.constatns.IntentExtra
import com.github.frayeralex.bibiphelp.constatns.RequestStatuses
import com.github.frayeralex.bibiphelp.utils.EventCategoryModelUtils
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.utils.DistanceCalculator
import com.github.frayeralex.bibiphelp.viewModels.ListEventViewModel
import kotlinx.android.synthetic.main.item_event.view.*
import kotlinx.android.synthetic.main.list_event.*

class ListEventActivity : AppCompatActivity() {

    private val viewModel by viewModels<ListEventViewModel>()
    val mEventAdapter = EventAdapter()
    var myLocation: Location? = null

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
        myRecyclerView.setHasFixedSize(true)

        viewModel.getEvents()
            .observe(this, Observer<MutableList<EventModel>> { mEventAdapter.refreshEvents(it) })

        viewModel.getLocationData()
            .observe(this, Observer<Location> {
                myLocation = it
                mEventAdapter.refreshDistance(it)
            })
        viewModel.getEventsRequestStatus()
            .observe(this, Observer<String> { handleEventRequestStatus(it) })
    }

    private fun handleEventRequestStatus(status: String) {
        when (status) {
            RequestStatuses.PENDING -> {
                progressBar.isVisible = true
            }
            RequestStatuses.SUCCESS -> {
                progressBar.isVisible = false
            }
            RequestStatuses.FAILURE -> {
                progressBar.isVisible = false
                Toast.makeText(
                    baseContext, R.string.error_common,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
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
        }

        fun refreshEvents(events: MutableList<EventModel>) {
            events.sortWith(compareBy { it ->
                DistanceCalculator.distance(
                    it.lat ?: 0.0,
                    it.long ?: 0.0,
                    myLocation?.latitude ?: 0.0,
                    myLocation?.longitude ?: 0.0
                )
            })
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

        fun bind(dataEvent: EventModel, myLocation: Location?) {
            if (dataEvent != null) {
                mdataEvent = dataEvent
            }
            view.messageInput.text = mdataEvent.message
            view.type.text = getString(EventCategoryModelUtils.getTypeLabel(mdataEvent))

            mDistance = DistanceCalculator.distance(
                dataEvent.lat ?: 0.0,
                dataEvent.long ?: 0.0,
                myLocation?.latitude ?: 0.0,
                myLocation?.longitude ?: 0.0
            )
            view.distance.text = resources.getString(
                R.string.distance_km!!,
                DistanceCalculator.formatDistance(mDistance)
            )

            val myBorder = view.frame_item_event.getBackground() as GradientDrawable
            myBorder.setColor(resources.getColor(EventCategoryModelUtils.getTypeColor(mdataEvent)))
        }

        override fun onClick(v: View?) {
            val intent = Intent(this@ListEventActivity, EventDetails::class.java)
            intent.putExtra(IntentExtra.eventId, mdataEvent.id)
            intent.putExtra(IntentExtra.eventDistance, mDistance)
            startActivity(intent)
        }
    }
}