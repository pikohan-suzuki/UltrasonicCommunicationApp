package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.util

import androidx.fragment.app.Fragment
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository =
        ServiceLoader.provideRepository(requireContext())
    return ViewModelFactory(repository)
}