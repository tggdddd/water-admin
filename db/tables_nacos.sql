CREATE database if NOT EXISTS `nacos` default character set utf8mb4 collate utf8mb4_general_ci;
use `nacos`;
/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : 127.0.0.1:3306
 Source Schema         : nacos-os

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 06/08/2022 15:12:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`
(
    `id`           bigint(20)                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL,
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT 'content',
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
    `gmt_modified` datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin         NULL COMMENT 'source user',
    `src_ip`       varchar(20) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'source ip',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL,
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT '' COMMENT '租户字段',
    `c_desc`       varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL,
    `c_use`        varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `effect`       varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `type`         varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `c_schema`     text CHARACTER SET utf8 COLLATE utf8_bin         NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfo_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 43
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info`
VALUES (1, 'jeecg-dev.yaml', 'DEFAULT_GROUP',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        'edb0e79d570edf341755caf3853f11e4', '2021-03-03 13:01:11', '2022-08-06 07:10:17', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info`
VALUES (2, 'jeecg.yaml', 'DEFAULT_GROUP',
        'server:\n  tomcat:\n    max-swallow-size: -1\n  error:\n    include-exception: true\n    include-stacktrace: ALWAYS\n    include-message: ALWAYS\n  compression:\n    enabled: true\n    min-response-size: 1024\n    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*\nmanagement:\n  health:\n    mail:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    health:\n      sensitive: true\n  endpoint:\n    health:\n      show-details: ALWAYS\nspring:\n  servlet:\n    multipart:\n      max-file-size: 10MB\n      max-request-size: 10MB\n  mail:\n    host: smtp.163.com\n    username: jeecgos@163.com\n    password: ??\n    properties:\n      mail:\n        smtp:\n          auth: true\n          starttls:\n            enable: true\n            required: true\n  quartz:\n    job-store-type: jdbc\n    initialize-schema: embedded\n    auto-startup: false\n    startup-delay: 1s\n    overwrite-existing-jobs: true\n    properties:\n      org:\n        quartz:\n          scheduler:\n            instanceName: MyScheduler\n            instanceId: AUTO\n          jobStore:\n            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore\n            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate\n            tablePrefix: QRTZ_\n            isClustered: true\n            misfireThreshold: 12000\n            clusterCheckinInterval: 15000\n          threadPool:\n            class: org.quartz.simpl.SimpleThreadPool\n            threadCount: 10\n            threadPriority: 5\n            threadsInheritContextClassLoaderOfInitializingThread: true\n  jackson:\n    date-format:   yyyy-MM-dd HH:mm:ss\n    time-zone:   GMT+8\n  aop:\n    proxy-target-class: true\n  activiti:\n    check-process-definitions: false\n    async-executor-activate: false\n    job-executor-activate: false\n  jpa:\n    open-in-view: false\n  freemarker:\n    suffix: .ftl\n    content-type: text/html\n    charset: UTF-8\n    cache: false\n    prefer-file-system-access: false\n    template-loader-path:\n      - classpath:/templates\n  mvc:\n    static-path-pattern: /**\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  resource:\n    static-locations: classpath:/static/,classpath:/public/\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\nmybatis-plus:\n  mapper-locations: classpath*:org/jeecg/modules/**/xml/*Mapper.xml\n  global-config:\n    banner: false\n    db-config:\n      id-type: ASSIGN_ID\n      table-underline: true\n  configuration:\n    call-setters-on-nulls: true',
        '2117a96ba08e8fd0f66825e87416af27', '2021-03-03 13:01:42', '2022-08-05 13:12:21', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info`
VALUES (3, 'jeecg-gateway-router.json', 'DEFAULT_GROUP',
        '[{\n  \"id\": \"jeecg-system\",\n  \"order\": 0,\n  \"predicates\": [{\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/sys/**\",\n      \"_genkey_1\": \"/jmreport/**\",\n      \"_genkey_3\": \"/online/**\",\n      \"_genkey_4\": \"/generic/**\"\n    }\n  }],\n  \"filters\": [],\n  \"uri\": \"lb://jeecg-system\"\n}, {\n  \"id\": \"jeecg-demo\",\n  \"order\": 1,\n  \"predicates\": [{\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/mock/**\",\n      \"_genkey_1\": \"/test/**\",\n      \"_genkey_2\": \"/bigscreen/template1/**\",\n      \"_genkey_3\": \"/bigscreen/template2/**\"\n    }\n  }],\n  \"filters\": [],\n  \"uri\": \"lb://jeecg-demo\"\n}, {\n  \"id\": \"jeecg-system-websocket\",\n  \"order\": 2,\n  \"predicates\": [{\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/websocket/**\",\n      \"_genkey_1\": \"/newsWebsocket/**\"\n    }\n  }],\n  \"filters\": [],\n  \"uri\": \"lb:ws://jeecg-system\"\n}, {\n  \"id\": \"jeecg-demo-websocket\",\n  \"order\": 3,\n  \"predicates\": [{\n    \"name\": \"Path\",\n    \"args\": {\n      \"_genkey_0\": \"/vxeSocket/**\"\n    }\n  }],\n  \"filters\": [],\n  \"uri\": \"lb:ws://jeecg-demo\"\n}]',
        'be6548051d99309d7fa5ac4398404201', '2021-03-03 13:02:14', '2022-02-23 11:49:01', NULL, '0:0:0:0:0:0:0:1', '',
        '', '', '', '', 'json', '');
INSERT INTO `config_info`
VALUES (20, 'jeecg-gateway-dev.yaml', 'DEFAULT_GROUP',
        'jeecg:\n  route:\n    config:\n      #type:database nacos yml\n      data-type: database\n      group: DEFAULT_GROUP\n      data-id: jeecg-gateway-router\nspring:\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    port: 6379\n    password:',
        '0fc619d2d5e304f18bc4ea8be99f68a4', '2022-08-04 16:36:11', '2022-08-06 07:11:34', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`
(
    `id`           bigint(20)                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `datum_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT '内容',
    `gmt_modified` datetime                                         NOT NULL COMMENT '修改时间',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum` (`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '增加租户字段'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`
(
    `id`           bigint(20)                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin  NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin  NOT NULL COMMENT 'group_id',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'app_name',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin      NOT NULL COMMENT 'content',
    `beta_ips`     varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL COMMENT 'betaIps',
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin   NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                                          NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
    `gmt_modified` datetime                                          NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin          NULL COMMENT 'source user',
    `src_ip`       varchar(20) CHARACTER SET utf8 COLLATE utf8_bin   NULL     DEFAULT NULL COMMENT 'source ip',
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfobeta_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info_beta'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`
(
    `id`           bigint(20)                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT '' COMMENT 'tenant_id',
    `tag_id`       varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL COMMENT 'app_name',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT 'content',
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
    `gmt_modified` datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin         NULL COMMENT 'source user',
    `src_ip`       varchar(20) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'source ip',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfotag_datagrouptenanttag` (`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info_tag'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`
(
    `id`        bigint(20)                                       NOT NULL COMMENT 'id',
    `tag_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
    `tag_type`  varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL DEFAULT NULL COMMENT 'tag_type',
    `data_id`   varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`  varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
    `nid`       bigint(20)                                       NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`nid`) USING BTREE,
    UNIQUE INDEX `uk_configtagrelation_configidtag` (`id`, `tag_name`, `tag_type`) USING BTREE,
    INDEX `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_tag_relation'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`
(
    `id`                bigint(20) UNSIGNED                              NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_id`          varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
    `quota`             int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
    `usage`             int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '使用量',
    `max_size`          int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
    `max_aggr_size`     int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
    `gmt_create`        datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
    `gmt_modified`      datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_group_id` (`group_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`
(
    `id`           bigint(20) UNSIGNED                              NOT NULL,
    `nid`          bigint(20) UNSIGNED                              NOT NULL AUTO_INCREMENT,
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `group_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL COMMENT 'app_name',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL,
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `gmt_create`   datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00',
    `gmt_modified` datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin         NULL,
    `src_ip`       varchar(20) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `op_type`      char(10) CHARACTER SET utf8 COLLATE utf8_bin     NULL     DEFAULT NULL,
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`nid`) USING BTREE,
    INDEX `idx_gmt_create` (`gmt_create`) USING BTREE,
    INDEX `idx_gmt_modified` (`gmt_modified`) USING BTREE,
    INDEX `idx_did` (`data_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 91
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '多租户改造'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info`
VALUES (20, 70, 'jeecg-gateway-dev.yaml', 'DEFAULT_GROUP', '',
        'jeecg:\n  route:\n    config:\n      #mode: database、nacos、yml\n      data-type: database\n      #nacos: jeecg-gateway-router.json\n      group: DEFAULT_GROUP\n      data-id: jeecg-gateway-router\nspring:\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    port: 6379\n    password:',
        '26fff601e10bbc8bc5ff1fa2b192087b', '2010-05-05 00:00:00', '2022-08-05 10:45:21', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 71, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao :\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg :\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path :\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: 127.0.0.1:9200\n    check-enabled: false\n  desform:\n    theme-color: \"#1890ff\"\n    upload-type: system\n    map:\n      baidu: ??\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\n\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n      # agent-app-secret: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        '17b0553ae2ade6474301e3d4eca6f05e', '2010-05-05 00:00:00', '2022-08-05 10:54:54', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 72, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: 127.0.0.1:9200\n    check-enabled: false\n  desform:\n    theme-color: \'#1890ff\'\n    upload-type: system\n    map:\n      baidu: ??\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        '70922f6374bf2e4ccf0de8c089445811', '2010-05-05 00:00:00', '2022-08-05 10:57:40', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 73, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  desform:\n    theme-color: \'#1890ff\'\n    upload-type: system\n    map:\n      baidu: ??\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        '035fff10fc0e5a38abf3c357afff7c67', '2010-05-05 00:00:00', '2022-08-05 10:59:02', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 74, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  desform:\n    theme-color: \"#1890ff\"\n    upload-type: system\n    map:\n      baidu: ??\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        '87ec968621f8ac532e2fc50f98dd4f57', '2010-05-05 00:00:00', '2022-08-05 11:00:08', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 75, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        'edb0e79d570edf341755caf3853f11e4', '2010-05-05 00:00:00', '2022-08-05 11:01:10', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 76, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  desform:\n    theme-color: \"#1890ff\"\n    upload-type: system\n    map:\n      baidu: ??\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        '53b1c1130dff673311ad863b4ce67c8e', '2010-05-05 00:00:00', '2022-08-05 11:02:49', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 77, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  desform:\n    theme-color: \"#1890ff\"\n    upload-type: system\n    map:\n      baidu: ??\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        '32d655df70c77beb8e39c5d3d8c69c9c', '2010-05-05 00:00:00', '2022-08-05 11:03:31', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 78, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\nsignUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  desform:\n    theme-color: \"#1890ff\"\n    upload-type: system\n    map:\n      baidu: ??\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        'd3b15d3bb35c4baed32f75eabb2bf864', '2010-05-05 00:00:00', '2022-08-05 11:04:54', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 79, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  desform:\n    theme-color: \"#1890ff\"\n    upload-type: system\n    map:\n      baidu: ??\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        '53b1c1130dff673311ad863b4ce67c8e', '2010-05-05 00:00:00', '2022-08-05 13:02:54', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 80, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  desform:\n    theme-color: \"#1890ff\"\n    upload-type: system\n    map:\n      baidu: ??\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true',
        '92ced3a81dece861666606c44cd4f630', '2010-05-05 00:00:00', '2022-08-05 13:03:28', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 81, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp',
        '364814ff81fb2a38c869f7bb5aa92f45', '2010-05-05 00:00:00', '2022-08-05 13:03:55', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 82, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379',
        '064d0471e33d707a5b70e0807f8f0d93', '2010-05-05 00:00:00', '2022-08-05 13:04:39', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (2, 83, 'jeecg.yaml', 'DEFAULT_GROUP', '',
        'server:\n  tomcat:\n    max-swallow-size: -1\n  error:\n    include-exception: true\n    include-stacktrace: ALWAYS\n    include-message: ALWAYS\n  compression:\n    enabled: true\n    min-response-size: 1024\n    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*\nmanagement:\n  health:\n    mail:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    health:\n      sensitive: true\n  endpoint:\n    health:\n      show-details: ALWAYS\nspring:\n  servlet:\n    multipart:\n      max-file-size: 10MB\n      max-request-size: 10MB\n  mail:\n    host: smtp.163.com\n    username: jeecgos@163.com\n    password: ??\n    properties:\n      mail:\n        smtp:\n          auth: true\n          starttls:\n            enable: true\n            required: true\n  quartz:\n    job-store-type: jdbc\n    initialize-schema: embedded\n    auto-startup: false\n    startup-delay: 1s\n    overwrite-existing-jobs: true\n    properties:\n      org:\n        quartz:\n          scheduler:\n            instanceName: MyScheduler\n            instanceId: AUTO\n          jobStore:\n            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore\n            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate\n            tablePrefix: QRTZ_\n            isClustered: true\n            misfireThreshold: 12000\n            clusterCheckinInterval: 15000\n          threadPool:\n            class: org.quartz.simpl.SimpleThreadPool\n            threadCount: 10\n            threadPriority: 5\n            threadsInheritContextClassLoaderOfInitializingThread: true\n  jackson:\n    date-format:   yyyy-MM-dd HH:mm:ss\n    time-zone:   GMT+8\n  aop:\n    proxy-target-class: true\n  activiti:\n    check-process-definitions: false\n    async-executor-activate: false\n    job-executor-activate: false\n  jpa:\n    open-in-view: false\n  freemarker:\n    suffix: .ftl\n    content-type: text/html\n    charset: UTF-8\n    cache: false\n    prefer-file-system-access: false\n    template-loader-path:\n      - classpath:/templates\n  mvc:\n    static-path-pattern: /**\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  resource:\n    static-locations: classpath:/static/,classpath:/public/\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\nmybatis-plus:\n  mapper-locations: classpath*:org/jeecg/modules/**/xml/*Mapper.xml\n  global-config:\n    banner: false\n    db-config:\n      id-type: ASSIGN_ID\n      table-underline: true\n  configuration:\n    call-setters-on-nulls: true',
        '2117a96ba08e8fd0f66825e87416af27', '2010-05-05 00:00:00', '2022-08-05 13:05:34', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 84, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    dynamic:\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379',
        'a12eaf6e6c090b303590f1e83c22ac3f', '2010-05-05 00:00:00', '2022-08-05 13:07:33', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 85, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    dynamic:\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379',
        '5e33b9dc9022eee8a1652e473dadbc42', '2010-05-05 00:00:00', '2022-08-05 13:09:30', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (2, 86, 'jeecg.yaml', 'DEFAULT_GROUP', '', 'server:\n  tomcat:\n    max-swallow-size: -1',
        '4525d8351d9498a8e5f43373ee6367a1', '2010-05-05 00:00:00', '2022-08-05 13:09:55', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (2, 87, 'jeecg.yaml', 'DEFAULT_GROUP', '',
        'server:\n  tomcat:\n    max-swallow-size: -1\n  error:\n    include-exception: true\n    include-stacktrace: ALWAYS\n    include-message: ALWAYS\n  compression:\n    enabled: true\n    min-response-size: 1024\n    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*\nmanagement:\n  health:\n    mail:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    health:\n      sensitive: true\n  endpoint:\n    health:\n      show-details: ALWAYS\nspring:\n  servlet:\n    multipart:\n      max-file-size: 10MB\n      max-request-size: 10MB\n  mail:\n    host: smtp.163.com\n    username: jeecgos@163.com\n    password: ??\n    properties:\n      mail:\n        smtp:\n          auth: true\n          starttls:\n            enable: true\n            required: true\n  quartz:\n    job-store-type: jdbc\n    initialize-schema: embedded\n    auto-startup: false\n    startup-delay: 1s\n    overwrite-existing-jobs: true\n    properties:\n      org:\n        quartz:\n          scheduler:\n            instanceName: MyScheduler\n            instanceId: AUTO\n          jobStore:\n            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore\n            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate\n            tablePrefix: QRTZ_\n            isClustered: true\n            misfireThreshold: 12000\n            clusterCheckinInterval: 15000\n          threadPool:\n            class: org.quartz.simpl.SimpleThreadPool\n            threadCount: 10\n            threadPriority: 5\n            threadsInheritContextClassLoaderOfInitializingThread: true\n  jackson:\n    date-format:   yyyy-MM-dd HH:mm:ss\n    time-zone:   GMT+8\n  aop:\n    proxy-target-class: true\n  activiti:\n    check-process-definitions: false\n    async-executor-activate: false\n    job-executor-activate: false\n  jpa:\n    open-in-view: false\n  freemarker:\n    suffix: .ftl\n    content-type: text/html\n    charset: UTF-8\n    cache: false\n    prefer-file-system-access: false\n    template-loader-path:\n      - classpath:/templates\n  mvc:\n    static-path-pattern: /**\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  resource:\n    static-locations: classpath:/static/,classpath:/public/\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\nmybatis-plus:\n  mapper-locations: classpath*:org/jeecg/modules/**/xml/*Mapper.xml\n  global-config:\n    banner: false\n    db-config:\n      id-type: ASSIGN_ID\n      table-underline: true\n  configuration:\n    call-setters-on-nulls: true',
        '2117a96ba08e8fd0f66825e87416af27', '2010-05-05 00:00:00', '2022-08-05 13:10:58', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (2, 88, 'jeecg.yaml', 'DEFAULT_GROUP', '',
        'server:\n  tomcat:\n    max-swallow-size: -1\n  error:\n    include-exception: true\n    include-stacktrace: ALWAYS\n    include-message: ALWAYS\n  compression:\n    enabled: true\n    min-response-size: 1024\n    mime-types: application/javascript,application/json,application/xml,text/html,text/xml,text/plain,text/css,image/*\nmanagement:\n  health:\n    mail:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    health:\n      sensitive: true\n  endpoint:\n    health:\n      show-details: ALWAYS\nspring:\n  servlet:\n    multipart:\n      max-file-size: 10MB\n      max-request-size: 10MB\n  mail:\n    host: smtp.163.com\n    username: jeecgos@163.com\n    password: ??\n    properties:\n      mail:\n        smtp:\n          auth: true\n          starttls:\n            enable: true\n            required: true\n  quartz:\n    job-store-type: jdbc\n    initialize-schema: embedded\n    auto-startup: false\n  jackson:\n    date-format:   yyyy-MM-dd HH:mm:ss\n    time-zone:   GMT+8\n  aop:\n    proxy-target-class: true\n  activiti:\n    check-process-definitions: false\n    async-executor-activate: false\n    job-executor-activate: false\n  jpa:\n    open-in-view: false\n  freemarker:\n    suffix: .ftl\n    content-type: text/html\n    charset: UTF-8\n    cache: false\n    prefer-file-system-access: false\n    template-loader-path:\n      - classpath:/templates\n  mvc:\n    static-path-pattern: /**\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  resource:\n    static-locations: classpath:/static/,classpath:/public/\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\nmybatis-plus:\n  mapper-locations: classpath*:org/jeecg/modules/**/xml/*Mapper.xml\n  global-config:\n    banner: false\n    db-config:\n      id-type: ASSIGN_ID\n      table-underline: true\n  configuration:\n    call-setters-on-nulls: true',
        'a1effef2c22a7d2846f84728aa29ecd4', '2010-05-05 00:00:00', '2022-08-05 13:12:21', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (1, 89, 'jeecg-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n        allow:\n      web-stat-filter:\n        enabled: true\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,wall,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n        master:\n          url: jdbc:mysql://jeecg-boot-mysql:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\n          username: root\n          password: root\n          driver-class-name: com.mysql.cj.jdbc.Driver\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    password:\n    port: 6379\n  rabbitmq:\n    host: jeecg-boot-rabbitmq\n    username: guest\n    password: guest\n    port: 5672\n    publisher-confirms: true\n    publisher-returns: true\n    virtual-host: /\n    listener:\n      simple:\n        acknowledge-mode: manual\n        concurrency: 1\n        max-concurrency: 1\n        retry:\n          enabled: true\nminidao:\n  base-package: org.jeecg.modules.jmreport.*,org.jeecg.modules.drag.*\njeecg:\n  signatureSecret: dd05f1c54d63749eda95f9fa6d49v442a\n  signUrls: /sys/dict/getDictItems/*,/sys/dict/loadDict/*,/sys/dict/loadDictOrderByValue/*,/sys/dict/loadDictItem/*,/sys/dict/loadTreeData,/sys/api/queryTableDictItemsByCode,/sys/api/queryFilterTableDictInfo,/sys/api/queryTableDictByKeys,/sys/api/translateDictFromTable,/sys/api/translateDictFromTableByKeys\n  uploadType: local\n  domainUrl:\n    pc: http://localhost:3100\n    app: http://localhost:8051\n  path:\n    upload: /opt/upFiles\n    webapp: /opt/webapp\n  shiro:\n    excludeUrls: /test/jeecgDemo/demo3,/test/jeecgDemo/redisDemo/**,/category/**,/visual/**,/map/**,/jmreport/bigscreen2/**\n  desform:\n    theme-color: \"#1890ff\"\n    upload-type: system\n    map:\n      baidu: ??\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    bucketName: jeecgdev\n    staticDomain: ??\n  elasticsearch:\n    cluster-name: jeecg-ES\n    cluster-nodes: jeecg-boot-es:9200\n    check-enabled: false\n  file-view-domain: 127.0.0.1:8012\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: otatest\n  jmreport:\n    mode: dev\n    is_verify_token: false\n    verify_methods: remove,delete,save,add,update\n  wps:\n    domain: https://wwo.wps.cn/office/\n    appid: ??\n    appsecret: ??\n  xxljob:\n    enabled: false\n    adminAddresses: http://jeecg-boot-xxljob:9080/xxl-job-admin\n    appname: ${spring.application.name}\n    accessToken: \'\'\n    logPath: logs/jeecg/job/jobhandler/\n    logRetentionDays: 30\n  redisson:\n    address: jeecg-boot-redis:6379\n    password:\n    type: STANDALONE\n    enabled: true\nlogging:\n  level:\n    org.jeecg.modules.system.mapper : info\ncas:\n  prefixUrl: http://localhost:8888/cas\nknife4j:\n  production: false\n  basic:\n    enable: false\n    username: jeecg\n    password: jeecg1314\njustauth:\n  enabled: true\n  type:\n    GITHUB:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/github/callback\n    WECHAT_ENTERPRISE:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/wechat_enterprise/callback\n      agent-id: ??\n    DINGTALK:\n      client-id: ??\n      client-secret: ??\n      redirect-uri: http://sso.test.com:8080/jeecg-boot/thirdLogin/dingtalk/callback\n  cache:\n    type: default\n    prefix: \'demo::\'\n    timeout: 1h\nthird-app:\n  enabled: false\n  type:\n    WECHAT_ENTERPRISE:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??\n    DINGTALK:\n      enabled: false\n      client-id: ??\n      client-secret: ??\n      agent-id: ??',
        '53b1c1130dff673311ad863b4ce67c8e', '2010-05-05 00:00:00', '2022-08-06 07:10:17', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');
INSERT INTO `his_config_info`
VALUES (20, 90, 'jeecg-gateway-dev.yaml', 'DEFAULT_GROUP', '',
        'jeecg:\n  route:\n    config:\n      #mode:database nacos yml\n      data-type: database\n      group: DEFAULT_GROUP\n      data-id: jeecg-gateway-router\nspring:\n  redis:\n    database: 0\n    host: jeecg-boot-redis\n    port: 6379\n    password:',
        'b08a4c456f508fdd0fc347305da39a9e', '2010-05-05 00:00:00', '2022-08-06 07:11:34', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`
(
    `role`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `resource` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `action`   varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    UNIQUE INDEX `uk_role_permission` (`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`
(
    `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `role`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    UNIQUE INDEX `uk_username_role` (`username`, `role`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles`
VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`
(
    `id`                bigint(20) UNSIGNED                              NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`         varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
    `quota`             int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
    `usage`             int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '使用量',
    `max_size`          int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
    `max_aggr_size`     int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
    `gmt_create`        datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
    `gmt_modified`      datetime                                         NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '租户容量信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`
(
    `id`            bigint(20)                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `kp`            varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
    `tenant_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
    `tenant_name`   varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
    `tenant_desc`   varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
    `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL DEFAULT NULL COMMENT 'create_source',
    `gmt_create`    bigint(20)                                       NOT NULL COMMENT '创建时间',
    `gmt_modified`  bigint(20)                                       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_tenant_info_kptenantid` (`kp`, `tenant_id`) USING BTREE,
    INDEX `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'tenant_info'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info`
VALUES (1, '1', 'ac14ab82-51f8-4f0c-aa5b-25fb8384bfb6', 'jeecg', 'jeecg 测试命名空间', 'nacos', 1653291038942,
        1653291038942);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `enabled`  tinyint(1)                                                    NOT NULL,
    PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users`
VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;