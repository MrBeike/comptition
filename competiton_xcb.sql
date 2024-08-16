/*
MySQL Data Transfer
Source Host: localhost
Source Database: competiton_xcb
Target Host: localhost
Target Database: competiton_xcb
Date: 2023/2/17 11:04:06
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `que_id` int(8) NOT NULL auto_increment,
  `que_num` int(8) default NULL,
  `que_title` varchar(500) collate utf8_bin default NULL,
  `que_answer` varchar(5000) collate utf8_bin default NULL,
  `flag` int(2) default NULL,
  `status` int(2) default NULL,
  `backtime` int(4) default NULL,
  `fenzhi` int(2) default NULL,
  PRIMARY KEY  (`que_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records 
-- ----------------------------
