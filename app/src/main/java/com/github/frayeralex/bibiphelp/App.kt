package com.github.frayeralex.bibiphelp

import android.app.Application
import com.github.frayeralex.bibiphelp.cache.SPManager

class App : Application() {

    private var cache: SPManager? = null

    fun getCacheManager() = cache ?: SPManager(this)
}