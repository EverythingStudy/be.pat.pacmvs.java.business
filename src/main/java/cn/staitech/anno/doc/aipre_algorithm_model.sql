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

 Date: 02/11/2023 16:03:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for aipre_algorithm_model
-- ----------------------------
DROP TABLE IF EXISTS `aipre_algorithm_model`;
CREATE TABLE `aipre_algorithm_model`  (
  `model_id` bigint NOT NULL COMMENT '算法模型id',
  `model_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '算法模型名称',
  `model_name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '算法模型名称en',
  `tissue_id` bigint NULL DEFAULT NULL COMMENT '病例组织id',
  PRIMARY KEY (`model_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '眼科-算法模型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aipre_algorithm_model
-- ----------------------------
INSERT INTO `aipre_algorithm_model` VALUES (1, '小鼠眼底炫彩mcFP影像拼接', '小鼠眼底炫彩mcFP影像拼接en', 1);
INSERT INTO `aipre_algorithm_model` VALUES (2, '食蟹猴七视野眼底彩照拼接', '食蟹猴七视野眼底彩照拼接en', 1);

SET FOREIGN_KEY_CHECKS = 1;
