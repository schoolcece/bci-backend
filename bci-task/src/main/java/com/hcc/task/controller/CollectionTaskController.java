package com.hcc.task.controller;


import com.hcc.common.utils.R;
import com.hcc.task.entity.CollectionTaskEntity;
import com.hcc.task.entity.CollectionVo;
import com.hcc.task.mapper.CollectionTaskMapper;
import com.hcc.task.mapper.TaskMapper;
import com.hcc.task.service.CollectionTaskService;
import com.hcc.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/task")
public class CollectionTaskController {

    @Autowired
    CollectionTaskService collectionTaskService;

    @Autowired
    CollectionTaskMapper collectionTaskMapper;

    @RequestMapping("/buildCollection")
    public R buildCollection(@RequestBody CollectionVo collectionVo){
        CollectionTaskEntity collectionTaskEntity = new CollectionTaskEntity();
        collectionTaskEntity.setType(collectionVo.getDevice());
        collectionTaskEntity.setStatus(0);
        collectionTaskEntity.setComment(collectionVo.getComment());
        collectionTaskEntity.setBuilder("hcc");
        collectionTaskEntity.setCreateTime(new Date());
        String config = "socket "+collectionVo.getDatasourceIP()+":"+collectionVo.getDatasourcePORT()
                + " -> "+"kafka "+ collectionVo.getKafkaIP()+":"+collectionVo.getKafkaPORT()+":"+collectionVo.getKafkaTOPIC();
        collectionTaskEntity.setConfig(config);
        collectionTaskService.runCollectionTask(collectionVo);
        collectionTaskMapper.insert(collectionTaskEntity);
        return R.ok();
    }
    
    @RequestMapping("/listCollection")
    public R listCollection(){
        List<CollectionTaskEntity> collectionTaskEntities = collectionTaskMapper.selectList(null);
//        System.out.println(collectionTaskEntities.get(5).getType());
        return R.ok().put("data",collectionTaskEntities);
    }
}
