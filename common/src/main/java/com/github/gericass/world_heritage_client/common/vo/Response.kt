package com.github.gericass.world_heritage_client.common.vo

class Response<out T>(
    val status: Status,
    val data: T?,
    val error: Throwable?
) {
    companion object {
        fun <T> onSuccess(data: T) = Response<T>(Status.SUCCESS, data, null)
        fun <T> onError(e: Throwable) = Response<T>(Status.ERROR, null, e)
    }
}