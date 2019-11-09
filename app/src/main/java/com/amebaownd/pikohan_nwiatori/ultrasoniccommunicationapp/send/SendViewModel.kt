package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.send

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.data.Constant
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

    private var _hexText =MutableLiveData<String>()
    val hexText :LiveData<String> = _hexText

    var _sendEvent = MutableLiveData<Event<Boolean>>(Event(false))
    val sendEvent :LiveData<Event<Boolean>> = _sendEvent

    var _openReceiveEvent = MutableLiveData<Event<Boolean>>(Event(false))
    val openReceiveEvent :LiveData<Event<Boolean>> = _openReceiveEvent

    var isAbleToClickSendButton  = MutableLiveData<Boolean>(true)

    fun onSendButtonClicked(){

        if(!text.value.isNullOrEmpty()){
            val mAudioTrack = AudioTrack(STREM_TYPE, SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, MODE)

            viewModelScope.launch(Dispatchers.IO) {

                isAbleToClickSendButton.postValue(false)

                val byteArray = text.value!!.toByteArray()
                Log.d("BYTE", byteArray.contentToString())
                val hexList = mutableListOf<Int>()
                for (value in byteArray) {
                    hexList.add(value.toInt().ushr(4).and(0x0f))
                    hexList.add(value.toInt().and(0x0f))
                }
                _hexText.postValue(hexList.toString())
                Log.d("BYTE", hexList.toString())

                //add space between same values.
                val hexListWithSpace = mutableListOf<Int>()
                for(i in 0 until hexList.size-1){
                    hexListWithSpace.add(hexList[i])
                    if(hexList[i] == hexList[i+1]){
                        hexListWithSpace.add(Constant.SPACE)
                    }
                }
                hexListWithSpace.add(hexList.last())

                val soundGenerator = SoundGenerator(hexListWithSpace)
                val soundDoubleArray = soundGenerator.getSoundData(Constant.SOUND_LENGTH, 44100)
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

                isAbleToClickSendButton.postValue(true)
                //_sendEvent.value = Event(true)
            }
        }else{
            Log.d("SEND","failed: text is null or empty")
        }
    }

    fun onMoveToReceiveButtonClicked(){
        _openReceiveEvent.value = Event(true)
    }
}