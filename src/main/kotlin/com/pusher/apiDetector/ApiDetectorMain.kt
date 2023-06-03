package com.pusher.apiDetector

import burp.api.montoya.BurpExtension
import burp.api.montoya.MontoyaApi

class ApiDetectorMain: BurpExtension {
    override fun initialize(api: MontoyaApi?) {
        if (api == null){
            // Null safety check to avoid errors
            return
        }

        api.extension().setName(EXTENSION_NAME)

        // Create a single ApiDetectorTab instance
        val apiDetectorTab = ApiDetectorTab(api)

//        api.proxy().registerRequestHandler(ApiDetectorHttpRequestHandler(api))
        api.proxy().registerResponseHandler(ApiDetectorHttpResponseHandler(api, apiDetectorTab))
        api.userInterface().registerSuiteTab(EXTENSION_NAME, apiDetectorTab)
    }
}
