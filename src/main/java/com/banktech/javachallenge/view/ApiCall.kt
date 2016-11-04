package com.banktech.javachallenge.view

import com.banktech.javachallenge.service.domain.game.SimpleResponse

data class ApiCall(var method: String, var url: String, var response: SimpleResponse)