package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.data.repository.Repository
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.receive.ReceiveViewModel
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.send.SendViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository:Repository

):ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val t = with(modelClass){
            when{
                isAssignableFrom(SendViewModel::class.java)->
                    SendViewModel(repository)
                isAssignableFrom(ReceiveViewModel::class.java)->
                    ReceiveViewModel(repository)
                else->
                    throw IllegalArgumentException("Unknown ViewModelClass $modelClass")
            }
        } as T
        return t
    }
}