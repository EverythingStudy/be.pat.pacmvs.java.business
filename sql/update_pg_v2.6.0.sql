-- todo 轮廓数据迁移


drop table if exists fr_annotation;
create table fr_annotation
(
    annotation_id BIGSERIAL PRIMARY KEY,
    area            decimal(16, 4),
    perimeter       decimal(16, 4),
    description     varchar(100),
    tag_id          bigint       default 0,
    contour         geometry,
    location_type   varchar(100),
    annotation_type varchar(50)  default 'Draw'::character varying,
    create_by       bigint,
    create_time     timestamp(0) default CURRENT_TIMESTAMP,
    update_by       bigint,
    update_time     timestamp(0) default CURRENT_TIMESTAMP,
    slide_id        bigint,
    json_id         varchar(500)
);
-- annotation_type创建索引
CREATE INDEX fr_annotation_annotation_type_index ON fr_annotation (annotation_type);
-- slide_id创建索引
CREATE INDEX fr_annotation_slide_id_index ON fr_annotation (slide_id);

comment on column fr_annotation.annotation_id is '主键id';

comment on column fr_annotation.area is '面积';

comment on column fr_annotation.perimeter is '周长';

comment on column fr_annotation.description is '轮廓描述';

comment on column fr_annotation.tag_id is '标签id';

comment on column fr_annotation.contour is '轮廓坐标625';

comment on column fr_annotation.location_type is '轮廓类型';

comment on column fr_annotation.annotation_type is '标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注)';

comment on column fr_annotation.create_by is '创建者';

comment on column fr_annotation.create_time is '创建时间';

comment on column fr_annotation.update_by is '更新者';

comment on column fr_annotation.update_time is '更新时间';

comment on column fr_annotation.slide_id is '切片id';

comment on column fr_annotation.json_id is 'geojson中数据id';

drop table if exists fr_annotation_del;
create table fr_annotation_del
(
    annotation_id BIGSERIAL PRIMARY KEY,
    area            decimal(16, 4),
    perimeter       decimal(16, 4),
    description     varchar(100),
    tag_id          bigint       default 0,
    contour         geometry,
    location_type   varchar(100),
    annotation_type varchar(50)  default 'Draw'::character varying,
    create_by       bigint,
    create_time     timestamp(0) default CURRENT_TIMESTAMP,
    delete_by       bigint,
    delete_time     timestamp(0) default CURRENT_TIMESTAMP,
    update_by       bigint,
    update_time     timestamp(0) default CURRENT_TIMESTAMP,
    slide_id        bigint,
    json_id         varchar(500)
);

comment on column fr_annotation_del.annotation_id is '主键id';

comment on column fr_annotation_del.area is '面积';

comment on column fr_annotation_del.perimeter is '周长';

comment on column fr_annotation_del.description is '轮廓描述';

comment on column fr_annotation_del.tag_id is '标签id';

comment on column fr_annotation_del.contour is '轮廓坐标625';

comment on column fr_annotation_del.location_type is '轮廓类型';

comment on column fr_annotation_del.annotation_type is '标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注)';

comment on column fr_annotation_del.create_by is '创建者';

comment on column fr_annotation_del.create_time is '创建时间';

comment on column fr_annotation_del.update_by is '更新者';

comment on column fr_annotation_del.update_time is '更新时间';

comment on column fr_annotation_del.delete_by is '删除者';

comment on column fr_annotation_del.delete_time is '删除时间';

comment on column fr_annotation_del.slide_id is '切片id';

comment on column fr_annotation_del.json_id is 'geojson中数据id';

drop table if exists fr_measure;
CREATE TABLE fr_measure
(
    measure_id BIGSERIAL PRIMARY KEY,
    slide_id          BIGINT NOT NULL,
    annotation_type   VARCHAR(255),
    area              VARCHAR(255),
    perimeter         VARCHAR(255),
    number            BIGINT,
    measure_type      INT,
    measure_relation  VARCHAR(255),
    measure_name      VARCHAR(255),
    measure_number    INT,
    mean_distance     DOUBLE PRECISION,
    max_distance      DOUBLE PRECISION,
    min_distance      DOUBLE PRECISION,
    inner_angle       VARCHAR(255),
    exterior_angle    VARCHAR(255),
    center_point      VARCHAR(255),
    location_type     VARCHAR(255),
    radius            VARCHAR(255),
    contour           geometry,
    create_by         bigint,
    create_time       timestamp(0) default CURRENT_TIMESTAMP,
    update_by         bigint,
    update_time       timestamp(0) default CURRENT_TIMESTAMP,
    measure_full_name VARCHAR(255)
);
COMMENT ON TABLE fr_measure IS '测量标注表';

CREATE INDEX fr_measure_slide_id_index ON fr_measure (slide_id);
CREATE INDEX fr_measure_location_type_index ON fr_measure (location_type);
CREATE INDEX fr_measure_measure_full_name_index ON fr_measure (measure_full_name);


COMMENT ON COLUMN fr_measure.measure_id IS '主键id';
COMMENT ON COLUMN fr_measure.slide_id IS '切片id';
COMMENT ON COLUMN fr_measure.annotation_type IS '标注类型(AI表示AI算出的标注，Draw表示前端绘制的标注，Measure表示测量工具数据)';
COMMENT ON COLUMN fr_measure.area IS '面积';
COMMENT ON COLUMN fr_measure.perimeter IS '周长';
COMMENT ON COLUMN fr_measure.number IS '标注名称';
COMMENT ON COLUMN fr_measure.measure_type IS '测量轮廓类型(0:正常,表示有关系,默认为0)';
COMMENT ON COLUMN fr_measure.measure_relation IS '测量关系';
COMMENT ON COLUMN fr_measure.measure_name IS '测量轮廓表示名称:L';
COMMENT ON COLUMN fr_measure.measure_number IS '测量轮廓标识：1';
COMMENT ON COLUMN fr_measure.mean_distance IS '平均间距';
COMMENT ON COLUMN fr_measure.max_distance IS '最大间距';
COMMENT ON COLUMN fr_measure.min_distance IS '最小间距';
COMMENT ON COLUMN fr_measure.inner_angle IS '内角';
COMMENT ON COLUMN fr_measure.exterior_angle IS '外角';
COMMENT ON COLUMN fr_measure.center_point IS '中心';
COMMENT ON COLUMN fr_measure.location_type IS '标注数据类型(LineString,Polygon,point,pc,p,L)';
COMMENT ON COLUMN fr_measure.radius IS '周长（圆）';
COMMENT ON COLUMN fr_measure.contour IS '标注数据（JSON格式）';
COMMENT ON COLUMN fr_measure.create_by IS '创建者';
COMMENT ON COLUMN fr_measure.create_time IS '创建时间';
COMMENT ON COLUMN fr_measure.update_by IS '更新者';
COMMENT ON COLUMN fr_measure.update_time IS '更新时间';
COMMENT ON COLUMN fr_measure.measure_full_name IS '标注名称';










