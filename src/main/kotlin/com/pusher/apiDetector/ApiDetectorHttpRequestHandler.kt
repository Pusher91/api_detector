package com.pusher.apiDetector

import burp.api.montoya.MontoyaApi
import burp.api.montoya.proxy.http.InterceptedRequest
import burp.api.montoya.proxy.http.ProxyRequestHandler
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction

class ApiDetectorHttpRequestHandler(private val api: MontoyaApi) : ProxyRequestHandler {
    override fun handleRequestReceived(interceptedRequest: InterceptedRequest?): ProxyRequestReceivedAction {
        TODO("Not yet implemented")
    }

    override fun handleRequestToBeSent(interceptedRequest: InterceptedRequest?): ProxyRequestToBeSentAction {
        TODO("Not yet implemented")
    }
}