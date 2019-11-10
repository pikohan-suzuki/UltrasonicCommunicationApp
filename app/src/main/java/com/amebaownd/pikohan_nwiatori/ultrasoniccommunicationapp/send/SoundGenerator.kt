package com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.send

import android.util.Log
import com.amebaownd.pikohan_nwiatori.ultrasoniccommunicationapp.data.Constant
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
            0 -> Constant.HZ_0
            1 -> Constant.HZ_1
            2 -> Constant.HZ_2
            3 -> Constant.HZ_3
            4 -> Constant.HZ_4
            5 -> Constant.HZ_5
            6 -> Constant.HZ_6
            7 -> Constant.HZ_7
            8 -> Constant.HZ_8
            9 -> Constant.HZ_9
            10 -> Constant.HZ_A
            11 -> Constant.HZ_B
            12 -> Constant.HZ_C
            13 -> Constant.HZ_D
            14 -> Constant.HZ_E
            15 -> Constant.HZ_F

            Constant.SPACE_CODE  ->  Constant.HZ_SPACE
            else ->
                throw IllegalArgumentException("Get not HEX data ${hex}")
        }
    }
}

