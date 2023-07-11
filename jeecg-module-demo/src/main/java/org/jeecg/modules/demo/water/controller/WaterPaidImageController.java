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
import org.jeecg.modules.demo.water.entity.WaterPaidImage;
import org.jeecg.modules.demo.water.service.IWaterPaidImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 收款二维码
 * @Author: jeecg-boot
 * @Date: 2023-07-10
 * @Version: V1.0
 */
@Api(tags = "收款二维码")
@RestController
@RequestMapping("/water/waterPaidImage")
@Slf4j
public class WaterPaidImageController extends JeecgController<WaterPaidImage, IWaterPaidImageService> {
    @Autowired
    private IWaterPaidImageService waterPaidImageService;

    /**
     * 分页列表查询
     *
     * @param waterPaidImage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "收款二维码-分页列表查询")
    @ApiOperation(value = "收款二维码-分页列表查询", notes = "收款二维码-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterPaidImage>> queryPageList(WaterPaidImage waterPaidImage,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest req) {
        QueryWrapper<WaterPaidImage> queryWrapper = QueryGenerator.initQueryWrapper(waterPaidImage, req.getParameterMap());
        Page<WaterPaidImage> page = new Page<WaterPaidImage>(pageNo, pageSize);
        IPage<WaterPaidImage> pageList = waterPaidImageService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param waterPaidImage
     * @return
     */
    @AutoLog(value = "收款二维码-添加")
    @ApiOperation(value = "收款二维码-添加", notes = "收款二维码-添加")
    @RequiresPermissions("water:water_paid_image:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterPaidImage waterPaidImage) {
        waterPaidImageService.save(waterPaidImage);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param waterPaidImage
     * @return
     */
    @AutoLog(value = "收款二维码-编辑")
    @ApiOperation(value = "收款二维码-编辑", notes = "收款二维码-编辑")
    @RequiresPermissions("water:water_paid_image:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterPaidImage waterPaidImage) {
        waterPaidImageService.updateById(waterPaidImage);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "收款二维码-通过id删除")
    @ApiOperation(value = "收款二维码-通过id删除", notes = "收款二维码-通过id删除")
    @RequiresPermissions("water:water_paid_image:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterPaidImageService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "收款二维码-批量删除")
    @ApiOperation(value = "收款二维码-批量删除", notes = "收款二维码-批量删除")
    @RequiresPermissions("water:water_paid_image:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterPaidImageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "收款二维码-通过id查询")
    @ApiOperation(value = "收款二维码-通过id查询", notes = "收款二维码-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterPaidImage> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterPaidImage waterPaidImage = waterPaidImageService.getById(id);
        if (waterPaidImage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterPaidImage);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterPaidImage
     */
    @RequiresPermissions("water:water_paid_image:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterPaidImage waterPaidImage) {
        return super.exportXls(request, waterPaidImage, WaterPaidImage.class, "收款二维码");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_paid_image:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WaterPaidImage.class);
    }

}
