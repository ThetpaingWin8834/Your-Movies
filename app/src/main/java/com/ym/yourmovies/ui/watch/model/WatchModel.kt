package com.ym.yourmovies.ui.watch.model

data class Server(
    val serverName :String,
    val serverIcon :String
)
data class WatchModel(
   val server: Server,
    val quality :String,
    val url :String
)