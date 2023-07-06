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
import org.jeecg.modules.demo.water.entity.WaterAd;
import org.jeecg.modules.demo.water.service.IWaterAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 轮播图
 * @Author: jeecg-boot
 * @Date: 2023-07-05
 * @Version: V1.0
 */
@Api(tags = "轮播图")
@RestController
@RequestMapping("/water/waterAd")
@Slf4j
public class WaterAdController extends JeecgController<WaterAd, IWaterAdService> {
    @Autowired
    private IWaterAdService waterAdService;

    /**
     * 分页列表查询
     *
     * @param waterAd
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "轮播图-分页列表查询")
    @ApiOperation(value = "轮播图-分页列表查询", notes = "轮播图-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterAd>> queryPageList(WaterAd waterAd,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
        QueryWrapper<WaterAd> queryWrapper = QueryGenerator.initQueryWrapper(waterAd, req.getParameterMap());
        Page<WaterAd> page = new Page<WaterAd>(pageNo, pageSize);
        IPage<WaterAd> pageList = waterAdService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param waterAd
     * @return
     */
    @AutoLog(value = "轮播图-添加")
    @ApiOperation(value = "轮播图-添加", notes = "轮播图-添加")
    @RequiresPermissions("water:water_ad:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterAd waterAd) {
        waterAdService.save(waterAd);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param waterAd
     * @return
     */
    @AutoLog(value = "轮播图-编辑")
    @ApiOperation(value = "轮播图-编辑", notes = "轮播图-编辑")
    @RequiresPermissions("water:water_ad:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterAd waterAd) {
        waterAdService.updateById(waterAd);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "轮播图-通过id删除")
    @ApiOperation(value = "轮播图-通过id删除", notes = "轮播图-通过id删除")
    @RequiresPermissions("water:water_ad:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterAdService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "轮播图-批量删除")
    @ApiOperation(value = "轮播图-批量删除", notes = "轮播图-批量删除")
    @RequiresPermissions("water:water_ad:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterAdService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "轮播图-通过id查询")
    @ApiOperation(value = "轮播图-通过id查询", notes = "轮播图-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterAd> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterAd waterAd = waterAdService.getById(id);
        if (waterAd == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterAd);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterAd
     */
    @RequiresPermissions("water:water_ad:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterAd waterAd) {
        return super.exportXls(request, waterAd, WaterAd.class, "轮播图");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_ad:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WaterAd.class);
    }

}
