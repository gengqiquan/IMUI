package com.gengqiquan.imui.model;

public class ImImage {
    private String url;
    private String thumb;
    private long height;
    private long width;

    public ImImage(String url, String thumb, long width, long height) {
        this.url = url;
        this.thumb = thumb;
        this.height = height;
        this.width = width;
    }

    public String getThumb() {
        return thumb == null ? url : thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }
}
