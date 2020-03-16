create TABLE roles_tbl (
  ID varchar(100) NOT NULL unique,
  name varchar(100) unique NOT NULL,
  category varchar(1000) NOT NULL,
  description varchar(1000) NOT NULL,
  role_image varchar(1000) NOT NULL,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
