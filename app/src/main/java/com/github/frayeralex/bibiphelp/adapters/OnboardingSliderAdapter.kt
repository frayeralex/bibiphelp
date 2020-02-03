package com.github.frayeralex.bibiphelp.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.github.frayeralex.bibiphelp.R

class OnboardingSliderAdapter(private val context: Context) : PagerAdapter() {

    private val titleList: List<String> = listOf(
        context.getString(R.string.onBoarding_title_1),
        context.getString(R.string.onBoarding_title_2),
        context.getString(R.string.onBoarding_title_3)
    )

    private val descriptionList: List<String> = listOf(
        context.getString(R.string.onBoarding_description_1),
        context.getString(R.string.onBoarding_description_2),
        context.getString(R.string.onBoarding_description_3)
    )

    private val backgroundList: List<Drawable> = listOf(
        context.getDrawable(R.drawable.onBoard_1)!!,
        context.getDrawable(R.drawable.onBoard_2)!!,
        context.getDrawable(R.drawable.onBoard_3)!!
    )

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.slider_layout, container, false) as View

        val slideText = layout.findViewById<TextView>(R.id.sliderTile)
        val sliderDescription = layout.findViewById<TextView>(R.id.sliderDescription)

        slideText.text = titleList[position]
        sliderDescription.text = descriptionList[position]
        layout.background = backgroundList[position]

        container.addView(layout)
        return layout
    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}
