create TABLE draw_List_tbl (
  openId varchar(100) NOT NULL,
  drawListId varchar(100) unique NOT NULL,
  drawGroup varchar(10000) NOT NULL,
  seriesId varchar(100) NOT NULL,
  drawTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (drawListId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
