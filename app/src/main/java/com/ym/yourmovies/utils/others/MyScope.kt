package com.ym.yourmovies.utils.others

inline fun <T>myScope(receiver : T ,block:T.()->Unit ){
       receiver.block()
}