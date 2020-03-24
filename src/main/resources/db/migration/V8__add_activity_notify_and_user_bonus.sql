alter table wx_user_tbl add bonus bigint;
update wx_user_tbl set bonus = 0 where open_id is not null;

alter table activity_tbl add notify varchar(2000);
alter table activity_tbl modify activity_description varchar(2000);
alter table activity_tbl drop content_img_addr;

