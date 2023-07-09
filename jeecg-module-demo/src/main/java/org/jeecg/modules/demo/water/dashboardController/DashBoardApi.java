package org.jeecg.modules.demo.water.dashboardController;

import org.jeecg.modules.demo.water.dashboardController.Bean.ChartData;
import org.jeecg.modules.demo.water.dashboardController.Bean.TotalBean;
import org.jeecg.modules.demo.water.dashboardController.Bean.TotalOrderBean;
import org.jeecg.modules.demo.water.mapper.WaterClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashBoardApi {
    @Autowired
    WaterClassMapper mapper;

    /**
     * 根据日期获取订单统计数据
     */
    @RequestMapping("orderTotalByDate")
    public ArrayList<ChartData> totalOrderByDate(HttpServletRequest request) {
        String beginTime = request.getParameter("create_time_begin");
        String endTime = request.getParameter("create_time_end");
        List<TotalOrderBean> totalOrderBeans = mapper.totalOrderByDate(beginTime, endTime);
        ArrayList<ChartData> result = new ArrayList<>(totalOrderBeans.size() * 3);
        for (TotalOrderBean totalOrderBean : totalOrderBeans) {
            result.add(new ChartData(totalOrderBean.getDay().toString(), totalOrderBean.getPrinces(), "销售额"));
            result.add(new ChartData(totalOrderBean.getDay().toString(), totalOrderBean.getOrders().toString(), "订单数"));
            result.add(new ChartData(totalOrderBean.getDay().toString(), totalOrderBean.getUsers().toString(), "用户数"));
        }
        return result;
    }

    /**
     * 顶部统计
     */
    @RequestMapping("total")
    public ArrayList<TotalBean> total() {
//        访问数 月
        Long visitorMonthTotal = mapper.appUserTotalCurrentMonth();
        Long visitorTotal = mapper.appUserTotal();
//        成交额 月
        Long amountMonthTotal = mapper.userPurchaseTotalCurrentMonth();
        Long amountTotal = mapper.userPurchaseTotal();
//        下单数 周
        Long orderWeekTotal = mapper.orderTotalCurrentWeek();
        Long orderTotal = mapper.orderTotal();
//        完成数 年
        Long orderFinishYearTotal = mapper.orderFinishYearTotal();
        Long orderFinishTotal = mapper.orderFinishTotal();
        ArrayList<TotalBean> result = new ArrayList();
        result.add(TotalBean.builder()
                .title("访问量")
                .icon("icon-jeecg-youhuiquan")
                .value(visitorMonthTotal == 0 ? "暂无" : String.valueOf(visitorMonthTotal))
                .total(visitorTotal == 0 ? "暂无" : String.valueOf(visitorTotal))
                .prefix("$")
                .color("green")
                .action("月").build());
        result.add(TotalBean.builder()
                .title("成交额")
                .icon("icon-jeecg-jifen")
                .value(amountMonthTotal == 0 ? "暂无" : String.valueOf(amountMonthTotal))
                .total(amountTotal == 0 ? "暂无" : String.valueOf(amountTotal))
                .prefix("$")
                .color("blue")
                .action("月").build());
        result.add(TotalBean.builder()
                .title("下单量")
                .icon("icon-jeecg-tupian")
                .value(orderWeekTotal == 0 ? "暂无" : String.valueOf(orderWeekTotal))
                .total(orderTotal == 0 ? "暂无" : String.valueOf(orderTotal))
                .prefix("$")
                .color("pink")
                .action("周").build());
        result.add(TotalBean.builder()
                .title("完成量")
                .icon("icon-jeecg-qianbao")
                .value(orderFinishYearTotal == 0 ? "暂无" : String.valueOf(orderFinishYearTotal))
                .total(orderFinishTotal == 0 ? "暂无" : String.valueOf(orderFinishTotal))
                .prefix("$")
                .color("yellow")
                .action("年").build());
        return result;
    }

}
