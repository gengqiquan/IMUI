package com.gengqiquan.demo

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tencent.imsdk.*
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.gengqiquan.imlib.uitls.JsonUtil
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMManager
import com.gengqiquan.imui.help.IMHelp
import com.gengqiquan.imui.help.LongPressHelp
import com.gengqiquan.imui.input.ButtonFactory
import com.gengqiquan.imlib.RealMsg
import com.gengqiquan.imlib.TIMViewFactory
import com.gengqiquan.imlib.audio.TIMAudioRecorder
import com.gengqiquan.imlib.TIMMsgBuilder
import com.gengqiquan.imlib.model.CustomElem
import com.gengqiquan.imlib.video.CameraActivity
import com.gengqiquan.imlib.video.listener.MediaCallBack
import com.gengqiquan.imui.help.ToastHelp
import com.gengqiquan.imui.interfaces.*
import com.gengqiquan.imui.model.MenuAction
import com.gengqiquan.qqresult.QQResult
import com.tencent.imsdk.TIMMessage
import com.tencent.imsdk.ext.message.TIMConversationExt
import com.tencent.imsdk.ext.message.TIMMessageExt
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt
import com.xhe.photoalbum.PhotoAlbum

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

//        val list = arrayListOf<RealMsg>()
//        list.add(RealMsg(TXMsg("你好", "", TXMsg.Type.TEXT), true))
//        list.add(RealMsg(TXMsg("你好防水防汗首付开上飞洒放松放松,你好防水防汗首付开上飞洒放松放松", "", TXMsg.Type.TEXT), false))
//        list.add(
//            RealMsg(
//                TXMsg(
//                    "",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556532526983&di=c6d64102aaa1d612e260eb8176366b93&imgtype=0&src=http%3A%2F%2Fs11.sinaimg.cn%2Fmw690%2F006hikKrzy7slvzPwSKba%26690",
//                    TXMsg.Type.IMG
//                )
//                , true
//            )
//        )
//        list.add(
//            RealMsg(
//                TXMsg(
//                    "",
//                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556532526982&di=1f14a4ba8e5ad52d27b99299fa3b6803&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201512%2F12%2F20151212193107_ujGZV.jpeg",
//                    TXMsg.Type.IMG
//                ), false
//            )
//        )
//        list.add(RealMsg(TXMsg("", "", TXMsg.Type.VIDEO), true))
//        list.add(RealMsg(TXMsg("", "", TXMsg.Type.VIDEO), false))
        IMHelp.init(applicationContext, TIMAudioRecorder(), TIMMsgBuilder(), object : ImImageDisplayer {
            override fun display(url: String, imageView: ImageView, listener: DisplayListener?) {
                Glide.with(imageView.context)
                    .load(url)
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            imageView.setImageBitmap(resource)
                            listener?.ready()
                        }
                    })
            }
        })
        IMHelp.addImViewFactory(TIMViewFactory(this))
//        im_ui.appendMsgs(list.toMutableList())
        TIMManager.getInstance().init(
            applicationContext, TIMSdkConfig(1400205051)
                .enableLogPrint(true)
                .enableCrashReport(false)
                .enableLogPrint(true)
                .setLogLevel(TIMLogLevel.DEBUG)
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/justfortest/")
        )
        val userConfig = TIMUserConfig().setGroupEventListener { }
            .setUserStatusListener(object : TIMUserStatusListener {
                override fun onUserSigExpired() {

                }

                override fun onForceOffline() {
                }
            })
            .setConnectionListener(object : TIMConnListener {
                override fun onConnected() {

                }

                override fun onWifiNeedAuth(p0: String?) {
                }

                override fun onDisconnected(p0: Int, p1: String?) {
                }
            })
            .setGroupEventListener { }
        userConfig.refreshListener = object : TIMRefreshListener {
            override fun onRefreshConversation(msgs: MutableList<TIMConversation>) {

            }

            override fun onRefresh() {
            }
        }
        userConfig.setUploadProgressListener { timMessage, _, _, progress ->
            Log.d(tag, "进度" + "===" + timMessage.status().status + "==" + +progress)
        }
        TIMManager.getInstance().userConfig = TIMUserConfigMsgExt(userConfig)
            .setMessageRevokedListener {
                im_ui.refresh()
            }
        TIMManager.getInstance().addMessageListener(object : TIMMessageListener {
            override fun onNewMessages(msgs: MutableList<TIMMessage>): Boolean {
                Log.d(tag, "新消息" + msgs.size)
                val list = mutableListOf<IimMsg>()
                msgs.forEach {
                    list.addAll(RealMsg.create(it))
                }
                list.reverse()
                im_ui.newMsgs(list)
                return true
            }
        })
        TIMManager.getInstance().login(indent, sig, object : TIMCallBack {
            override fun onError(code: Int, desc: String) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.d(tag, "login failed. code: $code errmsg: $desc")

            }

            override fun onSuccess() {
                Log.d(tag, "getLocalMessage login succ")
                afterLogin()

            }
        })


    }

    fun afterLogin() {
        val con = TIMManager.getInstance().getConversation(
            TIMConversationType.C2C,    //会话类型：单聊
            tel
        )
        val conversationExt = TIMConversationExt(con)
        fun loadMore() {
            val count = 10

//            conversationExt.getLocalMessage(count, lastMsg, object : TIMValueCallBack<List<TIMMessage>> {
//                override fun onSuccess(msgs: List<TIMMessage>) {
//                    Log.e(tag, "getLocalMessage" + msgs.size.toString())
//                    if (msgs.size < count) {
//                        im_ui.allInit()
//                    }
//                    if (msgs.isEmpty()) {
//                        return
//                    }
//
//                    val list = mutableListOf<IimMsg>()
//                    msgs.forEach {
//                        list.addAll(RealMsg.create(it))
//                    }
//                    list.reverse()
//                    im_ui.oldMsgs(list, lastMsg == null)
//                    lastMsg = msgs.last()
//                }
//
//                override fun onError(p0: Int, p1: String?) {
//                    Log.e(tag, "getLocalMessage" + p0.toString() + ":" + p1)
//                }
//            })
            conversationExt.getMessage(count, lastMsg, object : TIMValueCallBack<List<TIMMessage>> {
                override fun onSuccess(msgs: List<TIMMessage>) {
                    Log.e(tag, "获取漫游消息" + msgs.size.toString())
                    if (msgs.size < count) {
                        im_ui.allInit()
                    }
                    if (msgs.isEmpty()) {
                        return
                    }

                    val list = mutableListOf<IimMsg>()
                    msgs.forEach {
                        list.addAll(RealMsg.create(it))
                    }
                    list.reverse()
                    im_ui.oldMsgs(list, lastMsg == null)
                    lastMsg = msgs.last()
                }

                override fun onError(p0: Int, p1: String?) {
                    Log.e(tag, "获取漫游消息失败" + p0.toString() + ":" + p1)
                }
            })
        }
        loadMore()
        im_ui.setMoreOldmoreOldMsgListener(object : IMoreOldMsgListener {
            override fun more() {
                loadMore()
            }
        })
        IMHelp.registerMsgSender(this, object : IMsgSender {

            override fun send(msg: Any, senderListener: ISenderListener?) {
                val realMsg = RealMsg.create(msg as TIMMessage)
                im_ui.newMsgs(realMsg)
                senderListener?.sending()
//                im_ui.newMsgs(RealMsg.create(msg as TIMMessage))
                con.sendMessage(msg, object : TIMValueCallBack<TIMMessage> {
                    override fun onSuccess(msg: TIMMessage) {
                        Log.e(tag, "onSuccess" + msg.toString())
                        senderListener?.success()
//                        im_ui.updateMsgs(RealMsg.create(msg))
                        realMsg.forEach { it.success() }
                        im_ui.updateMsgs(realMsg)

                    }

                    override fun onError(p0: Int, p1: String?) {
                        Log.e(tag, "onError" + p0.toString() + ":" + p1)
                        senderListener?.failure()
                        realMsg.forEach { it.failure() }
                        im_ui.updateMsgs(realMsg)
                    }
                })
            }
        })

        im_ui.inputUI.otherProxy(object : OtherProxy {
            override fun proxy(type: Int, send: (Any) -> Unit) {
                if (type == ButtonFactory.PICTURE) {
                    QQResult.startActivityWith(
                        this@MainActivity,
                        PhotoAlbum(this@MainActivity).setLimitCount(4).albumIntent
                    )
                        .result {
                            send(IMHelp.getMsgBuildPolicy().buildImgMessage(PhotoAlbum.parseResult(it)))
                        }
                    return
                }
                if (type == ButtonFactory.CAMERA) {
                    val captureIntent = Intent(this@MainActivity, CameraActivity::class.java)
                    CameraActivity.mCallBack = object : MediaCallBack {
                        override fun onImageSuccess(path: String) {
                            send(IMHelp.getMsgBuildPolicy().buildImgMessage(arrayListOf(path)))
                        }

                        override fun onVideoSuccess(videoData: Intent) {
                            val imgPath = videoData.getStringExtra("image_path")
                            val videoPath = videoData.getStringExtra("video_path")
                            val imgWidth = videoData.getIntExtra("width", 0)
                            val imgHeight = videoData.getIntExtra("height", 0)
                            val duration = videoData.getLongExtra("duration", 0)
                            val msg =
                                IMHelp.getMsgBuildPolicy()
                                    .buildVideoMessage(imgPath, videoPath, imgWidth, imgHeight, duration)
                            send(msg)
                        }
                    }
                    startActivity(captureIntent)
                    return
                }
                if (type == ButtonFactory.CAR) {
                    val ele = CustomElem.create(json)
                    Log.d(tag, ele.toString())
                    Log.d(tag, JsonUtil.toJson(ele))
                    val msg =
                        IMHelp.getMsgBuildPolicy()
                            .buildCustomMessage(JsonUtil.toJson(ele))
                    send(msg)
                    return
                }
                if (type == ButtonFactory.CARD) {
                    val ele = CustomElem.create(json)
                    Log.d(tag, ele.toString())
                    Log.d(tag, JsonUtil.toJson(ele))
                    im_ui.newMsgs(RealMsg.create(TIMMsgBuilder.buildPreCustomMessage(JsonUtil.toJson(ele))))
                    return
                }

            }

        })
        var list = mutableListOf<MenuAction>()
        list.add(MenuAction("撤回", {
            Log.e(tag, "调用撤回")
            conversationExt.revokeMessage(it as TIMMessage, object : TIMCallBack {
                override fun onSuccess() {
                    // TODO: 2019-05-07 撤回成功
                    Log.e(tag, "撤回成功")
                    im_ui.refresh()
                }

                override fun onError(p0: Int, p1: String?) {
                    ToastHelp.toastShortMessage("只能撤回2分钟以内的消息")
                    Log.e(tag, "撤回失败" + p0.toString() + ":" + p1)
                }
            })
        }, true))
        list.add(MenuAction("删除") {
            if (!TIMMessageExt(it as TIMMessage).remove()) {
                Log.e(tag, "删除失败")
                return@MenuAction
            }
//            lastMsg = null
//            loadMore()
            im_ui.delete(it)
            Log.e(tag, "删除成功")
        })
        LongPressHelp.init(list)

    }

    val json = "{\n" +
            "    \"data\": {\n" +
            "        \"action_type\": 1,\n" +
            "        \"msg\": {\n" +
            "            \"content\": \"公司：南京市大锤二手车经营管理中心\",\n" +
            "            \"module\": \"模块来源\",\n" +
            "            \"pic_url\": \"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1336029781,3333225635&fm=26&gp=0.jpg\",\n" +
            "            \"title\": \"2013款 朗逸 改款 1.4TFSI 手动豪华版 5周年纪念款\"\n" +
            "        },\n" +
            "        \"router_url\": \"che300://open/webv/http://m.che300.com\",\n" +
            "        \"style\": 0\n" +
            "    },\n" +
            "    \"platform\": {\n" +
            "        \"device_info\": \"(OPPO A83,Android 7.1.1)\",\n" +
            "        \"from\": \"che300_pro\",\n" +
            "        \"from_user_id\": \"fjjkwerwiuafsjfkjf\",\n" +
            "        \"lat\": \"\",\n" +
            "        \"lng\": \"\",\n" +
            "        \"os\": \"ios\",\n" +
            "        \"receive_user_id\": \"wtwouafjklfaksjf\",\n" +
            "        \"version\": \"2.2.4.0\"\n" +
            "    },\n" +
            "    \"type\": \"share\"\n" +
            "}"
    var lastMsg: TIMMessage? = null
    val tag = "immmmmm"
    val tel = "129a14e8a4b3a123"
    val indent = "8849cc559d324811"
    val sig =
        "eJxlj81qg0AYRfc*hbi1lHH0M2MhCw0x2KYpRQ3FjcjMqNNY488oltJ3L7UNsfRuz*Fe7oeiqqoW7cPbjNLzUMtUvjdcU*9UDWk3V9g0gqWZTM2O-YN8akTH0yyXvJuhAQAYoaUjGK*lyMWvQYjlUArgMBNbxDAWZs9O6Tz3U2UhhBEg*KOIYoaP2*dN4E*hlwewFyj0rVYfS-5Qea1*2AR2krlH27N3q4q5OzyBK7xRH2IZl1ExbF-a5ET9Y-FqHe7jpy7We70CzyRR2fpFmBTr9WJSijd**WYDdkyyWtCRd70417OAkQEGNtF3NOVT*QKew13t"
}
