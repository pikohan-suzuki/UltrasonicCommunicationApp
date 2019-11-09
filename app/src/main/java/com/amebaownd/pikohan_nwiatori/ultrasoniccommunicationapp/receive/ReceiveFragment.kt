package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.receive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.databinding.FragmentSendBinding
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.databinding.FragmentReceiveBinding

class ReceiveFragment :Fragment(){

    private lateinit var fragmentReceiveBinding: FragmentReceiveBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentReceiveBinding = FragmentReceiveBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return fragmentReceiveBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}