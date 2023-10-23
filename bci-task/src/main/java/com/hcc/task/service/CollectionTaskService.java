package com.hcc.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.task.entity.*;

/**
 * 任务表
 *
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
public interface CollectionTaskService extends IService<CollectionTaskEntity> {

    void runCollectionTask(CollectionVo collectionVo);
}

