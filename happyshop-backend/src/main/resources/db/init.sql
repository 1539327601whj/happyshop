-- HappyShop 数据库初始化脚本
CREATE DATABASE IF NOT EXISTS happyshop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE happyshop;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `nickname` VARCHAR(50),
    `avatar` VARCHAR(255),
    `phone` VARCHAR(20),
    `email` VARCHAR(100),
    `role` VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER' COMMENT 'CUSTOMER/MERCHANT/ADMIN',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-禁用 1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商家表
CREATE TABLE IF NOT EXISTS `merchant` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `shop_name` VARCHAR(100) NOT NULL,
    `shop_logo` VARCHAR(255),
    `shop_desc` TEXT,
    `contact_phone` VARCHAR(20),
    `contact_name` VARCHAR(50),
    `business_license` VARCHAR(255),
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-待审核 1-审核通过 2-审核拒绝',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `parent_id` BIGINT NOT NULL DEFAULT 0,
    `icon` VARCHAR(255),
    `sort` INT NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品表
CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `merchant_id` BIGINT NOT NULL,
    `category_id` BIGINT NOT NULL,
    `name` VARCHAR(200) NOT NULL,
    `subtitle` VARCHAR(500),
    `main_image` VARCHAR(255),
    `price` DECIMAL(10,2) NOT NULL,
    `market_price` DECIMAL(10,2),
    `stock` INT NOT NULL DEFAULT 0,
    `sales` INT NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-下架 1-上架 2-待审核',
    `description` TEXT,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    KEY `idx_merchant_id` (`merchant_id`),
    KEY `idx_category_id` (`category_id`),
    FULLTEXT KEY `ft_name` (`name`) WITH PARSER ngram
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品图片表
CREATE TABLE IF NOT EXISTS `product_image` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_id` BIGINT NOT NULL,
    `image_url` VARCHAR(255) NOT NULL,
    `sort` INT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品规格表
CREATE TABLE IF NOT EXISTS `product_sku` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_id` BIGINT NOT NULL,
    `spec_name` VARCHAR(100) NOT NULL COMMENT '规格名称，如：颜色/尺码',
    `spec_value` VARCHAR(100) NOT NULL COMMENT '规格值，如：红色/XL',
    `price` DECIMAL(10,2) NOT NULL,
    `stock` INT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 收货地址表
CREATE TABLE IF NOT EXISTS `address` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `receiver_name` VARCHAR(50) NOT NULL,
    `receiver_phone` VARCHAR(20) NOT NULL,
    `province` VARCHAR(50) NOT NULL,
    `city` VARCHAR(50) NOT NULL,
    `district` VARCHAR(50) NOT NULL,
    `detail` VARCHAR(255) NOT NULL,
    `is_default` TINYINT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 购物车表
CREATE TABLE IF NOT EXISTS `cart` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `sku_id` BIGINT,
    `quantity` INT NOT NULL DEFAULT 1,
    `checked` TINYINT NOT NULL DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY `idx_user_id` (`user_id`),
    UNIQUE KEY `uk_user_product_sku` (`user_id`, `product_id`, `sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_no` VARCHAR(50) NOT NULL UNIQUE,
    `user_id` BIGINT NOT NULL,
    `merchant_id` BIGINT NOT NULL,
    `total_amount` DECIMAL(10,2) NOT NULL,
    `pay_amount` DECIMAL(10,2) NOT NULL,
    `freight_amount` DECIMAL(10,2) NOT NULL DEFAULT 0,
    `discount_amount` DECIMAL(10,2) NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-待付款 1-待发货 2-待收货 3-已完成 4-已取消 5-退款中',
    `receiver_name` VARCHAR(50) NOT NULL,
    `receiver_phone` VARCHAR(20) NOT NULL,
    `receiver_address` VARCHAR(500) NOT NULL,
    `remark` VARCHAR(500),
    `pay_time` DATETIME,
    `ship_time` DATETIME,
    `receive_time` DATETIME,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    KEY `idx_user_id` (`user_id`),
    KEY `idx_merchant_id` (`merchant_id`),
    KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单明细表
CREATE TABLE IF NOT EXISTS `order_item` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `product_name` VARCHAR(200) NOT NULL,
    `product_image` VARCHAR(255),
    `sku_id` BIGINT,
    `sku_spec` VARCHAR(200),
    `price` DECIMAL(10,2) NOT NULL,
    `quantity` INT NOT NULL,
    `total_amount` DECIMAL(10,2) NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 评价表
CREATE TABLE IF NOT EXISTS `review` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `order_id` BIGINT NOT NULL,
    `rating` TINYINT NOT NULL COMMENT '1-5星',
    `content` TEXT,
    `images` VARCHAR(1000) COMMENT 'JSON数组',
    `is_anonymous` TINYINT NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-隐藏 1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY `idx_product_id` (`product_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 促销活动表
CREATE TABLE IF NOT EXISTS `promotion` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(20) NOT NULL COMMENT 'FULL_REDUCE/DISCOUNT/COUPON',
    `description` TEXT,
    `discount_value` DECIMAL(10,2) NOT NULL COMMENT '满减金额/折扣比例',
    `min_amount` DECIMAL(10,2) COMMENT '最低消费金额',
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 优惠券表
CREATE TABLE IF NOT EXISTS `coupon` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `promotion_id` BIGINT,
    `user_id` BIGINT NOT NULL,
    `code` VARCHAR(50) NOT NULL UNIQUE,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(20) NOT NULL COMMENT 'FIXED/PERCENT',
    `value` DECIMAL(10,2) NOT NULL COMMENT '面额/折扣比例',
    `min_amount` DECIMAL(10,2) NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-未使用 1-已使用 2-已过期',
    `used_time` DATETIME,
    `expire_time` DATETIME NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始管理员账号（密码: admin123, BCrypt加密）
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`)
VALUES ('admin', '$2a$10$EqKcp1WFKVQIShMPC7B3kuznX9gAZMsVnSNjN0ABNuHVBCpzqABae', '系统管理员', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE `username` = `username`;

-- 初始分类数据
INSERT INTO `category` (`name`, `parent_id`, `sort`) VALUES
('手机数码', 0, 1),
('电脑办公', 0, 2),
('家用电器', 0, 3),
('服装鞋包', 0, 4),
('食品生鲜', 0, 5),
('美妆护肤', 0, 6),
('图书音像', 0, 7),
('运动户外', 0, 8)
ON DUPLICATE KEY UPDATE `name` = `name`;
