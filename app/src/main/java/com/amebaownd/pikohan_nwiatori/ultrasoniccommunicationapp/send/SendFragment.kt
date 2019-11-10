package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.send

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.databinding.FragmentReceiveBinding
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.databinding.FragmentSendBinding
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.util.EventObserver
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.util.getViewModelFactory

class SendFragment :Fragment(){

    private val viewModel :SendViewModel by viewModels { getViewModelFactory() }

    private lateinit var fragmentSendBinding: FragmentSendBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSendBinding = FragmentSendBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel =this@SendFragment.viewModel
        }
        return fragmentSendBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        onSendEvent()
        openReceive()
    }

    private fun onSendEvent(){

    }

    private fun openReceive(){
        viewModel.openReceiveEvent.observe(this,EventObserver{
            if(it) {
                val action = SendFragmentDirections
                    .actionSendFragmentToReceiveFragment()
                findNavController().navigate(action)
            }
        })
    }
}