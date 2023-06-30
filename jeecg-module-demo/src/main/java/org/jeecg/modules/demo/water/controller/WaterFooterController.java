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
import org.jeecg.modules.demo.water.entity.WaterFooter;
import org.jeecg.modules.demo.water.service.IWaterFooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 足迹
 * @Author: jeecg-boot
 * @Date: 2023-06-30
 * @Version: V1.0
 */
@Api(tags = "足迹")
@RestController
@RequestMapping("/water/waterFooter")
@Slf4j
public class WaterFooterController extends JeecgController<WaterFooter, IWaterFooterService> {
    @Autowired
    private IWaterFooterService waterFooterService;

    /**
     * 分页列表查询
     *
     * @param waterFooter
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "足迹-分页列表查询")
    @ApiOperation(value = "足迹-分页列表查询", notes = "足迹-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterFooter>> queryPageList(WaterFooter waterFooter,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<WaterFooter> queryWrapper = QueryGenerator.initQueryWrapper(waterFooter, req.getParameterMap());
        Page<WaterFooter> page = new Page<WaterFooter>(pageNo, pageSize);
        IPage<WaterFooter> pageList = waterFooterService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param waterFooter
     * @return
     */
    @AutoLog(value = "足迹-添加")
    @ApiOperation(value = "足迹-添加", notes = "足迹-添加")
    @RequiresPermissions("water:water_footer:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterFooter waterFooter) {
        waterFooterService.save(waterFooter);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param waterFooter
     * @return
     */
    @AutoLog(value = "足迹-编辑")
    @ApiOperation(value = "足迹-编辑", notes = "足迹-编辑")
    @RequiresPermissions("water:water_footer:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterFooter waterFooter) {
        waterFooterService.updateById(waterFooter);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "足迹-通过id删除")
    @ApiOperation(value = "足迹-通过id删除", notes = "足迹-通过id删除")
    @RequiresPermissions("water:water_footer:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterFooterService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "足迹-批量删除")
    @ApiOperation(value = "足迹-批量删除", notes = "足迹-批量删除")
    @RequiresPermissions("water:water_footer:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterFooterService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "足迹-通过id查询")
    @ApiOperation(value = "足迹-通过id查询", notes = "足迹-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterFooter> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterFooter waterFooter = waterFooterService.getById(id);
        if (waterFooter == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterFooter);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterFooter
     */
    @RequiresPermissions("water:water_footer:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterFooter waterFooter) {
        return super.exportXls(request, waterFooter, WaterFooter.class, "足迹");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_footer:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WaterFooter.class);
    }

}
