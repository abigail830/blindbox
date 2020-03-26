create TABLE draw_tbl (
  openId varchar(100) NOT NULL,
  drawId varchar(100) unique NOT NULL,
  drawStatus varchar(100) NOT NULL,
  productId varchar(100) NOT NULL,
  seriesId varchar(100) NOT NULL,
  drawTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (drawId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create TABLE product_v2_tbl (
  ID varchar(100) NOT NULL,
  version bigint NOT NULL DEFAULT 0,
  seriesID varchar(100) NOT NULL,
  name varchar(100) NOT NULL,
  isSpecial boolean NOT NULL,
  isPresale boolean NOT NULL,
  stock bigint NOT NULL,
  probability double NOT NULL,
  productImage varchar(1000) NOT NULL,
  productGrayImage varchar(1000) DEFAULT NULL,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (ID, version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

