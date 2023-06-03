package com.pusher.apiDetector

import burp.api.montoya.MontoyaApi
import burp.api.montoya.proxy.http.InterceptedResponse
import burp.api.montoya.proxy.http.ProxyResponseHandler
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction
import burp.api.montoya.proxy.http.ProxyResponseToBeSentAction
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject

class ApiDetectorHttpResponseHandler(private val api: MontoyaApi) : ProxyResponseHandler {
    override fun handleResponseReceived(interceptedResponse: InterceptedResponse?): ProxyResponseReceivedAction {
        if (interceptedResponse == null) {
            api.logging().logToError("Null response received.  Dropping message.")
            return ProxyResponseReceivedAction.drop()
        }

        if (this.api.scope().isInScope(interceptedResponse.initiatingRequest().url())) {
            api.logging().logToOutput("+ In Scope")
            val interceptedBody = interceptedResponse.bodyToString()
            val interceptedHeaders = interceptedResponse.headers()

            try {
                val json = Json.parseToJsonElement(interceptedBody)
                api.logging().logToOutput("JSON Detected:")
                api.logging().logToOutput("$interceptedHeaders")
                api.logging().logToOutput("$json")

            } catch (jsonException: SerializationException) {
                api.logging().logToOutput("This cannot be parsed as JSON")
            }
        }

        return ProxyResponseReceivedAction.continueWith(interceptedResponse)
    }

    override fun handleResponseToBeSent(interceptedResponse: InterceptedResponse?): ProxyResponseToBeSentAction {
        TODO("Not yet implemented")
    }
}
