-- 创建学生表
CREATE TABLE Student (
    Sno VARCHAR(10) NOT NULL PRIMARY KEY, -- 学号，非空且为主键
    Sname VARCHAR(20) NOT NULL, -- 姓名，非空
    Ssex VARCHAR(2) NOT NULL CHECK (Ssex IN ('男', '女')), -- 性别，非空且只能为'男'或'女'
    Sage INT NOT NULL CHECK (Sage >= 0), -- 年龄，非空且大于等于0
    Sdept VARCHAR(20) NOT NULL -- 专业，非空
);

-- 创建课程表
CREATE TABLE Course (
    Cno VARCHAR(10) NOT NULL PRIMARY KEY, -- 课程号，非空且为主键
    Cname VARCHAR(20) NOT NULL UNIQUE, -- 课程名，非空且唯一
    Ccredit INT NOT NULL CHECK (Ccredit > 0), -- 学分，非空且大于0
    Tno VARCHAR(10) NOT NULL, -- 教师工号，非空
    FOREIGN KEY (Tno) REFERENCES Teacher(Tno) -- 外键关联Teacher表
);

-- 创建教师表
CREATE TABLE Teacher (
    Tno VARCHAR(10) NOT NULL PRIMARY KEY, -- 教师工号，非空且为主键
    Tname VARCHAR(20) NOT NULL, -- 教师姓名，非空
    Tsex VARCHAR(2) NOT NULL CHECK (Tsex IN ('男', '女')), -- 教师性别，非空且只能为'男'或'女'
    Tage INT NOT NULL CHECK (Tage >= 0), -- 教师年龄，非空且大于等于0
    Ttitle VARCHAR(20) NOT NULL, -- 教师职称，非空
    phone VARCHAR(15) NOT NULL UNIQUE -- 教师手机号，非空且唯一
);

-- 创建用户表
CREATE TABLE Users (
    ID VARCHAR(20) NOT NULL PRIMARY KEY, -- 账号，非空且为主键
    PAWD VARCHAR(20) NOT NULL, -- 密码，非空
    Sno VARCHAR(10), -- 学号，可为空
    Tno VARCHAR(10), -- 教师工号，可为空
    phone VARCHAR(15) NOT NULL UNIQUE, -- 手机号，非空且唯一
    role VARCHAR(10) NOT NULL CHECK (role IN ('学生', '教师', '管理员')) -- 角色，非空且只能为'学生'、'教师'或'管理员'
);

-- 添加外键约束，确保Sno和Tno的一致性
ALTER TABLE Users ADD CONSTRAINT FK_Users_Student FOREIGN KEY (Sno) REFERENCES Student(Sno);
ALTER TABLE Users ADD CONSTRAINT FK_Users_Teacher FOREIGN KEY (Tno) REFERENCES Teacher(Tno);

-- 创建选课表
CREATE TABLE SC (
    Sno VARCHAR(10) NOT NULL, -- 学号，非空
    Cno VARCHAR(10) NOT NULL, -- 课程号，非空
    Grade INT CHECK (Grade >= 0 AND Grade <= 100), -- 成绩，取值范围在0到100之间
    LEVEL VARCHAR(10), -- 等级，可为空
    PRIMARY KEY (Sno, Cno), -- 联合主键，确保一个学生对一门课程只有一条选课记录
    FOREIGN KEY (Sno) REFERENCES Student(Sno), -- 外键关联Student表
    FOREIGN KEY (Cno) REFERENCES Course(Cno) -- 外键关联Course表
);