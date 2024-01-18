package com.hcc.bciauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.entity.UserTeamDO;
import com.hcc.common.model.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: mapper层 成员队伍关系操作
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
@Mapper
public interface UserTeamMapper extends BaseMapper<UserTeamDO> {
    void updateStatusByUserIdAndTeamId(@Param("userId") int userId, @Param("teamId") int teamId, @Param("approved") int approved);

    void updateRoleByUserIdAndTeamIdWithStatus(@Param("userId")int userId, @Param("teamId")int teamId, @Param("role")int role, @Param("approved")int approved);

    List<UserInfoVO> selectTeamMembersByTeamId(@Param("teamId")int teamId);
}
