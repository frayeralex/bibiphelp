package com.github.frayeralex.bibiphelp.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.github.frayeralex.bibiphelp.adapters.OnboardingSliderAdapter
import com.github.frayeralex.bibiphelp.R
import com.github.frayeralex.bibiphelp.list_users.ActivityList
import com.github.frayeralex.bibiphelp.list_users.SingltonUser


class OnBoardingActivity : AppCompatActivity() {

    private lateinit var slideAdapter: OnboardingSliderAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var sliderDots: LinearLayout
    private lateinit var finishBtn: Button
    private var currentSlide: Int = 0

    init {
        SingltonUser.mlistEvents
        Log.d("fff9", "${SingltonUser.mlistEvents.toString()}")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            currentSlide = savedInstanceState.getInt(POSITION_KEY)
        }

        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.sliderViewPager)
        sliderDots = findViewById(R.id.sliderDots)
        finishBtn = findViewById(R.id.buttonFinish)

        slideAdapter =
            OnboardingSliderAdapter(this)

        viewPager.adapter = slideAdapter

        addDotIndicator(currentSlide)
        updateFinishButtonView(currentSlide)

        viewPager.addOnPageChangeListener(slideListener)
        finishBtn.setOnClickListener{ clickHandler() }
    }

    private fun clickHandler() {
        startActivity(Intent(this, MainActivity::class.java))

        finish()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(POSITION_KEY, currentSlide)
    }

    private fun addDotIndicator(position: Int = 0) {
        sliderDots.removeAllViews()

        for (x in 0 until slideAdapter.count) {
            val dot = TextView(this)
            dot.text = Html.fromHtml("&#8226;")
            dot.textSize = 40.toFloat()

            if (x == position) {
                dot.setTextColor(resources.getColor(R.color.black))
            } else {
                dot.setTextColor(resources.getColor(R.color.blackTransparent))
            }

            sliderDots.addView(dot)
        }
    }

    private val slideListener: ViewPager.OnPageChangeListener = object: ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            currentSlide = position

            addDotIndicator(position)
            updateFinishButtonView(position)
        }
    }

    private fun updateFinishButtonView(position: Int = 0) {
        if (position == slideAdapter.count - 1) {
            finishBtn.isEnabled = true
            finishBtn.visibility = View.VISIBLE
        } else {
            finishBtn.isEnabled = false
            finishBtn.visibility = View.INVISIBLE
        }
    }

    companion object {
        const val POSITION_KEY = "POSITION_KEY"
    }
}
