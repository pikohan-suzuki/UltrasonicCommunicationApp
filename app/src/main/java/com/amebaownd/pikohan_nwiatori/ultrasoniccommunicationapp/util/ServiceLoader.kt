package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.util

import android.content.Context
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.data.repository.Repository

object ServiceLoader {
    var repository: Repository?=null

    fun provideRepository(context: Context):Repository{
        synchronized(this){
            return repository ?:
                    createRepository(context)
        }
    }

    private fun createRepository(context:Context):Repository{
        val result = Repository()
        repository = result
        return result
    }
}