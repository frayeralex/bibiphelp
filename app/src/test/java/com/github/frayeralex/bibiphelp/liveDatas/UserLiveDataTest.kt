package com.github.frayeralex.bibiphelp.liveDatas

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Assert.assertNotNull
import org.junit.Before

class UserLiveDataTest {

    private var liveData : UserLiveData? = null

    @Before
    fun setup() {
        liveData = UserLiveData()
    }

    @Test
    fun onActive() {
        assertNotNull(liveData)
        assertEquals(liveData?.value, null)
    }
}