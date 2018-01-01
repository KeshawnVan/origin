CREATE DATABASE origin;

use origin;


create table user(
	id bigint(20) not null auto_increment primary key comment '唯一标识',
    name varchar(50) default null comment '用户名称',
    age tinyint(3) default null comment '年龄',
    birthday datetime default null comment '生日'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';