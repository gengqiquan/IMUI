package com.gengqiquan.imlib

import android.view.ViewGroup
import com.gengqiquan.imui.interfaces.IimViewFactory
import com.gengqiquan.imui.ui.DefaultIMViewFactory
import com.gengqiquan.imui.ui.ImView

class TIMViewFactory : IimViewFactory {

    /**
     *viewType大于1000的类型均为特殊类型，常规类型不要使用
     *@author gengqiquan
     *@date 2019-05-09 15:01
     */
    override fun create(parent: ViewGroup, viewType: Int): ImView? {
        val context = parent.context
        if (viewType > 999) {
            return when (viewType / 1000) {
                1 -> {
                    ImPreSendView(context)
                }
                else -> null
            }
        }
        return when (viewType) {
            DefaultIMViewFactory.AUDIO -> {
                ImAudioView(context)
            }
            DefaultIMViewFactory.VIDEO -> {
                ImVideoView(context)
            }
            DefaultIMViewFactory.SHARE -> {
                ImShareView(context)
            }
            else -> null
        }
    }

}