package com.example.testowytestownik.data.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import kotlin.math.abs


object BzztMachen{

    private val client = HttpClient(Android){
        install(HttpTimeout){
            requestTimeoutMillis = 1000
        }
    }

    suspend fun dunno(url: String = "http://bzztmachen.local/machen"): Result<String>{
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

    fun lvlFromPercent(percent: Int): Int{
        var lvl = abs(percent) % 100
        lvl = 1000 - 950 * lvl
        return lvl
    }

    suspend fun machen(
        url: String = "http://bzztmachen.local/machen",
        lvl: Int = 1000, // 50<lvl<1000, clipped otherwise (50 hits hardest), just use lvlFromPercent
        player: Int = 0
    ): Result<String> {
        var freq = 50
        if (lvl>50){ freq = lvl%1001}
        return try {
            val body = withTimeout(1000L){
                val response = client.post(url){
                    setBody("$player,$freq")
                }
                response.bodyAsText()
            }
            Result.success((body))
        } catch (e:TimeoutCancellationException) {
            Result.failure(Exception("Timeout"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}