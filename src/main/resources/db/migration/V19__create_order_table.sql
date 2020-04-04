create TABLE order_tbl (
  orderId varchar(100) NOT NULL unique,
  openId varchar(100) NOT NULL,
  drawId varchar(100),
  productName varchar(100),
  productPrice double,
  address varchar(100),
  receiver varchar(100),
  mobile varchar(100),
  area varchar(100),
  associateCode varchar(100),
  detailAddress varchar(100),
  createTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  status varchar(25) NOT NULL,
  tranportOrderId varchar(100),
  PRIMARY KEY (orderId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

