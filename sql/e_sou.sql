USE `e_sou`;  -- 修改数据库名

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `tags` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签列表（json 数组）',
  `thumbNum` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `favourNum` int NOT NULL DEFAULT 0 COMMENT '收藏数',
  `userId` bigint NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1, '懒羊羊的开心日记', '今天天气晴，我早上睡过了，但是碰巧遇到了沸羊羊，他也睡过头了，哈哈，有人陪我一起迟到了', '[\"迟到\", \"上课\"]', 0, 0, 1, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (2, '喜羊羊的开心日记', '今天和村长一起研究新发明，虽然爆炸了三次，但最后成功做出了会飞的帽子！太好玩啦', '[\"发明\", \"冒险\"]', 0, 0, 2, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (3, '美羊羊的开心日记', '我在草原上发现了好多漂亮的小花，编了一个花环送给暖羊羊姐姐，她笑得好开心呀', '[\"花朵\", \"友谊\"]', 0, 0, 4, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (4, '沸羊羊的开心日记', '今天练习跑步打破了记录！虽然最后摔了一跤，但是村长说我进步很大，我要继续努力', '[\"运动\", \"进步\"]', 0, 0, 3, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (5, '懒羊羊的开心日记', '中午吃了三大碗青草蛋糕，还偷吃了喜羊羊的零食，嘿嘿，他肯定不知道是我吃的', '[\"美食\", \"调皮\"]', 0, 0, 1, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (6, '喜羊羊的开心日记', '帮助美羊羊找到了丢失的发卡，原来在草丛里。助人为乐的感觉真好！', '[\"助人\", \"快乐\"]', 0, 0, 2, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (7, '暖羊羊的开心日记', '今天给小朋友们讲了一天的故事，虽然嗓子有点累，但看到他们开心的样子就很满足', '[\"教学\", \"温暖\"]', 0, 0, 5, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (8, '沸羊羊的开心日记', '和懒羊羊比赛吃苹果，我赢了！不过他吃完就睡着的样子真的好搞笑', '[\"比赛\", \"有趣\"]', 0, 0, 3, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (9, '美羊羊的开心日记', '学做了新的草莓饼干，分给了大家，懒羊羊说好吃到飞起来，嘻嘻', '[\"烘焙\", \"分享\"]', 0, 0, 4, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (10, '懒羊羊的开心日记', '下午在树下睡觉，梦见了超大号的冰淇淋山，醒来发现口水流了一地，尴尬', '[\"做梦\", \"冰淇淋\"]', 0, 0, 1, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (11, '喜羊羊的开心日记', '发现了一条秘密小路，通往一个美丽的湖泊，以后可以带大家去野餐啦', '[\"探索\", \"发现\"]', 0, 0, 2, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (12, '沸羊羊的开心日记', '今天帮村长搬东西，虽然很重，但我可是大力士沸羊羊！一点都不累（其实手酸了）', '[\"力量\", \"帮忙\"]', 0, 0, 3, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (13, '美羊羊的开心日记', '下雨天和暖羊羊姐姐一起在教室里画画，画了我们的羊村大家庭', '[\"下雨\", \"绘画\"]', 0, 0, 4, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (14, '懒羊羊的开心日记', '上课的时候偷偷打瞌睡，被村长发现了，罚我打扫教室一周，呜呜呜', '[\"惩罚\", \"课堂\"]', 0, 0, 1, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (15, '喜羊羊的开心日记', '教懒羊羊做数学题，教了十遍他终于懂了，比抓灰太狼还难啊', '[\"学习\", \"耐心\"]', 0, 0, 2, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (16, '暖羊羊的开心日记', '组织了一次大扫除，大家都好配合，羊村变得干干净净的，真舒服', '[\"清洁\", \"团结\"]', 0, 0, 5, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (17, '沸羊羊的开心日记', '练习跳高，不小心跳到了树上，下不来了，最后还是喜羊羊救了我', '[\"尴尬\", \"救援\"]', 0, 0, 3, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (18, '美羊羊的开心日记', '种的小雏菊终于开花了！粉粉的好可爱，每天都要给它浇水', '[\"园艺\", \"成长\"]', 0, 0, 4, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (19, '懒羊羊的开心日记', '发现了一个超级舒服的草地，软软的像棉花糖，躺上去就不想起来了', '[\"休息\", \"舒适\"]', 0, 0, 1, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (20, '喜羊羊的开心日记', '和沸羊羊一起踢足球，我进了三个球，我们队赢啦！团队合作最重要', '[\"足球\", \"胜利\"]', 0, 0, 2, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (21, '沸羊羊的开心日记', '今天保护了被欺负的小兔子，感觉自己像个英雄！见义勇为真棒', '[\"正义\", \"勇敢\"]', 0, 0, 3, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (22, '暖羊羊的开心日记', '生病了，小羊们都来看望我，还带了礼物，好感动，有你们真好', '[\"友情\", \"感动\"]', 0, 0, 5, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (23, '美羊羊的开心日记', '学会了编辫子的新花样，给懒羊羊编了一个，他说像稻草人，哼！', '[\"发型\", \"玩笑\"]', 0, 0, 4, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (24, '懒羊羊的开心日记', '早餐吃了五个包子三碗粥，村长说我一个人吃了三个人的量，哪有！', '[\"早餐\", \"食量\"]', 0, 0, 1, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (25, '喜羊羊的开心日记', '破解了灰太狼的新陷阱，还顺便救了几只小动物，今天真是充实的一天', '[\"智慧\", \"救援\"]', 0, 0, 2, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (26, '沸羊羊的开心日记', '和美羊羊一起跳绳，她跳得好好看，我却总是绊倒，要多练习', '[\"运动\", \"坚持\"]', 0, 0, 3, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (27, '暖羊羊的开心日记', '批改作业到很晚，看到大家都有进步，再累也值得。明天要继续加油哦', '[\"工作\", \"欣慰\"]', 0, 0, 5, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (28, '懒羊羊的开心日记', '午睡醒来发现身上盖着毯子，一定是暖羊羊姐姐帮我盖的，好贴心', '[\"温暖\", \"关爱\"]', 0, 0, 1, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (29, '美羊羊的开心日记', '和大家一起去郊游，拍了很多照片，每一张都好珍贵，要永远保存', '[\"郊游\", \"回忆\"]', 0, 0, 4, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (30, '喜羊羊的开心日记', '晚上大家一起看星星，村长讲了星座的故事，原来天空这么神奇', '[\"星空\", \"知识\"]', 0, 0, 2, '2026-04-04 16:20:26', '2026-04-04 16:20:26', 0);
INSERT INTO `post` VALUES (31, '伊布的开心日记', '今天和皮卡丘在丛林里玩捉迷藏', '', 0, 0, 10, '2026-04-04 22:29:17', '2026-04-04 22:29:17', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_pk`(`userAccount` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Paddi', 'f51703256a38e6bab3d9410a070c32ea', '懒羊羊', NULL, '羊村最可爱的小羊，爱吃爱睡，乐观开朗，虽然有点懒但关键时刻也很可靠', 'admin', '2026-04-04 16:40:16', '2026-04-04 16:40:16', 0);
INSERT INTO `user` VALUES (2, 'Weslie', 'f51703256a38e6bab3d9410a070c32ea', '喜羊羊', NULL, '羊村最聪明的小羊，机智勇敢，乐于助人，是大家的开心果和主心骨', 'user', '2026-04-04 16:40:16', '2026-04-04 16:40:16', 0);
INSERT INTO `user` VALUES (3, 'Sparky', 'f51703256a38e6bab3d9410a070c32ea', '沸羊羊', NULL, '羊村最强壮的小羊，热爱运动，性格直爽，充满正义感，喜欢保护弱小', 'user', '2026-04-04 16:40:16', '2026-04-04 16:40:16', 0);
INSERT INTO `user` VALUES (4, 'Tibbie', 'f51703256a38e6bab3d9410a070c32ea', '美羊羊', NULL, '羊村最漂亮的小羊，温柔善良，心灵手巧，喜欢美丽的事物，擅长手工和烹饪', 'user', '2026-04-04 16:40:16', '2026-04-04 16:40:16', 0);
INSERT INTO `user` VALUES (5, 'Elinor', 'f51703256a38e6bab3d9410a070c32ea', '暖羊羊', NULL, '羊村最温暖的小羊，热心肠，有耐心，像大姐姐一样照顾大家，深受小朋友们喜爱', 'user', '2026-04-04 16:40:16', '2026-04-04 16:40:16', 0);
INSERT INTO `user` VALUES (10, 'yibu', 'f51703256a38e6bab3d9410a070c32ea', '伊布', NULL, '一只可爱的小精灵', 'admin', '2026-04-04 16:29:30', '2026-04-04 16:41:19', 0);

SET FOREIGN_KEY_CHECKS = 1;
