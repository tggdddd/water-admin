package org.jeecg.modules.demo.water.dashboardController.Bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotalBean {
    private String title;
    private String icon;
    private String value;
    private String total;
    private String prefix;
    private String color;
    private String action;
}