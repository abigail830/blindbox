create TABLE shipping_addr_tbl (
  ID bigint NOT NULL AUTO_INCREMENT,
  receiver varchar(100) NOT NULL,
  mobile varchar(1000) NOT NULL,
  area varchar(1000) NOT NULL,
  associate_code varchar(1000) NOT NULL,
  detail_address varchar(1000) NOT NULL,
  is_default_address BOOLEAN NOT NULL,
  open_id varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
