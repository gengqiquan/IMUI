package com.gengqiquan.imlib.manager

import com.tencent.imsdk.ext.message.TIMManagerExt

/**
 * @author mazhichao
 * @date 2019/05/13 18:04
 */
class SessionManager {
    fun getSessionList(){
        TIMManagerExt.getInstance().conversationList
    }
}