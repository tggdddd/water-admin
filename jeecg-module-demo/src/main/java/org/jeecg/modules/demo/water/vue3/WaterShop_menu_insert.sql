-- 注意：该页面对应的前台目录为views/water文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值


INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type,
                           sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description,
                           status, del_flag, rule_flag, create_by, create_time, update_by, update_time,
                           internal_or_external)
VALUES ('2023062611465000330', NULL, '商品', '/water/waterShopList', 'water/WaterShopList', NULL, NULL, 0, NULL, '1',
        0.00, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-06-26 23:46:33', NULL, NULL, 0);

-- 权限控制sql
-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms,
                           perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description,
                           create_by, create_time, update_by, update_time, del_flag, rule_flag, status,
                           internal_or_external)
VALUES ('2023062611465010331', '2023062611465000330', '添加商品', NULL, NULL, 0, NULL, NULL, 2, 'water:water_shop:add',
        '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-06-26 23:46:33', NULL, NULL, 0, 0, '1', 0);
-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms,
                           perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description,
                           create_by, create_time, update_by, update_time, del_flag, rule_flag, status,
                           internal_or_external)
VALUES ('2023062611465010332', '2023062611465000330', '编辑商品', NULL, NULL, 0, NULL, NULL, 2, 'water:water_shop:edit',
        '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-06-26 23:46:33', NULL, NULL, 0, 0, '1', 0);
-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms,
                           perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description,
                           create_by, create_time, update_by, update_time, del_flag, rule_flag, status,
                           internal_or_external)
VALUES ('2023062611465010333', '2023062611465000330', '删除商品', NULL, NULL, 0, NULL, NULL, 2,
        'water:water_shop:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-06-26 23:46:33', NULL, NULL, 0,
        0, '1', 0);
-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms,
                           perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description,
                           create_by, create_time, update_by, update_time, del_flag, rule_flag, status,
                           internal_or_external)
VALUES ('2023062611465010334', '2023062611465000330', '批量删除商品', NULL, NULL, 0, NULL, NULL, 2,
        'water:water_shop:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-06-26 23:46:33', NULL,
        NULL, 0, 0, '1', 0);
-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms,
                           perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description,
                           create_by, create_time, update_by, update_time, del_flag, rule_flag, status,
                           internal_or_external)
VALUES ('2023062611465010335', '2023062611465000330', '导出excel_商品', NULL, NULL, 0, NULL, NULL, 2,
        'water:water_shop:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-06-26 23:46:33', NULL, NULL,
        0, 0, '1', 0);
-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms,
                           perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description,
                           create_by, create_time, update_by, update_time, del_flag, rule_flag, status,
                           internal_or_external)
VALUES ('2023062611465010336', '2023062611465000330', '导入excel_商品', NULL, NULL, 0, NULL, NULL, 2,
        'water:water_shop:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2023-06-26 23:46:33', NULL,
        NULL, 0, 0, '1', 0);