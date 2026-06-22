# 后台管理系统

一个基于 Vue 3 + Spring Boot 3 的企业级后台管理系统，提供员工管理、部门管理、文章管理、数据统计等核心功能，支持角色权限控制和响应式布局。

## 项目简介

本项目是一个通用的后台管理应用，采用前后端分离架构。前端基于 Vue 3 Composition API 构建，使用 Element Plus 组件库；后端基于 Spring Boot 3 搭建，使用 MyBatis 作为 ORM 框架。系统覆盖了完整的 CRUD 业务场景，内置角色权限控制、数据可视化、文件上传、Excel 导入导出等常见后台功能。

## 技术栈

### 前端框架

- **Vue 3.5.32** - 渐进式 JavaScript 框架（Composition API + `<script setup>`）
- **Vite 8.0.8** - 下一代前端构建工具
- **Vue Router 5.0.4** - 官方路由管理器
- **Axios 1.16.1** - HTTP 客户端

### UI 组件库

- **Element Plus 2.14.0** - 基于 Vue 3 的企业级组件库
- **@element-plus/icons-vue 2.3.2** - Element Plus 图标库

### 功能库

- **ECharts 6.1.0** - 数据可视化图表库
- **@wangeditor/editor-for-vue 5.1.12** - 富文本编辑器
- **vue-router 5.0.4** - 路由管理

### 后端框架

- **Spring Boot 3.5.0** - 后端基础框架
- **Java 17** - 语言版本
- **MyBatis** - ORM 数据访问层
- **MySQL** - 关系型数据库
- **PageHelper** - 通用分页插件
- **Hutool 5.8.25** - Java 工具类库
- **Apache POI 5.2.5** - Excel 导入导出

### 开发工具

- **@vitejs/plugin-vue 6.0.6** - Vite Vue 插件
- **vite-plugin-vue-devtools 8.1.1** - Vue 开发者工具

## 功能特性

### 系统首页

- 实时数字时钟展示
- 最新文章列表展示
- 文章详情弹窗阅读
- 文章分页浏览
- 全屏背景图展示

### 数据统计

- 各部门员工数量柱状图
- 每日文章数量折线图
- 员工部门分布饼图
- ECharts 可视化展示

### 部门管理

- 部门信息 CRUD
- 分页条件查询
- 批量删除
- 名称模糊搜索

### 员工管理

- 员工信息 CRUD
- 分页条件查询
- 批量删除
- Excel 导入导出
- 头像上传
- 关联部门选择

### 管理员管理

- 管理员信息 CRUD
- 分页条件查询
- 批量删除
- 头像上传
- 名称模糊搜索

### 文章管理

- 文章 CRUD
- 富文本编辑器（wangEditor）
- 封面图片上传
- 文章内容预览
- 分页条件查询

### 个人信息

- 头像上传
- 个人信息编辑
- 账号/姓名/性别/工号/年龄/职位 修改

### 修改密码

- 原密码验证
- 新密码设置
- 确认新密码校验

### 用户认证

- 用户注册
- 用户登录（管理员/员工双角色）
- 密码加密存储
- 登录态本地持久化

### 权限控制

- 基于角色（admin / employee）的菜单动态渲染
- 路由守卫防越权访问
- 侧边栏按角色隐藏敏感功能

### 响应式布局

- 桌面/平板/手机三档适配
- 侧边栏自适应折叠
- 弹性网格布局
- 组件级响应式样式

## 项目结构

```
proj4/
├── vue/                            # 前端项目
│   ├── public/                     # 静态资源
│   ├── src/
│   │   ├── assets/                 # 资源文件
│   │   │   ├── global.css          # 全局样式（含响应式规则）
│   │   │   ├── background.jpg      # 登录页/首页背景
│   │   │   ├── java.svg            # 系统 Logo
│   │   │   └── pic_404.jpg         # 404 页面图片
│   │   ├── router/
│   │   │   └── index.js            # 路由配置 + 权限守卫
│   │   ├── utils/
│   │   │   └── request.js          # Axios 封装（拦截器）
│   │   ├── views/
│   │   │   ├── Manager.vue         # 主布局（导航栏 + 侧边栏）
│   │   │   ├── Home.vue            # 系统首页（时钟 + 文章列表）
│   │   │   ├── Login.vue           # 登录页
│   │   │   ├── Register.vue        # 注册页
│   │   │   ├── Data.vue            # 数据统计（ECharts 图表）
│   │   │   ├── Article.vue         # 文章管理（富文本编辑器）
│   │   │   ├── Department.vue      # 部门管理
│   │   │   ├── Employee.vue        # 员工管理（Excel 导入导出）
│   │   │   ├── Admin.vue           # 管理员管理
│   │   │   ├── Person.vue          # 个人信息
│   │   │   ├── ModifyPassword.vue  # 修改密码
│   │   │   └── 404.vue             # 404 页面
│   │   ├── App.vue                 # 根组件
│   │   └── main.js                 # 入口文件
│   ├── index.html
│   ├── package.json
│   └── vite.config.js              # Vite 配置（端口 5174）
│
├── springboot/                     # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/proj4/
│   │       │   ├── common/         # 通用类（CORS、统一响应）
│   │       │   ├── controller/     # 控制器层
│   │       │   │   ├── WebCotroller.java      # 登录/注册/密码
│   │       │   │   ├── AdminController.java   # 管理员 CRUD
│   │       │   │   ├── EmployeeController.java # 员工 CRUD + 导入导出
│   │       │   │   ├── DepartmentController.java # 部门 CRUD
│   │       │   │   ├── ArticleController.java  # 文章 CRUD
│   │       │   │   └── FileController.java     # 文件上传
│   │       │   ├── entity/         # 实体类
│   │       │   ├── mapper/         # MyBatis Mapper 接口
│   │       │   ├── service/        # 业务逻辑层
│   │       │   └── exception/      # 全局异常处理
│   │       └── resources/
│   │           ├── mapper/         # MyBatis XML 映射
│   │           └── application.yml # 数据库/MyBatis 配置
│   ├── pom.xml
│   └── README.md
└── temp/                           # 临时文件
```

## 快速开始

### 环境要求

- Node.js >= 20.0.0
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### 数据库配置

1. 创建数据库 `managesystem`
2. 执行 SQL 脚本初始化表结构（根据实体类自动建表或手动导入）
3. 修改 `springboot/src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root
    url: jdbc:mysql://localhost:3306/managesystem?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2b8
```

### 启动后端

```bash
cd springboot
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 启动前端

```bash
cd vue
npm install
npm run dev
```

前端服务将在 `http://localhost:5174` 启动

### 生产构建

```bash
cd vue
npm run build
```

构建产物将输出到 `dist` 目录

## 配置说明

### 前端代理配置

当前前端直接请求 `http://localhost:8080`（在 `utils/request.js` 中配置 baseURL），如需代理可在 `vite.config.js` 中添加：

```javascript
server: {
  port: 5174,
  strictPort: true,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
  },
}
```

### 路径别名

配置了 `@` 别名指向 `src` 目录：

```javascript
resolve: {
  alias: {
    '@': fileURLToPath(new URL('./src', import.meta.url))
  },
}
```

### 前后端联调

前端通过 Axios 实例访问后端接口：

```javascript
const request = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 30000
})
```

请求/响应拦截器统一处理错误提示和响应格式。

## 权限控制说明

### 角色体系

| 角色 | 说明 | 访问权限 |
|------|------|----------|
| `admin` | 管理员 | 全部功能 |
| `employee` | 普通员工 | 系统首页、个人信息、修改密码 |

### 前端控制

- **菜单渲染** — 侧边栏根据 `v-if="data.user.role === 'admin'"` 动态显示/隐藏
- **路由守卫** — `router.beforeEach` 拦截 `/manager/article`、`/manager/department`、`/manager/admin`、`/manager/employee`、`/manager/data` 等管理路由，非 admin 用户跳转到主页

### 后端控制

- 登录接口根据角色查询对应数据表（admin 表 / employee 表）
- 业务接口按需校验权限

## 核心功能说明

### 角色登录

系统支持管理员和员工两种角色登录：

```javascript
// Login.vue - 登录时附带角色参数
request.post("/login", {
  account: "...",
  password: "...",
  role: "admin"  // 或 "employee"
}).then(res => {
  // 存储用户信息到 localStorage
  localStorage.setItem("user", JSON.stringify(res.data))
})
```

### 数据可视化

使用 ECharts 实现三种数据图表：

```javascript
// 柱状图 - 各部门员工数量
const barChart = echarts.init(document.getElementById('bar'));
barChart.setOption({
  xAxis: { data: departmentNames },
  series: [{ type: 'bar', data: employeeCounts }]
});
```

### Excel 导入导出

基于 Apache POI 实现 Excel 操作：

- **导入**: 上传 Excel 文件 → 后端解析 → 批量插入数据库
- **导出**: 查询数据 → POI 生成 Workbook → 下载文件

### 富文本编辑

使用 wangEditor 实现文章内容编辑：

```vue
<Toolbar :editor="editorRef" />
<Editor v-model="data.form.content" @onCreated="handleCreated" />
```

## 浏览器支持

- Chrome >= 90
- Firefox >= 88
- Safari >= 14
- Edge >= 90

## 开发建议

1. **组件开发**: 遵循 Vue 3 Composition API + `<script setup>` 语法
2. **样式管理**: 优先使用 CSS 类名而非内联样式，媒体查询使用 `max-width` 断点
3. **权限扩展**: 如需新增角色，在路由守卫和菜单渲染处统一添加条件判断
4. **API 请求**: 统一使用 `utils/request.js` 封装的 Axios 实例
5. **后端规范**: Controller → Service → Mapper 三层结构，分页使用 PageHelper

## 许可证

MIT License
