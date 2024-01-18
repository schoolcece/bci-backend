package com.hcc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Description: 代码信息实体类
 *
 * @Author: hcc
 * @Date: 2023/12/11
 */
@Data
@TableName("bci_code")
@Builder
public class CodeDO {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 范式id
     */
    private int paradigmId;

    /**
     * 保存路径
     */
    private String url;

    /**
     * 上传用户id
     */
    private int userId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * md5
     */
    private String md5;

    /**
     * 上传时间
     */
    private Date createTime;

    /**
     * 状态： 0 代表待执行， 1 代表运行中， 2 代表运行成功， 3 代表运行失败
     */
//    private int status;

    /**
     * 是否展示: 0 代表不展示， 2 代表展示
     */
    @TableLogic(value = "1", delval = "0")
    private Integer showStatus;
}
