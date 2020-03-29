package com.github.frayeralex.bibiphelp.cache

import com.github.frayeralex.bibiphelp.App

class SPManager(context: App) : BaseSharedPreferencesManager(context, file) {

    var lastMapLat = getFloat(LAST_MAP_LAT, 49.444431f)
        set(value) {
            saveFloat(LAST_MAP_LAT, value)
        }

    var lastMapLong = getFloat(LAST_MAP_LONG, 32.059769f)
        set(value) {
            saveFloat(LAST_MAP_LONG, value)
        }

    var isOnBoarded = getBoolean(IS_ON_BOARDED)
        set(value) {
            saveBoolean(IS_ON_BOARDED, value)
        }

    var activeHelpRequest = getString(ACTIVE_HELP_REQUEST)
        set(value) {
            saveString(ACTIVE_HELP_REQUEST, value)
        }

    var meActiveHelperForEvent = getString(ME_ACTIVE_HELPER)
        set(value) {
            saveString(ME_ACTIVE_HELPER, value)
        }

    fun resetActivityHelp() {
        saveString(ACTIVE_HELP_REQUEST, DEFAULT_STRING)
    }

    fun resetMeHelpForEvent() {
        saveString(ME_ACTIVE_HELPER, DEFAULT_STRING)
    }

    companion object {
        const val file = "main"
        const val LAST_MAP_LAT = "LAST_MAP_LAT"
        const val LAST_MAP_LONG = "LAST_MAP_LONG"
        const val IS_ON_BOARDED = "IS_ON_BOARDED"
        const val ACTIVE_HELP_REQUEST = "ACTIVE_HELP_REQUEST"
        const val ME_ACTIVE_HELPER = "ME_ACTIVE_HELPER"
    }
}