alter table order_tbl
ADD updatedTime timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

UPDATE order_tbl set updatedTime=now() where status = 'NEW_TRANSPORT';