
ALTER TABLE `tb_species`
    MODIFY COLUMN `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '种属名称' AFTER `species_id`,
    MODIFY COLUMN `name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '种属名称EN' AFTER `name`,
    ADD COLUMN `organization_id` bigint NOT NULL COMMENT '机构ID' AFTER `name_en`,
    ADD UNIQUE INDEX `species_uk`(`species_id` ASC, `name` ASC, `name_en` ASC, `organization_id` ASC) USING BTREE COMMENT '种属唯一约束';

ALTER TABLE `tb_organ`
    ADD COLUMN `organization_id` bigint NOT NULL COMMENT '机构ID' AFTER `species_code`,
ADD UNIQUE INDEX `organ_uk`(`organ_id` ASC, `name` ASC, `species_code` ASC, `organization_id` ASC) USING BTREE COMMENT '脏器唯一约束';


ALTER TABLE `tb_product_series`
    MODIFY COLUMN `species_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '种属ID' AFTER `name_en`,
    ADD COLUMN `organization_id` bigint NULL COMMENT '机构ID' AFTER `species_id`,
    ADD UNIQUE INDEX `ps_uk`(`name` ASC, `name_en` ASC, `species_id` ASC, `organization_id` ASC) USING BTREE COMMENT '品系唯一约束';


ALTER TABLE `tb_structure`
    ADD COLUMN `organization_id` bigint NULL COMMENT '机构ID' AFTER `type`;


/*
* 修改脏器数据
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

UPDATE tb_organ
SET organization_id = 3
WHERE species_code in(
    SELECT species_id
    FROM tb_species
    WHERE organization_id = 3
);


/*
* 修改品系数据
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


UPDATE tb_product_series
SET organization_id = 3
WHERE species_id in(
    SELECT species_id
    FROM tb_species
    WHERE organization_id = 3
);



/*
* 修改结构数据
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


UPDATE tb_structure
SET organization_id = 3
WHERE species_id in(
    SELECT species_id
    FROM tb_species
    WHERE organization_id = 3
);




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
删除切片表tb_slide人工诊断状态
*/
ALTER TABLE `tb_slide`
DROP COLUMN `diagnosis`;




/**
删除专题表tb_topic删除状态
*/
ALTER TABLE `tb_topic`
DROP COLUMN `del_flag`;








SELECT organization_id,COUNT(*) AS SUMs
FROM tb_species
GROUP BY organization_id;