# HappyShop - 在线购物平台

类似淘宝的全功能电商 Web 应用。

## 技术栈

- **后端**: Spring Boot 3 + MyBatis-Plus + Spring Security + JWT
- **前端**: Vue 3 + Vite + Element Plus + Pinia
- **数据库**: MySQL 9.5
- **缓存**: Redis 8.4

## 功能模块

### 用户端
- 注册/登录
- 首页（轮播、推荐、分类）
- 商品搜索 + 分类筛选
- 商品详情（图片、价格、规格、评价）
- 购物车
- 下单 + 模拟支付
- 订单管理
- 商品评价
- 个人中心（收货地址、订单、收藏）

### 商家端
- 商家入驻
- 商品管理
- 订单管理
- 数据统计

### 管理员端
- 用户管理
- 商家审核
- 商品审核
- 分类管理
- 促销活动管理

## 快速开始

### 1. 初始化数据库

```bash
mysql -u root -p < happyshop-backend/src/main/resources/db/init.sql
```

### 2. 启动后端

```bash
cd happyshop-backend
mvn spring-boot:run
```

后端运行在 http://localhost:8080
Swagger 文档: http://localhost:8080/swagger-ui.html

### 3. 启动前端

```bash
cd happyshop-frontend
npm install
npm run dev
```

前端运行在 http://localhost:5173

### 4. 默认管理员账号

- 用户名: admin
- 密码: admin123
