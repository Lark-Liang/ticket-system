CREATE TABLE IF NOT EXISTS USERS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    nickname VARCHAR(50),
    phone VARCHAR(20),
    avatar VARCHAR(500),
    status INT DEFAULT 1,
    role VARCHAR(20) DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- 插入测试用户（密码都是123456）
INSERT INTO users (username, password, nickname, phone, email,avatar, bio, gender, birthday, background_image, role,status) VALUES
    ('testuser', '123456', '麦子b2', '13800138000', 'test@example.com',
    ' ',
    '',
    2,
    '2026-01-01',
    ' ',
    'user', 1),
    ('admin', '123456', '管理员', '13900139000', 'admin@example.com',
    null, null, 0, null, null,
    'admin', 1);

-- 插入测试地址
-- 注意：这里使用固定的用户ID 1（假设testuser的ID是1）
-- 如果testuser的ID不是1，需要先查询
INSERT INTO address (user_id, name, phone, street, address, is_default) VALUES
    (1, '张三', '13800138000', '中关村大街1号', '海龙大厦12层1205室', 1),
    (1, '李四', '13800138001', '陆家嘴环路1000号', '恒生银行大厦35层', 0);

-- 插入测试演出
INSERT INTO show_info (title, description, category, city, venue, cover_image,start_time, end_time, min_price, max_price, total_stock, available_stock, sale_start_time, status) VALUES
    ('周杰伦「嘉年华」世界巡回演唱会-北京站', '华语天王周杰伦全新巡演，豪华舞台、经典曲目全回顾。', 'concert', '北京', '国家体育场（鸟巢）', '/images/jay.jpg', '2024-06-20 19:30:00', '2024-06-20 22:30:00', 580.00, 2580.00, 10000, 10000, '2024-05-01 10:00:00', 1),
    ('话剧《茶馆》- 北京人民艺术剧院', '老舍经典话剧，中国话剧史上的瑰宝。', 'drama', '北京', '首都剧场', '/images/teahouse.jpg', '2024-07-15 19:00:00', '2024-07-15 21:30:00', 180.00, 880.00, 500, 500, '2024-06-01 10:00:00', 1),
    ('Taylor Swift | The Eras Tour - 上海站', '全球现象级巡演，史上最畅销女歌手震撼登场。', 'concert', '上海', '上海体育场', '/images/taylor.jpg', '2024-08-10 20:00:00', '2024-08-10 23:00:00', 880.00, 3880.00, 8000, 8000, '2024-07-01 10:00:00', 1);

-- 为第一个演出（周杰伦演唱会）添加场次
INSERT IGNORE INTO show_session (show_id, session_time, status)
SELECT id, '2024-06-20 19:30:00', 1 FROM show_info WHERE title = '周杰伦「嘉年华」世界巡回演唱会-北京站';

INSERT IGNORE INTO show_session (show_id, session_time, status)
SELECT id, '2024-06-21 19:30:00', 1 FROM show_info WHERE title = '周杰伦「嘉年华」世界巡回演唱会-北京站';

-- 为第一个演出添加票档
INSERT INTO ticket_tier (show_id, name, description, price, total_stock, available_stock, status)
SELECT id, 'VIP票', '前区最佳观赏位置', 2580.00, 1000, 1000, 1 FROM show_info WHERE title = '周杰伦「嘉年华」世界巡回演唱会-北京站';

INSERT IGNORE INTO ticket_tier (show_id, name, description, price, total_stock, available_stock, status)
SELECT id, '普通票', '看台区域', 580.00, 9000, 9000, 1 FROM show_info WHERE title = '周杰伦「嘉年华」世界巡回演唱会-北京站';

INSERT IGNORE INTO ticket_tier (show_id, name, description, price, total_stock, available_stock, status)
SELECT id, '学生票', '凭学生证购买', 380.00, 2000, 2000, 1 FROM show_info WHERE title = '周杰伦「嘉年华」世界巡回演唱会-北京站';

-- 为用户1添加测试订单
INSERT INTO `order` (order_no, user_id, show_id, session_id, ticket_tier_id,
                     quantity, unit_price, total_amount, status, created_at) VALUES
   ('ORDER202412130001', 1, 1, 1, 1, 2, 2580.00, 5160.00, 'pending', '2024-12-13 09:00:00'),
   ('ORDER202412130002', 1, 1, 1, 2, 1, 580.00, 580.00, 'paid', '2024-12-13 10:00:00');

-- 打印成功信息
SELECT '数据库数据插入完成!' AS message;