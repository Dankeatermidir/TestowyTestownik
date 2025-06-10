package com.example.testowytestownik.data.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout


object BzztMachen{

    private val client = HttpClient(Android){
        install(HttpTimeout){
            requestTimeoutMillis = 1000
        }
    }

    suspend fun machen(url: String = "http://bzztmachen.local:6969", freq: Int = 1000): Result<String>{
        return try {
            val body = withTimeout(1000L){
                val response = client.get(url)
                response.bodyAsText()
            }
            Result.success((body))
        } catch (e:TimeoutCancellationException) {
            Result.failure(Exception("Timeout"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun close() {
        client.close()
    }

}