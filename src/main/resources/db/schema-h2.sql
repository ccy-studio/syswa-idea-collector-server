DROP TABLE IF EXISTS t_user;

create table t_user
(
    id          bigint                                 not null comment 'ID'
        primary key auto_increment,
    open_id     varchar(50)                            not null comment 'OpenId',
    union_id    varchar(50)                            null comment 'UnionID',
    nick_name   varchar(25)                            null comment '用户昵称',
    avatar      varchar(255) default null comment '头像',
    city        varchar(25)  default null comment '城市',
    province    varchar(25)  default null comment '省份',
    gender      int          default 1 comment '性别 1男,0女',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '修改时间',
    is_delete   tinyint      default 0                 not null comment '删除标识 0: 未删除'
) COMMENT '用户表';

DROP TABLE IF EXISTS t_article;
create table t_article
(
    id          bigint      not null comment 'ID'
        primary key auto_increment,
    user_id     bigint      not null comment '作者',
    give        bigint      not null default 0 comment '点赞数',
    see         bigint      not null default 0 comment '阅读数',
    title       varchar(50) not null comment '标题',
    cover       varchar(255)         default null comment '封面',
    status      int(1)      not null default 0 comment '状态: 0进行中,1已结束',
    create_time timestamp            default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp            default CURRENT_TIMESTAMP not null comment '修改时间',
    is_delete   tinyint              default 0 not null comment '删除标识 0: 未删除'
) COMMENT '创意主表';


DROP TABLE IF EXISTS t_article_info;
create table t_article_info
(
    id          bigint                                 not null comment 'ID'
        primary key auto_increment,
    article_id  bigint                                 not null comment '创意主表ID',
    content     text                                   not null comment '内容描述',
    comment     varchar(255) default null comment '版本备注',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '修改时间',
    is_delete   tinyint      default 0                 not null comment '删除标识 0: 未删除'
) COMMENT '创意内容表';


DROP TABLE IF EXISTS t_article_comment;
create table t_article_comment
(
    id          bigint                              not null comment 'ID'
        primary key auto_increment,
    article_id  bigint                              not null comment '创意主表ID',
    user_id     bigint                              not null comment '评论用户',
    content     text                                not null comment '评论内容',
    give        bigint                              not null default 0 comment '点赞数',
    parent_id   bigint    default null comment '父评论ID',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null comment '修改时间',
    is_delete   tinyint   default 0                 not null comment '删除标识 0: 未删除'
) COMMENT '用户评论表';


DROP TABLE IF EXISTS t_ai_cover_task;
create table t_ai_cover_task
(
    id              bigint                                not null comment 'ID'
        primary key auto_increment,
    article_id      bigint                                not null comment '创意主表ID',
    width           int                                   not null comment '图片尺寸宽',
    height          int                                   not null comment '图片尺寸高',
    ai_model        varchar(30) default null comment '使用的模型名称',
    negative_prompt text        default null comment '反向词',
    prompt          text                                  not null comment '正向提示词',
    status          int(1)                                not null default 0 comment '任务状态：0执行中,1已结束',
    create_time     timestamp   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     timestamp   default CURRENT_TIMESTAMP not null comment '修改时间',
    is_delete       tinyint     default 0                 not null comment '删除标识 0: 未删除'
) COMMENT 'AI生图任务表';


DROP TABLE IF EXISTS t_ai_cover_task_result;
create table t_ai_cover_task_result
(
    id             bigint                                 not null comment 'ID'
        primary key auto_increment,
    cover_task_id  bigint                                 not null comment '任务主表ID',
    task_id        varchar(255)                           not null comment '任务ID',
    estimate       int          default null comment '预计生成时间/秒',
    gen_img_remote varchar(255) default null comment '临时生成图片地址',
    gen_img_local  varchar(255)  default null comment '保存本机图片路径',
    status         int(1)                                 not null default 0 comment '任务状态：0执行中,1生成成功,2生成失败',
    create_time    timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    timestamp    default CURRENT_TIMESTAMP not null comment '修改时间',
    is_delete      tinyint      default 0                 not null comment '删除标识 0: 未删除'
) COMMENT 'AI生图结果表';
