package org.jeecg.modules.demo.water.dashboardController.Bean;

import lombok.Data;

import java.sql.Date;

@Data
public class TotalOrderBean {
    private Date day;
    private String princes;
    private Long orders;
    private Long users;
}