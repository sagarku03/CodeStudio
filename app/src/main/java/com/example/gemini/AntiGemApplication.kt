package com.example.gemini

import android.app.Application
import android.util.Log

class AntiGemApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            com.example.gemini.data.updater.UpdateCheckWorker.schedule(this)
        } catch (e: Throwable) {
            Log.e("CodeStudio", "Worker schedule skipped: ${e.message}")
        }
    }
}
