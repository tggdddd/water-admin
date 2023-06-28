package org.jeecg.modules.demo.water.appController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.base.ThinkResult;
import org.jeecg.modules.demo.water.entity.WaterSearchRecord;
import org.jeecg.modules.demo.water.entity.WaterShop;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.service.IWaterSearchRecordService;
import org.jeecg.modules.demo.water.service.IWaterShopService;
import org.jeecg.modules.demo.water.vo.DictEnum;
import org.jeecg.modules.demo.water.vo.ShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/search")
public class SearchController {
    @Autowired
    IWaterShopService shopService;
    @Autowired
    private IWaterSearchRecordService waterSearchRecordService;

    /**
     * 搜索词获取
     */
    @RequestMapping(value = "index")
    public ThinkResult queryPageList(HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        JSONObject result = new JSONObject();
        Page<WaterSearchRecord> page = new Page<WaterSearchRecord>(1, 10);
//            获取用户搜索词
        if (username != null) {
            LambdaQueryWrapper<WaterSearchRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .select(WaterSearchRecord::getSearchWord)
                    .eq(WaterSearchRecord::getCreateBy, username)
                    .orderByDesc(WaterSearchRecord::getCreateTime);
            Page<WaterSearchRecord> page1 = waterSearchRecordService.page(page, queryWrapper);
            result.put("historyKeywordList", page1.getRecords());
        }
//           获取热门关键词
        MPJLambdaWrapper<WaterSearchRecord> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper
                .select(WaterSearchRecord::getSearchWord)
                .selectCount(WaterSearchRecord::getSearchWord, "count")
                .groupBy(WaterSearchRecord::getSearchWord)
                .orderByDesc("count");
        Page<WaterSearchRecord> page1 = waterSearchRecordService.page(page, queryWrapper);
        result.put("hotKeywordList", page1.getRecords());
//            随机关键词
        MPJLambdaWrapper<WaterSearchRecord> queryWrapper1 = new MPJLambdaWrapper<>();
        queryWrapper1
                .select(WaterSearchRecord::getSearchWord)
                .orderByAsc("random()");
        Page<WaterSearchRecord> a4 = new Page<WaterSearchRecord>(1, 1);
        Page<WaterSearchRecord> page2 = waterSearchRecordService.page(a4, queryWrapper);
        result.put("defaultKeyword", page2.getTotal() != 0 ? page2.getRecords().get(0) : null);
        return ThinkResult.ok(result);
    }

    /**
     * 搜索
     */
    @RequestMapping(value = "search")
    public ThinkResult search(@RequestParam String sort, @RequestParam String order
            , @RequestParam String sales, @RequestParam String keyword) {
//            增加搜索记录
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isNotBlank(keyword) && sysUser != null) {
            WaterSearchRecord waterSearchRecord = new WaterSearchRecord();
            waterSearchRecord.setSearchWord(keyword);
            waterSearchRecordService.save(waterSearchRecord);
        }
//            获取商品列表
        MPJLambdaWrapper<WaterShop> shopLambdaQueryWrapper = new MPJLambdaWrapper<>();
        shopLambdaQueryWrapper.eq(WaterShop::getStatus, DictEnum.Enable.getValue())
                .selectAll(WaterShop.class)
                .leftJoin(WaterShopItem.class, WaterShopItem::getFromId, WaterShop::getId)
                .select(WaterShopItem::getFromId)
                .selectCount(WaterShopItem::getReserve)
                .selectMin(WaterShopItem::getRetail)
                .eq(WaterShopItem::getStatus, DictEnum.Enable.getValue())
                .like(WaterShop::getName, "%" + keyword + "%")
                .groupBy(WaterShopItem::getFromId);
        if (Objects.equals(sort, "price")) {
            // 按价格
            shopLambdaQueryWrapper.orderBy(true, "asc".equals(order), "retail");
        } else if (Objects.equals(sort, "sales")) {
            // 按销量
            shopLambdaQueryWrapper.orderBy(true, "asc".equals(sales), WaterShop::getSale);
        } else {
            // 按商品添加时间
            shopLambdaQueryWrapper.orderByAsc(WaterShop::getCreateTime);
        }
        List<ShopVo> list = shopService.selectJoinList(ShopVo.class, shopLambdaQueryWrapper);
        return ThinkResult.ok(list);
    }

    /**
     * 获取搜索词
     */
    @RequestMapping("helper")
    public ThinkResult getHelper(@RequestParam("keyword") String keyword) {
        Page<WaterShop> page = new Page<WaterShop>(1, 20);
        LambdaQueryWrapper<WaterShop> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<WaterShop> like = wrapper.select(WaterShop::getName).like(WaterShop::getName, "%" + keyword + "%");
        Page<WaterShop> page1 = shopService.page(page, like);
        return ThinkResult.ok(page1.getRecords().stream().map(WaterShop::getName).collect(Collectors.toList()));
    }

    /**
     * 清空历史搜索记录
     */
    @RequestMapping("clear")
    public ThinkResult clear(HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        if (username != null) {
            LambdaQueryWrapper<WaterSearchRecord> wrapper = new LambdaQueryWrapper<WaterSearchRecord>()
                    .eq(WaterSearchRecord::getCreateBy, username);
            waterSearchRecordService.remove(wrapper);
        }
        return ThinkResult.ok(null);
    }

    /**
     * 删除历史记录
     */
    @AutoLog(value = "搜索历史记录-通过id删除")
    @ApiOperation(value = "搜索历史记录-通过id删除", notes = "搜索历史记录-通过id删除")
    @RequiresPermissions("water:water_search_record:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        waterSearchRecordService.removeById(id);
        return Result.OK("删除成功!");
    }
}
