create database if not exists bci;

use bci;

drop table if exists bci_event;
drop table if exists bci_paradigm;
drop table if exists bci_user_paradigm;
drop table if exists bci_user;
drop table if exists bci_team;
drop table if exists bci_user_team;
drop table if exists bci_application;

drop table if exists bci_code;

CREATE TABLE `bci_event`(
                            `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                            `event_name` VARCHAR(255) NOT NULL COMMENT '赛事名称',
                            `event_leader` INT NOT NULL COMMENT '赛事负责人',
                            `event_desc` TEXT NOT NULL COMMENT '赛事描述',
                            `logo_url` VARCHAR(255) NOT NULL COMMENT 'logo路径',
                            `start_time` TIMESTAMP NOT NULL COMMENT '赛事开始时间',
                            `end_time` TIMESTAMP NOT NULL COMMENT '赛事结束时间',
                            primary key (`id`)
);
ALTER TABLE
    `bci_event` comment '赛事表';

insert into bci_event (event_name, event_leader, event_desc, logo_url, start_time, end_time)
values ('python赛事', 1, 'python算法提交', 'gdf', '2024-06-01', '2024-07-31');

insert into bci_event (event_name, event_leader, event_desc, logo_url, start_time, end_time)
values ('matlab赛事', 1, 'matlab算法提交', 'gdf', '2024-06-01', '2024-07-31');

CREATE TABLE `bci_paradigm`(
                               `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                               `event_id` INT NOT NULL COMMENT '所属赛事',
                               `paradigm_name` VARCHAR(255) NOT NULL COMMENT '范式名称',
                               `paradigm_desc` TEXT NOT NULL COMMENT '范式描述',
                               `a_data` VARCHAR(255) NOT NULL COMMENT 'a榜数据集',
                               `b_data` VARCHAR(255) NOT NULL COMMENT 'b榜数据集',
                               `change_time` TIMESTAMP NOT NULL COMMENT '切榜时间',
                               `image` VARCHAR(50) NOT NULL,
                               primary key (`id`)
);
ALTER TABLE
    `bci_paradigm` comment '范式表';

insert into bci_paradigm (event_id, paradigm_name, paradigm_desc, a_data, b_data, change_time, image)
values (1, 'ssvep', '稳态视觉诱发电位', '/fff/fff', '/ff/ff', '2024-07-01', 'ssvep:v1.0');
insert into bci_paradigm (event_id, paradigm_name, paradigm_desc, a_data, b_data, change_time, image)
values (1, 'mi', '运动想象', '/fff/fff', '/ff/ff', '2024-07-01', 'mi:v1.0');
insert into bci_paradigm (event_id, paradigm_name, paradigm_desc, a_data, b_data, change_time, image)
values (1, 'erp', 'erp', '/fff/fff', '/ff/ff', '2024-07-01', 'erp:v1.0');
insert into bci_paradigm (event_id, paradigm_name, paradigm_desc, a_data, b_data, change_time, image)
values (1, 'turing', '图灵', '/fff/fff', '/ff/ff', '2024-07-01', 'turing:v1.0');
insert into bci_paradigm (event_id, paradigm_name, paradigm_desc, a_data, b_data, change_time, image)
values (1, 'attack', '对抗攻击', '/fff/fff', '/ff/ff', '2024-07-01', 'attack:v1.0');
insert into bci_paradigm (event_id, paradigm_name, paradigm_desc, a_data, b_data, change_time, image)
values (2, 'emotion', '情感', '/fff/fff', '/ff/ff', '2024-07-01', 'emotion:v1.0');

CREATE TABLE `bci_user_paradigm`(
                                    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                    `user_id` INT NOT NULL,
                                    `paradigm_id` INT NOT NULL,
                                    primary key (`id`)
);
ALTER TABLE
    `bci_user_paradigm` comment '范式负责人表';

CREATE TABLE `bci_user`(
                           `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                           `username` VARCHAR(255) NOT NULL COMMENT '用户名',
                           `mobile` VARCHAR(50) NOT NULL COMMENT '手机号',
                           `email` VARCHAR(50) NOT NULL COMMENT '邮箱',
                           `university` VARCHAR(50) NOT NULL COMMENT '学校',
                           `profession` VARCHAR(50) NOT NULL COMMENT '专业',
                           `uid` VARCHAR(32) NOT NULL COMMENT '用户唯一标识',
                           `birthday` TIMESTAMP NOT NULL COMMENT '用户出生年月',
                           `status` TINYINT NOT NULL DEFAULT '1' COMMENT '用户状态： 0代表禁用， 1代表正常可用',
                           `role` TINYINT NOT NULL COMMENT '用户角色： 1代表管理员， 0代表普通用户',
                           `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            primary key (`id`)
);
ALTER TABLE
    `bci_user` comment '用户表';

CREATE TABLE `bci_team`(
                           `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                           `event_id` INT NOT NULL COMMENT '队伍所属赛事',
                           `leader_id` INT NOT NULL COMMENT '队长',
                           `team_name` VARCHAR(255) NOT NULL COMMENT '队伍名称',
                           `instructor` VARCHAR(255) NOT NULL COMMENT '队伍导师',
                           `university` VARCHAR(255) NOT NULL COMMENT '学校、单位',
                           `status` TINYINT NOT NULL DEFAULT '0' COMMENT '队伍状态： 0代表正常可用， 1代表禁用， 2代表队伍解散注销',
                           `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '队伍创建时间',
                           primary key (`id`)
);
ALTER TABLE
    `bci_team` comment '队伍表';

CREATE TABLE `bci_user_team`(
                                `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                `user_id` INT NOT NULL COMMENT '用户id',
                                `team_id` INT NOT NULL COMMENT '队伍id',
                                `event_id` INT NOT NULL COMMENT '赛事id',
                                `role` TINYINT NOT NULL COMMENT '角色：0代表队员， 1代表队长',
                                `status` TINYINT NOT NULL COMMENT '队伍状态： 0代表待审核， 1代表审核通过'
);
ALTER TABLE
    `bci_user_team` comment '队伍成员表';

CREATE TABLE `bci_application`(
                                  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                  `paradigm_id` INT NOT NULL COMMENT '报名范式',
                                  `team_id` INT NOT NULL COMMENT '报名队伍id',
                                  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
                                  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态： 0代表待审核， 1代表审核通过， 2代表审核不通过',
                                  `update_user` INT NOT NULL COMMENT '最后操作人',
                                  `comment` TEXT NOT NULL COMMENT '审核备注',
                                  UNIQUE KEY unique_combination (paradigm_id, team_id)
);
ALTER TABLE
    `bci_application` comment '报名参赛表';


insert into bci.bci_user (id, username, mobile, email, university, profession, uid, birthday, role) values (null, 'hcc', '15735181737', '1301646502@qq.com', 'byut', '电子信息', 'hcc1573518', '1996-09-18', 1);

insert into bci.bci_user (id, username, mobile, email, university, profession, uid, birthday, role) values (null, 'hxx', '15735181737', '1301646502@qq.com', 'byut', '电子信息', 'hcc1573518', '1996-09-18', 0);

CREATE TABLE `bci_code`(
                           `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
                           `paradigm_id` INT NOT NULL comment '范式',
                           `url` VARCHAR(255) NOT NULL comment '代码路径',
                           `user_id` INT NOT NULL comment '上传用户',
                           `file_name` VARCHAR(255) NOT NULL comment '文件名',
                           `md5` VARCHAR(255) NOT NULL COMMENT '文件md5码',
                           `create_time` TIMESTAMP NOT NULL default CURRENT_TIMESTAMP comment '上传时间',
                           `show_status` TINYINT NOT NULL default 1 comment '是否展示： 0代表不展示, 1代表展示',
                           primary key (`id`)
);
ALTER TABLE
    `bci_code` comment '代码表';

CREATE TABLE `bci_task`(
                           `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `user_id` INT NOT NULL comment '用户id',
                           `team_id` INT NOT NULL comment '队伍id',
                           `code_id` INT NOT NULL comment '代码id',
                           `paradigm_id` INT NOT NULL comment '范式id',
                           `task_name` VARCHAR(255) NOT NULL comment '任务名称',
                           `task_type` TINYINT NOT NULL comment '任务类型 0代表cpu任务， 1代表gpu任务',
                           `dataset` TINYINT NOT NULL comment '数据集 0代表A榜， 1代表B榜',
                           `container_id` VARCHAR(255) default NULL comment '容器id',
                           `status` TINYINT NOT NULL default 0 comment '状态： 0:待运行 1:运行中 2:运行成功 3:运行失败',
                           `create_time` TIMESTAMP NOT NULL default CURRENT_TIMESTAMP comment '创建时间',
                           `score` float default NULL comment '得分',
                           `compute_node_ip` VARCHAR(25) comment '计算节点',
                           `show_status` TINYINT NOT NULL default 1 comment '逻辑删除'
);
ALTER TABLE
    `bci_task` comment '任务表';

CREATE TABLE `bci_compute_resource`(
                                       `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       `ip` VARCHAR(15) NOT NULL comment '节点ip',
                                       `running_tasks` INT NOT NULL comment '运行任务数',
                                       `max_tasks` TINYINT NOT NULL comment '最大运行任务数',
                                       `status` TINYINT NOT NULL comment '状态 0：禁用 1：可用'
);
ALTER TABLE
    `bci_compute_resource` comment '计算资源表';

CREATE TABLE `bci_container_log`(
                                    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    `task_id` INT NOT NULL comment '任务id',
                                    `content` TEXT NOT NULL comment '日志内容'
);
ALTER TABLE
    `bci_container_log` comment '容器日志表';