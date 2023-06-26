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
import org.jeecg.modules.demo.water.entity.WaterAddress;
import org.jeecg.modules.demo.water.service.IWaterAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 用户地址
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Api(tags = "用户地址")
@RestController
@RequestMapping("/water/waterAddress")
@Slf4j
public class WaterAddressController extends JeecgController<WaterAddress, IWaterAddressService> {
    @Autowired
    private IWaterAddressService waterAddressService;

    /**
     * 分页列表查询
     *
     * @param waterAddress
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "用户地址-分页列表查询")
    @ApiOperation(value = "用户地址-分页列表查询", notes = "用户地址-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterAddress>> queryPageList(WaterAddress waterAddress,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        QueryWrapper<WaterAddress> queryWrapper = QueryGenerator.initQueryWrapper(waterAddress, req.getParameterMap());
        Page<WaterAddress> page = new Page<WaterAddress>(pageNo, pageSize);
        IPage<WaterAddress> pageList = waterAddressService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param waterAddress
     * @return
     */
    @AutoLog(value = "用户地址-添加")
    @ApiOperation(value = "用户地址-添加", notes = "用户地址-添加")
    @RequiresPermissions("water:water_address:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterAddress waterAddress) {
        waterAddressService.save(waterAddress);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param waterAddress
     * @return
     */
    @AutoLog(value = "用户地址-编辑")
    @ApiOperation(value = "用户地址-编辑", notes = "用户地址-编辑")
    @RequiresPermissions("water:water_address:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterAddress waterAddress) {
        waterAddressService.updateById(waterAddress);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户地址-通过id删除")
    @ApiOperation(value = "用户地址-通过id删除", notes = "用户地址-通过id删除")
    @RequiresPermissions("water:water_address:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterAddressService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户地址-批量删除")
    @ApiOperation(value = "用户地址-批量删除", notes = "用户地址-批量删除")
    @RequiresPermissions("water:water_address:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterAddressService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "用户地址-通过id查询")
    @ApiOperation(value = "用户地址-通过id查询", notes = "用户地址-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterAddress> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterAddress waterAddress = waterAddressService.getById(id);
        if (waterAddress == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterAddress);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterAddress
     */
    @RequiresPermissions("water:water_address:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterAddress waterAddress) {
        return super.exportXls(request, waterAddress, WaterAddress.class, "用户地址");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_address:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WaterAddress.class);
    }

}
