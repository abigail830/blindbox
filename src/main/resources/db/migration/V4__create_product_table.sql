create TABLE series_tbl (
  ID bigint NOT NULL AUTO_INCREMENT,
  roleID bigint NOT NULL,
  name varchar(100) unique NOT NULL,
  releaseDate TIMESTAMP NOT NULL,
  isNewSeries boolean NOT NULL,
  isPresale boolean NOT NULL,
  price double NOT NULL,
  seriesImage varchar(1000) NOT NULL,
  matrixHeaderImage varchar(1000) NOT NULL,
  matrixCellImage varchar(1000) NOT NULL,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


create TABLE product_tbl (
  ID bigint NOT NULL AUTO_INCREMENT,
  seriesID bigint NOT NULL,
  name varchar(100) unique NOT NULL,
  isSpecial boolean NOT NULL,
  stock boolean NOT NULL,
  isPresale bigint NOT NULL,
  probability bigint NOT NULL,
  productImage varchar(1000) NOT NULL,
  postCardImage varchar(1000) NOT NULL,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;