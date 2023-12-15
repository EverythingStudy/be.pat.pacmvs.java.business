
/**
**第一部分：删除了开发环境大量废弃的表；测试、生产环境一定要慎重、仔细执行；若无足够的把握可不删除整个表，或删除前先备份！！！
**/



/**
删除废弃表-专题诊断明细
**/
DROP TABLE tb_special_diagnosis_detail;


/**
删除废弃表-专题诊断
**/
DROP TABLE tb_special_diagnosis;


/**
删除废弃表-报告记录
**/
DROP TABLE tb_report_record;

/**
删除废弃表- tb_structure_tmp
**/
DROP TABLE tb_structure_tmp;

/**
删除废弃表- tb_special_role_menu
**/
DROP TABLE tb_special_role_menu;

/**
删除废弃表- tb_special_role_user
**/
DROP TABLE tb_special_role_user;


/**
删除废弃表- 旧版专题相关
**/
DROP TABLE tb_special_annotation;
DROP TABLE tb_special_diagnosis;
DROP TABLE tb_special_image;
DROP TABLE tb_special_menu;
DROP TABLE tb_special_reclaim;
DROP TABLE tb_special_role;
DROP TABLE tb_special;

/**
删除废弃表-其它
**/
DROP TABLE tb_project_old;
DROP TABLE tb_files_tmp;
DROP TABLE tb_image_tmp;
DROP TABLE tb_question_bank_tmp;
DROP TABLE Sheet1;
DROP TABLE t_test;
DROP TABLE t_test2;
DROP TABLE tb_algorithm_assessment_tmp;
DROP TABLE tb_algorithm_json_tmp;
DROP TABLE product;
DROP TABLE gen_table;
DROP TABLE gen_table_column;




/**
**第二部分：此次发版相关表结构修改；测试、生产环境一定要慎重、仔细执行；执行前一定要先备份！！！
**/

/**
修改切片表tb_slide-删除人工诊断状态
*/
ALTER TABLE `tb_slide`
DROP COLUMN `diagnosis`;


/**
修改专题表tb_topic-删除删除状态
*/
ALTER TABLE `tb_topic`
DROP COLUMN `del_flag`;


/**
修改种属表tb_species-添加机构ID、唯一约束索引、注释
*/
ALTER TABLE `tb_species`
    MODIFY COLUMN `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '种属名称' AFTER `species_id`,
    MODIFY COLUMN `name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '种属名称EN' AFTER `name`,
    ADD COLUMN `organization_id` bigint NOT NULL COMMENT '机构ID' AFTER `name_en`,
    ADD UNIQUE INDEX `species_uk`(`species_id` ASC, `name` ASC, `name_en` ASC, `organization_id` ASC) USING BTREE COMMENT '种属唯一约束';

/**
修改脏器表tb_organ-添加机构ID、唯一约束索引、注释
*/
ALTER TABLE `tb_organ`
    ADD COLUMN `organization_id` bigint NOT NULL COMMENT '机构ID' AFTER `species_code`,
ADD UNIQUE INDEX `organ_uk`(`organ_id` ASC, `name` ASC, `species_code` ASC, `organization_id` ASC) USING BTREE COMMENT '脏器唯一约束';

/**
修改品系表tb_product_series-添加机构ID、唯一约束索引、注释
*/
ALTER TABLE `tb_product_series`
    MODIFY COLUMN `species_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '种属ID' AFTER `name_en`,
    ADD COLUMN `organization_id` bigint NULL COMMENT '机构ID' AFTER `species_id`,
    ADD UNIQUE INDEX `ps_uk`(`name` ASC, `name_en` ASC, `species_id` ASC, `organization_id` ASC) USING BTREE COMMENT '品系唯一约束';


/**
修改结构表tb_structure-添加机构ID、唯一约束索引、注释
*/
ALTER TABLE `tb_structure`
    ADD COLUMN `organization_id` bigint NULL COMMENT '机构ID' AFTER `type`;

/**
眼科AI-图像拼接-重置-添加3个字段
 */
ALTER TABLE `aipre_airepost`
    ADD COLUMN `init_level` int DEFAULT NULL COMMENT '初始层级' AFTER `level`,
    ADD COLUMN  `init_center_x` double DEFAULT NULL COMMENT '初始中心点x坐标' AFTER `modelName`,
    ADD COLUMN `init_center_y` double DEFAULT NULL COMMENT '初始中心点y坐标' AFTER `init_center_x`;


/*
* 评审表增加单审状态
*/
ALTER TABLE tb_review ADD COLUMN review_status char(1) DEFAULT '2' COMMENT '单审状态 默认1：未审 2：已审';


/**
**第三部分：此次发版脏器、品系、结构相关表机构ID数据更新；测试、生产环境一定要慎重、仔细执行；执行前一定要先备份！！！

注意步骤：
一、先手动tb_species分配测试、生产环境种每个种属对应的机构ID，一定不能错；
二、根据对应的机构ID执行如下语句，一定不能错；

以下SQL是根据需求，在开发环境执行的SQL，【测试、生产环境一定要依照自己实际业务的机构ID执行！！！】；
只处理如下3类机构中血研所、昭衍的数据

1、血研所（灵长类）2、昭衍(现在所有）3、其他机构（空就可以）

三、tb_pathological_indicator、tb_pathological_indicator_category表的机构ID数据，前期版本已经更新，本次不处理

**/

/*
* 更新脏器表机构ID数据
*/
UPDATE tb_organ
SET organization_id = 1
WHERE species_code in(
    SELECT species_id
    FROM tb_species
    WHERE organization_id = 1
);

UPDATE tb_organ
SET organization_id = 2
WHERE species_code in(
    SELECT species_id
    FROM tb_species
    WHERE organization_id = 2
);



/*
* 更新品系表机构ID数据
*/
UPDATE tb_product_series
SET organization_id = 1
WHERE species_id in(
    SELECT species_id
    FROM tb_species
    WHERE organization_id = 1
);

UPDATE tb_product_series
SET organization_id = 2
WHERE species_id in(
    SELECT species_id
    FROM tb_species
    WHERE organization_id = 2
);



/*
* 更新结构表机构ID数据
*/
UPDATE tb_structure
SET organization_id = 1
WHERE species_id in(
    SELECT species_id
    FROM tb_species
    WHERE organization_id = 1
);

UPDATE tb_structure
SET organization_id = 2
WHERE species_id in(
    SELECT species_id
    FROM tb_species
    WHERE organization_id = 2
);

