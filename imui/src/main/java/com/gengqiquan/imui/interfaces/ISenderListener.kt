package com.gengqiquan.imui.interfaces

interface ISenderListener {
    fun waiting()
    fun sending()
    fun success()
    fun failure()

}