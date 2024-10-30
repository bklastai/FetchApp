package com.fetch.interview.network

import android.content.Context
import com.fetch.interview.data.UnprocessedFetchItem
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

// source: https://github.com/android/codelab-cronet-basics/blob/main/solution/src/main/java/com/google/codelabs/cronet/NativeImageDownloader.kt
@OptIn(ExperimentalSerializationApi::class)
internal class NativeRemoteRepository(ctx: Context): RemoteRepository(ctx) {
    // The blocking suspend fun is always executed from an IO scope. There's no native i/o support
    // for coroutines, we intentionally demonstrate the state before using Cronet.
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getData(): DownloadResult {
        try {
            val url = URL(link)
            val bytesReceived = ByteArrayOutputStream()
            url.openStream().use { it.transfer(bytesReceived) }
            return DownloadResult(
                _content = Json.decodeFromStream<List<UnprocessedFetchItem>>(
                    ByteArrayInputStream(bytesReceived.toByteArray())
                ),
                success = true
            )
        } catch (e: IOException) {
            return DownloadResult(
                _content = emptyList(),
                success = false,
                exception = e
            )
        }
    }
}

// Backport of Java 9's transferTo method.
private fun InputStream.transfer(dest: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (read(buf).also { length = it } > 0) {
        dest.write(buf, 0, length)
    }
}