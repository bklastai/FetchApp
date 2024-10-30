package com.fetch.interview.network

import android.content.Context
import com.fetch.interview.data.UnprocessedFetchItem
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.chromium.net.CronetEngine
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayInputStream
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalSerializationApi::class)
class CronetRepository(ctx: Context, private val cronetEngine: CronetEngine): RemoteRepository(ctx) {
    private val executor: Executor = Executors.newSingleThreadExecutor()

    override suspend fun getData(): DownloadResult {
        return suspendCoroutine { cont ->
            cronetEngine.newUrlRequestBuilder(
                link,
                object : CronetCallback() {
                    override fun onSucceeded(
                        request: UrlRequest,
                        info: UrlResponseInfo,
                        bodyBytes: ByteArray
                    ) {
                        cont.resume(DownloadResult(
                            _content = Json.decodeFromStream<List<UnprocessedFetchItem>>(
                                ByteArrayInputStream(bodyBytes)
                            ),
                            success = true
                        ))
                    }
                    override fun onFailed(
                        request: UrlRequest?,
                        info: UrlResponseInfo?,
                        error: CronetException?
                    ) {
                        cont.resume(DownloadResult(emptyList(), false, error))
                    }
                },
                executor
            ).build().start()
        }
    }
}