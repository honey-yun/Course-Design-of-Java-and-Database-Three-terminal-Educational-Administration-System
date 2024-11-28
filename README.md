# JAVA与SQLserver数据库的课程设计
这是一个使用 Java 语言结合SQL server数据库编写的学生管理系统，旨在解决学生、老师以及教务处管理员在个人信息、课程、成绩上的问题。
学校需要一个系统来方便地管理学生、课程和教师的相关信息，包括信息的添加、删除、更新、查询以及展示等各种操作。

登录功能：提供管理员、学生和教师三种角色的登录界面，用户输入正确的账号和密码后可进入相应的主界面。

注册功能：管理员、学生和教师可在各自的注册界面进行注册，注册时需填写相关信息，系统会检查用户名是否已存在，若存在则会提示注册失败，反之注册成功，数据存入数据库。

管理员端信息添加功能：管理员可添加学生、课程和教师信息。

管理员端信息删除功能：管理员可删除学生、课程和教师信息。

管理员端信息更新功能：管理员可更新学生、课程和教师信息。

管理员端信息查询功能：管理员可根据学号查询学生的详细信息，包括个人信息、选课信息和成绩等。

管理员端信息展示功能：管理员可展示所有学生、课程、选课、用户和教师的信息。

教师端信息查询功能：教师可在课程管理界面查看自己所授课程信息以及自己的个人信息；教师还可查询自己所授课程的学生成绩信息。

教师端成绩修改功能：教师可以修改学习自己课程学生的成绩。

学生端信息查询功能：；学生可查询个人信息、已选课程信息和成绩。

学生端选课功能：学生可在选课界面查看所有课程信息，并可以选择课程进行选课操作。

# 下面是一下界面展示（包括E-R图）

![Image text](https://github.com/honey-yun/JAVA-/blob/main/image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-11-28%20172415.png)

![Image text](https://github.com/honey-yun/JAVA-/blob/main/image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-11-28%20172422.png)

![Image text](https://github.com/honey-yun/JAVA-/blob/main/image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-11-28%20172430.png)

![Image text](https://github.com/honey-yun/JAVA-/blob/main/image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-11-28%20172437.png)

![Image text](https://github.com/honey-yun/JAVA-/blob/main/image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-11-28%20172457.png)

![Image text](https://github.com/honey-yun/JAVA-/blob/main/image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-11-28%20172517.png)

![Image text](https://github.com/honey-yun/JAVA-/blob/main/image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-11-28%20172533.png)

# E-R图

![Image text](https://github.com/honey-yun/JAVA-/blob/main/image/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-11-28%20161823.png)

# 其他建议

如果有别的需求请自行修改

我同时也上传了E-R图转换成与具体机器上的DBMS （SQL Server）所支持的数据模型相符合的逻辑结构crebas.sql文件，以及SQLserver的建表文件。

简化后的SQLQuery1.sql的建表代码

PhysicalDataModel_1.pdm为E-R图源文件

可以自行查看
