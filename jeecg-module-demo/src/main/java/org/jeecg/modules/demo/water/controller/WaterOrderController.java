package org.jeecg.modules.demo.water.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.demo.water.constant.OrderConstant;
import org.jeecg.modules.demo.water.entity.WaterOrder;
import org.jeecg.modules.demo.water.service.IWaterOrderService;
import org.jeecg.modules.demo.water.service.IWetChatJSPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 订单
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Api(tags = "订单")
@RestController
@RequestMapping("/water/waterOrder")
@Slf4j
public class WaterOrderController extends JeecgController<WaterOrder, IWaterOrderService> {
    @Autowired
    private IWaterOrderService waterOrderService;
    @Autowired
    private IWetChatJSPayService payService;

    /**
     * 订单退款
     */
    @RequestMapping("refund")
    @RequiresPermissions("water:water_order:refund")
    @AutoLog(value = "订单-退款")
    @Transactional
    public Result refundOrder(@RequestParam(value = "orderId") String orderId) {
        WaterOrder byId = waterOrderService.getById(orderId);
        switch (byId.getOrdreStatus()) {
            case OrderConstant.CANCEL:
            case OrderConstant.UNCERTAIN:
            case OrderConstant.UNPAID:
            case OrderConstant.REFUND:
            case OrderConstant.REFUNDING:
                return Result.error("非已付款订单");
        }
        Transaction byOwnOrder = payService.getByOwnOrder(orderId);
        AmountReq amountReq = new AmountReq();
        amountReq.setTotal(byOwnOrder.getAmount().getTotal().longValue());
        amountReq.setRefund(byOwnOrder.getAmount().getTotal().longValue());
        amountReq.setCurrency("CNY");
        Refund a = payService.refund(byOwnOrder.getTransactionId(), orderId, amountReq, "退款");
        if (a.getStatus().equals(Status.ABNORMAL) || a.getStatus().equals(Status.CLOSED)) {
            return Result.error("退款失败!");
        }
        if (!byId.getOrdreStatus().equals(OrderConstant.REFUND)) {
            byId.setOrdreStatus(OrderConstant.REFUND);
            waterOrderService.updateById(byId);
        }
        return Result.ok("退款成功");
    }

    /**
     * 分页列表查询
     *
     * @param waterOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "订单-分页列表查询")
    @ApiOperation(value = "订单-分页列表查询", notes = "订单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterOrder>> queryPageList(WaterOrder waterOrder,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   HttpServletRequest req) {
        QueryWrapper<WaterOrder> queryWrapper = QueryGenerator.initQueryWrapper(waterOrder, req.getParameterMap());
        Page<WaterOrder> page = new Page<WaterOrder>(pageNo, pageSize);
        IPage<WaterOrder> pageList = waterOrderService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param waterOrder
     * @return
     */
    @AutoLog(value = "订单-添加")
    @ApiOperation(value = "订单-添加", notes = "订单-添加")
    @RequiresPermissions("water:water_order:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterOrder waterOrder) {
        waterOrderService.save(waterOrder);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param waterOrder
     * @return
     */
    @AutoLog(value = "订单-编辑")
    @ApiOperation(value = "订单-编辑", notes = "订单-编辑")
    @RequiresPermissions("water:water_order:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterOrder waterOrder) {
        waterOrderService.updateById(waterOrder);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单-通过id删除")
    @ApiOperation(value = "订单-通过id删除", notes = "订单-通过id删除")
    @RequiresPermissions("water:water_order:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterOrderService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "订单-批量删除")
    @ApiOperation(value = "订单-批量删除", notes = "订单-批量删除")
    @RequiresPermissions("water:water_order:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterOrderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "订单-通过id查询")
    @ApiOperation(value = "订单-通过id查询", notes = "订单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterOrder> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterOrder waterOrder = waterOrderService.getById(id);
        if (waterOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterOrder);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterOrder
     */
    @RequiresPermissions("water:water_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterOrder waterOrder) {
        return super.exportXls(request, waterOrder, WaterOrder.class, "订单");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_order:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WaterOrder.class);
    }

}
