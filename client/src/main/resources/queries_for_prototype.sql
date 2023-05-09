-- create database HSTS;
--
insert into student
values
(1,'yoyo@gmail.com','Eitan','Male','David','123123Y'),
(2,'eldadM@gmail.com','Eldad','Male','Moshe','2233PPP'),
(3,'danM@gmail.com','Dan','Male','David','123123Y'),
(4,'adiL@gmail.com','Adi','Female','Levi','11554ss'),
(5,'liorc@gmail.com','Lior','Female','Cohen','345yyy'),
(6,'shaniD@gmail.com','Shani','feMale','David','31ffdd'),
(7,'yaakovB@gmail.com','Yaakov','Male','Ben-Dor','1pppds2'),
(8,'lanaF@gmail.com','Lana','Female','Franco','2332211dd'),
(9,'leaP@gmail.com','Lea','Female','Pomeran','99ol9o'),
(10,'matanP@gmail.com','Matan','Male','Polik','yo2s3r'),
(11,'orenG@gmail.com','Oren','Male','Golan','123123'),
(12,'meitarY@gmail.com','Meitar','Female','Yeruham','9o8i7us');
--
--
insert into subject
values
(1,"Math");
--
insert into course
values(1,"Hedva",1);
--
insert into examform
values
(1,180,1,1);
--
insert into teacher
values
(1,"shosh@gmail.com","Shoshana","Female","Levi","tt2p10");
--
insert into scheduledtest
values
(1,'2022-03-10',20,'12:00:00',1,1),
(2,'2022-04-11',15,'16:00:00',1,1),
(3,'2022-05-17',10,'10:00:00',1,1),
(4,'2022-06-10',12,'17:00:00',1,1);
--
--
insert into studenttest
values
(1,85,180,1,1),
(2,90,180,4,1),
(3,87,140,2,2),
(4,85,175,3,2),
(5,49,162,1,3),
(6,67,100,4,3),
(7,99,150,3,4),
(8,100,155,2,4),
(9,54,180,4,5),
(10,72,177,1,5),
(11,83,179,2,6),
(12,65,140,3,6),
(13,97,100,4,7),
(14,57,70,1,7),
(15,75,68,2,8),
(16,47,150,4,8),
(17,66,170,3,9),
(18,85,132,1,9),
(19,99,155,3,10),
(20,93,176,4,10),
(21,76,120,1,11),
(22,85,180,2,11),
(23,83,170,2,12),
(24,100,180,3,12),
(25,100,160,4,12);
--
-- select * from student;
--
-- select s.id,s.first_name,s.last_name,t.scheduledTest_id,t.grade
-- from student as s
-- inner join studenttest as t
-- on s.id = t.id;
--
-- select s.id,s.first_name,s.last_name,t.scheduledTest_id,t.grade
-- from student as s
-- inner join studenttest as t
-- on s.id = t.id
-- where s.id = 3;

delete from studenttest;
delete from scheduledtest;
delete from teacher_course;
delete from teachers_subjects;
delete from teacher;
delete from examform;
delete from course;
delete from subject;
delete from student;

select * from course;
select * from teacher;
select * from examform;
select * from scheduledtest;
select * from student;
select * from studenttest;
select * from subject;
select * from teacher_course;
select * from teachers_subjects;



SET SQL_SAFE_UPDATES = 0;
