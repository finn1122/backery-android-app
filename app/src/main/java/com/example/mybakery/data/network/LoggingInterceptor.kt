package com.example.mybakery.data.network

import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log
import okio.Buffer

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log de la solicitud
        Log.d("Network", "Request URL: ${request.url}")
        Log.d("Network", "Request Method: ${request.method}")
        Log.d("Network", "Request Headers: ${request.headers}")

        // Log del cuerpo de la solicitud en formato JSON
        request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            Log.d("Network", "Request Body: ${buffer.readUtf8()}")
        }

        val response = chain.proceed(request)

        // Log de la respuesta
        Log.d("Network", "Response Code: ${response.code}")
        Log.d("Network", "Response Headers: ${response.headers}")

        return response
    }

}
