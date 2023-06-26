package org.jeecg.modules.demo.water.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.demo.water.entity.WaterShopCart;
import org.jeecg.modules.demo.water.service.IWaterShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Api(tags = "购物车")
@RestController
@RequestMapping("/water/waterShopCart")
@Slf4j
public class WaterShopCartController extends JeecgController<WaterShopCart, IWaterShopCartService> {
    @Autowired
    private IWaterShopCartService waterShopCartService;

    /**
     * 分页列表查询
     *
     * @param waterShopCart
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "购物车-分页列表查询")
    @ApiOperation(value = "购物车-分页列表查询", notes = "购物车-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterShopCart>> queryPageList(WaterShopCart waterShopCart,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        QueryWrapper<WaterShopCart> queryWrapper = QueryGenerator.initQueryWrapper(waterShopCart, req.getParameterMap());
        Page<WaterShopCart> page = new Page<WaterShopCart>(pageNo, pageSize);
        IPage<WaterShopCart> pageList = waterShopCartService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param waterShopCart
     * @return
     */
    @AutoLog(value = "购物车-添加")
    @ApiOperation(value = "购物车-添加", notes = "购物车-添加")
    @RequiresPermissions("water:water_shop_cart:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterShopCart waterShopCart) {
        waterShopCartService.save(waterShopCart);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param waterShopCart
     * @return
     */
    @AutoLog(value = "购物车-编辑")
    @ApiOperation(value = "购物车-编辑", notes = "购物车-编辑")
    @RequiresPermissions("water:water_shop_cart:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterShopCart waterShopCart) {
        waterShopCartService.updateById(waterShopCart);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "购物车-通过id删除")
    @ApiOperation(value = "购物车-通过id删除", notes = "购物车-通过id删除")
    @RequiresPermissions("water:water_shop_cart:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterShopCartService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "购物车-批量删除")
    @ApiOperation(value = "购物车-批量删除", notes = "购物车-批量删除")
    @RequiresPermissions("water:water_shop_cart:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterShopCartService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "购物车-通过id查询")
    @ApiOperation(value = "购物车-通过id查询", notes = "购物车-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterShopCart> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterShopCart waterShopCart = waterShopCartService.getById(id);
        if (waterShopCart == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterShopCart);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterShopCart
     */
    @RequiresPermissions("water:water_shop_cart:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterShopCart waterShopCart) {
        return super.exportXls(request, waterShopCart, WaterShopCart.class, "购物车");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_shop_cart:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WaterShopCart.class);
    }

}
