create database if not exists bci;

use bci;

drop table if exists bci_paradigm;
drop table if exists bci_user;
drop table if exists bci_user_additional;
drop table if exists bci_team;
drop table if exists bci_team_right_relation;
drop table if exists bci_code;
drop table if exists bci_task;
drop table if exists bci_log;
drop table if exists bci_rank;
drop table if exists bci_compute_resource;
drop table if exists bci_collection_task;

/*==============================================================*/
/* Table: bci_user                                              */
/*==============================================================*/

create table bci_user
(
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null COMMENT '邮箱',
  `university` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null COMMENT '学校',
  `profession` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null COMMENT '专业',
  `uid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null COMMENT '唯一标识',
  `age` int not null comment '年龄',
  `status` tinyint not null DEFAULT 1 COMMENT '状态  0：禁用   1：正常',
  `create_time` TIMESTAMP not null COMMENT '创建时间',
   PRIMARY KEY (`user_id`)
);

alter table bci_user comment '用户表';


/*==============================================================*/
/* Table: bci_user                                              */
/*==============================================================*/
-- create table bci_user
-- (
--     `user_id` int NOT NULL AUTO_INCREMENT,
--     `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
--     `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null COMMENT '密码',
--     `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not null COMMENT '手机号',
--     `status` tinyint not null DEFAULT 1 COMMENT '状态  0：禁用   1：正常',
--     `create_time` TIMESTAMP not null COMMENT '创建时间',
--     PRIMARY KEY (`user_id`)
-- );
--
-- alter table bci_user comment '用户表';

/*==============================================================*/
/* Table: bci_user_additional                                              */
/*==============================================================*/
create table bci_user_additional
(
    `uadd_id` int NOT NULL AUTO_INCREMENT,
    `user_id` int NOT NULL COMMENT '用户id',
    `competition` int NOT NULL COMMENT '赛事 0常规赛事 1Matlab赛事',
    `team_id` int null default null comment '队伍id',
    `role` int not null default 0 comment '角色 0普通用户 1队员 2队长',
    `create_time` TIMESTAMP not null COMMENT '创建时间',
    PRIMARY KEY (`uadd_id`)
);

alter table bci_user_additional comment '用户补充表';

/*==============================================================*/
/* Table: bci_team                                              */
/*==============================================================*/
create table bci_team
(
    `team_id` int NOT NULL AUTO_INCREMENT,
    `team_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '队伍名',
    `instructor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci not NULL COMMENT '指导老师',
    `university` varchar(25) not null COMMENT "权限",
    `status` tinyint not null default 1 COMMENT '状态  0：禁用   1：正常',
    `create_time` TIMESTAMP NULL DEFAULT NULL COMMENT '创建时间',
    `user_id` int not null comment "队长",
    `competition` int not null comment "赛事 0常规赛事 1Matlab赛事",
    PRIMARY KEY (`team_id`)
);

alter table bci_team comment '队伍表';

/*==============================================================*/
/* Table: bci_team_right_relation                                              */
/*==============================================================*/
create table bci_team_right_relation
(
    `trr_id` int NOT NULL AUTO_INCREMENT,
    `team_id` int not null COMMENT '队伍id',
    `right` int not null COMMENT '权限',
    `status` tinyint not null default 0 COMMENT '状态  0：未申请   1：待审核 2：审核完成',
    `create_time` TIMESTAMP NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`trr_id`)
);

alter table bci_team_right_relation comment '队伍权限表';

/*==============================================================*/
/* Table: bci_code                                              */
/*==============================================================*/
create table bci_code
(
    `code_id` int NOT NULL AUTO_INCREMENT,
    `type` int  not null COMMENT '范式 0:ssvep 1:mi 2:erp 3:emotion 4:other ',
    `url` varchar(255) COMMENT '路径',
    `user_id` int  COMMENT '用户id',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `status` int COMMENT  '状态： 0未执行 1初始化 2运行中 3已完成 4错误',
    `file_name` VARCHAR(255) COMMENT '文件名',
    `task_id` int null default null comment '代码执行任务',
    `show_status`  tinyint not null default 1 COMMENT '状态  0：删除   1：显示',
    PRIMARY KEY (`code_id`)
);

alter table bci_code comment '代码表';

/*==============================================================*/
/* Table: bci_task                                              */
/*==============================================================*/
create table bci_task
(
    `task_id` int NOT NULL AUTO_INCREMENT,
    `user_id` int  not null COMMENT '用户id',
    `team_id` int  not null COMMENT '用户提交时所在队伍id',
    `image` varchar(20)  not null COMMENT '镜像',
    `type` int  not null COMMENT '范式 0:ssvep 1:mi 2:erp 3:emotion 4:other ',
    `dataset` int not null COMMENT '数据集 0A榜 1B榜',
    `code_id` int  COMMENT '代码id',
    `container_id` varchar(200)  COMMENT '容器id',
    `status` tinyint not NULL DEFAULT 0 COMMENT '状态： 0:未提交 1:初始化 2:初始化失败 3:运行中 4:运行失败 5:已完成',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `score` float null default null comment '任务得分',
    `compute_node_id`  int not null comment '计算节点id',
    `show_status`  tinyint not null default 1 COMMENT '状态  0：删除   1：显示',
    PRIMARY KEY (`task_id`)
);

alter table bci_task comment '任务表';

/*==============================================================*/
/* Table: bci_log                                              */
/*==============================================================*/
create table bci_log
(
    `log_id` int NOT NULL AUTO_INCREMENT comment '日志id',
    `content` TEXT  COMMENT '日志内容',
    `task_id` int  COMMENT '任务id',
    PRIMARY KEY (`log_id`)
);

alter table bci_log comment '日志表';

/*==============================================================*/
/* Table: bci_compute_resource                                              */
/*==============================================================*/
create table bci_compute_resource
(
    `node_id` int NOT NULL AUTO_INCREMENT,
    `ip`  varchar(15)  COMMENT '节点ip',
    `running_tasks` int  COMMENT '现运行任务数',
    `max_tasks` int  COMMENT '最大运行任务数',
    `status` tinyint  COMMENT '目前是否可用 0 不可用 1 可用',
    PRIMARY KEY (`node_id`)
);

alter table bci_compute_resource comment '计算资源表';

/*==============================================================*/
/* Table: bci_collection_task                                       */
/*==============================================================*/
-- create table bci_collection_task
-- (
--     `collection_task_id` bigint NOT NULL AUTO_INCREMENT,
--     `type`  varchar(20)  COMMENT '设备类型',
--     `config` varchar(255)  COMMENT '配置',
--     `builder` varchar(20)  COMMENT '创建者用户名',
--     `status` int  COMMENT '状态 0未运行 1运行中 2运行错误',
--     `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
--     `show_status` int NULL DEFAULT 1 COMMENT '0 删除 1 展示',
--     `comment` varchar(255)  COMMENT '备注',
--     PRIMARY KEY (`collection_task_id`)
-- );
--
-- alter table bci_collection_task '采集任务表';


CREATE TABLE `undo_log` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `branch_id` bigint(20) NOT NULL,
                            `xid` varchar(100) NOT NULL,
                            `context` varchar(128) NOT NULL,
                            `rollback_info` longblob NOT NULL,
                            `log_status` int(11) NOT NULL,
                            `log_created` datetime NOT NULL,
                            `log_modified` datetime NOT NULL,
                            `ext` varchar(100) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
