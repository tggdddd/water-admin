package org.jeecg.modules.demo.water.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 轮播图
 * @Author: jeecg-boot
 * @Date: 2023-06-29
 * @Version: V1.0
 */
@Data
@TableName("water_ad")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "water_ad对象", description = "轮播图")
public class WaterAd implements Serializable {
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
     * 图片名
     */
    @Excel(name = "图片名", width = 15)
    @ApiModelProperty(value = "图片名")
    private java.lang.String name;
    /**
     * 图片
     */
    @Excel(name = "图片", width = 15)
    private transient java.lang.String imageString;

    private byte[] image;
    /**
     * 排序
     */
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer sort;
    /**
     * 点击链接
     */
    @Excel(name = "点击链接", width = 15)
    @ApiModelProperty(value = "点击链接")
    private java.lang.String url;
    /**
     * 图片描述
     */
    @Excel(name = "图片描述", width = 15)
    @ApiModelProperty(value = "图片描述")
    private java.lang.String description;

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
