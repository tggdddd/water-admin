package org.jeecg.modules.demo.water.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 实际商品
 * @Author: jeecg-boot
 * @Date: 2023-06-26
 * @Version: V1.0
 */
@ApiModel(value = "water_shop_item对象", description = "实际商品")
@Data
@TableName("water_shop_item")
public class WaterShopItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 商品编码
     */
    @Excel(name = "商品编码", width = 15)
    @ApiModelProperty(value = "商品编码")
    private java.lang.String code;
    /**
     * 商品名称
     */
    @Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
    private java.lang.String name;
    /**
     * 型号/规格
     */
    @Excel(name = "型号/规格", width = 15)
    @ApiModelProperty(value = "型号/规格")
    private java.lang.String model;
    /**
     * 成本
     */
    @Excel(name = "成本", width = 15)
    @ApiModelProperty(value = "成本")
    private java.lang.String cost;
    /**
     * 零售
     */
    @Excel(name = "零售", width = 15)
    @ApiModelProperty(value = "零售")
    private java.lang.String retail;
    /**
     * 重量(kg)
     */
    @Excel(name = "重量(kg)", width = 15)
    @ApiModelProperty(value = "重量(kg)")
    private java.lang.String weight;
    /**
     * 库存
     */
    @Excel(name = "库存", width = 15)
    @ApiModelProperty(value = "库存")
    private java.lang.String reserve;
    /**
     * 逻辑删除
     */
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    private java.lang.String isDelete;
    /**
     * 是否启用
     */
    @Excel(name = "是否启用", width = 15, dicCode = "yes_or_no")
    @ApiModelProperty(value = "是否启用")
    private java.lang.String status;
    /**
     * 图片
     */
    @Excel(name = "图片", width = 15)
    private transient java.lang.String imageString;

    private byte[] image;
    /**
     * 所属商品
     */
    @ApiModelProperty(value = "所属商品")
    private java.lang.String fromId;

    public byte[] getImage() {
        if (imageString == null) {
            return null;
        }
        try {
            return imageString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getImageString() {
        if (image == null || image.length == 0) {
            return "";
        }
        try {
            return new String(image, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
