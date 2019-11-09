package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.send

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.data.repository.Repository
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendViewModel(repository: Repository): ViewModel(){
    companion object{
        const val STREM_TYPE = AudioManager.STREAM_MUSIC
        const val SAMPLE_RATE_IN_HZ = 44100
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        val BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT)
        const val MODE = AudioTrack.MODE_STREAM
    }

    var text = MutableLiveData<String>()

    var _sendEvent = MutableLiveData<Event<Boolean>>(Event(false))
    val sendEvent :LiveData<Event<Boolean>> = _sendEvent

    fun onSendButtonClicked(){

        if(!text.value.isNullOrEmpty()){
            val mAudioTrack = AudioTrack(STREM_TYPE, SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, MODE)

            viewModelScope.launch(Dispatchers.IO) {
                val byteArray = text.value!!.toByteArray()
                Log.d("BYTE", byteArray.contentToString())
                val hexList = mutableListOf<Int>()
                for (index in byteArray.indices) {
                    hexList.add(byteArray[index].toInt().ushr(4).and(0x0f))
                    hexList.add(byteArray[index].toInt().and(0x0f))
                }
                Log.d("BYTE", hexList.toString())

                val soundGenerator = SoundGenerator(hexList)
                val soundDoubleArray = soundGenerator.getSoundData(200, 44100)
                val soundShortList = mutableListOf<Short>()
                for(dlValue in soundDoubleArray){
                    soundShortList.add((dlValue * Short.MAX_VALUE).toShort())
                }

                mAudioTrack.play()
                Log.d("SEND","Start sending.")
                Log.d("SEND",soundShortList.toString())
                mAudioTrack.write(soundShortList.toTypedArray().toShortArray(),0,soundShortList.size)
                Log.d("SEND","End sending.")
                mAudioTrack.stop()
                mAudioTrack.flush()
                //_sendEvent.value = Event(true)
            }
        }else{
            Log.d("SEND","failed: text is null or empty")
        }
    }
}