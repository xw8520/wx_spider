/*==============================================================*/
/* Table: NewsMessage                                           */
/*==============================================================*/
create table NewsMessage
(
   id                   int not null auto_increment,
   title                national varchar(200),
   sum                  varchar(200),
   sn                   varchar(200),
   postdate             datetime,
   createdate           datetime,
   nameen               varchar(50),
   namecn               national varchar(100),
   cover                varchar(1000),
   filename             varchar(200),
   views                text,
   comment              text,
   mainbody             text,
   primary key (id)
);

