package com.github.frayeralex.bibiphelp.list_users

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.activities.MainActivity
import com.github.frayeralex.bibiphelp.constatns.EventTypes
import com.github.frayeralex.bibiphelp.list_users.SingltonUser.getListEvent
import com.github.frayeralex.bibiphelp.models.EventModel
import com.github.frayeralex.bibiphelp.viewModels.ListEventViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.item_event.view.*


class FragmentList : Fragment() {

    private val viewModel by viewModels <ListEventViewModel>()
    //private lateinit var ownViewModel: ListEventViewModel



    lateinit var mUserAdapter: UserAdapter
    lateinit var myRecyclerView: RecyclerView
    lateinit var mEvents: MutableList<EventModel>
    private var user : FirebaseUser? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
        //ownViewModel = ViewModelProviders.of(this).get(ListEventViewModel::class.java)
//        Log.d ("frag1", "${viewModel.getEvents().value}")
//        mEvents = viewModel.getEvents().value

//        Log.d ("frag1", "11111")
//
//
//    }


    override fun onResume() {
        super.onResume()
        updateUI()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUser().observe(this, Observer<FirebaseUser> { user = it })
       // Log.d("mat999", "${user.toString()}")


        setHasOptionsMenu(true)
    }



    private fun handleEventsUpdated(events: MutableList<EventModel>?) {

//        Log.d("mat999", "${events.toString()}")
//
//        Log.d("mat999", "${events!![0].toString()}")
        //mEvents = events
    }


//    override fun onMapReady(googleMap: GoogleMap) {
//
//
//
//        Log.d ("mat3333", "${viewModel.getEvents().value.toString()}")
//
//

//        viewModel.getLocationData()
//            .observe(this, Observer<Location> { updateMyLocationMarker(it) })
//    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_list, container, false)
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.list_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)





        viewModel.getEvents()
            .observe(this, Observer<MutableList<EventModel>> { handleEventsUpdated(it) })

        Log.d ("fr333", "${handleEventsUpdated(viewModel.getEvents().value)}")

       // Log.d ("fr333", "${mEvents.toString()}")

        mUserAdapter = UserAdapter(SingltonUser.mlistEvents)

        myRecyclerView = view.myRecyclerView
        myRecyclerView.layoutManager = LinearLayoutManager(context)
        myRecyclerView.setHasFixedSize(true)
        updateUI()
        return view
    }

    fun updateUI() {

        myRecyclerView.adapter = mUserAdapter
        mUserAdapter.notifyDataSetChanged()

    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.toolbar_list_activity, menu)

    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_map_view -> {
            val intent = ActivityList.backMainActivity(context)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    inner class UserAdapter(val events: MutableList<EventModel>?) :
        RecyclerView.Adapter<UserHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return UserHolder(layoutInflater.inflate(R.layout.item_event, parent, false))

        }

        override fun getItemCount(): Int {
            Log.d ("getItem", "${events.toString()}")
            return events!!.size
        }

        override fun onBindViewHolder(holder: UserHolder, position: Int) {

            val dataEvent = events!![position]
            holder.bind(dataEvent)
            //var viev: View = holder.itemView
        }

    }

    inner class UserHolder(val view: View) : RecyclerView.ViewHolder(view), OnClickListener {

        init {

            view.setOnClickListener(this)
        }

        lateinit var mdataEvent: EventModel

        fun bind(dataEvent: EventModel?) {
             if (dataEvent != null) {
                mdataEvent = dataEvent
            }
            view.messageInput.text = "${mdataEvent.message}"
            view.type.text = getString(getType(mdataEvent))


            val myBorder = view.frame_item_event.getBackground() as GradientDrawable
            myBorder.setColor(resources.getColor(getTypeColor(mdataEvent)))


           // view.setBackgroundColor(resources.getColor(getTypeColor(mdataEvent)))
        }

        override fun onClick(v: View?) {
            val intent = ActivityUserPager.newIntent(context, mdataEvent.id)
            startActivity(intent)
            Toast.makeText(context, "Hi!!!", Toast.LENGTH_SHORT).show()
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