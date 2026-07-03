package com.fishing.android

import android.content.Context
import android.os.Build
import android.os.VibratorManager
import android.os.Vibrator

fun vibrateDevice(context: Context, pattern: LongArray = longArrayOf(0, 200, 100, 200)) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
}