create TABLE admin_user_tbl (
  ID bigint NOT NULL AUTO_INCREMENT,
  name varchar(100) unique NOT NULL,
  encrypted_password varchar(100) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
