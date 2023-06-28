package org.jeecg.modules.base;

import lombok.Data;

@Data
public class ThinkResult {

    private int errno;
    private String errmsg;
    private Object data;

    public ThinkResult() {

    }

    public ThinkResult(int errno, String errmsg, Object data) {
        this.errno = errno;
        this.errmsg = errmsg;
        this.data = data;
    }

    public static ThinkResult ok(Object data) {
        return new ThinkResult(0, "", data);
    }

    public static ThinkResult error() {
        return new ThinkResult(1000, "no permission", null);
    }

    public static ThinkResult error(int code) {
        return new ThinkResult(code, "", null);
    }

    public static ThinkResult error(String msg) {
        return new ThinkResult(1000, msg, null);
    }

    public static ThinkResult notLogin() {
        return new ThinkResult(401, "未登录", null);
    }
}
