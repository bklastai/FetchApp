package com.fetch.interview.network

import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels

// Source: https://github.com/android/codelab-cronet-basics/blob/main/solution/src/main/java/com/google/codelabs/cronet/ReadToMemoryCronetCallback.kt
internal abstract class CronetCallback : UrlRequest.Callback() {
    private val bytesReceived = ByteArrayOutputStream()
    private val receiveChannel = Channels.newChannel(bytesReceived)

    final override fun onRedirectReceived(
        request: UrlRequest, info: UrlResponseInfo?, newLocationUrl: String?
    ) {
        request.followRedirect()
    }

    final override fun onResponseStarted(request: UrlRequest, info: UrlResponseInfo) {
        request.read(ByteBuffer.allocateDirect(SIXTY_FOUR_KILOBYTES))
    }

    final override fun onReadCompleted(
        request: UrlRequest, info: UrlResponseInfo, byteBuffer: ByteBuffer
    ) {
        byteBuffer.flip()
        receiveChannel.write(byteBuffer)
        byteBuffer.clear()
        request.read(byteBuffer)
    }

    final override fun onSucceeded(request: UrlRequest, info: UrlResponseInfo) {
        val bodyBytes = bytesReceived.toByteArray()
        onSucceeded(request, info, bodyBytes)
    }

    abstract fun onSucceeded(request: UrlRequest, info: UrlResponseInfo, bodyBytes: ByteArray)

    companion object {
        private const val SIXTY_FOUR_KILOBYTES = 64 * 1024
    }
}