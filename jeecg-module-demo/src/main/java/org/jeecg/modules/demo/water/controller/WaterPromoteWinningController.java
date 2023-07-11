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
import org.jeecg.modules.demo.water.entity.WaterPromoteWinning;
import org.jeecg.modules.demo.water.service.IWaterPromoteWinningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 推广活动获奖记录
 * @Author: jeecg-boot
 * @Date: 2023-07-10
 * @Version: V1.0
 */
@Api(tags = "推广活动获奖记录")
@RestController
@RequestMapping("/water/waterPromoteWinning")
@Slf4j
public class WaterPromoteWinningController extends JeecgController<WaterPromoteWinning, IWaterPromoteWinningService> {
    @Autowired
    private IWaterPromoteWinningService waterPromoteWinningService;

    /**
     * 分页列表查询
     *
     * @param waterPromoteWinning
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "推广活动获奖记录-分页列表查询")
    @ApiOperation(value = "推广活动获奖记录-分页列表查询", notes = "推广活动获奖记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterPromoteWinning>> queryPageList(WaterPromoteWinning waterPromoteWinning,
                                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                            HttpServletRequest req) {
        QueryWrapper<WaterPromoteWinning> queryWrapper = QueryGenerator.initQueryWrapper(waterPromoteWinning, req.getParameterMap());
        Page<WaterPromoteWinning> page = new Page<WaterPromoteWinning>(pageNo, pageSize);
        IPage<WaterPromoteWinning> pageList = waterPromoteWinningService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param waterPromoteWinning
     * @return
     */
    @AutoLog(value = "推广活动获奖记录-添加")
    @ApiOperation(value = "推广活动获奖记录-添加", notes = "推广活动获奖记录-添加")
    @RequiresPermissions("water:water_promote_winning:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterPromoteWinning waterPromoteWinning) {
        waterPromoteWinningService.save(waterPromoteWinning);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param waterPromoteWinning
     * @return
     */
    @AutoLog(value = "推广活动获奖记录-编辑")
    @ApiOperation(value = "推广活动获奖记录-编辑", notes = "推广活动获奖记录-编辑")
    @RequiresPermissions("water:water_promote_winning:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterPromoteWinning waterPromoteWinning) {
        waterPromoteWinningService.updateById(waterPromoteWinning);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "推广活动获奖记录-通过id删除")
    @ApiOperation(value = "推广活动获奖记录-通过id删除", notes = "推广活动获奖记录-通过id删除")
    @RequiresPermissions("water:water_promote_winning:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterPromoteWinningService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "推广活动获奖记录-批量删除")
    @ApiOperation(value = "推广活动获奖记录-批量删除", notes = "推广活动获奖记录-批量删除")
    @RequiresPermissions("water:water_promote_winning:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterPromoteWinningService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "推广活动获奖记录-通过id查询")
    @ApiOperation(value = "推广活动获奖记录-通过id查询", notes = "推广活动获奖记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterPromoteWinning> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterPromoteWinning waterPromoteWinning = waterPromoteWinningService.getById(id);
        if (waterPromoteWinning == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterPromoteWinning);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterPromoteWinning
     */
    @RequiresPermissions("water:water_promote_winning:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterPromoteWinning waterPromoteWinning) {
        return super.exportXls(request, waterPromoteWinning, WaterPromoteWinning.class, "推广活动获奖记录");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_promote_winning:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WaterPromoteWinning.class);
    }

}
