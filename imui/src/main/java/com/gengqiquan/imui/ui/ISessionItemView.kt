package com.gengqiquan.imui.ui

import android.content.Context
import android.view.View

/**
 * @author mazhichao
 * @date 2019/05/13 15:53
 */
interface ISessionItemView {
    fun createView(context: Context):View
    fun bindView(item:ISession)
}