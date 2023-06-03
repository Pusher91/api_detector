package com.pusher.apiDetector

import burp.api.montoya.BurpExtension
import burp.api.montoya.MontoyaApi

class ApiDetectorMain: BurpExtension {
    override fun initialize(api: MontoyaApi?) {
        if (api == null){
//            Null safety check to avoid errors
            return
        }

        api.extension().setName("Api Detector123")

        api.proxy().registerResponseHandler(ApiDetectorHttpResponseHandler(api))
    }
}