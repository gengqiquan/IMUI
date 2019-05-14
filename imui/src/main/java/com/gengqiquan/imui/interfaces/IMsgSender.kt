package com.gengqiquan.imui.interfaces

interface IMsgSender {

    fun send(msg: Any, senderListener: ISenderListener?=null)

}