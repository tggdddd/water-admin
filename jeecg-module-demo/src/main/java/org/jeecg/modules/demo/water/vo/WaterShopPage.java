package org.jeecg.modules.demo.water.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.demo.water.entity.WaterShopItem;
import org.jeecg.modules.demo.water.entity.WaterShopModel;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 商品
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@Data
@ApiModel(value = "water_shopPage对象", description = "商品")
public class WaterShopPage {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**
     * 分类
     */
    @Excel(name = "分类", width = 15, dictTable = "water_class", dicText = "name", dicCode = "id")
    @Dict(dictTable = "water_class", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "分类")
    private java.lang.String typeId;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**
     * 简介
     */
    @Excel(name = "简介", width = 15)
    @ApiModelProperty(value = "简介")
    private java.lang.String biref;
    /**
     * 详情
     */
    @Excel(name = "详情", width = 15)
    @ApiModelProperty(value = "详情")
    private java.lang.String detail;
    /**
     * 图片
     */
    @Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private java.lang.String imageString;
    /**
     * 详情图片
     */
    @Excel(name = "详情图片", width = 15)
    @ApiModelProperty(value = "详情图片")
    private java.lang.String imagesString;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String unit;
    /**
     * 是否启用
     */
    @Excel(name = "是否启用", width = 15, dicCode = "yes_or_no")
    @Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "是否启用")
    private java.lang.String status;
    /**
     * 逻辑删除
     */
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    private java.lang.String isDelete;
    /**
     * 销量
     */
    @Excel(name = "销量", width = 15)
    @ApiModelProperty(value = "销量")
    private java.lang.String sale;

    @ExcelCollection(name = "规格")
    @ApiModelProperty(value = "规格")
    private List<WaterShopModel> waterShopModelList;
    @ExcelCollection(name = "售卖商品")
    @ApiModelProperty(value = "售卖商品")
    private List<WaterShopItem> waterShopItemList;

}
