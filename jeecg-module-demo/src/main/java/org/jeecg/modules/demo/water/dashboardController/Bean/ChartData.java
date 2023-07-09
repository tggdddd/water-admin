package org.jeecg.modules.demo.water.dashboardController.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChartData {
    private String name;
    private String value;
    private String type;

    private ChartData(String name, String value) {
        this.name = name;
        this.value = value;
    }
}

