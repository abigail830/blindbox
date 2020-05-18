create TABLE series_v2_tbl (
  ID varchar(100) NOT NULL unique,
  name varchar(100) unique NOT NULL,
  releaseDate TIMESTAMP NOT NULL,
  isNewSeries boolean NOT NULL,
  isPresale boolean NOT NULL,
  price double NOT NULL,
  columnSize INT DEFAULT 3,
  totalSize INT DEFAULT 3,
  seriesImage varchar(1000) NOT NULL,
  matrixHeaderImage varchar(1000) NOT NULL,
  matrixCellImage varchar(1000) NOT NULL,
  longImage varchar(1000) DEFAULT NULL,
  boxImage varchar(1000) DEFAULT NULL,
  posterBgImage varchar(1000) DEFAULT NULL,
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create TABLE series_role_mapping_tbl (
  ID bigint NOT NULL AUTO_INCREMENT,
  seriesId varchar(100) NOT NULL,
  roleId varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;