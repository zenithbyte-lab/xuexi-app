# 学学 App — 毕业设计全栈项目

## 项目架构

```
xuexi-app/
├── frontend/     Vue 3 + Vite H5 应用
├── miniprogram/  微信小程序套壳（web-view）
└── backend/      Spring Boot 3 REST API
```

## 快速启动

### 1. 数据库初始化

```bash
mysql -u root -p < backend/src/main/resources/sql/init.sql
```

### 2. 修改后端配置

编辑 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    password: your_mysql_password
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
# 服务运行于 http://localhost:8080
```

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
# H5 运行于 http://localhost:5173
```

### 5. 微信小程序

用微信开发者工具打开 `miniprogram/` 目录，将 `app.js` 中 `h5Url` 修改为实际地址。

---

## 演示流程

登录 → 首页打卡 → 打卡成功 → 课程列表 → 课程详情 → 视频播放 → 每日练习 → 练习结果 → 社群发帖 → 个人中心

## 技术栈

| 层 | 技术 |
|---|---|
| 小程序 Shell | 微信原生 + `<web-view>` |
| H5 前端 | Vue 3 + Vite + vue-router + Pinia + Axios |
| CSS | Tailwind CSS v3（Material You 色系） |
| 图标 | Material Symbols Outlined |
| 后端 | Spring Boot 3 + Spring Security + JWT |
| ORM | Spring Data JPA + Hibernate |
| 数据库 | MySQL 8 |
