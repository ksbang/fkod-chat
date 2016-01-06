# fkod-chat
FKOD 팀의 채팅(스윙버전) 

이 프로그램을 실행시키기 위해서는
오라클을 설치하시고,
첨부된 SQL 문을 실행시켜 주시고
exe 파일을 실행하세요.

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
