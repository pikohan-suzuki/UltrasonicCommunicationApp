package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.send

import android.util.Log
import java.lang.IllegalArgumentException

class SoundGenerator(private val soundHexData: List<Int>) {

    fun getSoundData(playMs: Int,samplingRate:Int):Array<Double> {
        val hzList = mutableListOf<Double>()
        val soundTime = samplingRate * playMs/1000
        for(soundHex in soundHexData){
            var t:Double= 0.toDouble()
            val soundHz =getHz(soundHex)
            for(i in 0 until soundTime ){
                hzList.add(Math.sin(2*Math.PI * t * soundHz))
                t+=1f/samplingRate
            }
        }
        Log.d("SEND",hzList.toString())
        return hzList.toTypedArray()
    }


    private fun getHz(hex: Int): Int {
        return when (hex) {
            0 -> 262
            1 -> 294
            2 -> 330
            3 -> 349
            4 -> 392
            5 -> 440
            6 -> 493
            7 -> 494
            8 -> 523
            9 -> 587
            10 -> 659
            11 -> 698
            12 -> 784
            13 -> 880
            14 -> 988
            15 -> 1047
            16 -> 1175
            else ->
                throw IllegalArgumentException("Get not HEX data ${hex}")
        }
    }
}

