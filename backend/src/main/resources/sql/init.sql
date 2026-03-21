CREATE DATABASE IF NOT EXISTS xuexi_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE xuexi_db;
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone       VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    nickname    VARCHAR(50) NOT NULL COMMENT '昵称',
    avatar_url  VARCHAR(500) COMMENT '头像URL',
    points      INT NOT NULL DEFAULT 0 COMMENT '积分',
    completed_courses INT NOT NULL DEFAULT 0 COMMENT '已完成课程数',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 打卡记录表
CREATE TABLE IF NOT EXISTS checkins (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    checkin_date DATE NOT NULL COMMENT '打卡日期',
    streak_days INT NOT NULL DEFAULT 1 COMMENT '连续打卡天数',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_date (user_id, checkin_date),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录表';

-- 课程表
CREATE TABLE IF NOT EXISTS courses (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(200) NOT NULL COMMENT '课程标题',
    category        VARCHAR(50) NOT NULL COMMENT '分类（数学/英语/设计/编程）',
    teacher         VARCHAR(100) NOT NULL COMMENT '讲师',
    cover_url       VARCHAR(500) COMMENT '封面图URL',
    description     TEXT COMMENT '课程描述',
    total_chapters  INT NOT NULL DEFAULT 0 COMMENT '总章节数',
    difficulty      VARCHAR(20) NOT NULL DEFAULT '初级' COMMENT '难度（初级/中级/高级）',
    student_count   INT NOT NULL DEFAULT 0 COMMENT '学生人数',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 章节表
CREATE TABLE IF NOT EXISTS chapters (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id   BIGINT NOT NULL COMMENT '课程ID',
    title       VARCHAR(200) NOT NULL COMMENT '章节标题',
    duration_sec INT NOT NULL DEFAULT 0 COMMENT '时长（秒）',
    video_url   VARCHAR(500) COMMENT '视频URL',
    order_num   INT NOT NULL DEFAULT 0 COMMENT '排序',
    KEY idx_course_id (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='章节表';

-- 用户课程进度表
CREATE TABLE IF NOT EXISTS user_course_progress (
    user_id             BIGINT NOT NULL COMMENT '用户ID',
    course_id           BIGINT NOT NULL COMMENT '课程ID',
    current_chapter     INT NOT NULL DEFAULT 1 COMMENT '当前章节序号',
    progress_pct        INT NOT NULL DEFAULT 0 COMMENT '进度百分比',
    last_watched_at     DATETIME COMMENT '最后观看时间',
    PRIMARY KEY (user_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户课程进度';

-- 帖子表
CREATE TABLE IF NOT EXISTS posts (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL COMMENT '用户ID',
    type          VARCHAR(20) NOT NULL DEFAULT 'share' COMMENT '类型（checkin/question/share）',
    content       TEXT NOT NULL COMMENT '内容',
    image_url     VARCHAR(500) COMMENT '图片URL',
    like_count    INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    comment_count INT NOT NULL DEFAULT 0 COMMENT '评论数',
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_user_id (user_id),
    KEY idx_type (type),
    KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- 评论表
CREATE TABLE IF NOT EXISTS comments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id     BIGINT NOT NULL COMMENT '帖子ID',
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    content     TEXT NOT NULL COMMENT '评论内容',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 帖子点赞表
CREATE TABLE IF NOT EXISTS post_likes (
    user_id     BIGINT NOT NULL,
    post_id     BIGINT NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子点赞表';

-- 练习题目表
CREATE TABLE IF NOT EXISTS quiz_questions (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject     VARCHAR(50) NOT NULL COMMENT '科目',
    content     TEXT NOT NULL COMMENT '题目内容',
    options_json TEXT NOT NULL COMMENT '选项JSON（{A:...,B:...,C:...,D:...}）',
    answer      CHAR(1) NOT NULL COMMENT '正确答案（A/B/C/D）',
    difficulty  VARCHAR(20) NOT NULL DEFAULT '中等' COMMENT '难度'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='练习题目表';

-- 用户练习记录表
CREATE TABLE IF NOT EXISTS user_practice_records (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL COMMENT '用户ID',
    practice_date   DATE NOT NULL COMMENT '练习日期',
    score           INT NOT NULL DEFAULT 0 COMMENT '得分',
    time_sec        INT NOT NULL DEFAULT 0 COMMENT '用时（秒）',
    correct_count   INT NOT NULL DEFAULT 0 COMMENT '正确题数',
    total           INT NOT NULL DEFAULT 10 COMMENT '总题数',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户练习记录';

-- ===================== 初始化示例数据 =====================

INSERT IGNORE INTO courses (id, title, category, teacher, description, total_chapters, difficulty, student_count) VALUES
(1, '线性代数基础', '数学', '王老师', '本课程系统讲解线性代数核心概念，涵盖向量空间、矩阵运算、行列式、特征值与特征向量等关键知识点。', 20, '中级', 2340),
(2, 'UI设计基础', '设计', '李老师', '从零开始学习UI设计，掌握配色、排版、交互设计原则，打造专业级界面。', 15, '初级', 1890),
(3, '全民Python编程', '编程', '张老师', 'Python从入门到实践，涵盖基础语法、数据结构、面向对象编程及常用库应用。', 30, '初级', 3210),
(4, '英语词汇与阅读', '英语', '陈老师', '提升英语词汇量与阅读理解能力，通过大量语境练习巩固单词记忆。', 25, '中级', 1560);

INSERT IGNORE INTO chapters (course_id, title, duration_sec, order_num) VALUES
(1, '向量与空间', 900, 1),
(1, '矩阵运算', 1350, 2),
(1, '行列式', 1125, 3),
(1, '特征值与特征向量', 1500, 4),
(2, 'UI设计原则', 720, 1),
(2, '配色系统', 960, 2),
(2, '排版与字体', 840, 3),
(3, 'Python环境搭建', 600, 1),
(3, '变量与数据类型', 900, 2),
(3, '控制流程', 780, 3);

INSERT IGNORE INTO quiz_questions (id, subject, content, options_json, answer, difficulty) VALUES
(1, '数学', '如果 a=3，b=4，求 a²+b² 的值？', '{"A":"20","B":"25","C":"24","D":"30"}', 'B', '简单'),
(2, '数学', '行列式 det(A)=0，则矩阵A是否可逆？', '{"A":"可逆","B":"不可逆","C":"不确定","D":"以上都错"}', 'B', '中等'),
(3, '英语', 'What is the past tense of "go"?', '{"A":"goed","B":"went","C":"gone","D":"going"}', 'B', '简单'),
(4, '设计', '以下哪种颜色对比度最高？', '{"A":"白底白字","B":"黑底白字","C":"灰底灰字","D":"蓝底蓝字"}', 'B', '简单'),
(5, '数学', '已知向量a=(1,2)，b=(3,4)，求a·b的值？', '{"A":"10","B":"11","C":"12","D":"14"}', 'B', '中等'),
(6, '编程', 'Python中列表 [1,2,3] 的长度是多少？', '{"A":"2","B":"3","C":"4","D":"1"}', 'B', '简单'),
(7, '英语', 'Which sentence is grammatically correct?', '{"A":"She go to school","B":"She goes to school","C":"She going to school","D":"She gone to school"}', 'B', '中等'),
(8, '数学', '矩阵乘法满足交换律吗？', '{"A":"满足","B":"不满足","C":"有时满足","D":"以上都错"}', 'B', '中等'),
(9, '设计', 'RGB颜色模式中，(255,0,0)代表什么颜色？', '{"A":"绿色","B":"红色","C":"蓝色","D":"白色"}', 'B', '简单'),
(10, '编程', 'Python中 print(type(1.0)) 的输出是什么？', '{"A":"<class int>","B":"<class float>","C":"<class str>","D":"<class num>"}', 'B', '简单');

-- 消息表
CREATE TABLE IF NOT EXISTS messages (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    type        VARCHAR(20) NOT NULL DEFAULT 'system' COMMENT '类型（system/chat/reward）',
    title       VARCHAR(200) NOT NULL COMMENT '标题',
    content     TEXT COMMENT '内容',
    is_read     TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
    icon        VARCHAR(50) COMMENT 'Material图标名',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_user_id (user_id),
    KEY idx_is_read (user_id, is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

INSERT IGNORE INTO messages (id, user_id, type, title, content, is_read, icon, created_at) VALUES
(1, 1, 'reward', '打卡成功', '恭喜你连续打卡第 7 天！已获得「坚持达人」徽章和 50 积分奖励。继续保持！', 0, 'check_circle', NOW() - INTERVAL 1 HOUR),
(2, 1, 'system', '学习提醒', '您今天还有一份每日练习未完成。保持学习习惯，每天积累，量变引起质变！', 0, 'notifications', NOW() - INTERVAL 1 DAY),
(3, 1, 'system', '课程更新', '您关注的课程《UI设计基础》已更新第 4 章「交互设计原则」，快去学习吧！', 1, 'auto_stories', NOW() - INTERVAL 3 DAY),
(4, 1, 'chat', '数学学习小组', '大家记得看新的例题，明天的随堂测验会从第三章出题，重点复习矩阵运算部分。', 0, 'groups', NOW() - INTERVAL 2 HOUR),
(5, 1, 'chat', '王老师', '你的作业已经批改了，整体思路很好，有几个细节下次课重点讲解。继续加油！', 0, 'person', NOW() - INTERVAL 3 HOUR);
