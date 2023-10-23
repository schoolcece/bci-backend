package com.hcc.scheduler.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hcc.scheduler.entity.ComputeNodeEntity;
import com.hcc.scheduler.mapper.ComputeNodeMapper;
import com.hcc.scheduler.service.ComputeNodeService;
import org.springframework.stereotype.Service;

@Service("computeResourceService")
public class ComputeNodeServiceImpl extends ServiceImpl<ComputeNodeMapper, ComputeNodeEntity> implements ComputeNodeService {

}
