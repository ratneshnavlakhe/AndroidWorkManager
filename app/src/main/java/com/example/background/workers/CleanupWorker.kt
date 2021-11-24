package com.example.background.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import java.io.File
import java.lang.Exception

class CleanupWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        makeStatusNotification("Cleaning up old temporary files", applicationContext)
        sleep()

        return try {
            val outPutDirectory = File(applicationContext.filesDir, OUTPUT_PATH)

            if (outPutDirectory.exists()) {
                val entries = outPutDirectory.listFiles()

                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name

                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Log.i(TAG, "Deleted $name - $deleted")
                        }
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}