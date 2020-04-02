alter table series_tbl add column temp_name varchar(100);

update series_tbl
set temp_name = name;

alter table series_tbl drop column name;

alter table series_tbl add column name varchar(100);

update series_tbl
set name = temp_name;

alter table series_tbl drop column temp_name;
