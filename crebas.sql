/*==============================================================*/
/* DBMS name:      Sybase SQL Anywhere 12                       */
/* Created on:     2024/11/28 16:27:51                          */
/*==============================================================*/


if exists(select 1 from sys.sysforeignkey where role='FK_SC_REFERENCE_STUDENT') then
    alter table SC
       delete foreign key FK_SC_REFERENCE_STUDENT
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_SC_REFERENCE_COURSE') then
    alter table SC
       delete foreign key FK_SC_REFERENCE_COURSE
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_Users_Student') then
    alter table Users
       delete foreign key FK_Users_Student
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_Users_Teacher') then
    alter table Users
       delete foreign key FK_Users_Teacher
end if;

drop table if exists Course;

drop table if exists SC;

drop table if exists Student;

drop table if exists Teacher;

drop table if exists Users;

/*==============================================================*/
/* Table: Course                                                */
/*==============================================================*/
create table Course 
(
   Cno                  VARCHAR(10)                    not null,
   Cname                VARCHAR(20)                    not null,
   Ccredit              INT                            not null
   	constraint CKC_CCREDIT_COURSE check (Ccredit > 0),
   Tno                  VARCHAR(10)                    not null
);

/*==============================================================*/
/* Table: SC                                                    */
/*==============================================================*/
create table SC 
(
   Sno                  VARCHAR(10)                    not null,
   Cno                  VARCHAR(10)                    not null,
   Grade                INT                            null
   	constraint CKC_GRADE_SC check (Grade is null or (Grade between 0 and 100)),
   LEVEL                VARCHAR(10)                    null,
   constraint PK_SC primary key (Sno, Cno)
);

/*==============================================================*/
/* Table: Student                                               */
/*==============================================================*/
create table Student 
(
   Sno                  VARCHAR(10)                    not null,
   Sname                VARCHAR(20)                    not null,
   Ssex                 VARCHAR(2)                     not null
   	constraint CKC_SSEX_STUDENT check (Ssex in ('男','女')),
   Sage                 INT                            not null
   	constraint CKC_SAGE_STUDENT check (Sage >= 0),
   Sdept                VARCHAR(20)                    not null
);

/*==============================================================*/
/* Table: Teacher                                               */
/*==============================================================*/
create table Teacher 
(
   Tno                  VARCHAR(10)                    not null,
   Tname                VARCHAR(20)                    not null,
   Tsex                 VARCHAR(2)                     not null
   	constraint CKC_TSEX_TEACHER check (Tsex in ('男','女')),
   Tage                 INT                            not null
   	constraint CKC_TAGE_TEACHER check (Tage >= 0),
   Ttitle               VARCHAR(20)                    not null,
   phone                VARCHAR(15)                    not null
);

/*==============================================================*/
/* Table: Users                                                 */
/*==============================================================*/
create table Users 
(
   ID                   VARCHAR(20)                    not null,
   PAWD                 VARCHAR(20)                    not null,
   Sno                  VARCHAR(10)                    null,
   Tno                  VARCHAR(10)                    null,
   phone                VARCHAR(15)                    not null,
   role                 VARCHAR(10)                    not null
   	constraint CKC_ROLE_USERS check (role in ('学生','教师','管理员'))
);

alter table SC
   add constraint FK_SC_REFERENCE_STUDENT foreign key (Sno)
      references Student (Sno)
      on update restrict
      on delete restrict;

alter table SC
   add constraint FK_SC_REFERENCE_COURSE foreign key (Cno)
      references Course (Cno)
      on update restrict
      on delete restrict;

alter table Users
   add constraint FK_Users_Student foreign key (Sno)
      references Student (Sno)
      on update restrict
      on delete restrict;

alter table Users
   add constraint FK_Users_Teacher foreign key (Tno)
      references Teacher (Tno)
      on update restrict
      on delete restrict;

