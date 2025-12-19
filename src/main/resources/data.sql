-- 插入测试用户（密码都是123456）
INSERT INTO users (username, password, nickname, phone, email, role) VALUES
    ('testuser', '123456', '测试用户', '13800138000', 'test@example.com', 'user'),
    ('admin', '123456', '管理员', '13900139000', 'admin@example.com', 'admin');
UPDATE USERS SET email = 'test@example.com' WHERE username = 'testuser';
UPDATE USERS SET email = 'admin@example.com' WHERE username = 'admin';

-- 插入测试地址
INSERT INTO address (user_id, name, phone, address, is_default) VALUES
    (1, '张三', '13800138000', '北京市海淀区中关村大街1号', 1),
    (1, '李四', '13800138001', '上海市浦东新区陆家嘴', 0);

-- 插入测试演出
INSERT INTO show_info (title, description, category, city, cover_image) VALUES
    ('周杰伦演唱会2024', '周杰伦世界巡回演唱会北京站', 'concert', '北京', 'https://example.com/cover1.jpg'),
    ('话剧《雷雨》', '经典话剧《雷雨》北京保利剧院', 'drama', '北京', 'https://example.com/cover2.jpg');

-- 插入演出测试数据
INSERT INTO show_info (title, description, category, city, venue, cover_image, start_time, end_time, min_price, max_price, total_stock, available_stock, sale_start_time) VALUES
    ('周杰伦「嘉年华」世界巡回演唱会-北京站', '华语天王周杰伦全新巡演，豪华舞台、经典曲目全回顾。', 'concert', '北京', '国家体育场（鸟巢）', 'https://example.com/jay.jpg', '2024-06-20 19:30:00', '2024-06-20 22:30:00', 580.00, 2580.00, 10000, 10000, '2024-05-01 10:00:00'),
    ('话剧《茶馆》- 北京人民艺术剧院', '老舍经典话剧，中国话剧史上的瑰宝。', 'drama', '北京', '首都剧场', 'https://example.com/teahouse.jpg', '2024-07-15 19:00:00', '2024-07-15 21:30:00', 180.00, 880.00, 500, 500, '2024-06-01 10:00:00'),
    ('Taylor Swift | The Eras Tour - 上海站', '全球现象级巡演，史上最畅销女歌手震撼登场。', 'concert', '上海', '上海体育场', 'https://example.com/taylor.jpg', '2024-08-10 20:00:00', '2024-08-10 23:00:00', 880.00, 3880.00, 8000, 8000, '2024-07-01 10:00:00'),
    ('沉浸式艺术展「梵高再现」- 北京站', '全球巡回沉浸式光影大展，走进梵高的艺术世界。', 'exhibition', '北京', '北京展览馆', 'https://example.com/vangogh.jpg', '2024-05-01 09:00:00', '2024-08-31 21:00:00', 98.00, 198.00, 5000, 5000, '2024-04-01 10:00:00'),
    ('迪士尼经典音乐会 - 上海交响乐团', '迪士尼百年经典配乐，交响乐现场演奏。', 'music', '上海', '上海交响乐团音乐厅', 'https://example.com/disney.jpg', '2024-09-05 19:30:00', '2024-09-05 21:30:00', 180.00, 680.00, 1200, 1200, '2024-08-01 10:00:00'),
    ('开心麻花爆笑舞台剧《乌龙山伯爵》', '开心麻花经典爆笑话剧，票房神话。', 'drama', '深圳', '深圳保利剧院', 'https://example.com/wulongshan.jpg', '2024-07-20 19:30:00', '2024-07-20 22:00:00', 180.00, 880.00, 800, 800, '2024-06-15 10:00:00'),
    ('「五月天」2024 Just Rock It 演唱会-广州站',  '华人第一天团五月天，带你重回青春摇滚夜。', 'concert', '广州', '广州天河体育场', 'https://example.com/mayday.jpg', '2024-10-01 19:00:00', '2024-10-01 22:30:00', 380.00, 1580.00, 15000, 15000, '2024-09-01 10:00:00');

-- 为第一个演出（周杰伦演唱会）添加场次
INSERT INTO show_session (show_id, session_time) VALUES
    (1, '2024-06-20 19:30:00'),
    (1, '2024-06-21 19:30:00');

-- 为第一个演出添加票档
INSERT INTO ticket_tier (show_id, name, description, price, total_stock, available_stock) VALUES
    (1, 'VIP票', '前区最佳观赏位置', 2580.00, 1000, 1000),
    (1, '普通票', '看台区域', 580.00, 9000, 9000),
    (1, '学生票', '凭学生证购买', 380.00, 2000, 2000);

-- 为用户1添加3个测试订单
INSERT INTO `order` (order_no, user_id, show_id, session_id, ticket_tier_id,
                     quantity, unit_price, total_amount, status, created_at) VALUES
   ('ORDER202412130001', 1, 1, 1, 1, 2, 2580.00, 5160.00, 'pending', '2024-12-13 09:00:00'),
   ('ORDER202412130002', 1, 1, 1, 2, 1, 580.00, 580.00, 'paid', '2024-12-13 10:00:00'),
   ('ORDER202412130003', 1, 2, 3, 1, 1, 180.00, 180.00, 'cancelled', '2024-12-13 11:00:00'),
   ('ORDER202412130004', 1, 1, 1, 1, 1, 2580.00, 2580.00, 'pending', '2024-12-13 12:00:00'),
   ('ORDER202412130005', 1, 3, 5, 2, 3, 880.00, 2640.00, 'paid', '2024-12-13 13:00:00');