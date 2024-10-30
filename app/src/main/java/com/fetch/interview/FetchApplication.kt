package com.fetch.interview

import android.app.Application
import android.content.Context
import android.util.Log
import com.fetch.interview.network.CronetRepository
import com.fetch.interview.network.NativeRemoteRepository
import com.fetch.interview.network.RemoteRepository
import com.google.android.gms.net.CronetProviderInstaller
import org.chromium.net.CronetEngine
import java.util.concurrent.atomic.AtomicReference

class FetchApplication: Application() {

    internal lateinit var remoteRepo: AtomicReference<RemoteRepository>

    override fun onCreate() {
        super.onCreate()
        initRemoteRepo(this)
    }

    private fun initRemoteRepo(ctx: Context) {
        remoteRepo = AtomicReference(NativeRemoteRepository(this))
        CronetProviderInstaller.installProvider(ctx).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i(TAG, "Successfully installed Play Services provider: $it")
                val cronetEngine = CronetEngine.Builder(ctx)
                    .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_IN_MEMORY, TEN_MEGS)
                    .build()
                remoteRepo.set(CronetRepository(ctx, cronetEngine))
            } else {
                Log.w(TAG, "Unable to load Cronet from Play Services", it.exception)
            }
        }
    }

    companion object {
        private const val TEN_MEGS: Long = 10 * 1024 * 1024
        private const val TAG = "FetchApplication"
    }
}