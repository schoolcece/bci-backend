package com.hcc.bciauthserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.bciauthserver.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
