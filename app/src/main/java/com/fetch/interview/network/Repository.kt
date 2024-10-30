package com.fetch.interview.network

import android.content.Context
import com.fetch.interview.R

abstract class RemoteRepository(ctx: Context) {
    internal val link = ctx.resources.getString(R.string.link_for_things)
    abstract suspend fun getData(): DownloadResult
}