package com.hcc.bciauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: mapper层 用户操作
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    /**
     * 查询用户的范式负责人权限
     * @param userId
     * @return
     */
    List<Integer> selectByUserId(@Param("userId") int userId);
}
