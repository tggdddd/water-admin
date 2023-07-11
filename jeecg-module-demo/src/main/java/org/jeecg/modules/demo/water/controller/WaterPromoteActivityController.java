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
import org.jeecg.modules.demo.water.entity.WaterPromoteActivity;
import org.jeecg.modules.demo.water.service.IWaterPromoteActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 推广活动
 * @Author: jeecg-boot
 * @Date: 2023-07-10
 * @Version: V1.0
 */
@Api(tags = "推广活动")
@RestController
@RequestMapping("/water/waterPromoteActivity")
@Slf4j
public class WaterPromoteActivityController extends JeecgController<WaterPromoteActivity, IWaterPromoteActivityService> {
    @Autowired
    private IWaterPromoteActivityService waterPromoteActivityService;

    /**
     * 分页列表查询
     *
     * @param waterPromoteActivity
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "推广活动-分页列表查询")
    @ApiOperation(value = "推广活动-分页列表查询", notes = "推广活动-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterPromoteActivity>> queryPageList(WaterPromoteActivity waterPromoteActivity,
                                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                             HttpServletRequest req) {
        QueryWrapper<WaterPromoteActivity> queryWrapper = QueryGenerator.initQueryWrapper(waterPromoteActivity, req.getParameterMap());
        Page<WaterPromoteActivity> page = new Page<WaterPromoteActivity>(pageNo, pageSize);
        IPage<WaterPromoteActivity> pageList = waterPromoteActivityService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param waterPromoteActivity
     * @return
     */
    @AutoLog(value = "推广活动-添加")
    @ApiOperation(value = "推广活动-添加", notes = "推广活动-添加")
    @RequiresPermissions("water:water_promote_activity:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterPromoteActivity waterPromoteActivity) {
        waterPromoteActivityService.save(waterPromoteActivity);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param waterPromoteActivity
     * @return
     */
    @AutoLog(value = "推广活动-编辑")
    @ApiOperation(value = "推广活动-编辑", notes = "推广活动-编辑")
    @RequiresPermissions("water:water_promote_activity:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterPromoteActivity waterPromoteActivity) {
        waterPromoteActivityService.updateById(waterPromoteActivity);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "推广活动-通过id删除")
    @ApiOperation(value = "推广活动-通过id删除", notes = "推广活动-通过id删除")
    @RequiresPermissions("water:water_promote_activity:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterPromoteActivityService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "推广活动-批量删除")
    @ApiOperation(value = "推广活动-批量删除", notes = "推广活动-批量删除")
    @RequiresPermissions("water:water_promote_activity:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterPromoteActivityService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "推广活动-通过id查询")
    @ApiOperation(value = "推广活动-通过id查询", notes = "推广活动-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterPromoteActivity> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterPromoteActivity waterPromoteActivity = waterPromoteActivityService.getById(id);
        if (waterPromoteActivity == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterPromoteActivity);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterPromoteActivity
     */
    @RequiresPermissions("water:water_promote_activity:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterPromoteActivity waterPromoteActivity) {
        return super.exportXls(request, waterPromoteActivity, WaterPromoteActivity.class, "推广活动");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_promote_activity:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WaterPromoteActivity.class);
    }

}
