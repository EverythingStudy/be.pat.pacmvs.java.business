-- 创建用户设置表，包含（图像增强开关；阅片模式：1-列表模式、2-矩阵模式；默认截图尺寸:宽、高；滚轮灵敏度；拖拽灵敏度）

CREATE TABLE tb_user_settings (
   user_settings_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
   user_id BIGINT NOT NULL COMMENT '用户ID',
   enhance_switch TINYINT NOT NULL DEFAULT 0 COMMENT '图像增强开关: 0-关闭, 1-开启',
   reading_mode TINYINT NOT NULL DEFAULT 1 COMMENT '阅片模式: 1-列表模式, 2-矩阵模式',
   default_screenshot_width INT NOT NULL DEFAULT 200 COMMENT '默认截图尺寸-宽',
   default_screenshot_height INT NOT NULL DEFAULT 200 COMMENT '默认截图尺寸-高',
   scroll_sensitivity INT NOT NULL DEFAULT 50 COMMENT '滚轮灵敏度',
   drag_sensitivity INT NOT NULL DEFAULT 50 COMMENT '拖拽灵敏度',
   UNIQUE INDEX idx_user_id (user_id)
) COMMENT = '用户设置表';