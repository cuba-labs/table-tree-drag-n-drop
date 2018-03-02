alter table SALES_CUSTOMER add column GRADE integer ^
update SALES_CUSTOMER set GRADE = 10 where GRADE is null ;
alter table SALES_CUSTOMER alter column GRADE set not null ;
