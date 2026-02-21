-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `nickname` VARCHAR(50),
    `phone` VARCHAR(20),
    `email` VARCHAR(100),
    `avatar` VARCHAR(500),
    `status` INT DEFAULT 1,
    `role` VARCHAR(20) DEFAULT 'user',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 收货地址表
CREATE TABLE IF NOT EXISTS `address` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `name` VARCHAR(50) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `street` VARCHAR(200) NOT NULL,
    `address` VARCHAR(500) NOT NULL,
    `is_default` INT DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_user_id` (`user_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 演出表
CREATE TABLE IF NOT EXISTS `show_info` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `category` VARCHAR(50),
    `city` VARCHAR(50),
    `venue` VARCHAR(200),
    `cover_image` VARCHAR(500),
    `start_time` TIMESTAMP,
    `end_time` TIMESTAMP,
    `min_price` DECIMAL(10, 2),
    `max_price` DECIMAL(10, 2),
    `status` INT DEFAULT 1,
    `total_stock` INT DEFAULT 0,
    `available_stock` INT DEFAULT 0,
    `sale_start_time` TIMESTAMP,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_city` (`city`),
    INDEX `idx_category` (`category`),
    INDEX `idx_status` (`status`),
    INDEX `idx_sale_time` (`sale_start_time`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 演出场次表
CREATE TABLE IF NOT EXISTS `show_session` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `show_id` BIGINT NOT NULL,
    `session_time` TIMESTAMP NOT NULL,
    `status` INT DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_show_id` (`show_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 票档表
CREATE TABLE IF NOT EXISTS `ticket_tier` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `show_id` BIGINT NOT NULL,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(200),
    `price` DECIMAL(10, 2) NOT NULL,
    `total_stock` INT NOT NULL,
    `available_stock` INT NOT NULL,
    `version` INT DEFAULT 0,  -- 乐观锁版本号
    `status` INT DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_show_id` (`show_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_no` VARCHAR(50) NOT NULL UNIQUE,
    `user_id` BIGINT NOT NULL,
    `show_id` BIGINT NOT NULL,
    `session_id` BIGINT NOT NULL,
    `ticket_tier_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `unit_price` DECIMAL(10, 2) NOT NULL,
    `total_amount` DECIMAL(10, 2) NOT NULL,
    `status` VARCHAR(20) DEFAULT 'pending',
    `address_id` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_show_id` (`show_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;