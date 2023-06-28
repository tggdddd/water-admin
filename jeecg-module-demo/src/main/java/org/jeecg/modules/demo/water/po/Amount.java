package org.jeecg.modules.demo.water.po;

import lombok.Data;

@Data
public class Amount {

    private String currency;
    private String payerCurrency;
    private Long payerTotal;
    private Long total;


}
