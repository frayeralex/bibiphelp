package com.github.frayeralex.bibiphelp.activities

import android.content.Intent
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.models.EventCategoryModel
import com.github.frayeralex.bibiphelp.models.EventCategoryModelUtils
import com.github.frayeralex.bibiphelp.viewModels.CategoriesViewModel
import kotlinx.android.synthetic.main.activity_categories.*


class CategoriesActivity : AppCompatActivity() {

    private val viewModel by viewModels<CategoriesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        setSupportActionBar(findViewById(R.id.toolbar_1))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getCategories()
            .observe(this, Observer<MutableList<EventCategoryModel>> { handleCategoryUpdated(it) })
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            super.onBackPressed()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun handleCategoryUpdated(categories: MutableList<EventCategoryModel>?) {
        categoryList.removeAllViews()

        categories?.forEach {
            val listItem = createCategoryItem(it)

            listItem.addView(createCategoryImage(it))
            listItem.addView(createCategoryLabel(it))

            listItem.setOnClickListener { _ -> handleItemClick(it.id!!, it.label!!) }

            categoryList.addView(listItem)
        }
    }

    private fun handleItemClick(id: Int, categoryLabel: String) {
        val intent = Intent(this, HelpFormActivity::class.java)
        intent.putExtra(CATEGORY_ID_KEY, id)
        intent.putExtra(CATEGORY_LABEL, categoryLabel)
        startActivity(intent)
    }

    private fun createCategoryItem(category: EventCategoryModel): LinearLayout {
        val listItem = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            id = category.id!!
        }


        listItem.setPadding(20, 30, 20, 30)

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

        img.setImageResource(EventCategoryModelUtils.getImgResource(category))

        return img
    }

    companion object {
        const val CATEGORY_ID_KEY = "CATEGORY_ID_KEY"
        const val CATEGORY_LABEL = "CATEGORY_TITLE"
    }
}