create database if not exists bci;

use bci;

drop table if exists bci_event;
drop table if exists bci_paradigm;
drop table if exists bci_user_paradigm;
drop table if exists bci_user;
drop table if exists bci_team;
drop table if exists bci_user_team;
drop table if exists bci_application;

CREATE TABLE `bci_event`(
                            `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            `event_name` VARCHAR(255) NOT NULL COMMENT '赛事名称',
                            `event_leader` INT NOT NULL COMMENT '赛事负责人',
                            `event_desc` TEXT NOT NULL COMMENT '赛事描述',
                            `logo_url` VARCHAR(255) NOT NULL COMMENT 'logo路径',
                            `start_time` TIMESTAMP NOT NULL COMMENT '赛事开始时间',
                            `end_time` TIMESTAMP NOT NULL COMMENT '赛事结束时间'
);
ALTER TABLE
    `bci_event` comment '赛事表';

CREATE TABLE `bci_paradigm`(
                               `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               `event_id` INT NOT NULL COMMENT '所属赛事',
                               `paradigm_name` VARCHAR(255) NOT NULL COMMENT '范式名称',
                               `paradigm_desc` TEXT NOT NULL COMMENT '范式描述',
                               `a_data` VARCHAR(255) NOT NULL COMMENT 'a榜数据集',
                               `b_data` VARCHAR(255) NOT NULL COMMENT 'b榜数据集',
                               `change_time` TIMESTAMP NOT NULL COMMENT '切榜时间',
                               `image` VARCHAR(50) NOT NULL
);
ALTER TABLE
    `bci_paradigm` comment '范式表';

CREATE TABLE `bci_user_paradigm`(
                                    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    `user_id` INT NOT NULL,
                                    `paradigm_id` INT NOT NULL
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
                           `id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `event_id` INT NOT NULL COMMENT '队伍所属赛事',
                           `leader_id` INT NOT NULL COMMENT '队长',
                           `team_name` VARCHAR(255) NOT NULL UNIQUE COMMENT '队伍名称',
                           `instructor` VARCHAR(255) NOT NULL COMMENT '队伍导师',
                           `university` VARCHAR(255) NOT NULL COMMENT '学校、单位',
                           `status` TINYINT NOT NULL DEFAULT '0' COMMENT '队伍状态： 0代表正常可用， 1代表禁用， 2代表队伍解散注销',
                           `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '队伍创建时间'
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
                                  `status` TINYINT NOT NULL COMMENT '审核状态： 0代表待审核， 1代表审核通过， 2代表审核不通过',
                                  `update_user` INT NOT NULL COMMENT '最终审核人',
                                  `comment` TEXT NOT NULL COMMENT '审核备注'
);
ALTER TABLE
    `bci_application` comment '报名参赛表';


insert into bci.bci_user (id, username, mobile, email, university, profession, uid, birthday, role) values (null, 'hcc', '15735181737', '1301646502@qq.com', 'byut', '电子信息', 'hcc1573518', '1996-09-18', 1);

insert into bci.bci_user (id, username, mobile, email, university, profession, uid, birthday, role) values (null, 'hxx', '15735181737', '1301646502@qq.com', 'byut', '电子信息', 'hcc1573518', '1996-09-18', 0);
