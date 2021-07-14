#��Ű��(DB) ����
create database wannabe default character set UTF8;

# ����� ����
create user bee@'%' identified by 'honey';

# ���� �ֱ�
grant all privileges on wannabe.* to bee@'%';


# ���̺� ��� ����
show tables;

# ��Ű��(DB)����
use wannabe;

#<�׽�Ʈ spl>#
#select * from product_img where uploadpath = DATE_FORMAT( date_sub(NOW(),INTERVAL 1 DAY)  ,'%Y\%m\%d');
#
#
#
#

# ������ �� �������� ����
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

# �� ���� ���� �ȵ� �ϳ��� ����
#-----------------------------------------------------------
# 1 ȸ�� ���̺�
create table member(
    member_id VARCHAR(20) PRIMARY KEY, # ��� ���̵� 
    member_pw VARCHAR(200) not null, # ��� ��й�ȣ
    member_name VARCHAR(20) not null, # ��� �̸�
    member_phone VARCHAR(50) not null, # ��� ����ó
    member_email VARCHAR(50) not null, # ��� �̸���
    member_authority char(1) not null default 'b', # ��� ����
    regdate datetime default now() , # ������
    updatedate datetime default now(), # ������
    secession char(1) default 'n' # Ż�� ����
    ); 
#-----------------------------------------------------------
# 2 ī�װ� ���̺�
create table product_category(
	catename varchar(20), # ī�װ� ��
	catecode varchar(30) primary key, # ī�װ� �ڵ��ȣ
	catecoderef varchar(30), #����
	foreign key(catecoderef) references product_category(catecode)
);
insert into PRODUCT_CATEGORY(catename, catecode) values('ħ��','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('�¿�ħ��','101','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('ö��ħ��','102','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('����ħ��','103','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('�̱�ħ��','104','100');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('���۽̱�/����ħ��','105','100');

insert into PRODUCT_CATEGORY(catename, catecode) values('����','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('ö������','201','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('��������','202','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('���̽�����','203','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('�߿ܿ�����','204','200');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('�ȶ�/�������','205','200');

insert into PRODUCT_CATEGORY(catename, catecode) values('����','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('�к긯����','301','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('���׼���','302','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('�������','303','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('1��/2�ο� ����','304','300');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('3��/4�ο� ����','305','300');

insert into PRODUCT_CATEGORY(catename, catecode) values('������','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('ö��������','401','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('���������','402','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('��������','403','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('����/������','404','400');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('������','405','400');

insert into PRODUCT_CATEGORY(catename, catecode) values('���̺�','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('ö�����̺�','501','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('�������̺�','502','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('��Ź','503','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('å��/����ũ','504','500');
insert into PRODUCT_CATEGORY(catename, catecode,catecoderef) values('�ٿ뵵/�������̺�','505','500');
#-----------------------------------------------------------  
# 3 ��ǰ ���̺�
create table product(
	product_no int not null auto_increment, # ��ǰ ��ȣ
	product_category_code varchar(30), # �з�
	product_title varchar(100), # ��ǰ ����
	product_content longtext, # ��ǰ ����
	product_writer varchar(20), # �ۼ���-ȸ�� ���̵�
	product_price int, # ��ǰ ����
	product_delivery_message varchar(100), # ��ۺ� ����
	product_delivery_price int, # ��ۺ�
	product_sale_percent int, # ������
	product_company varchar(30), #�ǸŻ�
	product_quantity int, # ��ǰ ����
	product_type varchar(30), # ��ǰ Ÿ��
	product_code varchar(30), # ��ǰ �ڵ�
	regdate datetime default now(), # �ۼ���
	updatedate datetime default now(), # ������
	secession char(1) default 'n', # ���� ���� 
	primary key(product_no),
	foreign key(product_category_code) references product_category(catecode)
);
#-----------------------------------------------------------
# 4 ���� ���̺�
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
# 5 ���� ���̺�
create table qna (
  qna_no int not null auto_increment,  # ���� ��ȣ
  product_no int(10) not null, # ��ǰ ��ȣ
  qna_content varchar(1000) not null, # ���� ����
  member_id varchar(20) not null, # ���� �ۼ���
  replyDate datetime default now(), # �ۼ���
  updateDate datetime default now(), # ������
isAnswered boolean not null default FALSE, 
  primary key(qna_no),
  foreign key(product_no) references product(product_no),
  foreign key (member_id) references member(member_id),
  title varchar(50) not null,
  view_count int not null default 0 
);

insert into qna (product_no, qna_content, member_id, title) values(113, "���ǵ帳�ϴ�", "ara123", "����� ���� �ǳ���?");
insert into qna (product_no, qna_content, member_id, title) values(113, "���ǵ帳�ϴ�22", "ara123", "����� ���� �ǳ���?222");
insert into qna (product_no, qna_content, member_id, title) values(113, "���ǵ帳�ϴ�33", "ara123", "����� ���� �ǳ���?333");

#------------------------���� �亯

create table qna_reply (
   parent_no int,
    foreign key(parent_no) references qna(qna_no) ON DELETE CASCADE,
    qna_reply_writer varchar(20) default '������(WANNABE)',
    qna_reply_title varchar(20) default '�亯�帳�ϴ�.', 
    qna_reply_no int auto_increment primary key,
    qna_reply_content varchar(1000) not null,
    qna_reply_date datetime default now(),
    view_count int default 0,
    product_no int,
    foreign key (product_no) references product(product_no)
);
#-----------------------------------------------------------
# 6 ��ٱ��� ���̺�
create table cart(
	cart_no int  not null auto_increment, # ��ٱ��� ��ȣ
	product_no int, # ��ǰ ��ȣ
	member_id varchar(20), # ȸ�� ���̵�
	cart_product_quantity int, # ����
	regdate datetime default now(), # ��ٱ��� ������
	primary key(cart_no),
	foreign key(member_id) references member(member_id),
	foreign key(product_no) references product(product_no)
);
#-----------------------------------------------------------
# 7 ���ų��� ���̺�
create table purchase_history(
	member_id varchar(20), # ȸ�� ���̵�
	product_no int, # ��ǰ ��ȣ
	product_category_code varchar(30), # �з�
	product_title varchar(100), # ��ǰ ����
	product_content longtext, # ��ǰ ����
	product_writer varchar(20), # �ۼ���-ȸ�� ���̵�
	product_price int, # ��ǰ ����
	product_delivery_message varchar(100), # ��ۺ� ����
	product_delivery_price int, # ��ۺ�
	product_sale_percent int, # ������
	product_company varchar(30), #�ǸŻ�
	product_type varchar(30), # ��ǰ Ÿ��
	product_code varchar(30), # ��ǰ �ڵ� 
	address_name varchar(20) not null, # �ּ����� 
	address_first varchar(500) not null, # �����ȣ 
	address_middle varchar(500) not null, # ���θ�
	address_last varchar(500) not null, # ���ּ�
	address_message varchar(100), # ��û����
	purchase_history_quantity int, # ����
	purchase_history_state varchar(30), # ����
	regdate datetime default now(), # ������
	delivery_start_datetime datetime, # ��� ������
	delivery_end_datetime datetime, # ��� �Ϸ���
	cancel_request_datetime datetime, # ��� ��û��
	cancel_complete_datetime datetime, # ��� �Ϸ���
	refund_request_datetime datetime, # ȯ�� ��û��
	refund_complete_datetime datetime, # ȯ�� �Ϸ���
	foreign key(member_id) references member(member_id),
	foreign key(product_no) references product(product_no),
	foreign key(product_category_code) references product_category(catecode)
);
#-----------------------------------------------------------
# 8 �ּ��� ���̺�
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
# 9 ��ǰ �̹��� ���̺�
create table product_img(
	 uuid varchar(100), # ������
	 uploadpath varchar(200), # ���
	 filename varchar(100), # �����̸�
	 filetype varchar(20),
	 product_img_no int(20) NOT NULL auto_increment, # �̹��� ��ȣ
	 product_no int(20), # ��ǰ�Խñ۹�ȣ
	 primary key(product_img_no)
	 );
 #-----------------------------------------------------------
 # 10 ���� �̹��� ���̺�
  create table review_img(
    uuid varchar(100), # ������
    uploadpath varchar(500), # ���
    filename varchar(500), # �����̸�
    filetype varchar(50), # ����Ÿ��
    review_no int(20), # ��ǰ�Խñ۹�ȣ
    fileOriginalName varchar(500),
    Foreign Key (review_no) references review (review_no) ON DELETE CASCADE,
    product_no int,
    foreign key(product_no) references product(product_no)
    );   
#-----------------------------------------------------------
 



