package com.neko.hiepdph.monster_voice.common

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Torch {
    private var cameraManager: CameraManager? = null
    private var context: Context? = null
    var speed = 0.0
    var job: Job? = null
    var scope = CoroutineScope(Dispatchers.Default)
    private var isFlashOn: Boolean = false
    private var flash = false

    fun flashLight(cameraManager: CameraManager?, context: Context?) {
        this.cameraManager = cameraManager
        this.context = context
    }

    fun flashLightOn() {
        try {
            val cameraId = cameraManager!!.cameraIdList[0]
            cameraManager!!.setTorchMode(cameraId, true)
            isFlashOn = true
        } catch (_: Exception) {
        }
    }

    fun flashLightOff() {
        try {
            val cameraId = cameraManager!!.cameraIdList[0]
            cameraManager!!.setTorchMode(cameraId, false)
            isFlashOn = false
        } catch (_: Exception) {
        }
    }

    fun updateSpeed() {
        val sos_speed = 50.0
        speed = 1 / (sos_speed / 100);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun strobe(frequency: Long) {
        //strobe thread
        flash = true
        job =scope.launch {
            try {
                while (flash) {
                    flashLightOn()
                    delay(100)
                    flashLightOff()
                    delay(100)
                    delay(frequency)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun strobocancel() {
        //interrupt thread
        scope.launch {
            flash=false
            job?.cancelAndJoin()
            flashLightOff()
        }
    }

}