create TABLE order_tbl (
  orderId varchar(100) NOT NULL unique,
  openId varchar(100) NOT NULL,
  drawId varchar(100),
  productName varchar(100),
  productPrice double,
  prepayId varchar(1000),
  nonceStr varchar(1000),
  preOrderTime varchar(1000),
  paySign varchar(1000),
  address varchar(100),
  receiver varchar(100),
  mobile varchar(100),
  area varchar(100),
  associateCode varchar(100),
  detailAddress varchar(100),
  createTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  status varchar(25) NOT NULL,
  PRIMARY KEY (orderId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

