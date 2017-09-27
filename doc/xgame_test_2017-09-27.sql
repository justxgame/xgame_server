# ************************************************************
# Sequel Pro SQL dump
# Version 4499
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 117.50.8.212 (MySQL 5.7.19)
# Database: xgame_test
# Generation Time: 2017-09-27 05:55:34 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table broadcast_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `broadcast_info`;

CREATE TABLE `broadcast_info` (
  `transection` bigint(19) NOT NULL AUTO_INCREMENT,
  `server_id` varchar(20) DEFAULT NULL,
  `msg` text,
  `send_user` varchar(20) DEFAULT NULL,
  `indate` datetime DEFAULT NULL,
  PRIMARY KEY (`transection`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `broadcast_info` WRITE;
/*!40000 ALTER TABLE `broadcast_info` DISABLE KEYS */;

INSERT INTO `broadcast_info` (`transection`, `server_id`, `msg`, `send_user`, `indate`)
VALUES
	(7,'1011','祝贺 xx获得xxx','admin','2017-09-16 00:07:20'),
	(8,'1011','祝贺 xx获得xxx','admin','2017-09-16 00:07:20'),
	(9,'1011','祝贺 xx获得xxx','admin','2017-09-16 00:07:20'),
	(10,'1011','祝贺 xx获得xxx','admin','2017-09-16 00:07:20'),
	(11,'1000','test2',NULL,'2017-09-23 14:06:06'),
	(12,'1000','test2',NULL,'2017-09-23 14:06:09'),
	(13,'1000','test2',NULL,'2017-09-23 14:06:15'),
	(14,'1000','test4',NULL,'2017-09-23 14:06:33'),
	(15,'1000','test5',NULL,'2017-09-23 14:06:46'),
	(16,'1000','123123',NULL,'2017-09-23 14:09:03'),
	(17,'1000','123123',NULL,'2017-09-23 14:11:02'),
	(18,'1000','1111',NULL,'2017-09-23 14:13:41'),
	(19,'1000','123123123123123123123123',NULL,'2017-09-23 14:36:36'),
	(20,'1000','66666',NULL,'2017-09-23 14:39:41'),
	(21,'1000','11111111111111111111111111111111111111',NULL,'2017-09-23 14:40:17'),
	(22,'1000','test4','game','2017-09-23 15:08:29'),
	(23,'1000','测试内容','game','2017-09-24 11:36:40'),
	(24,'1000','123456','game','2017-09-24 14:22:00'),
	(25,'1000','12345567667','game','2017-09-24 14:24:58'),
	(26,'1000','test','测试','2017-09-25 10:46:53'),
	(27,'1000','test2','测试','2017-09-25 10:47:28'),
	(28,'1000','是大法师父是否','game','2017-09-26 21:25:28'),
	(29,'1000','阿斯发生大幅','game','2017-09-26 21:35:45'),
	(30,'1000','上帝发誓地方是','game','2017-09-26 21:36:50'),
	(31,'1000','阿斯大法撒旦发撒','game','2017-09-26 21:37:52'),
	(32,'1000','sadfasfas','game','2017-09-26 21:41:23'),
	(33,'1000','1231231','game','2017-09-26 21:42:06'),
	(34,'1000','aaaa','game','2017-09-26 21:43:02'),
	(35,'1000','a','game','2017-09-26 21:44:26'),
	(36,'1000','a','game','2017-09-26 21:44:32'),
	(37,'1000','test','game','2017-09-26 21:45:57'),
	(38,'1000','as','game','2017-09-26 21:46:45'),
	(39,'1000','ces','game','2017-09-26 21:49:01'),
	(40,'1000','asdadasdasdsfadsfasdfasdfsd','game','2017-09-26 21:49:09'),
	(41,'1000','a','game','2017-09-26 21:51:15'),
	(42,'1000','aaaa','game','2017-09-26 21:51:20'),
	(43,'1000','asdasdasdasdasdasda','game','2017-09-26 21:51:26'),
	(44,'1000','a','game','2017-09-26 21:53:45'),
	(45,'1000','sadasdasdasdasdasdasdasdasdasda','game','2017-09-26 21:53:51'),
	(46,'1000','a阿斯顿撒法师打发斯蒂芬 a阿斯顿撒法师打发斯蒂芬 a阿斯顿撒法师打发斯蒂芬 a阿斯顿撒法师打发斯蒂芬 a阿斯顿撒法师打发斯蒂芬 a阿斯顿撒法师打发斯蒂芬 a阿斯顿撒法师打发斯蒂芬','game','2017-09-26 21:54:03'),
	(47,'1000','a阿斯顿撒法师打发斯蒂芬 a阿斯顿撒法师打发斯蒂芬','game','2017-09-26 21:54:19'),
	(48,'1011','11111','game','2017-09-26 22:06:28'),
	(49,'1000','11111112312312312312312313','game','2017-09-26 22:06:34'),
	(50,'1000','123','game','2017-09-26 22:08:04'),
	(51,'1000','系统官博测试','game','2017-09-26 22:08:16'),
	(52,'1000','1','game','2017-09-26 22:09:24'),
	(53,'1000','11111111','game','2017-09-26 22:09:44'),
	(54,'1000','这是一个测试信息','game','2017-09-26 22:10:07'),
	(55,'1000','a','game','2017-09-26 22:10:28'),
	(56,'1000','大萨达所大所','game','2017-09-26 22:10:33'),
	(57,'1000','阿斯顿发生的发生的发生大发','game','2017-09-26 22:10:45'),
	(58,'1000','我自如果很多很多很多很多的话，会怎么样啊啊','game','2017-09-26 22:10:57'),
	(59,'1000','奥术大师大所大所大所大所大所多','game','2017-09-26 22:11:19'),
	(60,'1000','啊啊','game','2017-09-26 22:11:37'),
	(61,'1000','奥术大师','game','2017-09-26 22:11:49'),
	(62,'1000','奥术大师大多','game','2017-09-26 22:11:53'),
	(63,'1000','奥术大师大所','game','2017-09-26 22:11:57'),
	(64,'1000','是的da','game','2017-09-26 22:12:01'),
	(65,'1000','a','game','2017-09-26 22:13:39'),
	(66,'1000','asdas das dasd as da','game','2017-09-26 22:13:44'),
	(67,'1000','a','game','2017-09-26 22:14:02'),
	(68,'1000','asdas','game','2017-09-26 22:14:39');

/*!40000 ALTER TABLE `broadcast_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table order_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `order_id` varchar(32) NOT NULL DEFAULT '' COMMENT '关联 order_log表',
  `req_id` varchar(32) DEFAULT NULL COMMENT '???',
  `server_id` varchar(20) DEFAULT NULL COMMENT '取自 order_log表',
  `uid` varchar(32) DEFAULT NULL COMMENT '取自 order_log表',
  `item_id` int(10) DEFAULT NULL COMMENT '取自 order_log表,游戏提供, 关联 reward_info表',
  `item_type` int(10) DEFAULT NULL COMMENT '取自 order_log表 ,游戏提供 ，暂时不用',
  `item_count` int(10) DEFAULT NULL COMMENT '取自 order_log表 ,游戏提供， 该类商品的购买数量',
  `order_status` int(10) DEFAULT NULL COMMENT '订单状态  1失败， 0=成功    2=初始化，3=充值中',
  `order_exception` mediumtext COMMENT '异常原因',
  `is_reorder` int(10) DEFAULT NULL COMMENT '取自 order_log表',
  `indate` datetime DEFAULT NULL COMMENT '取自 order_log表',
  `id` bigint(20) DEFAULT NULL COMMENT '取自 order_log表',
  `phone` varchar(20) DEFAULT NULL COMMENT '取自 order_log表',
  `message` mediumtext COMMENT '发货过程中的消息,目前只是为了追踪',
  `callback_message` mediumtext COMMENT '会调给游戏方数据，包括卡密等',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `order_info` WRITE;
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;

INSERT INTO `order_info` (`order_id`, `req_id`, `server_id`, `uid`, `item_id`, `item_type`, `item_count`, `order_status`, `order_exception`, `is_reorder`, `indate`, `id`, `phone`, `message`, `callback_message`)
VALUES
	('1506490309678767',NULL,'1000','2',1010101,100,1,0,'',NULL,'2017-09-27 13:32:00',2,'13818254762','null,  game call back success',NULL);

/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table order_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `order_log`;

CREATE TABLE `order_log` (
  `order_id` varchar(32) NOT NULL COMMENT '程序自动生成唯一',
  `server_id` varchar(20) DEFAULT NULL COMMENT '游戏提供',
  `uid` varchar(32) DEFAULT NULL COMMENT '游戏提供',
  `item_id` int(10) DEFAULT NULL COMMENT '游戏提供, 关联 reward_info表的 reward_id',
  `item_type` int(10) DEFAULT NULL COMMENT '游戏提供 ，暂时不用',
  `item_count` int(10) DEFAULT NULL COMMENT '游戏提供， 该类商品的购买数量',
  `order_type` int(10) DEFAULT NULL COMMENT '订单状态 0=没消费， 1=消费',
  `is_reorder` int(10) DEFAULT NULL COMMENT '是否是重发的',
  `indate` datetime DEFAULT NULL COMMENT '写入时间',
  `id` bigint(20) NOT NULL COMMENT '游戏提供 ，游戏内的订单id',
  `phone` varchar(20) DEFAULT NULL COMMENT '游戏提供',
  `op_type` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `order_log` WRITE;
/*!40000 ALTER TABLE `order_log` DISABLE KEYS */;

INSERT INTO `order_log` (`order_id`, `server_id`, `uid`, `item_id`, `item_type`, `item_count`, `order_type`, `is_reorder`, `indate`, `id`, `phone`, `op_type`)
VALUES
	('1506010577091808','1000','2',10201,200,1,1,NULL,'2017-09-22 00:16:17',1,NULL,NULL),
	('1506133452634236','1000','2',10101,100,1,1,NULL,'2017-09-23 10:24:12',1,NULL,NULL),
	('1506133456653429','1000','2',10101,100,1,1,NULL,'2017-09-23 10:24:16',1,NULL,NULL),
	('1506133458933231','1000','2',10101,100,1,1,NULL,'2017-09-23 10:24:18',1,NULL,NULL),
	('1506143630917071','1000','2',10101,100,1,1,NULL,'2017-09-23 13:13:50',1,'13916994164',NULL),
	('1506145907733226','1000','2',10101,100,1,1,NULL,'2017-09-23 13:51:47',1,NULL,NULL),
	('1506150090395843','1000','2',10101,100,1,1,NULL,'2017-09-23 15:01:30',1,NULL,NULL),
	('1506267637361508','1000','2',10101,100,1,1,NULL,'2017-09-24 23:40:37',1,NULL,NULL),
	('1506317757055145','1000','5',10201,200,1,1,NULL,'2017-09-25 13:35:57',1,NULL,NULL),
	('1506337418686775','1000','2',10101,100,1,1,NULL,'2017-09-25 19:03:38',1,NULL,NULL),
	('1506347822719808','1000','8',10203,1,1,1,NULL,'2017-09-25 21:57:02',1111111,'18817393348',NULL),
	('1506347824317406','1000','8',10101,1,1,1,NULL,'2017-09-25 21:57:04',1,NULL,NULL),
	('1506347825070159','1000','8',10106,1,1,1,NULL,'2017-09-25 21:57:05',1,NULL,NULL),
	('1506347827687776','1000','8',10101,1,1,1,NULL,'2017-09-25 21:57:07',1,NULL,NULL),
	('1506347849733822','1000','8',10101,1,1,1,NULL,'2017-09-25 21:57:29',1,NULL,NULL),
	('1506347851692781','1000','8',10101,1,1,1,NULL,'2017-09-25 21:57:31',1,NULL,NULL),
	('1506348053727816','1000','8',10107,1,1,1,NULL,'2017-09-25 22:00:53',1,NULL,NULL),
	('1506348448425514','1000','8',10201,200,1,1,NULL,'2017-09-25 22:07:28',1,'18817393348',NULL),
	('1506490309678767','1000','2',1010101,100,1,1,NULL,'2017-09-27 13:31:49',2,'13818254762',NULL),
	('1507347823592681','1000','8',10102,100,1,1,NULL,'2017-09-25 21:57:03',111234,'18817393348',NULL),
	('4506337529128217','1000','2',10201,200,1,1,NULL,'2017-09-25 19:05:29',12345,'18817393348',NULL);

/*!40000 ALTER TABLE `order_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table pay_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pay_log`;

CREATE TABLE `pay_log` (
  `order_id` varchar(256) COLLATE utf8_bin NOT NULL DEFAULT '',
  `pay_log` mediumtext COLLATE utf8_bin,
  `state` int(11) DEFAULT NULL,
  `msg` mediumtext COLLATE utf8_bin,
  `create_time` timestamp NULL DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

LOCK TABLES `pay_log` WRITE;
/*!40000 ALTER TABLE `pay_log` DISABLE KEYS */;

INSERT INTO `pay_log` (`order_id`, `pay_log`, `state`, `msg`, `create_time`, `last_update_time`)
VALUES
	(X'3130303033313036355F34353830353035345F313530363438323834355F32323635',X'7B3130303033313036355F34353830353035345F313530363438323834355F32323635203435383035303534203130303020302E303120337C312031353036343832383435203764656238656134643765656162623461646138376435376336373738636363206134623036386638356232396639303865396666366364346437316233386338203320317D',9,'','2017-09-27 03:27:25','2017-09-27 03:27:26');

/*!40000 ALTER TABLE `pay_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table provider_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `provider_info`;

CREATE TABLE `provider_info` (
  `provider_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `desc` varchar(128) DEFAULT NULL COMMENT '描述',
  `last_edit_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_edit_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`provider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供货商表';

LOCK TABLES `provider_info` WRITE;
/*!40000 ALTER TABLE `provider_info` DISABLE KEYS */;

INSERT INTO `provider_info` (`provider_id`, `desc`, `last_edit_date`, `last_edit_user_id`)
VALUES
	(1,'欧飞','2017-09-26 05:36:14',NULL);

/*!40000 ALTER TABLE `provider_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table reward_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `reward_info`;

CREATE TABLE `reward_info` (
  `id` int(11) NOT NULL COMMENT '游戏提供的物品id 关联 item_id',
  `type` int(10) DEFAULT NULL COMMENT '游戏提供的物品type',
  `memo` varchar(20) DEFAULT NULL COMMENT '游戏提供的物品描述',
  `catalog` int(11) DEFAULT NULL COMMENT '商品类目   100=直冲电话卡， 101=卡密电话卡,  201=卡密油卡',
  `provider_id` int(11) DEFAULT NULL COMMENT '使用的供货商id',
  `price` int(11) DEFAULT NULL COMMENT '面额',
  `extra` varchar(32) DEFAULT NULL COMMENT '额外参数',
  `instock` int(11) DEFAULT NULL COMMENT '0=没有 1=有',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `reward_info` WRITE;
/*!40000 ALTER TABLE `reward_info` DISABLE KEYS */;

INSERT INTO `reward_info` (`id`, `type`, `memo`, `catalog`, `provider_id`, `price`, `extra`, `instock`)
VALUES
	(1010101,100,'5元话费直冲',100,1,5,'',1),
	(1010201,100,'10元话费直冲',100,1,10,NULL,1),
	(1010301,100,'30元话费直冲',100,1,30,NULL,1),
	(1010302,100,'30元话费卡密',101,1,30,'130103|130298|1399896',1),
	(1010401,100,'100元话费卡密',100,1,100,'130102|130204|1399899',1),
	(1010402,100,'100元话费直冲',101,1,100,NULL,1);

/*!40000 ALTER TABLE `reward_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table server_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `server_info`;

CREATE TABLE `server_info` (
  `server_id` varchar(20) NOT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `port` varchar(20) DEFAULT NULL,
  `gm_port` int(11) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `indate` datetime DEFAULT NULL,
  `server_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `server_info` WRITE;
/*!40000 ALTER TABLE `server_info` DISABLE KEYS */;

INSERT INTO `server_info` (`server_id`, `ip`, `port`, `gm_port`, `url`, `status`, `indate`, `server_name`)
VALUES
	('1000','117.50.8.212','10008',10009,'117.50.8.212:10009',1,'2017-09-27 13:54:39',NULL),
	('1011','117.50.8.212','10008',10009,'117.50.8.212:10009',1,'2017-09-23 10:23:50','test2');

/*!40000 ALTER TABLE `server_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sub_order_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sub_order_info`;

CREATE TABLE `sub_order_info` (
  `sub_order_id` varchar(64) NOT NULL DEFAULT '' COMMENT '子订单id',
  `order_id` varchar(32) DEFAULT NULL COMMENT '父order_id',
  `batch_number` int(11) DEFAULT NULL COMMENT '批次',
  `state` int(11) DEFAULT NULL COMMENT '订单状态  1失败， 0=成功    2=初始化，3=充值中',
  `message` mediumtext,
  `indate` datetime DEFAULT NULL,
  PRIMARY KEY (`sub_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='子订单表';

LOCK TABLES `sub_order_info` WRITE;
/*!40000 ALTER TABLE `sub_order_info` DISABLE KEYS */;

INSERT INTO `sub_order_info` (`sub_order_id`, `order_id`, `batch_number`, `state`, `message`, `indate`)
VALUES
	('1506490309678767_0','1506490309678767',0,0,'111','2017-09-27 13:55:08');

/*!40000 ALTER TABLE `sub_order_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table token_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `token_data`;

CREATE TABLE `token_data` (
  `user_id` varchar(11) NOT NULL,
  `token` varchar(128) NOT NULL DEFAULT '',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token_access` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `token_data` WRITE;
/*!40000 ALTER TABLE `token_data` DISABLE KEYS */;

INSERT INTO `token_data` (`user_id`, `token`)
VALUES
	('system','1d48cd06dab83b7'),
	('test','3172107ae5e8657a'),
	('game','70ce75f834fbf359');

/*!40000 ALTER TABLE `token_data` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_config`;

CREATE TABLE `user_config` (
  `user_id` varchar(11) NOT NULL DEFAULT '',
  `user_name` varchar(11) NOT NULL DEFAULT '',
  `password` varchar(128) NOT NULL DEFAULT '',
  `approve` int(11) NOT NULL DEFAULT '0' COMMENT '1=通过， 0=不通过',
  `is_administrator` int(11) NOT NULL DEFAULT '0' COMMENT '是否管理员',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user_config` WRITE;
/*!40000 ALTER TABLE `user_config` DISABLE KEYS */;

INSERT INTO `user_config` (`user_id`, `user_name`, `password`, `approve`, `is_administrator`)
VALUES
	('anonymous','anonymous','anonymous',0,0),
	('game','game','1234',1,0),
	('system','管理员','111111',1,1),
	('test','测试','1234',1,0),
	('zhucw','朱崇文','123456',1,0);

/*!40000 ALTER TABLE `user_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `uid` varchar(32) NOT NULL,
  `server_id` varchar(20) NOT NULL,
  `user_name` varchar(32) DEFAULT NULL,
  `address` tinytext,
  `phone_no` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`uid`,`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;

INSERT INTO `user_info` (`uid`, `server_id`, `user_name`, `address`, `phone_no`)
VALUES
	('10','1000','测试名字','城市地址之类','18817393348'),
	('1001','1000','test1','test2','18817393348'),
	('11','1000','测试名字','城市地址之类','18817393348'),
	('11','1015','测试名字','城市地址之类','18817393348'),
	('12','1015','测试名字','城市地址之类','18817393348'),
	('13','1015','测试名字','城市地址之类','18817393348'),
	('14','1015','测试名字','城市地址之类','18817393348'),
	('15','1015','测试名字','城市地址之类','18817393348'),
	('16','1015','测试名字','城市地址之类','18817393348'),
	('17','1015','测试名字','城市地址之类','18817393348'),
	('2','1000','测试名字','城市地址之类','18817393348'),
	('2','1001','测试名字','城市地址','18817393348'),
	('2','1002','测试名字','城市地址','18817393348'),
	('3','1000','test','aaa','11111111111'),
	('4','1000','测试名字','城市地址之类','18817393348'),
	('5','1000','测试名字','城市地址之类','18817393348'),
	('6','1000','测试名字','城市地址之类','18817393348'),
	('7','1000','测试名字','城市地址之类','18817393348'),
	('8','1000','测试名字','城市地址之类','18817393348'),
	('9','1000','测试名字','城市地址之类','18817393348');

/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_status
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_status`;

CREATE TABLE `user_status` (
  `uid` int(10) NOT NULL,
  `server_id` int(10) DEFAULT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `online_flag` int(11) DEFAULT NULL,
  `indate` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `server_id` (`server_id`),
  KEY `flag` (`online_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user_status` WRITE;
/*!40000 ALTER TABLE `user_status` DISABLE KEYS */;

INSERT INTO `user_status` (`uid`, `server_id`, `nick_name`, `online_flag`, `indate`)
VALUES
	(2,1000,'小倩',1,'2017-09-27 13:51:59'),
	(3,1000,'xyzsm500973359',0,'2017-09-27 11:34:47'),
	(4,1000,'xyzsm500973359',0,'2017-09-27 10:22:01'),
	(5,1000,'123',0,'2017-09-26 18:05:50'),
	(6,1000,'嘎嘎嘎嘎',0,'2017-09-25 17:20:49'),
	(7,1000,'123123',0,'2017-09-25 17:21:03'),
	(8,1000,'33333',0,'2017-09-26 11:29:43'),
	(9,1000,'xyzsm500973359',0,'2017-09-26 00:07:10'),
	(10,1000,'lcr12345678',0,'2017-09-25 20:24:31'),
	(11,1000,'小倩',1,'2017-09-26 15:50:53'),
	(12,1000,'123123',0,'2017-09-26 15:43:55'),
	(13,1000,'11111',0,'2017-09-26 16:34:16'),
	(14,1000,'11',1,'2017-09-26 18:37:33'),
	(15,1015,'22111',0,'2017-09-21 09:58:19'),
	(16,1015,'221111',0,'2017-09-21 10:31:00'),
	(17,1015,'73F79143A7D070C12637C53E6CCF8AE6',0,'2017-09-21 10:15:35'),
	(20,1000,'12312232',0,'2017-09-26 16:54:56'),
	(21,1000,'22233',0,'2017-09-26 17:21:19'),
	(22,1000,'13ss',0,'2017-09-26 17:43:13'),
	(23,1000,'333344',0,'2017-09-26 17:44:28'),
	(24,1000,'3333444',0,'2017-09-26 17:45:11'),
	(25,1000,'33334441',0,'2017-09-26 17:48:48'),
	(26,1000,'000',0,'2017-09-26 17:49:09'),
	(27,1000,'0000',0,'2017-09-26 17:50:36'),
	(28,1000,'231',0,'2017-09-26 17:52:09'),
	(29,1000,'12123',0,'2017-09-26 17:58:36'),
	(30,1000,'12312',0,'2017-09-26 17:58:49'),
	(31,1000,'12314',0,'2017-09-26 17:59:29'),
	(32,1000,'2312312',0,'2017-09-26 17:59:37'),
	(33,1000,'133',0,'2017-09-26 18:00:04'),
	(99,1000,'ssx1',0,'2017-09-20 19:25:01');

/*!40000 ALTER TABLE `user_status` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
