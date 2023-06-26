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
import org.jeecg.modules.demo.water.entity.WaterSearchRecord;
import org.jeecg.modules.demo.water.service.IWaterSearchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 搜索历史记录
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Api(tags = "搜索历史记录")
@RestController
@RequestMapping("/water/waterSearchRecord")
@Slf4j
public class WaterSearchRecordController extends JeecgController<WaterSearchRecord, IWaterSearchRecordService> {
	@Autowired
	private IWaterSearchRecordService waterSearchRecordService;

	/**
	 * 分页列表查询
	 *
	 * @param waterSearchRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "搜索历史记录-分页列表查询")
	@ApiOperation(value = "搜索历史记录-分页列表查询", notes = "搜索历史记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<WaterSearchRecord>> queryPageList(WaterSearchRecord waterSearchRecord,
														  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
														  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
														  HttpServletRequest req) {
		QueryWrapper<WaterSearchRecord> queryWrapper = QueryGenerator.initQueryWrapper(waterSearchRecord, req.getParameterMap());
		Page<WaterSearchRecord> page = new Page<WaterSearchRecord>(pageNo, pageSize);
		IPage<WaterSearchRecord> pageList = waterSearchRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 添加
	 *
	 * @param waterSearchRecord
	 * @return
	 */
	@AutoLog(value = "搜索历史记录-添加")
	@ApiOperation(value = "搜索历史记录-添加", notes = "搜索历史记录-添加")
	@RequiresPermissions("water:water_search_record:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody WaterSearchRecord waterSearchRecord) {
		waterSearchRecordService.save(waterSearchRecord);
		return Result.OK("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param waterSearchRecord
	 * @return
	 */
	@AutoLog(value = "搜索历史记录-编辑")
	@ApiOperation(value = "搜索历史记录-编辑", notes = "搜索历史记录-编辑")
	@RequiresPermissions("water:water_search_record:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
	public Result<String> edit(@RequestBody WaterSearchRecord waterSearchRecord) {
		waterSearchRecordService.updateById(waterSearchRecord);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "搜索历史记录-通过id删除")
	@ApiOperation(value = "搜索历史记录-通过id删除", notes = "搜索历史记录-通过id删除")
	@RequiresPermissions("water:water_search_record:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
		waterSearchRecordService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "搜索历史记录-批量删除")
	@ApiOperation(value = "搜索历史记录-批量删除", notes = "搜索历史记录-批量删除")
	@RequiresPermissions("water:water_search_record:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.waterSearchRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "搜索历史记录-通过id查询")
	@ApiOperation(value = "搜索历史记录-通过id查询", notes = "搜索历史记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<WaterSearchRecord> queryById(@RequestParam(name = "id", required = true) String id) {
		WaterSearchRecord waterSearchRecord = waterSearchRecordService.getById(id);
		if (waterSearchRecord == null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(waterSearchRecord);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param waterSearchRecord
	 */
	@RequiresPermissions("water:water_search_record:exportXls")
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, WaterSearchRecord waterSearchRecord) {
		return super.exportXls(request, waterSearchRecord, WaterSearchRecord.class, "搜索历史记录");
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("water:water_search_record:importExcel")
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, WaterSearchRecord.class);
	}

}
