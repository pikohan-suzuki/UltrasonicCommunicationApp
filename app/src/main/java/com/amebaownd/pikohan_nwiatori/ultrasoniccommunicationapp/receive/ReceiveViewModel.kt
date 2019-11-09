package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.receive

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.data.repository.Repository
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.fft.FFT4g
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.data.Constant
import java.lang.IllegalArgumentException
import kotlin.math.abs


class ReceiveViewModel(repository: Repository) : ViewModel() {

    companion object {
        const val FFT_SIZE = 4096

        const val SAMPLING_RATE = 44100
        const val FRAME_RATE = 10
        const val ONE_FRAME_DATA_COUNT = SAMPLING_RATE / FRAME_RATE
        const val ONE_FRAME_SIZE_IN_BYTE = ONE_FRAME_DATA_COUNT * 2
        val audioBufferSizeInByte =
            max(
                ONE_FRAME_SIZE_IN_BYTE * 10,
                AudioRecord.getMinBufferSize(
                    SAMPLING_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
            )
        val bufSize = AudioRecord.getMinBufferSize(
            SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )


    }

    private val _receivedText = MutableLiveData<String>()
    var receivedText: LiveData<String> = _receivedText

    private var dB_baseline = Math.pow(2.0, 15.0) * FFT_SIZE * Math.sqrt(2.0)
    private val mAudioRecord: AudioRecord
    private var isRecording: Boolean = false
    private var fft = FFT4g(FFT_SIZE)
    var resol = SAMPLING_RATE / FFT_SIZE.toDouble()

    init {
        mAudioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            audioBufferSizeInByte
        )
    }

    fun onReceiveButtonClicked() {
        Log.d("RECEIVE", "START ${System.currentTimeMillis()}")
        mAudioRecord.startRecording()
        isRecording = true

        val receivedData = mutableListOf<Int>()
        viewModelScope.launch(Dispatchers.IO) {

            val audioDataArray = ShortArray(bufSize)
            var startFlg = false
            while (isRecording) {
                mAudioRecord.read(audioDataArray, 0, bufSize)
                val doubleDataArray = DoubleArray(FFT_SIZE)
                for (i in 0 until bufSize) {
                    doubleDataArray[i] = audioDataArray[i].toDouble()
                }
                fft.rdft(1, doubleDataArray)

                val dbfs = DoubleArray(FFT_SIZE / 2)
                var max_db = -120.0
                var max_i = 0
                var i = 0
                while (i < FFT_SIZE) {
                    dbfs[i / 2] = ((20 * Math.log10(
                        (Math.sqrt(
                            Math
                                .pow(doubleDataArray[i], 2.0) + Math.pow(
                                doubleDataArray[i + 1],
                                2.0
                            )
                        ) / dB_baseline)
                    ))).toInt().toDouble()
                    if (max_db < dbfs[i / 2]) {
                        max_db = dbfs[i / 2]
                        max_i = i / 2
                    }
                    i += 2
                }
//                Log.d("fft","周波数："+ resol * max_i+" [Hz] 音量：" +  max_db+" [dB]")


                val hz = (resol * max_i).toInt()
                if (startFlg && hz < 4500) {
                    isRecording = false
                } else if (!startFlg && hz >= 4500) {
                    startFlg = true
                }
                if (startFlg) {
                    receivedData.add(hz)
                    _receivedText.postValue(hz.toString() + "Hz")
                }
            }
            mAudioRecord.stop()
            mAudioRecord.release()

            val hzData = samplingData(receivedData)
            val hexData = getHexFromHzList(hzData)
            Log.d("RECEIVE", "receivedData : $hexData")
//            Log.d("RECEIVE", "receivedData : $receivedData")
            Log.d("RECEIVE", "END ${System.currentTimeMillis()}")
        }

    }

    fun onStropButtonClicked() {
        isRecording = false
    }

    private fun samplingData(hzList: List<Int>): List<Int> {
        val result = mutableListOf<Int>()
        val subList = mutableListOf<Int>()
        for (i in hzList) {
            if (subList.isEmpty() || Math.abs(i - subList.average()) < 100) {
                subList.add(i)
            } else {
                result.add(subList.average().toInt())
                subList.clear()
            }
        }
        return result
    }

    private fun getHexFromHzList(hzList: List<Int>): List<Int> {
        val hexList = mutableListOf<Int>()
        for (hz in hzList) {
            hexList.add(
                when {
                    hz in 4750 until 5250 -> 0
                    hz in 5250 until 5750 -> 1
                    hz in 5750 until 6250 -> 2
                    hz in 6250 until 6750 -> 3
                    hz in 6750 until 7250 -> 4
                    hz in 7250 until 7750 -> 5
                    hz in 7750 until 8250 -> 6
                    hz in 8250 until 8750 -> 7
                    hz in 8750 until 9250 -> 8
                    hz in 9250 until 9750 -> 9
                    hz in 9750 until 10250 -> 10
                    hz in 10250 until 10750 -> 11
                    hz in 10750 until 11250 -> 12
                    hz in 11250 until 11750 -> 13
                    hz in 11750 until 12250 -> 14
                    hz in 12250 until 12750 -> 15
                    hz in 14750 until 15250 -> Constant.SPACE
                    else -> throw IllegalArgumentException("unknown hz data $hz")
                }
            )
        }
        hexList.removeAll{it==Constant.SPACE}
        return hexList
    }
}