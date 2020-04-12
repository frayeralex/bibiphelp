package com.github.frayeralex.bibiphelp.viewModels

import org.junit.Test
import org.junit.Before
import org.mockito.MockitoAnnotations
import com.github.frayeralex.bibiphelp.repository.FBRefs
import com.github.frayeralex.bibiphelp.repository.FBRefsInterface
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class CloseEventViewModelTest {

    val mock = mock<FBRefsInterface> {
        on { eventsRef } doReturn { fun child() {} } as DatabaseReference
    }

    private var viewModel : CloseEventViewModel? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = CloseEventViewModel()
    }

    @Test
    fun getRequestingState() {
        Assert.assertNotNull(viewModel?.getRequestingState())
    }

    @Test
    fun getIsClosed() {
        Assert.assertNotNull(viewModel?.closeEvent("testId", 1))
        verify(mock.eventsRef, times(1)).child("testId")
    }

    @Test
    fun closeEvent() {
        Assert.assertNotNull(viewModel?.getIsClosed())
    }
}