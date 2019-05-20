package com.gengqiquan.imlib;

import com.gengqiquan.imui.ui.ISession;

import org.jetbrains.annotations.Nullable;

/**
 * @author mazhichao
 * @date 2019/05/14 09:50
 */
public class RealSession implements ISession {

    private String head;
    private String name;
    private String msg;
    private String time;
    private String unRead;

    public RealSession(String head,String name,String msg,String time,String unRead) {
        super();
        this.head = head;
        this.name = name;
        this.msg = msg;
        this.time = time;
        this.unRead = unRead;
    }

    @Nullable
    @Override
    public String head() {
        return head;
    }

    @Nullable
    @Override
    public String name() {
        return name;
    }

    @Nullable
    @Override
    public String msg() {
        return msg;
    }

    @Nullable
    @Override
    public String time() {
        return time;
    }

    @Nullable
    @Override
    public String unRead() {
        return unRead;
    }
}
