package com.gengqiquan.imui.interfaces

interface IMsgSender {

    fun send(msg: Any, repeat: Boolean = false, senderListener: ISenderListener? = null)

}