package org.jeecg.modules.demo.water.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.service.IWaterShopItemService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.vo.WaterShopPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @Description: 商品
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Api(tags = "商品")
@RestController
@RequestMapping("/water/waterShop")
@Slf4j
public class WaterShopController {
    @Autowired
    private IWaterShopService waterShopService;
    @Autowired
    private IWaterShopItemService waterShopItemService;

    /**
     * 分页列表查询
     *
     * @param waterShop
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "商品-分页列表查询")
    @ApiOperation(value = "商品-分页列表查询", notes = "商品-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<WaterShop>> queryPageList(WaterShop waterShop,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<WaterShop> queryWrapper = QueryGenerator.initQueryWrapper(waterShop, req.getParameterMap());
        Page<WaterShop> page = new Page<WaterShop>(pageNo, pageSize);
        IPage<WaterShop> pageList = waterShopService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param waterShopPage
     * @return
     */
    @AutoLog(value = "商品-添加")
    @ApiOperation(value = "商品-添加", notes = "商品-添加")
    @RequiresPermissions("water:water_shop:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody WaterShopPage waterShopPage) {
        WaterShop waterShop = new WaterShop();
        BeanUtils.copyProperties(waterShopPage, waterShop);
        waterShopService.saveMain(waterShop, waterShopPage.getWaterShopItemList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param waterShopPage
     * @return
     */
    @AutoLog(value = "商品-编辑")
    @ApiOperation(value = "商品-编辑", notes = "商品-编辑")
    @RequiresPermissions("water:water_shop:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody WaterShopPage waterShopPage) {
        WaterShop waterShop = new WaterShop();
        BeanUtils.copyProperties(waterShopPage, waterShop);
        WaterShop waterShopEntity = waterShopService.getById(waterShop.getId());
        if (waterShopEntity == null) {
            return Result.error("未找到对应数据");
        }
        waterShopService.updateMain(waterShop, waterShopPage.getWaterShopItemList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "商品-通过id删除")
    @ApiOperation(value = "商品-通过id删除", notes = "商品-通过id删除")
    @RequiresPermissions("water:water_shop:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterShopService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "商品-批量删除")
    @ApiOperation(value = "商品-批量删除", notes = "商品-批量删除")
    @RequiresPermissions("water:water_shop:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.waterShopService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "商品-通过id查询")
    @ApiOperation(value = "商品-通过id查询", notes = "商品-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<WaterShop> queryById(@RequestParam(name = "id", required = true) String id) {
        WaterShop waterShop = waterShopService.getById(id);
        if (waterShop == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waterShop);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "实际商品通过主表ID查询")
    @ApiOperation(value = "实际商品主表ID查询", notes = "实际商品-通主表ID查询")
    @GetMapping(value = "/queryWaterShopItemByMainId")
    public Result<List<WaterShopItem>> queryWaterShopItemListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<WaterShopItem> waterShopItemList = waterShopItemService.selectByMainId(id);
        return Result.OK(waterShopItemList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param waterShop
     */
    @RequiresPermissions("water:water_shop:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WaterShop waterShop) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<WaterShop> queryWrapper = QueryGenerator.initQueryWrapper(waterShop, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //配置选中数据查询条件
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        //Step.2 获取导出数据
        List<WaterShop> waterShopList = waterShopService.list(queryWrapper);

        // Step.3 组装pageList
        List<WaterShopPage> pageList = new ArrayList<WaterShopPage>();
        for (WaterShop main : waterShopList) {
            WaterShopPage vo = new WaterShopPage();
            BeanUtils.copyProperties(main, vo);
            List<WaterShopItem> waterShopItemList = waterShopItemService.selectByMainId(main.getId());
            vo.setWaterShopItemList(waterShopItemList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "商品列表");
        mv.addObject(NormalExcelConstants.CLASS, WaterShopPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品数据", "导出人:" + sysUser.getRealname(), "商品"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("water:water_shop:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<WaterShopPage> list = ExcelImportUtil.importExcel(file.getInputStream(), WaterShopPage.class, params);
                for (WaterShopPage page : list) {
                    WaterShop po = new WaterShop();
                    BeanUtils.copyProperties(page, po);
                    waterShopService.saveMain(po, page.getWaterShopItemList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

}
