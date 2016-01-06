drop table Article;

create sequence seq 
start with 1000;

create table Article(
	seq number,
	title varchar2(100) not null,
	content varchar2(500) not null,
	regdate date,
	userid varchar2(50) not null,
	constraint article_pk primary key seq,
	constraint article_member_fk
		foreign key userid references Member(userid);
);