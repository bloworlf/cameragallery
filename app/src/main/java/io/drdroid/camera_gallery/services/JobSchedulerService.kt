package io.drdroid.camera_gallery.services

import android.app.job.JobInfo
import android.app.job.JobInfo.TriggerContentUri
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import io.drdroid.camera_gallery.App


class JobSchedulerService : JobService() {
    private var mContext: Context? = null

    override fun onStartJob(params: JobParameters): Boolean {
        mContext = this
        // Did we trigger due to a content change?
        if (params.triggeredContentAuthorities != null) {
            if (params.triggeredContentUris != null) {
                // If we have details about which URIs changed, then iterate through them
                // and collect either the ids that were impacted or note that a generic
                // change has happened.
                val ids = ArrayList<String>()
                for (uri in params.triggeredContentUris!!) {
                    if (uri != null) {
                        val handler = Handler()
                        handler.post(Runnable {
                            //Perform your task here
                        })
                    }
                }
                jobFinished(params, true) // see this, we are saying we just finished the job
                // We will emulate taking some time to do this work, so we can see batching happen.
                scheduleJob(this)
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }

    companion object {
        private const val ASJOBSERVICE_JOB_ID = 999

        // A pre-built JobInfo we use for scheduling our job.
        private var JOB_INFO: JobInfo? = null
        fun a(context: Context): Int {
            val schedule =
                (context.getSystemService(JobScheduler::class.java) as JobScheduler).schedule(
                    JOB_INFO!!
                )
            Log.i("PhotosContentJob", "JOB SCHEDULED!")
            return schedule
        }

        // Schedule this job, replace any existing one.
        fun scheduleJob(context: Context) {
            if (JOB_INFO != null) {
                a(context)
            } else {
                val js = context.getSystemService(JobScheduler::class.java)
                val builder = JobInfo.Builder(
                    ASJOBSERVICE_JOB_ID,
                    ComponentName(App.instance.packageName, JobSchedulerService::class.java.name)
                )
                builder.addTriggerContentUri(
                    TriggerContentUri(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS
                    )
                )
                builder.addTriggerContentUri(
                    TriggerContentUri(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                        TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS
                    )
                )
                builder.setTriggerContentMaxDelay(500)
                JOB_INFO = builder.build()
                js.schedule(JOB_INFO!!)
            }
        }
    }
}