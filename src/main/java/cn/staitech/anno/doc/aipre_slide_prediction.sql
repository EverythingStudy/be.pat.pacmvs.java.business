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

 Date: 06/11/2023 09:15:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for aipre_slide_prediction
-- ----------------------------
DROP TABLE IF EXISTS `aipre_slide_prediction`;
CREATE TABLE `aipre_slide_prediction`  (
  `slide_prediction_id` bigint NOT NULL AUTO_INCREMENT COMMENT '切片预测ID',
  `slide_id` bigint NULL DEFAULT NULL COMMENT '合成的切片ID',
  `image_id` bigint NULL DEFAULT NULL COMMENT '图像ID',
  `ai_analyzed` smallint NULL DEFAULT 0 COMMENT 'AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败）',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `organization_id` bigint NULL DEFAULT 0 COMMENT '机构ID',
  `main_image` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '2' COMMENT '是否是主图默认为2，1是，2否',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT 'sysdate()' COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT 'sysdate()' COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`slide_prediction_id`) USING BTREE,
  INDEX `asp_slide_id`(`slide_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '眼科切片预测表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
