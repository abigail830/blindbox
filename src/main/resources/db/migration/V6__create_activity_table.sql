create TABLE activity_tbl (
  ID varchar(100) NOT NULL unique,
  activity_name varchar(200) NOT NULL,
  activity_description varchar(1000),
  shown_in_ad BOOLEAN NOT NULL,
  main_img_addr varchar(1000) NOT NULL,
  content_img_addr varchar(2000),
  activity_start_date TIMESTAMP NOT NULL,
  activity_end_date TIMESTAMP NOT NULL,
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
