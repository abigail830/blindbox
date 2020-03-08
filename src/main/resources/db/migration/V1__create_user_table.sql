create TABLE wx_user_tbl (
  ID bigint NOT NULL AUTO_INCREMENT,
  open_id varchar(100) unique NOT NULL,
  gender varchar(45) DEFAULT NULL,
  nick_name varchar(255) DEFAULT NULL,
  city varchar(255) DEFAULT NULL,
  country varchar(255) DEFAULT NULL,
  province varchar(255) DEFAULT NULL,
  lang varchar(45) DEFAULT NULL,
  avatar_url varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
