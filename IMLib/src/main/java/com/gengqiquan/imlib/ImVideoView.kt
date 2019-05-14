package com.gengqiquan.imlib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.IdRes
import com.gengqiquan.imlib.video.VideoViewActivity
import com.gengqiquan.imlib.video.util.FileUtil
import com.gengqiquan.imui.help.IMHelp
import com.gengqiquan.imui.interfaces.IimMsg
import com.gengqiquan.imui.ui.*
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMValueCallBack
import com.tencent.imsdk.TIMVideoElem
import com.tencent.imsdk.conversation.ProgressInfo
import org.jetbrains.anko.*

class ImVideoView(context: Context) : RealImView(context) {
    var iv_img: ImageView? = null
    var iv_paly: ImageView? = null
    var iv_loading: ImageView? = null
    var fl_content: FrameLayout? = null
    //    var ll_content: RelativeLayout? = null
    override fun floatBaseView(): View = iv_img!!

    @SuppressLint("ResourceType")
    @IdRes
    val localId = 0xff9900

    override fun createItemView(contentView: FrameLayout): View {
        return contentView.apply {
            relativeLayout {
                layoutParams = RelativeLayout.LayoutParams(matchParent, wrapContent)
                gravity = Gravity.CENTER_VERTICAL

                fl_content = frameLayout {
                    id = localId
                    layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent)
                    iv_img = imageView {
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                        layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
                    }
                    iv_paly = imageView {
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                        setImageResource(R.drawable.im_play)
                        visibility = View.GONE
                        layoutParams = FrameLayout.LayoutParams(dip(50), dip(50)).apply {
                            gravity = Gravity.CENTER
                        }
                    }

                }
                iv_loading = imageView {
                    backgroundColor = Color.BLACK
                    layoutParams = RelativeLayout.LayoutParams(dip(30), dip(30))
//                    visibility = View.GONE
                }
            }


        }
    }

    fun loading() {
        iv_loading?.show()
        iv_loading?.setImageDrawable(CircularProgressDrawable(context).apply {
            start()
            setStyle(CircularProgressDrawable.DEFAULT)
            arrowScale = 0.5f
            strokeWidth = dip(3).toFloat()
            setColorSchemeColors(Color.GRAY)
        })
    }

    override fun fail() {
        super.fail()
        iv_loading?.gone()
    }

    override fun success() {
        super.success()
        Log.d("RealImView","自容器success")
        iv_loading?.gone()
    }

    override fun decoratorItemView(item: IimMsg) {
        fl_content?.layoutParams = (fl_content?.layoutParams as RelativeLayout.LayoutParams).apply {
            if (item.isSelf()) alignParentRight() else alignParentLeft()
        }
        iv_loading?.layoutParams = (iv_loading?.layoutParams as RelativeLayout.LayoutParams).apply {
            if (item.isSelf()) leftOf(fl_content!!) else rightOf(fl_content!!)
            centerVertically()
        }
        Log.d("RealImView","自容器decoratorItemView")
        iv_loading?.isShow(item.status() == 1)
        val video = item.video()
        val videoElem = video.video as TIMVideoElem

        val snapshot = videoElem.snapshotInfo
        val videoInfo = videoElem.videoInfo
        reLayout(snapshot.width.toInt(), snapshot.height.toInt(), item.isSelf())

        val imagePath = IMHelp.getImagePath() + snapshot.uuid
        if (!TextUtils.isEmpty(videoElem.snapshotPath)) {
            IMHelp.getImageDisplayer().display(videoElem.snapshotPath, iv_img!!)
        } else if (FileUtil.exists(imagePath)) {
            videoElem.snapshotPath = imagePath
            IMHelp.getImageDisplayer().display(imagePath, iv_img!!)
        } else {
//            imgReady.start()

            snapshot.getImage(imagePath, object : TIMValueCallBack<ProgressInfo> {
                override fun onError(i: Int, s: String) {

                }

                override fun onSuccess(progressInfo: ProgressInfo) {
//                    imgReady.loading(progressInfo.currentSize, progressInfo.totalSize)
                }
            }, object : TIMCallBack {
                override fun onError(i: Int, s: String) {
//                    imgReady.error()
                }

                override fun onSuccess() {
                    videoElem.snapshotPath = imagePath
                    IMHelp.getImageDisplayer().display(imagePath, iv_img!!)
                }
            })
        }
        fun ready(path: String) {
            videoElem.videoPath = path
//            iv_loading?.gone()
            iv_paly?.show()
            iv_img?.singleClick {
                context.startActivity<VideoViewActivity>(
                    IMHelp.IMAGE_PATH to imagePath,
                    IMHelp.VIDEO_PATH to path
                )
            }
        }

        val videoPath = IMHelp.getVideoPath() + videoInfo.uuid
        if (!TextUtils.isEmpty(videoElem.videoPath)) {
            ready(videoElem.videoPath)
        } else if (FileUtil.exists(imagePath)) {
            ready(videoPath)
        } else {
//            videoReady.start()
            videoInfo.getVideo(videoPath, object : TIMValueCallBack<ProgressInfo> {
                override fun onError(i: Int, s: String) {

                }

                override fun onSuccess(progressInfo: ProgressInfo) {
//                    videoReady.loading(progressInfo.currentSize, progressInfo.totalSize)
                }
            }, object : TIMCallBack {
                override fun onError(i: Int, s: String) {
//                    videoReady.error()
                }

                override fun onSuccess() {
                    ready(videoPath)
                }
            })
        }

    }

    fun reLayout(w: Int, h: Int, isSelf: Boolean) {
        iv_img?.setImageDrawable(null)
        iv_paly?.gone()
//        iv_loading?.gone()
        val max = dip(140)
        if (h <= 0) {
            return
        }
        val newWidth: Int
        val newHeight: Int
        val per = h / w.toFloat()
        Log.d("display", w.toString() + ":" + h.toString() + "++" + per)
        if (per < 1) {
            newWidth = max
            newHeight = (h * max / w.toFloat()).toInt()
            iv_img?.scaleType = ImageView.ScaleType.CENTER_CROP
        } else if (per > 3) {
            newWidth = dip(56)
            newHeight = max
            iv_img?.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            newWidth = (w * max / h.toFloat()).toInt()
            newHeight = max
            iv_img?.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        iv_img?.layoutParams = (iv_img?.layoutParams as FrameLayout.LayoutParams).apply {
            gravity = if (isSelf) Gravity.RIGHT else Gravity.LEFT
            width = newWidth
            height = newHeight
        }
        val min = Math.min(newWidth, newHeight) / 2
        iv_paly?.layoutParams = FrameLayout.LayoutParams(min, min).apply {
            gravity = Gravity.CENTER
        }
    }
}