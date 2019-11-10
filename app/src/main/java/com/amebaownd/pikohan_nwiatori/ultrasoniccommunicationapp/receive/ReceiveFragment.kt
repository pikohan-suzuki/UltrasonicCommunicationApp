package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.receive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.databinding.FragmentReceiveBinding
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.util.getViewModelFactory

class ReceiveFragment :Fragment(){

    private val viewModel:ReceiveViewModel by viewModels{getViewModelFactory()}

    private lateinit var fragmentReceiveBinding: FragmentReceiveBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentReceiveBinding = FragmentReceiveBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ReceiveFragment.viewModel
        }
        return fragmentReceiveBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}