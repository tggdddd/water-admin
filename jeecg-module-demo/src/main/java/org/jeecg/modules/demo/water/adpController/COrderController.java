package org.jeecg.modules.demo.water.adpController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
import org.jeecg.modules.demo.water.vo.OrderSendItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/order")
public class COrderController {
    @Autowired
    IWaterOrderService orderService;

    /**
     * 骑手领取订单
     */
    @RequestMapping("pick/up")
    public void pickUpOrder(@RequestParam(value = "orderId") String orderId, HttpServletRequest request, HttpServletResponse response) {
        String username = JwtUtil.getUserNameByToken(request);
        setResponseStatus(orderService.pickUpOrder(orderId, username), response);
    }

    /**
     * 骑手取消订单
     */
    @RequestMapping("cancel")
    public void cancelOrder(@RequestParam(value = "sendId") String sendId, HttpServletRequest request, HttpServletResponse response) {
        String username = JwtUtil.getUserNameByToken(request);
        setResponseStatus(orderService.cancelOrder(sendId, username), response);
    }

    /**
     * 骑手完成订单
     */
    @RequestMapping("finish")
    public void finishOrder(@RequestParam(value = "sendId") String sendId, HttpServletRequest request, HttpServletResponse response) {
        String username = JwtUtil.getUserNameByToken(request);
        setResponseStatus(orderService.finishOrder(sendId, username), response);
    }

    private void setResponseStatus(boolean is200, HttpServletResponse response) {
        if (!is200) {
            response.setStatus(500);
        }
    }

    /**
     * 骑手可接订单
     */
    @RequestMapping("pick/list")
//    @RequiresPermissions("")
    public Page<OrderSendItemVO> listReceiveOrders(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer current) {
        return orderService.pagePaidOrderItem(new Page<>(current, size));
    }

    /**
     * 骑手已接订单
     */
    @RequestMapping("receive/list")
    public Page<OrderSendItemVO> listGotToItOrders(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer current, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        return orderService.pageOwnOrderItemWithOutSend(new Page<>(current, size), username);
    }

    /**
     * 骑手已送订单
     */
    @RequestMapping("finish/list")
    public Page<OrderSendItemVO> listFinishOrders(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                  @RequestParam(value = "page", defaultValue = "1") Integer current, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        return orderService.pageOwnSendOrderItem(new Page<>(current, size), username);
    }

    /**
     * 骑手所有订单
     */
    @RequestMapping("own/list")
    public Page<OrderSendItemVO> listOwnOrders(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                               @RequestParam(value = "page", defaultValue = "1") Integer current, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        return orderService.pageOwnOrderAndItem(new Page<>(current, size), username);
    }
}
