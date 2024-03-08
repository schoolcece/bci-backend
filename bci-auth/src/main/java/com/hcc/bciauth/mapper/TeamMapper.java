package com.hcc.bciauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.dto.PermissionInfoDTO;
import com.hcc.common.model.dto.TeamInfoDTO;
import com.hcc.common.model.entity.ApplicationDO;
import com.hcc.common.model.entity.TeamDO;
import com.hcc.common.model.vo.TeamInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: mapper层 队伍操作
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@Mapper
public interface TeamMapper extends BaseMapper<TeamDO> {

    /**
     * 查询用户的队伍信息
     * @param userId
     * @return
     */
    List<TeamInfoDTO> selectTeamInfoByUserId(@Param("userId") int userId);

    /**
     * 查询队伍各范式的权限
     * @param teamId
     * @return
     */
    List<PermissionInfoDTO> selectPermissionByTeamId(@Param("teamId") int teamId);

    void updateStatusByTeamId(@Param("teamId")int teamId, @Param("disband") int disband);

    void insertApplicationIfNotExist(@Param("application") ApplicationDO applicationDO);

    List<TeamInfoVO> selectByEventLikeTeamName(@Param("event")int event, @Param("teamName")String teamName, @Param("index")int index, @Param("pageSize")int pageSize);

    List<TeamInfoVO> getTeamInfo(@Param("event")int event, @Param("paradigm")int paradigm);

    void updateAppStatusByTeamId(@Param("teamId")int teamId, @Param("paradigm")int paradigm, @Param("status")int status);

    String selectTeamNameByTeamId(@Param("teamId")int teamId);
}
