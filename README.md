# 易搜（E-Sou）

### 项目介绍
一个支持多数据源（用户、图片、帖子）统一搜索的平台，通过设计模式实现高扩展性，并采用 Elasticsearch 优化搜索效果

### 技术栈
- **后端框架**：Spring Boot 2.7 + MyBatis-Plus
- **搜索引擎**：Elasticsearch 7.17 + IK分词器（中文友好）
- **数据库**：MySQL 8.0
- **数据抓取**：Jsoup

### 架构设计
<img width="1982" height="992" alt="image" src="https://github.com/user-attachments/assets/f374d6e3-d718-4083-bd4a-184492a23750" />

### 核心亮点
#### 1. 统一搜索接口（门面模式）
  - 前端一次请求可同时查询多种类型数据
#### 2. 多数据源扩展机制（适配器 + 注册器模式）
  - 定义 `DataSource` 接口，新数据源只需实现该接口即可接入
  - 使用**注册器模式**替代多个 `if-else`，新增数据源时无需修改核心搜索逻辑，符合开闭原则
#### 3. 搜索优化
  - 帖子标题、内容、标签存入 Elasticsearch，使用 **IK 中文分词器** 提升搜索效果
  - 对于**更新频繁的字段**（如点赞数）不存入 ES，查询时使用**双查策略**（先查 ES 获取基础信息，再批量从 MySQL 补充）
#### 4. 数据同步方案
  - **首次启动**：全量写入 ES
  - **增量同步**：定时任务扫描 MySQL 更新，通过唯一 ID 保证同步准确性
  - 查询 ES 时发现 MySQL 中已删除的数据，自动从 ES 删除，清理垃圾数据

### 快速启动
#### 环境要求
- JDK 1.8
- MySQL 8.0
- Elasticsearch 7.17 + IK分词器

#### 运行步骤
1. 修改 application.yaml 配置
2. 启动 ES 服务
3. 运行项目（启动类：com.yibu.MyApplication）

### 截图展示
<img width="2834" height="1622" alt="屏幕截图 2026-04-09 144744" src="https://github.com/user-attachments/assets/b4a8ac76-0c43-44e3-9b15-ab09693215b1" />
<img width="2878" height="1720" alt="屏幕截图 2026-04-09 144515" src="https://github.com/user-attachments/assets/e6204aee-5bc6-4afe-96c6-491c8e4a4376" />
