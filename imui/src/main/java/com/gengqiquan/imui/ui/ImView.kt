package com.gengqiquan.imui.ui

import android.view.View
import android.view.ViewGroup
import com.gengqiquan.imui.interfaces.iDecorator

interface ImView : iDecorator {
    fun get(): View
}