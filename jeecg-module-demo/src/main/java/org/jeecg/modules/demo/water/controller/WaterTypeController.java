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
import org.jeecg.modules.demo.water.entity.WaterType;
import org.jeecg.modules.demo.water.service.IWaterTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 水的类型
 * @Author: jeecg-boot
 * @Date: 2023-06-25
 * @Version: V1.0
 */
@Api(tags = "水的类型")
@RestController
@RequestMapping("/water/waterType")
@Slf4j
public class WaterTypeController extends JeecgController<WaterType, IWaterTypeService> {
	@Autowired
	private IWaterTypeService waterTypeService;

	/**
	 * 分页列表查询
	 *
	 * @param waterType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "水的类型-分页列表查询")
	@ApiOperation(value = "水的类型-分页列表查询", notes = "水的类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<WaterType>> queryPageList(WaterType waterType,
												  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
												  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
												  HttpServletRequest req) {
		QueryWrapper<WaterType> queryWrapper = QueryGenerator.initQueryWrapper(waterType, req.getParameterMap());
		Page<WaterType> page = new Page<WaterType>(pageNo, pageSize);
		IPage<WaterType> pageList = waterTypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 添加
	 *
	 * @param waterType
	 * @return
	 */
	@AutoLog(value = "水的类型-添加")
	@ApiOperation(value = "水的类型-添加", notes = "水的类型-添加")
	@RequiresPermissions("water:water_type:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody WaterType waterType) {
		waterTypeService.save(waterType);
		return Result.OK("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param waterType
	 * @return
	 */
	@AutoLog(value = "水的类型-编辑")
	@ApiOperation(value = "水的类型-编辑", notes = "水的类型-编辑")
	@RequiresPermissions("water:water_type:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
	public Result<String> edit(@RequestBody WaterType waterType) {
		waterTypeService.updateById(waterType);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "水的类型-通过id删除")
	@ApiOperation(value = "水的类型-通过id删除", notes = "水的类型-通过id删除")
	@RequiresPermissions("water:water_type:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
		waterTypeService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "水的类型-批量删除")
	@ApiOperation(value = "水的类型-批量删除", notes = "水的类型-批量删除")
	@RequiresPermissions("water:water_type:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.waterTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "水的类型-通过id查询")
	@ApiOperation(value = "水的类型-通过id查询", notes = "水的类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<WaterType> queryById(@RequestParam(name = "id", required = true) String id) {
		WaterType waterType = waterTypeService.getById(id);
		if (waterType == null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(waterType);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param waterType
	 */
	@RequiresPermissions("water:water_type:exportXls")
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, WaterType waterType) {
		return super.exportXls(request, waterType, WaterType.class, "水的类型");
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("water:water_type:importExcel")
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, WaterType.class);
	}

}
