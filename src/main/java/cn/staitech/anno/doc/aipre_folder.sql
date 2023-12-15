/*
 Navicat Premium Data Transfer

 Source Server         : 眼科
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 172.31.2.101:3306
 Source Schema         : patsaas_eye

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 06/11/2023 16:25:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for aipre_folder
-- ----------------------------
DROP TABLE IF EXISTS `aipre_folder`;
CREATE TABLE `aipre_folder`  (
  `folder_id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件夹id',
  `folder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件夹名称',
  `folder_size` bigint NULL DEFAULT NULL COMMENT '文件大小',
  `folder_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件夹url地址',
  `files_id` bigint NULL DEFAULT NULL COMMENT '压缩包id',
  `organization_id` bigint NULL DEFAULT NULL COMMENT '机构ID',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `delete_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '逻辑删除（0删除，1未删除）',
  PRIMARY KEY (`folder_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '眼科-文件夹表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
