package com.hcc.bciauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.entity.UserTeamDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: mapper层 成员队伍关系操作
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
@Mapper
public interface UserTeamMapper extends BaseMapper<UserTeamDO> {
}
