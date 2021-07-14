#스키마(DB) 생성
create database wannabe default character set UTF8;

# 사용자 생성
create user bee@'%' identified by 'honey';

# 권한 주기
grant all privileges on wannabe.* to bee@'%';


# 테이블 목록 보기
show tables;

# 스키마(DB)선택
use wannabe;

#<테스트 spl>#
#select * from product_img where uploadpath = DATE_FORMAT( date_sub(NOW(),INTERVAL 1 DAY)  ,'%Y\%m\%d');
#
#
#
#

# 삭제할 때 역순으로 실행
drop table member;
drop table product_category;
drop table product;
drop table review;
drop table qna;
drop table cart;
drop table purchase_history;
drop table address;
drop table product_img;
drop table review_img ;

# 한 번에 생성 안됨 하나씩 실행
#-----------------------------------------------------------
# 1 회원 테이블
create table member(
    member_id VARCHAR(20) PRIMARY KEY, # 멤버 아이디 
    member_pw VARCHAR(200) not null, # 멤버 비밀번호
    member_name VARCHAR(20) not null, # 멤버 이름
    member_phone VARCHAR(50) not null, # 멤버 연락처
    member_email VARCHAR(50) not null, # 멤버 이메일
    member_authority char(1) not null default 'b', # 멤버 권한
    regdate datetime default now() , # 가입일
    updatedate datetime default now(), # 수정일
    secession char(1) default 'n' # 탈퇴 여부
    ); 
#-----------------------------------------------------------
# 2 카테고리 테이블
create table product_category(
	catename varchar(20), # 카테고리 명
	catecode varchar(30) primary key, # 카테고리 코드번호
	catecoderef varchar(30), #참조
	foreign key(catecoderef) references product_category(catecode)
);
insert into PRODUCT_CATEGORY(catename, catecode) values('침대','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('온열침대','101','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('철제침대','102','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('원목침대','103','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('싱글침대','104','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('슈퍼싱글/더블침대','105','100');

insert into PRODUCT_CATEGORY(catename, catecode) values('의자','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('철제의자','201','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('원목의자','202','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('접이식의자','203','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('야외용의자','204','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('안락/흔들의자','205','200');

insert into PRODUCT_CATEGORY(catename, catecode) values('소파','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('패브릭소파','301','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('가죽소파','302','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('원목소파','303','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('1인/2인용 소파','304','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('3인/4인용 소파','305','300');

insert into PRODUCT_CATEGORY(catename, catecode) values('수납장','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('철제수납장','401','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('원목수납장','402','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('모듈수납장','403','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('옷장/수납장','404','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('서랍장','405','400');

insert into PRODUCT_CATEGORY(catename, catecode) values('테이블','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('철제테이블','501','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('원목테이블','502','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('식탁','503','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('책상/데스크','504','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('다용도/수납테이블','505','500');
#-----------------------------------------------------------  
# 3 상품 테이블
create table product(
	product_no int not null auto_increment, # 상품 번호
	product_category_code varchar(30), # 분류
	product_title varchar(100), # 상품 제목
	product_content longtext, # 상품 내용
	product_writer varchar(20), # 작성자-회원 아이디
	product_price int, # 상품 가격
	product_delivery_message varchar(100), # 배송비 내용
	product_delivery_price int, # 배송비
	product_sale_percent int, # 할인율
	product_company varchar(30), #판매사
	product_quantity int, # 상품 수량
	product_type varchar(30), # 상품 타입
	product_code varchar(30), # 상품 코드
	regdate datetime default now(), # 작성일
	updatedate datetime default now(), # 수정일
	secession char(1) default 'n', # 삭제 여부 
	primary key(product_no),
	foreign key(product_category_code) references product_category(catecode)
);
#-----------------------------------------------------------
# 4 리뷰 테이블
    create table review (
   review_no int(10) primary key auto_increment,
   product_no int(20),
   regdate datetime default now(),
   constraint fk_review_po foreign key(product_no) references product(product_no),  
   review_content varchar(1000) not null, 
   member_id varchar(20) not null,
	rating int default 5,
   likes int default 0,
   constraint fk_review_id foreign key(member_id) references member(member_id)
);


#-----------------------------------------------------------
# 5 문의 테이블
create table qna (
  qna_no int not null auto_increment,  # 문의 번호
  product_no int(10) not null, # 상품 번호
  qna_content varchar(1000) not null, # 문의 내용
  member_id varchar(20) not null, # 문의 작성자
  replyDate datetime default now(), # 작성일
  updateDate datetime default now(), # 수정일
isAnswered boolean not null default FALSE, 
  primary key(qna_no),
  foreign key(product_no) references product(product_no),
  foreign key (member_id) references member(member_id),
  title varchar(50) not null,
  view_count int not null default 0 
);

insert into qna (product_no, qna_content, member_id, title) values(113, "문의드립니다", "ara123", "배송은 언제 되나요?");
insert into qna (product_no, qna_content, member_id, title) values(113, "문의드립니다22", "ara123", "배송은 언제 되나요?222");
insert into qna (product_no, qna_content, member_id, title) values(113, "문의드립니다33", "ara123", "배송은 언제 되나요?333");

#------------------------문의 답변

create table qna_reply (
   parent_no int,
    foreign key(parent_no) references qna(qna_no) ON DELETE CASCADE,
    qna_reply_writer varchar(20) default '관리자(WANNABE)',
    qna_reply_title varchar(20) default '답변드립니다.', 
    qna_reply_no int auto_increment primary key,
    qna_reply_content varchar(1000) not null,
    qna_reply_date datetime default now(),
    view_count int default 0,
    product_no int,
    foreign key (product_no) references product(product_no)
);
#-----------------------------------------------------------
# 6 장바구니 테이블
create table cart(
	cart_no int  not null auto_increment, # 장바구니 번호
	product_no int, # 상품 번호
	member_id varchar(20), # 회원 아이디
	cart_product_quantity int, # 수량
	regdate datetime default now(), # 장바구니 생성일
	primary key(cart_no),
	foreign key(member_id) references member(member_id),
	foreign key(product_no) references product(product_no)
);
#-----------------------------------------------------------
# 7 구매내역 테이블
create table purchase_history(
	member_id varchar(20), # 회원 아이디
	product_no int, # 상품 번호
	product_category_code varchar(30), # 분류
	product_title varchar(100), # 상품 제목
	product_content longtext, # 상품 내용
	product_writer varchar(20), # 작성자-회원 아이디
	product_price int, # 상품 가격
	product_delivery_message varchar(100), # 배송비 내용
	product_delivery_price int, # 배송비
	product_sale_percent int, # 할인율
	product_company varchar(30), #판매사
	product_type varchar(30), # 상품 타입
	product_code varchar(30), # 상품 코드 
	address_name varchar(20) not null, # 주소지명 
	address_first varchar(500) not null, # 우편번호 
	address_middle varchar(500) not null, # 도로명
	address_last varchar(500) not null, # 상세주소
	address_message varchar(100), # 요청사항
	purchase_history_quantity int, # 수량
	purchase_history_state varchar(30), # 상태
	regdate datetime default now(), # 구매일
	delivery_start_datetime datetime, # 배송 시작일
	delivery_end_datetime datetime, # 배송 완료일
	cancel_request_datetime datetime, # 취소 요청일
	cancel_complete_datetime datetime, # 취소 완료일
	refund_request_datetime datetime, # 환불 요청일
	refund_complete_datetime datetime, # 환불 완료일
	foreign key(member_id) references member(member_id),
	foreign key(product_no) references product(product_no),
	foreign key(product_category_code) references product_category(catecode)
);
#-----------------------------------------------------------
# 8 주소지 테이블
create table address(
	address_id int primary key auto_increment,
	address_name varchar(20) not null, 
	zipcode varchar(10),
	address_first varchar(500) not null, 
	address_middle varchar(500) not null, 
	address_last varchar(500) not null, 
	address_message varchar(100), 
	member_id varchar(20) not null,
	constraint FK_address foreign key(member_id) references member(member_id) on delete cascade
	); 
#-----------------------------------------------------------
# 9 상품 이미지 테이블
create table product_img(
	 uuid varchar(100), # 고유값
	 uploadpath varchar(200), # 경로
	 filename varchar(100), # 파일이름
	 filetype varchar(20),
	 product_img_no int(20) NOT NULL auto_increment, # 이미지 번호
	 product_no int(20), # 상품게시글번호
	 primary key(product_img_no)
	 );
 #-----------------------------------------------------------
 # 10 리뷰 이미지 테이블
  create table review_img(
    uuid varchar(100), # 고유값
    uploadpath varchar(500), # 경로
    filename varchar(500), # 파일이름
    filetype varchar(50), # 파일타입
    review_no int(20), # 상품게시글번호
    fileOriginalName varchar(500),
    Foreign Key (review_no) references review (review_no) ON DELETE CASCADE,
    product_no int,
    foreign key(product_no) references product(product_no)
    );   
#-----------------------------------------------------------
 



