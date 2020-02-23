package com.github.frayeralex.bibiphelp.activities

import android.os.Bundle
import android.util.Log
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.models.EventCategoryModel
import com.google.firebase.database.*

class CategoriesActivity : AppCompatActivity() {

    private lateinit var categoriesRef: DatabaseReference
    private lateinit var categoryList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        setSupportActionBar(findViewById(R.id.toolbar_1))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        categoryList = findViewById(R.id.category_list)

        categoriesRef = FirebaseDatabase.getInstance().getReference(DB_CATEGORIES)

        categoriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                handleEventChanged(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                handleEventChangeError(error)
            }
        })
    }

    private fun handleEventChanged(dataSnapshot: DataSnapshot) {
        for (eventSnapshot in dataSnapshot.children) {
            val category = eventSnapshot.getValue(EventCategoryModel::class.java)

            if (category != null) {
                val listItem = createCategoryItem(category)

                listItem.addView(createCategoryImage(category))
                listItem.addView(createCategoryLabel(category))

                listItem.setOnClickListener { handleItemClick(it) }

                categoryList.addView(listItem)
            }
        }
    }

    private fun handleItemClick(view: View?) {
        // todo add start activity with selected category id
        Log.d(TAG, view?.id.toString())
    }

    private fun createCategoryItem(category: EventCategoryModel): LinearLayout {
        val listItem = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            id = category.id!!
        }


        listItem.setPadding(20,30,20,30)

        listItem.setBackgroundColor(Color.WHITE)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(16, 8, 16, 8)

        listItem.layoutParams = params

        listItem.gravity = Gravity.CENTER_VERTICAL

        return listItem
    }

    private fun createCategoryLabel(category: EventCategoryModel) = TextView(this).apply {
        text = category.label
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun createCategoryImage(category: EventCategoryModel): ImageView {
        val img = ImageView(this)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 0, 25, 0)

        img.layoutParams = params

        img.setImageResource(category.getImgResource())

        return img
    }

    private fun handleEventChangeError(error: DatabaseError) {
        Log.d(TAG, "Failed to read categories values.", error.toException())
    }

    companion object {
        const val TAG = "CATEGORIES_ACTIVITY"
        const val DB_CATEGORIES = "categories"
    }
}