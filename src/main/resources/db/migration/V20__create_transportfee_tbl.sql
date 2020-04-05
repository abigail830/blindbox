create TABLE transport_fee_tbl (
  area varchar(1000) NOT NULL unique,
  transport_fee double,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  remarks varchar(1000),
  PRIMARY KEY (area)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into transport_fee_tbl (area, transport_fee) values ('广东省',8.0);
insert into transport_fee_tbl (area, transport_fee) values ('安徽省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('北京市',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('重庆市',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('福建省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('甘肃省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('广西壮族自治区',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('贵州省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('海南省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('河北省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('黑龙江省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('河南省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('湖北省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('湖南省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('江苏省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('江西省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('吉林省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('辽宁省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('宁夏回族自治区',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('青海省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('山东省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('上海市',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('山西省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('陕西省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('四川省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('天津市',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('云南省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('浙江省',10.0);
insert into transport_fee_tbl (area, transport_fee) values ('内蒙古自治区',15.0);
insert into transport_fee_tbl (area, transport_fee) values ('新疆维吾尔族自治区',15.0);
insert into transport_fee_tbl (area, transport_fee) values ('西藏自治区',15.0);
insert into transport_fee_tbl (area, transport_fee, remarks) values ('港澳',null,'到付');
