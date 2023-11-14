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

 Date: 02/11/2023 16:03:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for aipre_pathological_tissue
-- ----------------------------
DROP TABLE IF EXISTS `aipre_pathological_tissue`;
CREATE TABLE `aipre_pathological_tissue`  (
  `tissue_id` bigint NOT NULL COMMENT '病理组织id',
  `tissue_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '病理组织名称',
  `tissue_name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '病理组织英文名称',
  `project_type_id` bigint NULL DEFAULT NULL COMMENT '项目类型id',
  PRIMARY KEY (`tissue_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '眼科-病理组织表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aipre_pathological_tissue
-- ----------------------------
INSERT INTO `aipre_pathological_tissue` VALUES (1, '眼', '眼en', 6);

SET FOREIGN_KEY_CHECKS = 1;
