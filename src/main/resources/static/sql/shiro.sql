/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1 本地
Source Server Version : 50723
Source Host           : 127.0.0.1:3306
Source Database       : shiro

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2020-05-12 14:17:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for back_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `back_sys_user`;
CREATE TABLE `back_sys_user` (
  `id` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of back_sys_user
-- ----------------------------

-- ----------------------------
-- Table structure for relations_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `relations_role_permission`;
CREATE TABLE `relations_role_permission` (
  `tsp_id` int(11) NOT NULL,
  `tsr_id` int(11) NOT NULL,
  KEY `FK9c0hs0hv6h8pq2uqm35966cfh` (`tsp_id`),
  KEY `FKi80wvnonvt9l2v45rdu56hodc` (`tsr_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of relations_role_permission
-- ----------------------------
INSERT INTO `relations_role_permission` VALUES ('1', '1');
INSERT INTO `relations_role_permission` VALUES ('1', '2');
INSERT INTO `relations_role_permission` VALUES ('2', '2');
INSERT INTO `relations_role_permission` VALUES ('2', '3');
INSERT INTO `relations_role_permission` VALUES ('1', '3');
INSERT INTO `relations_role_permission` VALUES ('3', '2');
INSERT INTO `relations_role_permission` VALUES ('3', '3');
INSERT INTO `relations_role_permission` VALUES ('4', '3');

-- ----------------------------
-- Table structure for relations_user_role
-- ----------------------------
DROP TABLE IF EXISTS `relations_user_role`;
CREATE TABLE `relations_user_role` (
  `tsu_id` bigint(20) NOT NULL,
  `tsr_id` int(11) NOT NULL,
  KEY `FKtgn46gxydk1r0fqcc0prb0td1` (`tsr_id`),
  KEY `FKb1f3eogl70f7wxh7de173bmnf` (`tsu_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of relations_user_role
-- ----------------------------
INSERT INTO `relations_user_role` VALUES ('1', '1');
INSERT INTO `relations_user_role` VALUES ('4', '3');
INSERT INTO `relations_user_role` VALUES ('4', '3');
INSERT INTO `relations_user_role` VALUES ('5', '2');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `tsp_id` int(11) NOT NULL AUTO_INCREMENT,
  `pname` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tsp_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', 'Retrieve', '');
INSERT INTO `sys_permission` VALUES ('2', 'Create', '');
INSERT INTO `sys_permission` VALUES ('3', 'Update', '');
INSERT INTO `sys_permission` VALUES ('4', 'Delete', '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `tsr_id` int(11) NOT NULL AUTO_INCREMENT,
  `rname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tsr_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'guest');
INSERT INTO `sys_role` VALUES ('2', 'user');
INSERT INTO `sys_role` VALUES ('3', 'admin');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `tsu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tsu_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '123456', null, 'zhangsan');
INSERT INTO `sys_user` VALUES ('5', 'a8fa7af88eb0d05603963361ae41aa51', 'e0c8c8a7d4c63627a585ae93a0a2a18c', 'admin');
