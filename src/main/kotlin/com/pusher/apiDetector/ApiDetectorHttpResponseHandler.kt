package com.pusher.apiDetector

import burp.api.montoya.MontoyaApi
import burp.api.montoya.proxy.http.InterceptedResponse
import burp.api.montoya.proxy.http.ProxyResponseHandler
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction
import burp.api.montoya.proxy.http.ProxyResponseToBeSentAction
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import burp.api.montoya.ui.editor.EditorOptions
import burp.api.montoya.ui.editor.HttpRequestEditor
import burp.api.montoya.ui.editor.HttpResponseEditor


class ApiDetectorHttpResponseHandler(private val api: MontoyaApi, private val tab: ApiDetectorTab) : ProxyResponseHandler {
    override fun handleResponseReceived(interceptedResponse: InterceptedResponse?): ProxyResponseReceivedAction {
        if (interceptedResponse == null) {
            api.logging().logToError("Null response received.  Dropping response.")
            return ProxyResponseReceivedAction.drop()
        }

        if (this.api.scope().isInScope(interceptedResponse.initiatingRequest().url())) {
            val interceptedBody = interceptedResponse.bodyToString()
            val initiatingRequest = interceptedResponse.initiatingRequest()

            try {
                // Check if response body is valid json
                val json = Json.parseToJsonElement(interceptedBody)

                // Add a row to the table
                val method = initiatingRequest.method()
                val url = initiatingRequest.url().toString()
                val status = interceptedResponse.statusCode().toString()
                val length = interceptedBody.length.toString()
                tab.addInterceptedResponse(interceptedResponse, arrayOf(method, url, status, length))

            } catch (jsonException: SerializationException) {}
        }

        return ProxyResponseReceivedAction.continueWith(interceptedResponse)
    }

    override fun handleResponseToBeSent(interceptedResponse: InterceptedResponse?): ProxyResponseToBeSentAction {
        TODO("Not yet implemented")
    }
}
