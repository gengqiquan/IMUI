package com.gengqiquan.imui.ui

import android.content.Context
import android.view.ViewGroup
import com.gengqiquan.imui.interfaces.IimViewFactory

/**
 *viewType大于1000的类型均为特殊类型，常规类型不要使用
 *@author gengqiquan
 *@date 2019-05-09 15:01
 */
class DefaultIMViewFactory(val context: Context) : IimViewFactory {
    companion object {
        val REVOKE = -1
        val TEXT = 1
        val IMG = 2
        val VIDEO = 3
        val AUDIO = 4
        val SHARE = 5
        val MORE_REFRESH = -9
    }

    override fun create(parent: ViewGroup, viewType: Int): ImView? {
        return when (viewType) {
            REVOKE -> {
                ImRevokeView(context)
            }
            TEXT -> {
                ImTextView(context)
            }
            IMG -> {
                ImImageView(context)
            }
            MORE_REFRESH -> {
                IMLoadMoreView(context)
            }
            else -> {
                ImUnKnowView(context)
            }
        }
    }

}