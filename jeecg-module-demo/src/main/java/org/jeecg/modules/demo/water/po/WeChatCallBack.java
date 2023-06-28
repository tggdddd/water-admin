package org.jeecg.modules.demo.water.po;

import lombok.Data;


@Data
public class WeChatCallBack {

    private String createTime;
    private String eventType;
    private String id;
    private Resource resource;
    private String resourceType;
    private String summary;

    public static enum EventType {
        SUCCESS("TRANSACTION.SUCCESS");

        private final String value;

        EventType(String value) {
            this.value = value;
        }

        public String get() {
            return value;
        }
    }
}
