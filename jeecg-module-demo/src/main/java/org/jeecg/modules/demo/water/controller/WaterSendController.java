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
import org.jeecg.modules.demo.water.entity.WaterSend;
import org.jeecg.modules.demo.water.service.IWaterSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 派送表
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Api(tags = "派送表")
@RestController
@RequestMapping("/water/waterSend")
@Slf4j
public class WaterSendController extends JeecgController<WaterSend, IWaterSendService> {
    @Autowired
    private IWaterSendService waterSendService;

    /**
     * 分页列表查询
     *
     * @param waterSend
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "派送表-分页列表查询")
    @ApiOperation(value = "派送表-分页列表查询", notes = "派送表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterSend>> queryPageList(WaterSend waterSend,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<WaterSend> queryWrapper = QueryGenerator.initQueryWrapper(waterSend, req.getParameterMap());
        Page<WaterSend> page = new Page<WaterSend>(pageNo, pageSize);
        IPage<WaterSend> pageList = waterSendService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param waterSend
     * @return
     */
    @AutoLog(value = "派送表-添加")
    @ApiOperation(value = "派送表-添加", notes = "派送表-添加")
    @RequiresPermissions("water:water_send:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterSend waterSend) {
        waterSendService.save(waterSend);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param waterSend
     * @return
     */
    @AutoLog(value = "派送表-编辑")
    @ApiOperation(value = "派送表-编辑", notes = "派送表-编辑")
    @RequiresPermissions("water:water_send:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterSend waterSend) {
        waterSendService.updateById(waterSend);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "派送表-通过id删除")
    @ApiOperation(value = "派送表-通过id删除", notes = "派送表-通过id删除")
    @RequiresPermissions("water:water_send:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterSendService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "派送表-批量删除")
    @ApiOperation(value = "派送表-批量删除", notes = "派送表-批量删除")
    @RequiresPermissions("water:water_send:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterSendService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "派送表-通过id查询")
    @ApiOperation(value = "派送表-通过id查询", notes = "派送表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterSend> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterSend waterSend = waterSendService.getById(id);
        if (waterSend == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterSend);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterSend
     */
    @RequiresPermissions("water:water_send:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterSend waterSend) {
        return super.exportXls(request, waterSend, WaterSend.class, "派送表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_send:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WaterSend.class);
    }

}
